(ns anomaly-detection-clj.core-test
  (:require [clojure.test :refer :all]
            [anomaly-detection-clj.core :refer :all]))

(deftest a-test
  (testing "Test anomaly detection results"
    (let [results (testAD)
          malignant-result (:malignant-result results)
          benign-result (:benign-result results)]
      (is (= true malignant-result))
      (is (= false benign-result)))))
