(ns knowledge-graph-navigator-clj.entities-by-name
  (:require [knowledge-graph-navigator-clj.sparql :as sparql]
            [knowledge-graph-navigator-clj.cache :as cache]
            [knowledge-graph-navigator-clj.colorize :as colorize])
  (:use clojure.pprint))

(defn dbpedia-get-entities-by-name [name dbpedia-type]
  (let [sparql-query
        (clojure.string/join
          ""
          ["select distinct ?s ?comment where { ?s <http://www.w3.org/2000/01/rdf-schema#label> \""
           name
           "\"@en . ?s <http://www.w3.org/2000/01/rdf-schema#comment>  ?comment  . FILTER  (lang(?comment) = \"en\") . ?s <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> "
           dbpedia-type
           ". }"])]
    (println "Generated SPARQL to get DBPedia entity URIs from a name:")
    (println (colorize/colorize-sparql sparql-query))
    (cache/cached-sparql-query sparql-query)))
    ;; for non-cached queries use: (sparql/dbpedia sparql-query)

(defn -main
  "I don't do a whole lot."
  [& args]
  (println (dbpedia-get-entities-by-name "Steve Jobs" "<http://dbpedia.org/ontology/Person>")))
