(defproject gemini_api "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [clj-http "3.12.1"]
                 [org.clojure/data.json "2.5.0"]
                 [com.google.genai/google-genai "1.11.0"]
                 ]
  :repl-options {:init-ns gemini-api.core})
