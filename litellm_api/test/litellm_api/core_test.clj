(ns litellm-api.core-test
  (:require [clojure.test :refer :all]
            [litellm.router :as router]))

;; available-providers;
;;  #{:gemini :openrouter :mistral :anthropic :openai :ollama}

(deftest openai-completions-test
  (testing "OpenAI completions API with LiteLLM"
   (router/register!
    :fast
    {:provider :openai
     :model "gpt-4o-mini"
     :config {:api-key (System/getenv "OPENAI_API_KEY")}})
   (let [response (router/completion :fast
                   {:messages [{:role :user :content "please generate a 10 word sentence"}]})]
     (println (router/extract-content response))
     ;;(println response)
     (is (not (nil? response))))))

(deftest google-completions-test
  (testing "Google Gemini completions API with LiteLLM"
   (router/register!
    :fast
    {:provider :gemini
     :model "gemini-2.5-flash"
     :config {:api-key (System/getenv "GOOGLE_API_KEY")}})
   (let [response (router/completion :fast
                   {:messages [{:role :user :content "please generate a 10 word sentence"}]})]
     (println (router/extract-content response))
     ;;(print response)
     (is (not (nil? response))))))
