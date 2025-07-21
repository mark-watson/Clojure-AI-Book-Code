(ns moonshot-api.core
  (:require [clj-http.client :as client]
            [clojure.data.json :as json])
  (:gen-class))

;; Define the API key, base URL, and model for Moonshot AI
(def moonshot-api-key (System/getenv "MOONSHOT_API_KEY"))
(def moonshot-base-url "https://api.moonshot.ai/v1")
(def moonshot-model "kimi-k2-0711-preview")

(defn completions
  "Sends promp request to the Moonshot AI chat completions API."
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

(defn- chat
  "Sends a chat request to the Moonshot AI chat completions API with tool support."
  [messages]
  (let [url (str moonshot-base-url "/chat/completions")
        headers {"Authorization" (str "Bearer " moonshot-api-key)
                 "Content-Type" "application/json"}
        body {:model moonshot-model
              :messages messages
              :temperature 0.3
              :tools [{:type "builtin_function"
                       :function {:name "$web_search"}}]}
        response (client/post url {:headers headers
                                   :body    (json/write-str body)
                                   :throw-exceptions false})
        parsed-body (json/read-str (:body response) :key-fn keyword)]
    (if (= (:status response) 200)
      (-> parsed-body :choices first)
      (throw (Exception. (str "Error: Received status " (:status response) ". Body: " (:body response)))))))

(defn search
  "Performs a Kimi 2 completion with web search."
  [user-question]
  (if-not moonshot-api-key
    (println "Error: MOONSHOT_API_KEY environment variable not set.")
    (try
      (loop [messages [{:role "system" :content "You are Kimi an AI assistant who returns all answers in English."}
                       {:role "user" :content user-question}]]
        (let [choice (chat messages)
              finish-reason (:finish_reason choice)]
          (if (= finish-reason "tool_calls")
            (let [assistant-message (:message choice)
                  tool-calls (-> assistant-message :tool_calls)
                  tool-messages (map (fn [tool-call]
                                       (let [tool-call-name (-> tool-call :function :name)]
                                         (if (= tool-call-name "$web_search")
                                           (let [tool-call-args (json/read-str (-> tool-call :function :arguments) :key-fn keyword)]
                                             {:role "tool"
                                              :tool_call_id (:id tool-call)
                                              :name tool-call-name
                                              :content (json/write-str tool-call-args)})
                                           (let [error-message (str "Error: unable to find tool by name '" tool-call-name "'")]
                                             {:role "tool"
                                              :tool_call_id (:id tool-call)
                                              :name tool-call-name
                                              :content (json/write-str error-message)}))))
                                     tool-calls)]
              (recur (concat messages [assistant-message] tool-messages)))
            (-> choice :message :content))))
      (catch Exception e
        (str "An exception occurred: " (.getMessage e))))))