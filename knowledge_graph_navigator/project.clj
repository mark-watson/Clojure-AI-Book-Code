(defproject knowledge_graph_navigator_clj "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [clj-http "3.10.3"]
                 [com.cemerick/url "0.1.1"]
                 [org.clojure/data.csv "1.0.0"]
                 [org.clojure/data.json "1.0.0"]
                 [org.clojure/math.combinatorics "0.1.6"]]
  :repl-options {:init-ns knowledge-graph-navigator-clj.kgn}
  :main ^:skip-aot knowledge-graph-navigator-clj.kgn)
