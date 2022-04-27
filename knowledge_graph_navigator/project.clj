(defproject knowledge_graph_navigator_clj "0.1.0-SNAPSHOT"
  :description "Knowledge Graph Navigator"
  :url "https://markwatson.com"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :source-paths      ["src"]
  :java-source-paths ["src-java"]
  :javac-options     ["-target" "1.8" "-source" "1.8"]
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [clj-http "3.12.3"]
                 [com.cemerick/url "0.1.1"]
                 [org.clojure/data.csv "1.0.0"]
                 [org.clojure/data.json "1.0.0"]
                 [org.clojure/math.combinatorics "0.1.6"]
                 [org.apache.derby/derby "10.15.2.0"]
                 [org.apache.derby/derbytools "10.15.2.0"]
                 [org.apache.derby/derbyclient "10.15.2.0"]
                 [org.apache.jena/apache-jena-libs "4.4.0" :extension "pom"]]
  :repl-options {:init-ns knowledge-graph-navigator-clj.kgn}
  :main ^:skip-aot knowledge-graph-navigator-clj.kgn)
