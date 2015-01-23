(defproject chip8 "0.1.0-SNAPSHOT"
  :description "chip-8 emulator"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/tools.namespace "0.2.8"]
                 [org.clojure/data.generators "0.1.2"]
                 [speclj "3.1.0"]]
  :plugins  [[speclj "3.1.0"]]
  :test-paths ["spec"]
  :main ^:skip-aot chip8.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
