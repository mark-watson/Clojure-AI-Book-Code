(ns openai-api.core
  (:require [clj-http.client :as client])
  (:require [clojure.data.json :as json]))

(def model2 "gpt-4o-mini")

(def api-key (System/getenv "OPENAI_API_KEY"))

(defn completions [prompt]
  (let [url "https://api.openai.com/v1/chat/completions"
        headers {"Authorization" (str "Bearer " api-key)
                 "Content-Type" "application/json"}
        body {:model model2
              :messages [{:role "user" :content prompt}]}
        response (client/post url {:headers headers
                                   :body (json/write-str body)})]
    ;;(println (:body response))
    (get
     (get
      (first
       (get
        (json/read-str (:body response)  :key-fn keyword)
        :choices))
      :message)
     :content)))

(defn summarize [text]
  (completions (str "Summarize the following text:\n\n" text)))

(defn answer-question
  "Use the OpenAI API for question answering"
  [text]
  (completions (str "Answer the following question:\n\n" text)))
  

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
              "Authorization" (str "Bearer " api-key)}
             :body   body})]
          ((first ((json/read-str (json-results :body)) "data")) "embedding"))
    (catch Exception e
      (println "Error:" (.getMessage e))
      "")))

(defn dot-product [a b]
  (reduce + (map * a b)))

