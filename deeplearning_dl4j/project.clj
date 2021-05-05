(defproject deeplearning_dl4j_clj "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.deeplearning4j/deeplearning4j-datasets "1.0.0-beta7"]
                 [org.deeplearning4j/deeplearning4j-core "1.0.0-beta7"]
                 [org.nd4j/nd4j-native "1.0.0-beta7"]
                 [org.datavec/datavec-api "1.0.0-beta7"]]
  :main ^:skip-aot deeplearning-dl4j-clj.wisconsin-data
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
