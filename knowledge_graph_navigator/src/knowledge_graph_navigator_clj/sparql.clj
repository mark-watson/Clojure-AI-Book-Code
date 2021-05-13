(ns knowledge-graph-navigator-clj.sparql
  (:require [clj-http.client :as client])
  (:require clojure.stacktrace)
  (:require [cemerick.url :refer (url-encode)])
  (:require [clojure.data.csv :as csv])
  (:require [semantic-web-jena-clj.core :as jena]))

;; Copied from https://github.com/mark-watson/clj-sparql

(def USE-LOCAL-GRAPHDB false)
(def USE-CACHING true)                                      ;; use Jena wrapper

(defn dbpedia [sparql-query]
  (let [q (str "https://dbpedia.org//sparql?output=csv&query=" (url-encode sparql-query))
        response (client/get q)
        body (:body response)]
    (csv/read-csv body)))

(defn- graphdb-helper [host port graph-name sparql-query]
  (let [q (str host ":" port "/repositories/" graph-name "?query=" (url-encode sparql-query))
        response (client/get q)
        body (:body response)]
    (csv/read-csv body)))

(defn graphdb
  ([graph-name sparql-query] (graphdb-helper "http://127.0.0.1" 7200 graph-name sparql-query))
  ([host port graph-name sparql-query] (graphdb-helper host port graph-name sparql-query)))

(defn sparql-endpoint [sparql-query]
  (try
    (if USE-LOCAL-GRAPHDB
      (graphdb "dbpedia" sparql-query)
      (if USE-CACHING
        (jena/query-dbpedia sparql-query)
        (dbpedia sparql-query)))
    (catch Exception e
      (do
        (println "WARNING: a SPARQL query failed:\n" sparql-query)
        (println (.getMessage e))
        (clojure.stacktrace/print-stack-trace e)
        []))))

(defn -main
  "SPARQL example"
  [& _]
  (println (sparql-endpoint "select * { ?s ?p ?o } limit 10")))
