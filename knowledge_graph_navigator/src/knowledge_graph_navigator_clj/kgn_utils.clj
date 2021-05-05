(ns knowledge-graph-navigator-clj.kgn-utils
  (:require [knowledge-graph-navigator-clj.sparql :as sparql]
            [knowledge-graph-navigator-clj.colorize :as colorize]))

(defn dbpedia-get-entities-by-name [name dbpedia-type]
  (let [sparql-query
        (clojure.string/join
          ""
          ["select distinct ?s ?comment {{ ?s ?p \""
           name
           "\"@en . ?s <http://www.w3.org/2000/01/rdf-schema#comment>  ?comment  . FILTER  (lang(?comment) = 'en') . ?s <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> "
           dbpedia-type
           ". }}"])]
    (print "Generated SPARQL to get DBPedia entity URIs from a name:")
    (print (colorize/colorize-sparql sparql-query))
    (sparql/dbpedia sparql-query)))

;;(pprint (dbpedia-get-entities-by-name "Bill Gates" "<http://dbpedia.org/ontology/Person>"))
