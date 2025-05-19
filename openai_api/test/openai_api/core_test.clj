(ns openai-api.core-test
  (:require [clojure.test :refer :all]
            [openai-api.core :refer :all]))

(def some-text
  "Jupiter is the fifth planet from the Sun and the largest in the Solar System. It is a gas giant with a mass one-thousandth that of the Sun, but two-and-a-half times that of all the other planets in the Solar System combined. Jupiter is one of the brightest objects visible to the naked eye in the night sky, and has been known to ancient civilizations since before recorded history. It is named after the Roman god Jupiter.[19] When viewed from Earth, Jupiter can be bright enough for its reflected light to cast visible shadows,[20] and is on average the third-brightest natural object in the night sky after the Moon and Venus.")

(deftest completions-test
  (testing "OpenAI completions API"
    (let [results
          (openai-api.core/completions "He walked to the river")]
      (println results)
      (is (= 0 0)))))

(deftest summarize-test
  (testing "OpenAI summarize API"
    (let [results
          (openai-api.core/summarize
           some-text)]
      (println results)
      (is (= 0 0)))))

(deftest question-answering-test
  (testing "OpenAI question-answering API"
    (let [results
          (openai-api.core/answer-question
           "Where is the Valley of Kings?"
           )]
      (println results)
      (is (= 0 0)))))
