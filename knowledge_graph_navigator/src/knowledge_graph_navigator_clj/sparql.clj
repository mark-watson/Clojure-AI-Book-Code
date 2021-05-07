(ns knowledge-graph-navigator-clj.sparql
  (:require [clj-http.client :as client])
  (:require [clojure.data.json :as json]))

(require '[cemerick.url :refer (url-encode)])

;; Copied from https://github.com/mark-watson/clj-sparql

(defn dbpedia [sparql-query]
  (let [q (str "https://dbpedia.org//sparql?output=json&query=" (url-encode sparql-query))
        _ (println q)
        response (client/get q)
        body(json/read-str (:body response))
        vars ((body "head") "vars")
        values (map
                 (fn [x]
                   (for [v vars]
                     ((x v) "value")))
                 ((body "results") "bindings"))]
    (cons vars values)))

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

(defn -main
  "I don't do a whole lot."
  [& args]
  (println (dbpedia "select * { ?s ?p ?o } limit 2")))

