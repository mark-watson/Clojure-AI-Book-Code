;; Simple RDF SPARQL. Copyright 2024 Mark Watson. All rights reserved.
;; GNU AFFERO GENERAL PUBLIC LICENSE Version 3
                       
(ns simple-rdf-sparql.core 
  ;;(:require [clojure.pprint :refer [pprint]])
  (:require [clojure.string :as str]))

;; RDF triple structure
(defrecord Triple [subject predicate object])

;; RDF datastore
(def ^:dynamic *rdf-store* (atom []))

;; Add a triple to the datastore
(defn add-triple [subject predicate object]
  (swap! *rdf-store* conj (->Triple subject predicate object)))

;; Remove a triple from the datastore
(defn remove-triple [subject predicate object]
  (swap! *rdf-store* (fn [store]
                       (remove #(and (= (:subject %) subject)
                                     (= (:predicate %) predicate)
                                     (= (:object %) object))
                               store))))

;; Helper function to check if a string is a variable
(defn variable? [s]
  (and (string? s) (not (empty? s)) (= (first s) \?)))

;; Convert triple to binding
(defn triple-to-binding [triple pattern]
  (into {}
        (filter second
                (map (fn [field pattern-item]
                       (when (variable? pattern-item)
                         [pattern-item (field triple)]))
                     [:subject :predicate :object]
                     pattern))))

(defn query-triples [subject predicate object]
  (filter (fn [triple]
            (and (or (nil? subject) (variable? subject) (= (:subject triple) subject))
                 (or (nil? predicate) (variable? predicate) (= (:predicate triple) predicate))
                 (or (nil? object) (variable? object) (= (:object triple) object))))
          @*rdf-store*))

;; Print all triples in the datastore
(defn print-all-triples []
  (println "All triples in the datastore:")
  (doseq [triple @*rdf-store*]
    (println (str (:subject triple) " " (:predicate triple) " " (:object triple))))
  (println))

;; SPARQL query structure
(defrecord SPARQLQuery [select-vars where-patterns])

;; Apply bindings to a pattern
(defn apply-bindings [pattern bindings]
  (mapv (fn [item]
          (if (variable? item)
            (get bindings item item)
            item))
        pattern))

;; Merge bindings
(defn merge-bindings [binding1 binding2]
  (merge binding1 binding2))

(defn parse-where-patterns [where-clause]
  (loop [tokens where-clause
         current-pattern []
         patterns []]
    (cond
      (empty? tokens)
      (if (empty? current-pattern)
        patterns
        (conj patterns current-pattern))

      (= (first tokens) ".")
      (recur (rest tokens)
             []
             (if (empty? current-pattern)
               patterns
               (conj patterns current-pattern)))

      :else
      (recur (rest tokens)
             (conj current-pattern (first tokens))
             patterns))))

(defn parse-sparql-query [query-string]
  (let [tokens (remove #{"{"  "}"} (str/split query-string #"\s+"))
        select-index (.indexOf tokens "select")
        where-index (.indexOf tokens "where")
        select-vars (subvec (vec tokens) (inc select-index) where-index)
        where-clause (subvec (vec tokens) (inc where-index))
        where-patterns (parse-where-patterns where-clause)]
    (->SPARQLQuery select-vars where-patterns)))

(defn remove-duplicate-bindings [bindings]
  (into {} bindings))

(defn project-results [results select-vars]
  (if (= select-vars ["*"])
    (map remove-duplicate-bindings results)
    (map (fn [result]
           (remove-duplicate-bindings
            (select-keys result select-vars)))
         results)))

;; Execute WHERE patterns with bindings
(defn execute-where-patterns-with-bindings [patterns bindings]
  (if (empty? patterns)
    [bindings]
    (let [pattern (first patterns)
          remaining-patterns (rest patterns)
          bound-pattern (apply-bindings pattern bindings)
          matching-triples (apply query-triples bound-pattern)
          new-bindings (map #(merge-bindings bindings (triple-to-binding % pattern))
                            matching-triples)]
      (if (empty? remaining-patterns)
        new-bindings
        (mapcat #(execute-where-patterns-with-bindings remaining-patterns %)
                new-bindings)))))

(defn execute-where-patterns [patterns]
  (if (empty? patterns)
    [{}]
    (let [pattern (first patterns)
          remaining-patterns (rest patterns)
          matching-triples (apply query-triples pattern)
          bindings (map #(triple-to-binding % pattern) matching-triples)]
      (if (empty? remaining-patterns)
        bindings
        (mapcat (fn [binding]
                  (let [results (execute-where-patterns-with-bindings remaining-patterns binding)]
                    (map #(merge-bindings binding %) results)))
                bindings)))))

(defn execute-sparql-query [query-string]
  (let [query (parse-sparql-query query-string)
        where-patterns (:where-patterns query)
        select-vars (:select-vars query)
        results (execute-where-patterns where-patterns)
        projected-results (project-results results select-vars)]
    projected-results))

(defn print-query-results [query-string]
  (println "Query:" query-string)
  (let [results (execute-sparql-query query-string)]
    (println "Final Results:")
    (if (empty? results)
      (println "  No results")
      (doseq [result results]
        (println "  " (str/join ", " (map (fn [[k v]] (str k ": " v)) result)))))
    (println)))

(defn -main []
  (reset! *rdf-store* [])

  (add-triple "John" "age" "30")
  (add-triple "John" "likes" "pizza")
  (add-triple "Mary" "age" "25")
  (add-triple "Mary" "likes" "sushi")
  (add-triple "Bob" "age" "35")
  (add-triple "Bob" "likes" "burger")

  (print-all-triples)

  (print-query-results "select * where { ?name age ?age . ?name likes ?food }")
  (print-query-results "select ?s ?o where { ?s likes ?o }")
  (print-query-results "select * where { ?name age ?age . ?name likes pizza }"))
