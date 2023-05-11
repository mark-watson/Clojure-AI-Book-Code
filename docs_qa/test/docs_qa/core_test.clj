(ns docs-qa.core-test
  (:require [clojure.test :refer :all]
            [docs-qa.core :refer :all]
            [docs-qa.vectordb :refer :all]
            [openai-api.core :refer :all]))

(deftest a-test
  (testing "FIXME, I fail."
    (println
         (openai-api.core/answer-question
          "Where is the Valley of Kings?"
          50))
    (println
     (docs-qa.vectordb/openai-embedding-vector
      "Congress increased the spending limit by 1 trillion dollars"))
    (is (= 0 0))))
