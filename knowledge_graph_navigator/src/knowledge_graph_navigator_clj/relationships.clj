(ns knowledge-graph-navigator-clj.relationships
  (:require [knowledge-graph-navigator-clj.sparql :as sparql] ;; for non-cached
            [knowledge-graph-navigator-clj.colorize :as colorize])
  (:use clojure.pprint))

(defn dbpedia-get-relationships [s-uri o-uri]
  (let [query
        (clojure.string/join
          ""
          ["SELECT DISTINCT ?p {{  "
           s-uri " ?p " o-uri " . FILTER (!regex(str(?p), \"wikiPage\", \"i\")) }} LIMIT 5"])
        results (sparql/sparql-endpoint query)]
    (println "Generated SPARQL to get relationships between two entities:")
    (println (colorize/colorize-sparql query))
    (println "++++++" results)
    (map
      (fn [u] (clojure.string/join "" ["<" u ">"]))
      (second results))))                                   ; discard SPARQL variable name p (?p)

(defn entity-results->relationship-links [uris-no-brackets]
  (let [uris (map
               (fn [u] (clojure.string/join "" ["<" u ">"]))
               uris-no-brackets)
        relationship-statements (new java.util.ArrayList)]
    (doseq [e1 uris]
      (doseq [e2 uris]
        (println "e1" e1 "e2" e2)
        (if (not (= e1 e2))
          (let [l1 (dbpedia-get-relationships e1 e2)
                l2 (dbpedia-get-relationships e2 e1)]
            (println "l1" l1 "l2" l2)
            (doseq [x l1]
              (let [a-tuple [e1 x e2]]
                (if (not (. relationship-statements contains a-tuple))
                  (. relationship-statements add a-tuple))))
            (doseq [x l2]
              (let [a-tuple [e1 x e2]]
                (if (not (. relationship-statements contains a-tuple))
                  (. relationship-statements add a-tuple))))))))
    relationship-statements))

;;(pprint (entity-results->relationship-links ["http://dbpedia.org/resource/Bill_Gates" "http://dbpedia.org/resource/Microsoft"]))

(defn -main
  "I don't do a whole lot."
  [& args]
  (println "Testing entity-results->relationship-links")
  (pprint (entity-results->relationship-links ["http://dbpedia.org/resource/Bill_Gates" "http://dbpedia.org/resource/Microsoft"])))

