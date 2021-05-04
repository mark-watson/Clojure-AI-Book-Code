(ns anomaly-detection-clj.visualization
  (:use clojure.pprint)
  (:use (incanter core stats charts)))

(require '[clojure.data.csv :as csv]
         '[clojure.java.io :as io]
         ;;'[clojure.math.numeric-tower :as math]
         )

(view (histogram (sample-normal 1000)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (view (histogram (sample-normal 1000))))
