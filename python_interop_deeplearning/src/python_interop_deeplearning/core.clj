(ns python-interop-deeplearning.core
    (:require [libpython-clj.require :refer [require-python]]
              [libpython-clj.python :as py :refer [py. py.. py.-]]))

(require-python '[numpy :as np])
(require-python '[nltk :as nltk])
(require-python '[nltk.book :as book])
(require-python '[spacy :as sp])

;;(def qa (tr/pipeline "question-answering" :model "NeuML/bert-small-cord19-squad2" :tokenizer "NeuML/bert-small-cord19qa"))

;;nlp = spacy.load("en_core_web_sm")
;;doc = nlp("Apple is looking at buying U.K. startup for $1 billion")

(def nlp (sp/load "en_core_web_sm"))

(def test-text "John Smith worked for IBM in Mexico last year and earned $1 million in salary and bonuses.")
(def doc2 (nlp test-text))
(println doc2)
(doseq [token doc2]
  (println token))
(let [doc (nlp "Apple is looking at buying U.K. startup for $1 billion")]
  (map (fn [token]
         [(py.- token text) (py.- token pos_) (py.- token dep_)])
       doc))
(defn text->tokens [text]
  (map (fn [token] (py.- token text))
       (nlp text)))
;;  (text->tokens test-text)
(defn text->pos [text]
  (map (fn [token] (py.- token pos_))
       (nlp text)))
;;  (text->pos test-text)
  
(defn text->tokens-and-pos [text]
  (map (fn [token] [(py.- token text) (py.- token pos_)])
       (nlp text)))
;;  (text->tokens-and-pos test-text)
;;for ent in doc.ents:
;;    print(ent.text, ent.start_char, ent.end_char, ent.label_)
(defn text->entities [text]
  (map (fn [entity] (py.- entity label_))
       (py.- (nlp text) ents)))
;;  (text->entities test-text)

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
  
