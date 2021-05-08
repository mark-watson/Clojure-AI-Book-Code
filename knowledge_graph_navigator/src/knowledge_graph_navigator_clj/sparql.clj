(ns knowledge-graph-navigator-clj.sparql
  (:require [clj-http.client :as client])
  (:require [clojure.data.json :as json]))

(require '[cemerick.url :refer (url-encode)])
(require '[clojure.data.csv :as csv])

;; Copied from https://github.com/mark-watson/clj-sparql

(defn dbpedia [sparql-query]
  ;;(let [q (str "https://dbpedia.org//sparql?output=csv&query=" (url-encode sparql-query))
  (let [q (str "http://127.0.0.1:8080/sparql?output=csv&query=" (url-encode sparql-query))
        _ (println q)
        response (client/get q)
        body (:body response)]
    (csv/read-csv body)))

(defn wikidata
  "note: WikiData currently does not return /text/csv values, even when requested"
  [sparql-query]
  (let [q (str "http://query.wikidata.org/bigdata/namespace/wdq/sparql?query=" (url-encode sparql-query))
        response (client/get q {:accept "application/sparql-results+json"})
        body (json/read-str (:body response))
        vars ((body "head") "vars")
        values (map
                (fn [x]
                  (for [v vars]
                    ((x v) "value")))
                ((body "results") "bindings"))]
    (cons vars values)))


(defn- graphdb-helper [host port graph-name sparql-query]
  (let [q (str host ":" port "/repositories/" graph-name "?query=" (url-encode sparql-query))
        response (client/get q)
        body (:body response)]
    (csv/read-csv body)))

(defn graphdb
  ([graph-name sparql-query] (graphdb-helper "http://127.0.0.1" 7200 graph-name sparql-query))
  ([host port graph-name sparql-query] (graphdb-helper host port graph-name sparql-query)))

(defn sparql-endpoint [sparql-query]
  ;;(println "\nSPARQL:\n" sparql-query)
  (try
    (graphdb "dbpedia" sparql-query)
    ;;(dbpedia sparql-query)
    (catch Exception e
      (do
        (println "WARNING: a SPARQL query failed:\n" sparql-query)
        (println (.getMessage e))
        (clojure.stacktrace/print-stack-trace e)
        []))))

(defn -main
  "I don't do a whole lot."
  [& args]
  (println (sparql-endpoint "select * { ?s ?p ?o } limit 10")))
