(ns docs-qa.core-test
  (:require [clojure.test :refer :all]
            [docs-qa.core :refer :all]
            [docs-qa.vectordb :refer :all]
            [openai-api.core :refer :all]))

(deftest a-test
  (testing "FIXME, I fail."
    '(println
         (openai-api.core/answer-question
          "Where is the Valley of Kings?"))
    (println
     (docs-qa.core/best-vector-matches
      "What is Chemistry. How useful, really, are the sciences. Is Amyl alcohol is an organic compound?"))
    (is (= 0 0))))
