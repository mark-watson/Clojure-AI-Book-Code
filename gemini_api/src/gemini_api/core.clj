(ns gemini-api.core
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]
            [clojure.tools.logging :as log]))

(def model "gemini-2.5-flash") ; or gemini-3.0-pro, etc.

(def google-api-key (System/getenv "GOOGLE_API_KEY"))
(when (nil? google-api-key)
  (log/error "GOOGLE_API_KEY environment variable not set!"))

(def base-url "https://generativelanguage.googleapis.com/v1beta/models")

(defn build-request-url [model api-key]
  (str base-url "/" model ":generateContent?key=" api-key))

(defn generate-content [prompt & {:keys [model api-key] :or {model model api-key google-api-key}}]
  (let [url (build-request-url model api-key)
        headers {"Content-Type" "application/json"}
        body {:contents [{:parts [{:text prompt}]}]}
        request-opts {:headers headers
                      :body (json/write-str body)
                      :content-type :json
                      :accept :json
                      :socket-timeout 10000
                      :connection-timeout 10000}]
    (try
      (let [response (client/post url request-opts)
            parsed-response (json/read-str (:body response) :key-fn keyword)
            candidates (:candidates parsed-response)]
        (if (seq candidates)
          (let [text (get-in (first candidates) [:content :parts 0 :text])]
            (if text
              text
              (do
                (log/warn "No text found in response structure:" parsed-response)
                nil)))
          (do
            (log/warn "No candidates found in response:" parsed-response)
            nil)))
      (catch Exception e
        (log/error "Error making request:" (.getMessage e))
        (when-let [response-body (-> e ex-data :body)]
          (log/error "Error response body:" response-body))
        nil))))

(defn summarize [text]
  (generate-content (str "Summarize the following text:\n\n" text)))

;; Example usage:
;; (generate-content "Explain quantum computing")
;; (summarize "Long text to summarize...")