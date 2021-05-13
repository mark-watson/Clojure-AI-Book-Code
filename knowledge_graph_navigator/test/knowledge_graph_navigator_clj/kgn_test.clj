(ns knowledge-graph-navigator-clj.kgn-test
  (:require [clojure.test :as test]
            [knowledge-graph-navigator-clj.kgn :as kgn])
  (:require [knowledge-graph-navigator-clj.relationships :as enitites])
  (:require [clojure.pprint :as pp]))


(test/deftest get-relations-test
    (test/testing "test dbpedia-get-relationships"
      (pp/pprint (enitites/dbpedia-get-relationships
                "<http://dbpedia.org/resource/Bill_Gates>"
                "<http://dbpedia.org/resource/Microsoft>"))
      (test/is (= 0 0))))
  
(test/deftest relations-test
  (test/testing "test entity-results->relationship-links"
    (pp/pprint (enitites/entity-results->relationship-links
              ["http://dbpedia.org/resource/Bill_Gates"
               "http://dbpedia.org/resource/Microsoft"]))
    (test/is (= 0 0))))


(test/deftest rtop-level-test
  (test/testing "Top level test"
    (let [results
          (kgn/kgn {:People       ["Bill Gates" "Steve Jobs" "Melinda Gates"]
                    :Organization ["Microsoft"]
                    :Place        ["California"]})]
      (println results)
      (test/is (= (count results) 2)))))

