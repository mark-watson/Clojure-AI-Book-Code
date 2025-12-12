(defproject gemini-api "0.1.0-SNAPSHOT"
  :description "Gemini API client for Clojure"
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [clj-http "3.12.3"]
                 [org.clojure/data.json "2.4.0"]
                 [org.clojure/tools.logging "1.2.4"]]  ; Add this line
  :repl-options {:init-ns gemini-api.core}
  :profiles {:dev {:dependencies [[org.clojure/test.check "1.1.1"]]
                   :plugins [[lein-cljfmt "0.8.0"]]}})
