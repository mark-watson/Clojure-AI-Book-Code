(ns anomaly-detection-clj.core
  (:gen-class)
  (:use clojure.pprint)
  (:use (incanter core stats charts)))

(require '[clojure.data.csv :as csv]
         '[clojure.java.io :as io]
         ;;'[clojure.math.numeric-tower :as math]
         )

(import (com.markwatson.anomaly_detection AnomalyDetection))
(import (com.markwatson.anomaly_detection PrintHistogram))

(def GENERATE_PLOTS false)

(def NUM_HISTOGRAM_BINS 5)

(defn to-bin [a-vector num-bins vmin vmax]
  (map
    (fn [x]
      (int (/ (* 0.99 (- x vmin) num-bins) vmax)))
    a-vector))

(defn print-histogram [title values-2d index-to-display]
  (println "** plotting:" title)
  (let [column (for [row values-2d]
                 (nth row index-to-display))]
    (view (histogram column :title title))))


(defn data->gausian [vector-of-numbers-as-strings]
  "separate labeled output, and then make the data look more like a Gausian (bell curve shaped) distribution"
  (let [v (map read-string vector-of-numbers-as-strings)
        training-data0 (map
                        (fn [x] (Math/log (+ (* 0.1 x) 1.2)))
                        (butlast v))
        target-output (* 0.5 (- (last v) 2)) ; make target output be [0,1] instead of [2,4]
        vmin (apply min training-data0)
        vmax (apply max training-data0)
        training-data (map
                        (fn [x] (/
                                  (- x vmin)
                                  (+ 0.0001 (- vmax vmin))))
                        training-data0)]
    (concat training-data [target-output])))

(defn testAD []
  (def ad (AnomalyDetection.))
  (println ad)
  (def cdata
      (map
        data->gausian
        (with-open [reader (io/reader "data/cleaned_wisconsin_cancer_data.csv")]
          (doall
            (csv/read-csv reader)))))
;;  (println cdata)
  ;;(def java-training-array (into-array (doall (map (fn [x] (into-array java.lang.Double x)) cdata))))
  ;;(pprint java-training-array)

  ;;(def csvdata (slurp "data/cleaned_wisconsin_cancer_data.csv"))
  ;;(PrintHistogram/historam "Clump Thickness"  cdata 0 0.0 1.0 NUM_HISTOGRAM_BINS )
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
;;  AnomalyDetection detector = new AnomalyDetection(10, line_count - 1, training_data);
  (def first-row (first cdata))
  (println first-row)
  (def jfr (float-array first-row))
  (println jfr)
  (def java-cdata (into-array (map double-array cdata)))
  (println java-cdata)
  (def detector (new AnomalyDetection 10 (- (count cdata) 1) java-cdata))
  (. detector train)
  ;; get best model parameters:
  (def best_epsilon (. detector bestEpsilon))
  (def mean_values (. detector muValues))
  (def sigma_squared (. detector sigmaSquared))
  ;; to use this model, use the method AnomalyDetection.isAnamoly(double []):
  (def test_malignant (double-array [0.5 1 1 0.8 0.5 0.5 0.7 1 0.1]))
  (def test_benign (double-array [0.5 0.4 0.5 0.1 0.8 0.1 0.3 0.6 0.1]))
  (def malignant_result (. detector isAnamoly test_malignant))
  (def benign_result (. detector isAnamoly test_benign))
  (if malignant_result
    (println "malignant_result true")
    (println "malignant_result false"))
  (if benign_result
    (println "benign_result true")
    (println "benign_result false"))

  )
(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (testAD))
