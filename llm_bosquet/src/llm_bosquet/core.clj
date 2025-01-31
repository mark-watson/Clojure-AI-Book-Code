(ns llm-bosquet.core
  (:require [bosquet.llm.generator :refer [generate llm]]
            [bosquet.llm.wkk :as wkk]))
  
(defn ollama-generateXX [prompt]
  (generate prompt)) ;;  defaults to OpenAI

(defn ollama-generate [prompt]
(generate {:question-answer "Question: {{question}} Answer: {{answer}}"
           :answer          (llm :ollama wkk/model-params {:model :zephyr :max-tokens 50})
           :self-eval       ["{{question-answer}}"
                             "Is this a correct answer?"
                             "{{test}}"]
           :test            (llm :ollama wkk/model-params {:model :zephyr :max-tokens 50})}
          {:question prompt}))