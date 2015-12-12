(defproject clj-center "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "https://github.com/Yuhta/clj-center"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[ch.qos.logback/logback-classic "1.1.3"]
                 [org.clojure/clojure "1.7.0"]
                 [org.clojure/tools.logging "0.3.1"]
                 [org.clojure/tools.nrepl "0.2.12"]
                 [mwdict "0.1.0-SNAPSHOT"]]
  :plugins [[lein-bin "0.3.5"]]
  :main ^:skip-aot clj-center.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  :bin {:name "clj-center"})
