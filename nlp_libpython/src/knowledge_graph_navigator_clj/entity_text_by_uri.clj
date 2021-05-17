(ns knowledge-graph-navigator-clj.entity-text-by-uri
  (:require [knowledge-graph-navigator-clj.sparql :as sparql])
  (:require [knowledge-graph-navigator-clj.sparql-utils :as utils])
  (:require [clojure.pprint :as pp])
  (:require clojure.string))

(defn dbpedia-get-entity-text-by-name [name dbpedia-type]
  ;(println "** dbpedia-get-entities-by-name: name=" name "dbpedia-type=" dbpedia-type)
  (let [sparql-query
        (utils/sparql_template
          "get_entity_text.sparql"
          {"<ENTITY_NAME>" name "<ENTITY_DBPEDIA_TYPE_URI>" dbpedia-type})
        _ (println "++++  sparql-query-text:" sparql-query)
        results (sparql/dbpedia sparql-query)
        _ (println "++++  results:" results)]
    results))

(defn -main
  "test/dev entities by name"
  [& _]
  (println (dbpedia-get-entities-by-name "Steve Jobs" "<http://dbpedia.org/ontology/Person>")))
