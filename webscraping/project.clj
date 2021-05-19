(defproject webscraping "0.1.0-SNAPSHOT"
  :description "Demonstration of using Java Jsoup library"
  :url "http://markwatson.com"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.jsoup/jsoup "1.7.2"]
                 [com.bhauman/rebel-readline "0.1.4"]]
  :repl-options {:init-ns webscraping.core})
