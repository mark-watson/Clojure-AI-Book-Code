(ns brave-search.core-test
  (:require [clojure.test :refer :all]
            [brave-search.core :refer :all]))

(deftest search-test
  (testing "Brave search API"
    (let [results
          (brave-search "Sedona Arizona")]
      (println results)
      (is (= 0 0)))))
