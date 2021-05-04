(ns python-interop-deeplearning.core
    (:require [libpython-clj.require :refer [require-python]]
              [libpython-clj.python :as py :refer [py. py.. py.-]]))

(require-python '[numpy :as np])
(require-python '[nltk :as nltk])
(require-python '[nltk.book :as book])
;;(require-python '[transformers :as tr])
(require-python '[spacy :as sp])

;;(def qa (tr/pipeline "question-answering" :model "NeuML/bert-small-cord19-squad2" :tokenizer "NeuML/bert-small-cord19qa"))

;;nlp = spacy.load("en_core_web_sm")
;;doc = nlp("Apple is looking at buying U.K. startup for $1 billion")

(def nlp (sp/load "en_core_web_sm"))
(def doc2 (nlp  "John Smith went to Mexico"))
(println doc2) 
(for [token doc2]
  (println token))
(let [doc (nlp "Apple is looking at buying U.K. startup for $1 billion")]
  (map (fn [token]
         [(py.- token text) (py.- token pos_) (py.- token dep_)])
       doc))


(defn foo1 []
  (np/array [[1 2][3 4]]))

(defn foo2 []
  (nltk/download "book")
  (println book/texts))

;;(defun foo3 []
;;  (println (qa { "question" "Where does Bill Gates live?"
;;                 "context"  "Bill and Melinda Gate's home is in Seatle"
;;	       })))
	       
(defn -main
  [& args]
  (println "These examples run in the repl. Don't 'lein run', rather 'lein repl'"))
  
