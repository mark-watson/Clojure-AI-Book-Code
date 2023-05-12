(ns docs-qa.vectordb
  (:require [clojure.java.jdbc :as jdbc]))

;; (clojure.string/join " " ["1.0" "2.0" "3.0"])
;; (clojure.string/trim

(defn string-to-floats [s]
  (map #(Float/parseFloat %) (clojure.string/split s #" ")))

(defn truncate-string [s max-length]
  (if (< (count s) max-length)
    s
    (subs s 0 max-length)))

(defn break-into-chunks [s chunk-size]
  (let [chunks (partition-all chunk-size s)]
    (map #(apply str %) chunks)))

(defn openai-embedding-vector [text] 
  "TBD")