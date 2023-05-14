(ns docs-qa.core
  (:require [clojure.java.jdbc :as jdbc]
            [openai-api.core :refer :all]
            [docs-qa.vectordb :refer :all])
  (:gen-class))

(defn best-vector-matches [query]
  (print "**count:" (count docs-qa.vectordb/embeddings-with-chunk-texts))
  (clojure.string/join
   " ."
   (let [query-embedding (openai-api.core/embeddings query)]
    (map
     second
     (filter
      (fn [emb-text-pair]
        (let [emb (first emb-text-pair)
              text (second emb-text-pair)]
          (> (openai-api.core/dot-product
              query-embedding
              emb)
             0.79)))
      docs-qa.vectordb/embeddings-with-chunk-texts)))))

(defn answer-prompt [prompt]
  '(openai-api.core/answer-question
   prompt
   50)
  "TBDTBD1234")

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (loop []
  (println "Enter a string:")
  (let [input (read-line)]
    (if (empty? input)
      (println "Done.")
      (do
        (let [text (best-vector-matches input)
              prompt
              (clojure.string/join "\n"
                                   ["With the following CONTEXT:\n\n"
                                    text
                                    "\n\nANSWER:\n\n"
                                    input])]
          (println "** PROMPT:" prompt)
          (println (answer-prompt prompt)))
          (recur))))))
