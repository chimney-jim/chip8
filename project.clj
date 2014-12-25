(defproject chip8 "0.1.0-SNAPSHOT"
  :description "chip-8 emulator"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/tools.namespace "0.2.8"]]
  :main ^:skip-aot chip8.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
