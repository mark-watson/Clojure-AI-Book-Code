(ns knowledge-graph-navigator-clj.entity-text-by-uri
  (:require [knowledge-graph-navigator-clj.sparql :as sparql])
  (:require [knowledge-graph-navigator-clj.sparql-utils :as utils])
  (:require [clojure.pprint :as pp])
  (:require clojure.string))

(defn dbpedia-get-entity-text-by-name [name dbpedia-type]
  (let [sparql-query
        (utils/sparql_template
          "get_entity_text.sparql"
          {"<ENTITY_NAME>" name "<ENTITY_DBPEDIA_TYPE_URI>" dbpedia-type})
        results (sparql/dbpedia sparql-query)]
    (clojure.string/join " " (map second (rest results)))))

(defn -main
  "test/dev entities by name"
  [& _]
  (println (dbpedia-get-entity-text-by-name "Paris" "<http://dbpedia.org/ontology/Place>"))
  (println (dbpedia-get-entity-text-by-name "IBM" "<http://dbpedia.org/ontology/Organization>"))
  (println (dbpedia-get-entity-text-by-name "Bill Gates" "<http://dbpedia.org/ontology/Person>")))
