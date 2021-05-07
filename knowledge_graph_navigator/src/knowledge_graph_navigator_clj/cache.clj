(ns knowledge-graph-navigator-clj.cache
  (:require [knowledge-graph-navigator-clj.sparql :as sparql])
  (:use clojure.pprint))

(require '[cemerick.url :refer (url-encode url-decode)])

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
  (let [s1 (url-encode (pr-str a-query))
        results (jdbc/execute-one! ds [(str "select * from DBPEDIA where query='" s1 "'")]
                                   {:builder-fn rs/as-unqualified-lower-maps})
        data (get results :result nil)]
    ;;(pprint results)
    (pprint data)
    (pprint (url-decode data))
    (if data (read-string (url-decode data) nil))))

(defn cached-sparql-query [a-query]
  (let [cached-result (read-cache a-query)]
    (or
      cached-result
      (do
        (let [result (sparql/dbpedia a-query)
              s1 (url-encode  (pr-str a-query))
              s2 (url-encode (pr-str result))
              sql-insert (clojure.string/replace
                           (str "insert into DBPEDIA(query,result) values('" s1 "','" s2 "')")
                           ;;(str "insert into DBPEDIA(query,result) values('" a-query "','" (apply str result) "')")
                            #"\"\"" "\"")]
                            ;;#"\"\"" "\"")]
          (jdbc/execute! ds [sql-insert])
          (println "Writing data to cache...")
          result)))))

(defn -main [& args]
  (def test-query "select * { ?s ?p ?o } limit 5")

  (def z (cached-sparql-query test-query))
  (println "z" z)
  (println "(type z)" (type z))
  (println "- first:" (first z))
  (println "- second:" (second z))

  (def z2 (cached-sparql-query test-query))
  (println "z2" z2)
  (println "(type z2)" (type z2))
  (println "- first:" (first z2))
  (println "- second:" (second z2)))

