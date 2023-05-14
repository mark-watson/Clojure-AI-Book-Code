(ns docs-qa.core
  (:require [clojure.java.jdbc :as jdbc]
            [openai-api.core :refer :all]
            [docs-qa.vectordb :refer :all])
  (:gen-class))

(defn best-vector-matches [query]
  (print "**count:" (count docs-qa.vectordb/embeddings-with-chunk-texts))
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
      docs-qa.vectordb/embeddings-with-chunk-texts))))
  
(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
