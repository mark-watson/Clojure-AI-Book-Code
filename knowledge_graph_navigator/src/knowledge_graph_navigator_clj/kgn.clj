(ns knowledge-graph-navigator-clj.kgn
  (:require [knowledge-graph-navigator-clj.sparql :as sparql])
  (:require [knowledge-graph-navigator-clj.entities-by-name :as entity-name])
  (:use clojure.pprint))
(require 'clojure.walk)

(def entity-map {:People       "<http://dbpedia.org/ontology/Person>"
                 :Organization "<http://dbpedia.org/ontology/Organization>"
                 :Place        "<http://dbpedia.org/ontology/Place>"})

(defn kgn
  "Top level function for the Knowledge Graph Navigator library
   Inputs: a map with keys Person, Place, and Organization. values list of names"
  [input-entity-map]
  ;;(println "* kgn:" input-entity-map)
  (let [entity-URI-list                                     ;; (new java.util.ArrayList)]
        (mapcat ;; flatten just top level
          identity
          (for [entity-key (keys input-entity-map)]
            (for [entity-name (input-entity-map entity-key)]
              (second
                (entity-name/dbpedia-get-entities-by-name entity-name (entity-map entity-key))))))]
    (println "++++  entity-URI-list:") (println entity-URI-list)
    entity-URI-list))

(defn -main
  "I don't do a whole lot."
  [& args]
  (let [results (kgn {:People ["Bill Gates" "Steve Jobs"]})]
    (println " -- results:") (println results)
    (doseq [result results]
      (println "* next result:") (println result))))
