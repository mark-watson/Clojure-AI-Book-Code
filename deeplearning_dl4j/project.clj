(defproject deeplearning_dl4j_clj "0.1.2-SNAPSHOT"
  :description "DL4J example"
  :url "https://markwatson.com"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.deeplearning4j/deeplearning4j-datasets "1.0.0-M2.1"]
                 [org.deeplearning4j/deeplearning4j-core "1.0.0-M2.1"]
                 [org.nd4j/nd4j-native "1.0.0-M2.1"]]
  :main ^:skip-aot deeplearning-dl4j-clj.wisconsin-data
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
