(ns knowledge-graph-navigator-clj.core-test
  (:require [clojure.test :refer :all]
            [knowledge-graph-navigator-clj.core :refer :all])
  (:require [knowledge-graph-navigator-clj.sparql :as sparql] ;; for non-cached
            [knowledge-graph-navigator-clj.cache :as cache]
            [knowledge-graph-navigator-clj.relationships :as enitites]
            [knowledge-graph-navigator-clj.colorize :as colorize])
  (:use clojure.pprint))


(comment
  (deftest get-relations-test
    (testing "FIXME"
      (pprint (enitites/dbpedia-get-relationships
                "<http://dbpedia.org/resource/Bill_Gates>"
                "<http://dbpedia.org/resource/Microsoft>"))
      (is (= 0 0))))
  )
(deftest relations-test
  (testing "FIXME"
    (pprint (enitites/entity-results->relationship-links
              ["http://dbpedia.org/resource/Bill_Gates"
               "http://dbpedia.org/resource/Microsoft"]))
    (is (= 0 0))))

