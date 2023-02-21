(ns semantic-web-jena-clj.core-test
  (:require [clojure.pprint :as pprint]
            [clojure.test :refer :all]
            [semantic-web-jena-clj.core :refer :all]))

(deftest load-data-and-sample-queries
  (testing "Load local triples files and make some SPARQL queries"
    (load-rdf-file "data/sample_news.nt")
    (let [results (query "select * { ?s ?p ?o } limit 5")]
      (pprint/pprint results)
      (is (= (count results) 6)))))

(deftest dbpedia-test
  (testing "Try SPARQL query to DBPedia endpoint"
    (pprint/pprint
      (query-dbpedia
        "select ?p where { <http://dbpedia.org/resource/Bill_Gates> ?p <http://dbpedia.org/resource/Microsoft> . } limit 10"))))

(deftest wikidata-test
  (testing "Try SPARQL query to WikiData endpoint"
    (pprint/pprint
      (query-dbpedia
        "select * where { ?subject ?property ?object . } limit 10"))))
