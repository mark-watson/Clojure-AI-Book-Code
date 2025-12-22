(ns gemini-java-api.core
  (:import (com.google.genai Client)
           (com.google.genai.types GenerateContentResponse GenerateContentConfig Tool GoogleSearch)))

(def DEBUG false)
(def model "gemini-3-flash-preview") ; or gemini-2.5-pro, etc.
(def google-api-key (System/getenv "GOOGLE_API_KEY")) ; Make sure to set this env variable

(defn generate-content
  "Sends a prompt to the Gemini API using the specified model and returns
   the text response."
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
 
 
(defn generate-content-with-search
  "Sends a prompt to the Gemini API using the specified model and enables
   the Google Search tool for grounded responses."
  [prompt]
  (let [client (Client.)
        config (-> (GenerateContentConfig/builder)
                   (.tools [(-> (Tool/builder)
                                (.googleSearch (-> (GoogleSearch/builder)
                                                 .build))
                                .build)])
                   .build)
        ^GenerateContentResponse resp
        (.generateContent (.models client)
                          model
                          prompt
                          config)]
    (when DEBUG
      (println (.text resp)))
    (.text resp)))


(defn summarize [text]
  (generate-content (str "Summarize the following text:\n\n" text)))
