(ns llm-bosquet.core
  (:require [bosquet.llm.generator :refer [generate llm]]
            [bosquet.llm.wkk :as wkk]))
  
;;(defn openai-gpt3-5turbo-generate [prompt]
;;  (generate prompt)) ;;  defaults to OpenAI

(defn ollama-generate [prompt]
(generate {:question-answer "Question: {{question}} Answer: {{answer}}"
           :answer          (llm :ollama wkk/model-params {:model :mistral-small :max-tokens 50})
           :self-eval       ["{{question-answer}}"
                             "Is this a correct answer?"
                             "{{test}}"]
           :test            (llm :ollama wkk/model-params {:model :mistral-small :max-tokens 50})}
          {:question prompt}))