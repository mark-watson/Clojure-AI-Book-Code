(ns llm-bosquet.core-test
  (:require [clojure.test :refer :all]
            [llm-bosquet.core :refer [ollama-generate]]))

(deftest test-ollama-generate
  (testing "ollama-generate function"
    (let [prompt "What is the distance from the Moon to Io?"
          response (ollama-generate prompt)
          answer (get-in response [:bosquet/completions :answer])]
      (println "\nComplete response: " response)
      (println "\nAnswer: ", answer))))
