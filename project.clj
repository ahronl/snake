(defproject snake "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main ^:skip-aot snake.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/java.jdbc "0.0.6"]
                 [org.clojure/test.generative "0.1.2"]
                 [ring "0.3.11" :exclusions [org.clojure/clojure
                                             org.clojure/clojure-contrib]]
                 [compojure "0.6.5"]
                 [hiccup "0.3.6"]
                 [jline "0.9.94"]
                 [hsqldb "1.8.0.10"]])
