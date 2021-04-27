(ns anomaly-detection-clj.core
  (:gen-class))

(import (com.markwatson.anomaly_detection AnomalyDetection))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (def ad (AnomalyDetection.))
  (println ad)
  (println "Hello, World!"))
