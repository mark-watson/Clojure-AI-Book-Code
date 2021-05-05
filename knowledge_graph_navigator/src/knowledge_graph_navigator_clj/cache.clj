(ns knowledge-graph-navigator-clj.cache
  (:require [knowledge-graph-navigator-clj.sparql :as sparql])
  (:use clojure.pprint))

(require '[next.jdbc :as jdbc])
(require '[next.jdbc.result-set :as rs])

(def db {:dbtype "h2" :dbname "dbpedia"})
(def ds (jdbc/get-datasource db))

(defn create-table-if-not-exists []
  (try
    (jdbc/execute! ds ["CREATE TABLE DBPEDIA (query varchar(256), result varchar(8192))"])
    (catch Exception e)))

(defn read-cache [a-query]
  (create-table-if-not-exists)
  (let [results (jdbc/execute-one! ds [(str "select * from DBPEDIA where query='" a-query "'")]
                                   {:builder-fn rs/as-unqualified-lower-maps})]
    (println "* read-cache: results=" results)
    (println "* read-cache: (type results)=" (type results))
    (let [r (get results :result nil)]
      (println "* read-cache: r=" r)
      (println "* read-cache: (type r)=" (type r))
      (let [ret2 (if r (read-string (str "[" r "]")) nil)]
        (println "* read-cache: ret2=") (pprint ret2)
        (println "* read-cache: (type ret2)=" (type ret2))
        ret2))))

(def test-query "select * { ?s ?p ?o } limit 2")

(defn cached-sparql-query [a-query]
  (let [cached-result (read-cache a-query)]
    (println "* * * cached-sparql-query: cached-result=" cached-result)
    (or
      cached-result
      (do
        (let [result (sparql/dbpedia a-query)
              sql-insert (str "insert into DBPEDIA(query,result) values('" a-query "','" (apply str result) "')")]
          (println "* cached-sparql-query: result=" result)
          (println "* cached-sparql-query: sql-insert=" sql-insert)
          (jdbc/execute! ds [sql-insert])
          ;;(insert! db :dbpedia {:query a-query :result (str (doall result))})
          (println "Writing data to cache...")
          result)))))

(create-table-if-not-exists)
;;(println (insert! db :dbpedia testdata))
;;(println (read-cache (str "select * from DBPEDIA where query='" test-query "'")))

;;(println "jdbc/execute! 1" (jdbc/execute! ds [(str "select * from DBPEDIA")]))
;;(println "read-cache 1" (read-cache test-query))
;;(println "jdbc/execute! 2" (jdbc/execute! ds [(str "select * from DBPEDIA")]))
(def z (cached-sparql-query test-query))

(println "z" z)

(println "- first:" (first z))
(println "- second:" (second z))
