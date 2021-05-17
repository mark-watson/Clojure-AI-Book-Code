(ns nlp-libpython-spacy.core-test
  (:require [clojure.test :as test]
            [nlp-libpython-spacy.core :as sp]))

(def test-text "John Smith worked for IBM in Mexico last year and earned $1 million in salary and bonuses.")

(test/deftest tokenization-test
  (test/testing "tokenization test"
    (test/is (= 033 (count (sp/text->tokens test-text))))))
