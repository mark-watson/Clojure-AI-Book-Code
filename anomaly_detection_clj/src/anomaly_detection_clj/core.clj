(ns anomaly-detection-clj.core
  (:gen-class))

(require '[clojure.data.csv :as csv]
         '[clojure.java.io :as io])

(import (com.markwatson.anomaly_detection AnomalyDetection))

(defn testAD []
  (def ad (AnomalyDetection.))
  (println ad)
  (def cdata
    (with-open [reader (io/reader "data/cleaned_wisconsin_cancer_data.csv")]
      (doall
        (csv/read-csv reader))))

  ;;(def csvdata (slurp "data/cleaned_wisconsin_cancer_data.csv"))
  (println cdata)


  )
(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!")
)
