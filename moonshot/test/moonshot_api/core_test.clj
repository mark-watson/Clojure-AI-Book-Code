(ns moonshot-api.core-test
  (:require [clojure.test :refer :all]
            [moonshot-api.core :refer :all]))

'(deftest completions-test
  (testing "Moonshot completions API"
    (let [results
          (moonshot-api.core/completions "Write a story starting with the text: He walked to the river")]
      (println results)
      (is (= 0 0)))))

(deftest search-test
  (testing "Moonshot completions with search API"
    (let [results
          (moonshot-api.core/search "Current weather in Flagstaff Arizona")]
      (println results)
      (is (= 0 0)))))
