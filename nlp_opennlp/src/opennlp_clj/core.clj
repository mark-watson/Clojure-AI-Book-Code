(ns opennlp-clj.core
  (:import (com.markwatson.opennlp NLP)))

(defn sentence-splitter "tokenize entire sentences" [string-input]
  (seq (NLP/sentenceSplitter string-input)))

(defn tokenize->seq "tokenize words to Clojure seq" [string-input]
  (seq (NLP/tokenize string-input)))

(defn tokenize->java "tokenize words to Java array" [string-input]
  (NLP/tokenize string-input))

;; Word analysis:

(defn POS "part of speech" [java-token-array]
  (seq (NLP/POS java-token-array)))

;; Entities:

(defn company-names [java-token-array]
  (seq (NLP/companyNames java-token-array)))

(defn location-names [java-token-array]
  (seq (NLP/locationNames java-token-array)))

(defn person-names [java-token-array]
  (seq (NLP/personNames java-token-array)))
