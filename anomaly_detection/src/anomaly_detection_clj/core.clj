(ns anomaly-detection-clj.core
  (:gen-class)
  (:require clojure.pprint)
  (:require (incanter core stats charts))
  (:require [clojure.data.csv :as csv])
  (:require [clojure.java.io :as io])
  (:require [clojure.data.csv :as csv]))

(import (com.markwatson.anomaly_detection AnomalyDetection))

(def GENERATE_PLOTS false)

(defn print-histogram [title values-2d index-to-display]
  (println "** plotting:" title)
  (let [column (for [row values-2d]
                 (nth row index-to-display))]
    (incanter.core/view (incanter.charts/histogram column :title title))))

(defn data->gausian
  "separate labeled output, and then make the data look more like a Gausian (bell curve shaped) distribution"
  [vector-of-numbers-as-strings]
  (let [v (map read-string vector-of-numbers-as-strings)
        training-data0 (map
                         (fn [x] (Math/log (+ (* 0.1 x) 1.2)))
                         (butlast v))
        target-output (* 0.5 (- (last v) 2))                ; make target output be [0,1] instead of [2,4]
        vmin (apply min training-data0)
        vmax (apply max training-data0)
        training-data (map
                        (fn [x] (/
                                  (- x vmin)
                                  (+ 0.0001 (- vmax vmin))))
                        training-data0)]
    (concat training-data [target-output])))

(defn testAD []
  (let [ad (AnomalyDetection.)
        cdata
        (map
          data->gausian
          (with-open [reader (io/reader "data/cleaned_wisconsin_cancer_data.csv")]
            (doall
              (csv/read-csv reader))))]
    (if GENERATE_PLOTS
      (do
        (print-histogram "Clump Thickness" cdata 0)
        (print-histogram "Uniformity of Cell Size" cdata 1)
        (print-histogram "Uniformity of Cell Shape" cdata 2)
        (print-histogram "Marginal Adhesion" cdata 3)
        (print-histogram "Single Epithelial Cell Size" cdata 4)
        (print-histogram "Bare Nuclei" cdata 5)
        (print-histogram "Bland Chromatin" cdata 6)
        (print-histogram "Normal Nucleoli" cdata 7)
        (print-histogram "Mitoses" cdata 8)))
    ;; get best model parameters:
    (let [java-cdata (into-array (map double-array cdata))
          detector (new AnomalyDetection 10 (- (count cdata) 1) java-cdata)]
      (. detector train)
      (let [test_malignant (double-array [0.5 1 1 0.8 0.5 0.5 0.7 1 0.1])
            test_benign (double-array [0.5 0.4 0.5 0.1 0.8 0.1 0.3 0.6 0.1])
            malignant_result (. detector isAnamoly test_malignant)
            benign_result (. detector isAnamoly test_benign)]
        (if malignant_result
          (println "malignant_result true")
          (println "malignant_result false"))
        (if benign_result
          (println "benign_result true")
          (println "benign_result false"))
        {:malignant-result malignant_result
         :benign-result benign_result}
        ))))
(defn -main
  "I don't do a whole lot ... yet."
  [& _]
  (testAD))
