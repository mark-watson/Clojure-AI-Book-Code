(ns knowledge-graph-navigator-clj.cache
  (:require [knowledge-graph-navigator-clj.sparql :as sparql])
  (:use clojure.pprint))

(require '[next.jdbc :as jdbc])
(require '[next.jdbc.result-set :as rs])

(def db {:dbtype "h2" :dbname "dbpedia"})
(def ds (jdbc/get-datasource db))

(defn create-table-if-not-exists []
  (try
    (jdbc/execute! ds ["CREATE TABLE DBPEDIA (query varchar(800), result varchar(18192))"])
    (catch Exception e)))

(defn read-cache [a-query]
  (create-table-if-not-exists)
  (let [results (jdbc/execute-one! ds [(str "select * from DBPEDIA where query='" a-query "'")]
                                   {:builder-fn rs/as-unqualified-lower-maps})]
    (let [r (get results :result nil)]
      (let [ret2 (if r (read-string (str "[" r "]")) nil)]
        ret2))))

(defn cached-sparql-query [a-query]
  (let [cached-result (read-cache a-query)]
    (or
      cached-result
      (do
        (let [result (sparql/dbpedia a-query)
              sql-insert (clojure.string/replace
                           (str "insert into DBPEDIA(query,result) values('" a-query "','" result "')")
                           ;;(str "insert into DBPEDIA(query,result) values('" a-query "','" (apply str result) "')")
                            #"\"\"" "\"")]
                            ;;#"\"\"" "\"")]
          (jdbc/execute! ds [sql-insert])
          (println "Writing data to cache...")
          result)))))

(comment
  (def test-query "select * { ?s ?p ?o } limit 3")
  (def z (cached-sparql-query test-query))
  (println "z" z)
  (println "- first:" (first z))
  (println "- second:" (second z)))
