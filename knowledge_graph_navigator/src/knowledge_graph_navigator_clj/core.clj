(ns knowledge-graph-navigator-clj.core
  (:require [knowledge-graph-navigator-clj.sparql :as sparql])
  (:require [knowledge-graph-navigator-clj.kgn-utils :as utils])
  (:use clojure.pprint))

(defn  -main
  "I don't do a whole lot."
  [& args]
  (println (sparql/dbpedia "select * where { ?subject ?property ?object . } limit 10"))
  (pprint (utils/dbpedia-get-entities-by-name "Bill Gates" "<http://dbpedia.org/ontology/Person>")))
