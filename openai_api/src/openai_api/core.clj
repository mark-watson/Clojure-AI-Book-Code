(ns openai-api.core
  (:require
   [wkok.openai-clojure.api :as api]
   [wkok.openai-clojure.openai :as openai])
  
  (:require [clj-http.client :as client])
(:require [clojure.data.json :as json]))

;; We are using Werner Kok's library https://github.com/wkok/openai-clojure/

;; define the environment variable "OPENAI_KEY" with the value of your OpenAI API key

;; remove:

(defn- openai-helper [body]
  (let [json-results
        (client/post
         "https://api.openai.com/v1/engines/davinci/completions"
         {:accept :json
          :headers
          {"Content-Type"  "application/json"
           "Authorization" (str "Bearer " (System/getenv "OPENAI_KEY"))}
          :body   body})]
    (clojure.string/trim
     ((first ((json/read-str (json-results :body)) "choices")) "text"))))



(defn completions
  "Use the OpenAI API for text completions"
  [prompt-text max-tokens]
  (let [response
         (api/create-completion
          {:model "text-davinci-003"
           :prompt prompt-text
           :max_tokens max-tokens
           :temperature 0.2})]
    ((first (response :choices)) :text)))
   

(defn summarize
  "Use the OpenAI API for text summarization"
  [prompt-text max-tokens]
  (let [response
         (api/create-completion
          {:model "text-davinci-003"
           :prompt
           (clojure.string/join
            ""
            ["Summarize this for a second-grade student:\n\n"
             prompt-text])
           :max_tokens max-tokens
           :temperature 0.3
           :top_k 1
           :frequency_penalty 0.0
           :presence_penalty 0.0})]
        ((first (response :choices)) :text)))


(defn answer-question
  "Use the OpenAI API for question answering"
  [prompt-text max-tokens]
  (let [response
          (api/create-completion
           {:model "text-davinci-003"
            :prompt
            (clojure.string/join
             ""
             ["Answer the following question:\n\n"
              prompt-text])
            :max_tokens max-tokens
            :temperature 0.3
            :top_k 1
            :frequency_penalty 0.0
            :presence_penalty 0.0})]
      ((first (response :choices)) :text)))


(defn embeddings_DOES_NOT_WORK [text]
  (let [response
        (api/create-embedding
         {:model "text-embedding-ada-002"
          :input text})]
    response))

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
