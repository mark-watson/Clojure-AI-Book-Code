(defproject opennlp-clj "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :source-paths      ["src"]
  :java-source-paths ["src-java"]
  :javac-options     ["-target" "1.8" "-source" "1.8"]

  :dependencies [[org.clojure/clojure "1.11.1"]
                 ;[com.markwatson/opennlp "1.0-SNAPSHOT"] ;;from my Java AI book
                 [opennlp/tools "1.5.0"]
                 ]
  :repl-options {:init-ns opennlp-clj.core})
