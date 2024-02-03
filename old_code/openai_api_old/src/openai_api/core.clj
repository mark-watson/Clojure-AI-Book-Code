(ns openai-api.core
  (:require [clj-http.client :as client])
  (:require [clojure.data.json :as json]))

;; define the environment variable "OPENAI_KEY" with the value of your OpenAI API key

(defn- openai-helper [body]
  (let [json-results
        (client/post
          "https://api.openai.com/v1/engines/davinci/completions"
          {:accept :json
           :headers
                   {"Content-Type"  "application/json"
                    "Authorization" (str "Bearer " (System/getenv "OPENAI_KEY"))
                    }
           :body   body
           })]
    ((first ((json/read-str (json-results :body)) "choices")) "text")))

(defn completions
  "Use the OpenAI API for text completions"
  [prompt-text max-tokens]
  (let
    [body
     (str
       "{\"prompt\": \"" prompt-text "\", \"max_tokens\": "
       (str max-tokens) "}")]
    (openai-helper body)))

(defn summarize
  "Use the OpenAI API for text summarization"
  [prompt-text max-tokens]
  (let
    [body
     (str
       "{\"prompt\": \"" prompt-text "\", \"max_tokens\": "
       (str max-tokens) ", \"presence_penalty\": 0.0"
       ", \"temperature\": 0.3, \"top_p\": 1.0, \"frequency_penalty\": 0.0"
       "}")]
    (openai-helper body)))


(defn answer-question
  "Use the OpenAI API for question answering"
  [prompt-text max-tokens]
  (let
    [body
     (str
       "{\"prompt\": \"" (str "nQ: " prompt-text) "nA:\", \"max_tokens\": "
       (str max-tokens) ", \"presence_penalty\": 0.0"
       ", \"temperature\": 0.3, \"top_p\": 1.0, \"frequency_penalty\": 0.0"
       ", \"stop\": [\"\\n\"]"
       "}")
     results (openai-helper body)
     ind (clojure.string/index-of results "nQ:")]
    (if (nil? ind)
      results
      (subs results 0 ind))))
