(ns gemini-java-api.core
  (:import (com.google.genai Client)
           (com.google.genai.types GenerateContentResponse)))

(def DEBUG false)
(def model "gemini-2.5-flash") ; or gemini-2.5-pro, etc.
(def google-api-key (System/getenv "GOOGLE_API_KEY")) ; Make sure to set this env variable

(defn generate-content
  "Sends a prompt to the Gemini API using the specified model and returns the text response.
  Handles basic API errors."
  [prompt]
  (let [client (Client.)
        ^GenerateContentResponse resp
        (.generateContent (.models client)
                          model
                          prompt
                          nil)]
    (when DEBUG
      (println (.text resp))
      (when-let [headers (some-> resp
                                 .sdkHttpResponse (.orElse nil)
                                 .headers        (.orElse nil))]
        (println "Response headers:" headers)))
    (.text resp)))


(defn summarize [text]
  (generate-content (str "Summarize the following text:\n\n" text)))
