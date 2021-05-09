(ns knowledge-graph-navigator-clj.kgn-test
  (:require [clojure.test :as test]
            [knowledge-graph-navigator-clj.kgn :as kgn])
  (:require [knowledge-graph-navigator-clj.relationships :as enitites])
  (:require [clojure.pprint :as pp]))


(test/deftest get-relations-test
    (test/testing "FIXME"
      (pp/pprint (enitites/dbpedia-get-relationships
                "<http://dbpedia.org/resource/Bill_Gates>"
                "<http://dbpedia.org/resource/Microsoft>"))
      (test/is (= 0 0))))
  
(test/deftest relations-test
  (test/testing "FIXME"
    (pp/pprint (enitites/entity-results->relationship-links
              ["http://dbpedia.org/resource/Bill_Gates"
               "http://dbpedia.org/resource/Microsoft"]))
    (test/is (= 0 0))))


(test/deftest rtop-level-test
  (test/testing "FIXME"
    (let [results
          (kgn/kgn {:People       ["Bill Gates" "Steve Jobs" "Melinda Gates"]
                    :Organization ["Microsoft"]
                    :Place        ["California"]})]
      (test/is (= (count results) 2)))))

