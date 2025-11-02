(ns litellm-api.core-test
  (:require [clojure.test :refer :all]
            [litellm.router :as router]))

(deftest completions-test
  (testing "OpenAI completions API with LiteLLM"
    (router/quick-setup!)
    (let [response (router/completion :openai
                                      {:messages [{:role :user :content "please generate a 10 word sentence"}]})]
      (println (router/extract-content response))
      (is (not (nil? response))))))
