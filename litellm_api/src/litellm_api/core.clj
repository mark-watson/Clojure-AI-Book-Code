(ns litellm-api.core
  (:require [litellm.router :as router]))

;; Quick setup from environment variables
(router/quick-setup!)

;; Or register custom configurations
(router/register! :fast
                  {:provider :openai
                   :model "gpt-4o-mini"
                   :config {:api-key (System/getenv "OPENAI_API_KEY")}})
