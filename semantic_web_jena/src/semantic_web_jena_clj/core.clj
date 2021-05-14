(ns semantic-web-jena-clj.core
  (:import (com.markwatson.semanticweb JenaApis Cache QueryResult)))

(defn- get-jena-api-model "get a default model with OWL reasoning" []
  (new JenaApis))

(defonce model (get-jena-api-model))

(defn- results->clj [results]
  (let [variable-list (seq (. results variableList))
        bindings-list (seq (map seq (. results rows)))]
    (cons variable-list bindings-list)))

(defn load-rdf-file [fpath]
  (. model loadRdfFile fpath))

(defn query "SPARQL query" [sparql-query]
  (results->clj (. model query sparql-query)))

(defn query-remote "remote service like DBPedia, etc." [remote-service sparql-query]
  (results->clj (. model queryRemote remote-service sparql-query)))

(defn query-dbpedia [sparql-query]
  (query-remote "https://dbpedia.org/sparql" sparql-query))

(defn query-wikidata [sparql-query]
  (query-remote "https://query.wikidata.org/bigdata/namespace/wdq/sparql" sparql-query))
