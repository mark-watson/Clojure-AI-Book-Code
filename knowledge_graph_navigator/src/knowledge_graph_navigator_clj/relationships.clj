(ns knowledge-graph-navigator-clj.relationships
  (:require [knowledge-graph-navigator-clj.sparql :as sparql]) ;; for non-cached
  (:require [knowledge-graph-navigator-clj.sparql-utils :as utils])
  (:require [clojure.pprint :as pp])
  (:require clojure.string))

(defn dbpedia-get-relationships [s-uri o-uri]
  (let [query
        (utils/sparql_template
          "relationships.sparql"
          {"<URI1>" s-uri "<URI2>" o-uri})
        results (sparql/sparql-endpoint query)]
    (map
      (fn [u] (clojure.string/join "" ["<" u ">"]))
      (second results))))                                   ; discard SPARQL variable name p (?p)

(defn entity-results->relationship-links [uris-no-brackets]
  (let [uris (map
               (fn [u] (clojure.string/join "" ["<" u ">"]))
               uris-no-brackets)
        relationship-statements (atom [])]
    (doseq [e1 uris]
      (doseq [e2 uris]
        (if (not (= e1 e2))
          (let [l1 (dbpedia-get-relationships e1 e2)
                l2 (dbpedia-get-relationships e2 e1)]
            (doseq [x l1]
              (let [a-tuple [e1 x e2]]
                (if (not (. @relationship-statements contains a-tuple))
                  (reset! relationship-statements (cons a-tuple @relationship-statements))
                  nil))
            (doseq [x l2]
              (let [a-tuple [e2 x e1]]
                (if (not (. @relationship-statements contains a-tuple))
                  (reset! relationship-statements (cons a-tuple @relationship-statements))
                  nil)))))
          nil)))
    @relationship-statements))

(defn -main
  "dev/test entity relationships code"
  [& _]
  (println "Testing entity-results->relationship-links")
  (pp/pprint (entity-results->relationship-links ["http://dbpedia.org/resource/Bill_Gates" "http://dbpedia.org/resource/Microsoft"])))

