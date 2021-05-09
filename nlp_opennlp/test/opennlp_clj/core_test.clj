(ns opennlp-clj.core-test
  (:require [clojure.test :as test])
  (:require [opennlp-clj.core :as onlp]))

(def
  test-text
  "The cat chased the mouse around the tree while Mary Smith (who works at IBM in San Francisco) watched.")

(test/deftest pos-test
  (test/testing "parts of speech"
    (let [token-java-array (onlp/tokenize->java test-text)
          token-clojure-seq (onlp/tokenize->seq test-text)
          words-pos (onlp/POS token-java-array)
          companies (onlp/company-names token-java-array)
          places (onlp/location-names token-java-array)
          people (onlp/person-names token-java-array)]
      (println "Input text:\n" test-text)
      (println "Tokens as Java array:\n" token-java-array)
      (println "Tokens as Clojure seq:\n" token-clojure-seq)
      (println "Part of speech tokens:\n" words-pos)
      (println "Companies:\n" companies)
      (println "Places:\n" places)
      (println "People:\n" people)
      (test/is (= (first words-pos) "DT")))))
