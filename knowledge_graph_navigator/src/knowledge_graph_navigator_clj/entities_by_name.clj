(ns knowledge-graph-navigator-clj.entities-by-name
  (:require [knowledge-graph-navigator-clj.sparql :as sparql])
  (:require [clojure.pprint :as pp])
  (:require clojure.string))

(defn dbpedia-get-entities-by-name [name dbpedia-type]
  ;(println "** dbpedia-get-entities-by-name: name=" name "dbpedia-type=" dbpedia-type)
  (let [sparql-query
        (clojure.string/join
          ""
          ["select distinct ?s ?comment where { ?s <http://www.w3.org/2000/01/rdf-schema#label> \""
           name
           "\"@en . ?s <http://www.w3.org/2000/01/rdf-schema#comment>  ?comment  . FILTER  (lang(?comment) = \"en\") . ?s <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> "
           dbpedia-type
           ". }"])
        results (sparql/sparql-endpoint sparql-query)]
    ;(println "Generated SPARQL to get DBPedia entity URIs from a name:")
    ;(println (colorize/colorize-sparql sparql-query))
    ;(println "Results:") (pprint results)
    results))

(defn -main
  "I don't do a whole lot."
  [& _]
  (println (dbpedia-get-entities-by-name "Steve Jobs" "<http://dbpedia.org/ontology/Person>"))
  (println (dbpedia-get-entities-by-name "Microsoft" "<http://dbpedia.org/ontology/Organization>"))
  (pp/pprint (dbpedia-get-entities-by-name "California" "<http://dbpedia.org/ontology/Place>"))
  )
