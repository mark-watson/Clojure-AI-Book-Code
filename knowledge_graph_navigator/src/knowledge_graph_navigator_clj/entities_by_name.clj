(ns knowledge-graph-navigator-clj.entities-by-name
  (:require [knowledge-graph-navigator-clj.sparql :as sparql])
  (:require [knowledge-graph-navigator-clj.sparql-utils :as utils])
  (:require [clojure.pprint :as pp])
  (:require clojure.string))

(defn dbpedia-get-entities-by-name [name dbpedia-type]
  ;(println "** dbpedia-get-entities-by-name: name=" name "dbpedia-type=" dbpedia-type)
  (let [sparql-query
        (utils/sparql_template
          "entities_by_name.sparql"
          {"<NAME>" name "<ENTITY_TYPE>" dbpedia-type})
        results (sparql/sparql-endpoint sparql-query)]
    ;(println "Generated SPARQL to get DBPedia entity URIs from a name:")
    (println sparql-query)
    ;(println "Results:") (pprint results)
    results))

(defn -main
  "test/dev entities by name"
  [& _]
  (println (dbpedia-get-entities-by-name "Steve Jobs" "<http://dbpedia.org/ontology/Person>"))
  (println (dbpedia-get-entities-by-name "Microsoft" "<http://dbpedia.org/ontology/Organization>"))
  (pp/pprint (dbpedia-get-entities-by-name "California" "<http://dbpedia.org/ontology/Place>"))
  )
