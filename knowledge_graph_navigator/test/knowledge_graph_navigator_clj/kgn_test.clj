(ns knowledge-graph-navigator-clj.kgn-test
  (:require [clojure.test :refer :all]
            [knowledge-graph-navigator-clj.kgn :refer :all])
  (:require [knowledge-graph-navigator-clj.relationships :as enitites])
  (:refer clojure.pprint :only [pprint]))


(deftest get-relations-test
    (testing "FIXME"
      (pprint (enitites/dbpedia-get-relationships
                "<http://dbpedia.org/resource/Bill_Gates>"
                "<http://dbpedia.org/resource/Microsoft>"))
      (is (= 0 0))))
  
(deftest relations-test
  (testing "FIXME"
    (pprint (enitites/entity-results->relationship-links
              ["http://dbpedia.org/resource/Bill_Gates"
               "http://dbpedia.org/resource/Microsoft"]))
    (is (= 0 0))))

