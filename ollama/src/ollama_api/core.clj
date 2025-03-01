(ns ollama-api.core
  (:require [clj-http.client :as client])
  (:require [clojure.data.json :as json]))


(defn- ollama-helper [body]
  (let [json-results
        (client/post
          "http://localhost:11434/api/generate"
          {:accept :json
           :headers
           {"Content-Type" "application/json"}
           :body   body
           })]
    ((json/read-str (json-results :body)) "response")))

(defn completions
  "Use the Ollama API for text completions"
  [prompt-text]
  (let
    [body
     (json/write-str
       {:prompt prompt-text
        :model "mistral-small"
        :stream false})]
    (ollama-helper body)))

(defn summarize
  "Use the Ollama API for text summarization"
  [prompt-text]
  (completions (str "Summarize the following text: " prompt-text)))


(defn answer-question
  "Use the Ollama API for question answering"
  [prompt-text]
  (completions (str "Answer the following question: " prompt-text)))
