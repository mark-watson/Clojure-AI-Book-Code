(ns knowledge-graph-navigator-clj.sparql
  (:require [clj-http.client :as client])
  (:require clojure.stacktrace)
  (:require [cemerick.url :refer (url-encode)])
  (:require [clojure.data.csv :as csv]))

;; Copied from https://github.com/mark-watson/clj-sparql

(defn dbpedia [sparql-query]
  (let [q (str "https://dbpedia.org//sparql?output=csv&query=" (url-encode sparql-query))
        response (client/get q)
        body (:body response)]
    (csv/read-csv body)))

(defn -main
  "SPARQL example"
  [& _]
  (println (dbpedia "select * { ?s ?p ?o } limit 4")))
