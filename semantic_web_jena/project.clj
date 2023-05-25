(defproject semantic_web_jena_clj "0.1.0-SNAPSHOT"
  :description "Clojure Wrapper for the Apache Jena RDF and SPARQL library"
  :url "https://markwatson.com"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :source-paths      ["src"]
  :java-source-paths ["src-java"]
  :javac-options     ["-target" "1.8" "-source" "1.8"]
  :dependencies [[org.clojure/clojure "1.11.1"]
                 ;[com.markwatson/semanticweb "1.0.3-SNAPSHOT"]
                 [org.apache.derby/derby "10.15.2.0"]
                 [org.apache.derby/derbytools "10.15.2.0"]
                 [org.apache.derby/derbyclient "10.15.2.0"]
                 [org.apache.jena/apache-jena-libs "3.17.0" :extension "pom"]]
  :repl-options {:init-ns semantic-web-jena-clj.core})
