(ns brave-search.core 
  (:require [clj-http.client :as client]
             [cheshire.core :as json]
             [clojure.pprint :refer [pprint]]))

;; define the environment variable "BRAVE_SEARCH_API_KEY" with the value of your Brave search API key

(defn brave-search [query]
  (let [subscription-key (System/getenv "BRAVE_SEARCH_API_KEY")
        endpoint "https://api.search.brave.com/res/v1/web/search"
        params {:q query}
        headers {"X-Subscription-Token" subscription-key}

        ;; Call the API
        response (client/get endpoint {:headers headers
                                       :query-params params})

        ;; Pull out results
        results (get-in (json/parse-string (:body response) true) [:web :results])

        ;; Create a vector of vectors containing title, URL, and description
        res (mapv (fn [result]
                    [(:title result)
                     (:url result)
                     (:description result)])
                  results)]

    ;; Return the results
    res))