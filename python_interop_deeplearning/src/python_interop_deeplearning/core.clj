(ns python-interop-deeplearning.core
    (:require [libpython-clj.require :refer [require-python]]
              [libpython-clj.python :as py :refer [py. py.. py.-]]))

(require-python '[numpy :as np])
(require-python '[nltk :as nltk])
(require-python '[nltk.book :as book])

(defn foo1 []
  (np/array [[1 2][3 4]]))

(defn foo2 []
  (nltk/download "book")
  (println book/texts))
  
(defn -main
  [& args]
  (println "These examples run in the repl. Don't 'lein run', rather 'lein repl'"))
  
