(ns gemini-java-api.core-test
  (:require [clojure.test :refer :all]
            [gemini-java-api.core :refer :all]))

(def some-text
  "Jupiter is the fifth planet from the Sun and the largest in the Solar System. It is a gas giant with a mass one-thousandth that of the Sun, but two-and-a-half times that of all the other planets in the Solar System combined. Jupiter is one of the brightest objects visible to the naked eye in the night sky, and has been known to ancient civilizations since before recorded history. It is named after the Roman god Jupiter.[19] When viewed from Earth, Jupiter can be bright enough for its reflected light to cast visible shadows,[20] and is on average the third-brightest natural object in the night sky after the Moon and Venus.")

(deftest completions-test
  (testing "gemini completions API"
    (let [results
          (gemini-java-api.core/generate-content "He walked to the river")]
      (println results)
      (is (= 0 0)))))

(deftest summarize-test
  (testing "gemini summarize API"
    (let [results
          (gemini-java-api.core/summarize
           some-text)]
      (println results)
      (is (= 0 0)))))

(deftest question-answering-test
  (testing "gemini question-answering API"
    (let [results
          (gemini-java-api.core/generate-content
            ;;"If it is not used for hair, a round brush is an example of what 1. hair brush 2. bathroom 3. art supplies 4. shower ?"
           "Where is the Valley of Kings?"
            ;"Where is San Francisco?"
           )]
      (println results)
      (is (= 0 0)))))

(deftest completions-with-search-test
  (testing "gemini completions with search"
    (let [results
          (gemini-java-api.core/generate-content-with-search "What sci-fi moves are playing in Harkens 16 in Flagstaff today?")]
      (println results)
      (is (= 0 0)))))
