(ns knowledge-graph-navigator-clj.kgn
  (:require [knowledge-graph-navigator-clj.entities-by-name :as entity-name])
  (:require [knowledge-graph-navigator-clj.relationships :as rel])
  (:require [clojure.math.combinatorics :as combo])
  (:require [clojure.pprint :as pp]))

(def entity-map {:People       "<http://dbpedia.org/ontology/Person>"
                 :Organization "<http://dbpedia.org/ontology/Organization>"
                 :Place        "<http://dbpedia.org/ontology/Place>"})

(defn kgn
  "Top level function for the Knowledge Graph Navigator library
   Inputs: a map with keys Person, Place, and Organization. values list of names"
  [input-entity-map]
  ;;(println "* kgn:" input-entity-map)
  (let [entities-summary-data
        (filter
          (fn [x] (> (count x) 1))                          ;; get rid of empty SPARQL results
          (mapcat                                           ;; flatten just top level
            identity
            (for [entity-key (keys input-entity-map)]
              (for [entity-name (input-entity-map entity-key)]
                (cons
                  entity-name
                  (second
                    (entity-name/dbpedia-get-entities-by-name 
                     entity-name
                     (entity-map entity-key))))))))
        entity-uris (map second entities-summary-data)
        combinations-by-2-of-entity-uris (combo/combinations entity-uris 2)
        discovered-relationships
        (filter
          (fn [x] (> (count x) 0))
          (for [pair-of-uris combinations-by-2-of-entity-uris]
            (seq (rel/entity-results->relationship-links pair-of-uris))))]
    {:entity-summaries entities-summary-data
     :discovered-relationships discovered-relationships}))

(defn -main
  "Main function for KGN example"
  [& _]
  (let [results (kgn {:People       ["Bill Gates" "Steve Jobs" "Melinda Gates"]
                      :Organization ["Microsoft"]
                      :Place        ["California"]})]
    (println " -- results:") (pp/pprint results)))
