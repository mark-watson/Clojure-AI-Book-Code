(ns nlp-libpython-spacy.core
    (:require [libpython-clj.require :refer [require-python]]
              [libpython-clj.python :as py :refer [py. py.. py.-]]))

(require-python '[spacy :as sp])

(def nlp (sp/load "en_core_web_sm"))

(def test-text "John Smith worked for IBM in Mexico last year and earned $1 million in salary and bonuses.")

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

(defn text->entities [text]
  (map (fn [entity] (py.- entity label_))
       (py.- (nlp text) ents)))
;;  (text->entities test-text)
	       
(defn -main
  [& _]
  (println (text->entities test-text))
  (println (text->tokens-and-pos test-text))
  (println (text->pos test-text))
  (println (text->tokens test-text)))
  
