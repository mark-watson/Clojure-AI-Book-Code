(ns nlp-libpython-spacy.core
  (:require [libpython-clj.require :refer [require-python]]
            [libpython-clj.python :as py :refer [py. py.-]])
  (:require [knowledge-graph-navigator-clj.entity-text-by-uri :as kgn]))

(require-python '[spacy :as sp])
(require-python '[QA :as qa])                               ;; loads the local file QA.py

(def nlp (sp/load "en_core_web_sm"))

(def test-text "John Smith worked for IBM in Mexico last year and earned $1 million in salary and bonuses.")

(defn text->tokens [text]
  ;; use py-. to 
  (map (fn [token] (py.- token text))
       (nlp text)))

(defn text->pos [text]
  (map (fn [token] (py.- token pos_))
       (nlp text)))

(defn text->tokens-and-pos [text]
  (map (fn [token] [(py.- token text) (py.- token pos_)])
       (nlp text)))

(defn text->entities [text]
  (println "!! text/entities text:" text)
  (map (fn [entity] [(py.- entity text)  (py.- entity label_)])
       (py.- (nlp text) ents)))
  
(defn qa
  "Use Transformer model for question answering"
  [question context-text]
  (qa/answer question context-text))                        ;; prints to stdout and returns a map

(defn spacy-qa-demo [natural-language-query]
  (let [entity-map
        {"PERSON" "<http://dbpedia.org/ontology/Person>"
         "ORG"    "<http://dbpedia.org/ontology/Organization>"
         "GPE"    "<http://dbpedia.org/ontology/Place>"}
        entities (text->entities natural-language-query)
        get-text-fn
        (fn [entity]
          (clojure.string/join
           " "
           (for [entity entities]
             (kgn/dbpedia-get-entity-text-by-name
              (first entity)
              (get entity-map (second entity))))))
        context-text
        (clojure.string/join
          " "
          (for [entity entities]
            (get-text-fn entity)))
        answer (qa natural-language-query context-text)]
    answer))

(defn -main
  [& _]
  (println (text->entities test-text))
  (println (text->tokens-and-pos test-text))
  (println (text->pos test-text))
  (println (text->tokens test-text))
  (qa "where does Bill call home?"
      "Since last year, Bill lives in Seattle. He likes to skateboard.")
  (qa "what does Bill enjoy?"
      "Since last year, Bill lives in Seattle. He likes to skateboard.")
  (spacy-qa-demo "what is the population of Paris?")
  (spacy-qa-demo "where does Bill Gates Work?"))
