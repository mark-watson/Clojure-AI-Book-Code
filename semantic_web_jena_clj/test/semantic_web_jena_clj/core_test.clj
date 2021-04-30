(ns semantic-web-jena-clj.core-test
  (:require [clojure.test :refer :all]
            [semantic-web-jena-clj.core :refer :all]))

(deftest load-data-and-sample-queries
  (testing "Load local triples files and make some SPARQL queries"
    (load-rdf-file "data/sample_news.nt")
    (let [results (query "select * { ?s ?p ?o } limit 5")
          variable-list (seq (. results variableList))]
      (println results)
      (println variable-list))
    (is (= 0 0))))
