(ns docs-qa.core-test
  (:require [clojure.test :refer :all]
            [docs-qa.core :refer :all]
            [openai-api.core :refer :all]))

(deftest a-test
  (testing "FIXME, I fail."
    (println
        (openai-api.core/answer-question
          "Where is the Valley of Kings?"
          50))
    (is (= 0 0))))
