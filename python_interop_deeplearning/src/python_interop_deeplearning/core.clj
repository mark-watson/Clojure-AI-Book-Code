(ns python-interop-deeplearning.core
    (:require [libpython-clj.require :refer [require-python]]
              [libpython-clj.python :as py :refer [py. py.. py.-]]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  ;;(py/initialize! :python-executable "/Users/markw/bin/conda/bin/python"
  ;;                :library-path "/Users/markw/bin/conda/pkgs/python-3.8.8-h4e93d89_0_cpython/lib/libpython3.8.dylib")
  (require-python '[numpy :as np])
  (def test-ary (np/array [[1 2][3 4]]))
  (println test-ary))
