(ns moonshot-api.core
  (:require [clj-http.client :as client]
            [clojure.data.json :as json])
  (:gen-class))

;; Define the API key, base URL, and model for Moonshot AI
(def moonshot-api-key (System/getenv "MOONSHOT_API_KEY"))
(def moonshot-base-url "https://api.moonshot.ai/v1")
(def moonshot-model "kimi-k2-0711-preview")

(defn completions
  "Sends a predefined request to the Moonshot AI chat completions API."
  [prompt]
  (if-not moonshot-api-key
    (println "Error: MOONSHOT_API_KEY environment variable not set.")
    (try
      (let [url (str moonshot-base-url "/chat/completions")
            headers {"Authorization" (str "Bearer " moonshot-api-key)
                     "Content-Type" "application/json"}
            ;; Construct the request body to match the Python example
            body {:model moonshot-model
                  :messages [{:role "system" :content "You are Kimi, an AI assistant provided by Moonshot AI. You are proficient in English conversations. You provide users with safe, helpful, and accurate answers."}
                             {:role "user" :content prompt}]
                  :temperature 0.3}
            ;; Make the POST request
            response (client/post url {:headers headers
                                       :body    (json/write-str body)
                                       ;; Throw an exception for non-2xx response codes
                                       :throw-exceptions false})
            ;; Parse the JSON response body
            parsed-body (json/read-str (:body response) :key-fn keyword)]

        (if (= (:status response) 200)
          ;; Extract the content from the response using the -> (thread-first) macro
          ;; This is equivalent to: (get (get (first (get parsed-body :choices)) :message) :content)
          (-> parsed-body :choices first :message :content)
          ;; Handle potential errors from the API
          (str "Error: Received status " (:status response) ". Body: " (:body response))))
      (catch Exception e
        (str "An exception occurred: " (.getMessage e))))))

(defn -main
  "Main function to execute the API call and print the result."
  [& args]
  (let [result (completions)]
    (println result)))