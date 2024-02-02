(ns openai-api.core
  (:require
   [wkok.openai-clojure.api :as api]
   [wkok.openai-clojure.openai :as openai])
  
  (:require [clj-http.client :as client])
(:require [clojure.data.json :as json]))

;; We are using Werner Kok's library https://github.com/wkok/openai-clojure/

;; define the environment variable "OPENAI_KEY" with the value of your OpenAI API key

(defn completions
  "Use the OpenAI API for question answering"
  [prompt-text]
  (((first ((api/create-chat-completion
              {:model    "gpt-3.5-turbo"
               :messages [{:role "system" :content "You are a helpful assistant. You complete the user's prompt text."}
                          {:role "user" :content prompt-text}
                          ]})
            :choices))
    :message)
   :content))

(defn summarize
  "Use the OpenAI API for question answering"
  [prompt-text]
  (((first ((api/create-chat-completion
              {:model    "gpt-3.5-turbo"
               :messages [{:role "system" :content "You are a helpful assistant."}
                          {:role "user"
                           :content
                           (clojure.string/join
                             ""
                             ["Summarize this for a second-grade student:\n\n"
                              prompt-text])}
                          ]})
            :choices))
    :message)
   :content))

(defn answer-question
  "Use the OpenAI API for question answering"
  [prompt-text]
  (((first ((api/create-chat-completion
    {:model    "gpt-3.5-turbo"
     :messages [{:role "system" :content "You are a helpful assistant."}
                {:role "user" :content prompt-text}
                ]})
   :choices))
   :message)
   :content))

(defn embeddings [text]
  (try
    (let* [body
           (str
            "{\"input\": \""
            (clojure.string/replace
             (clojure.string/replace text #"[\" \n :]" " ")
             #"\s+" " ")
            "\", \"model\": \"text-embedding-ada-002\"}")
           json-results
           (client/post
            "https://api.openai.com/v1/embeddings"
            {:accept :json
             :headers
             {"Content-Type"  "application/json"
              "Authorization" (str "Bearer " (System/getenv "OPENAI_KEY"))}
             :body   body})]
          ((first ((json/read-str (json-results :body)) "data")) "embedding"))
    (catch Exception e
      (println "Error:" (.getMessage e))
      "")))

(defn dot-product [a b]
  (reduce + (map * a b)))
