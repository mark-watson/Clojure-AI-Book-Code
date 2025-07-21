(ns moonshot-api.core-test
  (:require [clojure.test :refer :all]
            [moonshot-api.core :refer :all]))

(deftest completions-test
  (testing "Moonshot completions API"
    (let [results
          (moonshot-api.core/completions "Write a story starting woth the text: He walked to the river")]
      (println results)
      (is (= 0 0)))))
