(ns gemini-api.core
  (:require [clj-http.client :as client])
  (:require [clojure.data.json :as json]))

(def model "gemini-2.0-flash") ; or gemini-1.5-pro, etc.

(def google-api-key (System/getenv "GOOGLE_API_KEY")) ; Make sure to set this env variable

(def base-url "https://generativelanguage.googleapis.com/v1beta/models")

(defn generate-content [prompt]
  (let [url (str base-url "/" model ":generateContent?key=" google-api-key)
        headers {"Content-Type" "application/json"}
        body {:contents [{:parts [{:text prompt}]}]}]
    (try
      (let [response (client/post url {:headers headers
                                     :body (json/write-str body)
                                     :content-type :json
                                     :accept :json})
            _ (println "Raw response:" (:body response))  ; Debug print
            parsed-response (json/read-str (:body response) :key-fn keyword)
            candidates (:candidates parsed-response)]
        (if (seq candidates)
          (let [text (get-in (first candidates) [:content :parts 0 :text])]
            (if text
              text
              (do
                (println "No text found in response structure:" parsed-response)
                nil)))
          (do
            (println "No candidates found in response:" parsed-response)
            nil)))
      (catch Exception e
           (println "Error making request:" (.getMessage e))
           (when-let [response-body (-> e ex-data :body)]
             (println "Error response body:" response-body))
           nil))))

(defn summarize [text]
  (generate-content (str "Summarize the following text:\n\n" text)))
