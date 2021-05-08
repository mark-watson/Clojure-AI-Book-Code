(ns knowledge-graph-navigator-clj.kgn
  (:require [knowledge-graph-navigator-clj.entities-by-name :as entity-name])
  (:require [knowledge-graph-navigator-clj.relationships :as rel])
  (:require [clojure.math.combinatorics :as combo])
  (:refer clojure.pprint :only [pprint]))

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
          (fn [x] (> (count x) 1))                          ;; get rid of emty PSARQL results
          (mapcat                                           ;; flatten just top level
            identity
            (for [entity-key (keys input-entity-map)]
              (for [entity-name (input-entity-map entity-key)]
                (cons
                  entity-name
                  (second
                    (entity-name/dbpedia-get-entities-by-name entity-name (entity-map entity-key))))))))
        entity-uris (map second entities-summary-data)
        combinations-by-2-of-entity-uris (combo/combinations entity-uris 2)
        discovered-relationships
        (for [pair-of-uris combinations-by-2-of-entity-uris]
          (rel/entity-results->relationship-links pair-of-uris))]
    (println "++++  entities-summary-data:") (println entities-summary-data)
    (println "++++  combinations-by-2-of-entity-uris:") (println combinations-by-2-of-entity-uris)
    discovered-relationships))

(defn -main
  "I don't do a whole lot."
  [& args]
  (let [results (kgn {:People        ["Bill Gates" "Steve Jobs" "Melinda Gates"]
                      :Organization ["Microsoft"]
                      :Place        ["California"]})]
    (println " -- results:") (pprint results)
    (doseq [result results]
      (println "* next result:") (println result))))
