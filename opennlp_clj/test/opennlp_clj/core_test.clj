(ns opennlp-clj.core-test
  (:require [clojure.test :refer :all])
  (:require [opennlp-clj.core :refer :all]))

(def
  test-text
  "The cat chased the mouse around the tree while Mary Smith (who works at IBM in San Francisco) watched.")

(deftest pos-test
  (testing "parts of speech"
    (let [token-java-array (tokenize->java test-text)
          token-clojure-seq (tokenize->seq test-text)
          words-pos (POS token-java-array)
          companies (company-names token-java-array)
          places (location-names token-java-array)
          people (person-names token-java-array)]
      (println "Input text:\n" test-text)
      (println "Tokens as Java array:\n" token-java-array)
      (println "Tokens as Clojure seq:\n" token-clojure-seq)
      (println "Part of speech tokens:\n" words-pos)
      (println "Companies:\n" companies)
      (println "Places:\n" places)
      (println "People:\n" people)
      (is (= (first words-pos) "DT")))))
