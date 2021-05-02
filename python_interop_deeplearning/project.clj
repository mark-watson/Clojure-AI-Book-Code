(defproject python_interop_deeplearning "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :jvm-opts ["-Djdk.attach.allowAttachSelf"
             "-XX:+UnlockDiagnosticVMOptions"
             "-XX:+DebugNonSafepoints"]
    :plugins [[lein-tools-deps "0.4.5"]]
    :middleware [lein-tools-deps.plugin/resolve-dependencies-with-deps-edn]
      :lein-tools-deps/config {:config-files [:project]
                                 :resolve-aliases []}

    :mvn/repos {"central" {:url "https://repo1.maven.org/maven2/"}
                "clojars" {:url "https://clojars.org/repo"}}

   :dependencies [[org.clojure/clojure "1.10.1"]
                 ;;[clj-python/libpython-clj "1.37"]
                 [clj-python/libpython-clj "1.37"]
                 ]
  :main ^:skip-aot python-interop-deeplearning.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
