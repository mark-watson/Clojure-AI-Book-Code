(ns semantic-web-jena-clj.core
  (:import (com.markwatson.semanticweb JenaApis Cache QueryResult)))

(defn- get-jena-api-model "get a default model with OWL reasoning" []
  (new JenaApis))

(defonce model (get-jena-api-model))

(defn load-rdf-file [fpath]
  (. model loadRdfFile fpath))

(defn query "SPARQL query" [sparql-query]
  (. model query sparql-query))