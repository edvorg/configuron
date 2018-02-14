(defproject rocks.clj/configuron "0.1.0-SNAPSHOT"
  :description "Simple environ based config that reloads itself in dev mode"
  :url "https://github.com/edvorg/configuron"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.9.908" :scope "provided"]
                 [yogthos/config "0.9"]
                 [hawk "0.2.11"]
                 [com.cognitect/transit-clj "0.8.300"]
                 [cljs-http "0.1.43"]
                 [com.cognitect/transit-cljs "0.8.243"]]
  :plugins [[lein-cljsbuild "1.1.7"]]
  :clean-targets ^{:protect false} [:target-path
                                    [:cljsbuild :builds :app :compiler :output-dir]
                                    [:cljsbuild :builds :app :compiler :output-to]]
  :test-paths ["test/clj" "test/cljs"]
  :source-paths ["src/clj" "src/cljs"]
  :cljsbuild {:builds {:minify {:source-paths ["src/cljs"]
                                :compiler {:optimizations :advanced
                                           :pretty-print  false}}
                       :dev {:source-paths ["src/cljs"]
                             :compiler {:optimizations :none}}}}
  :javac-options ["-target" "1.7" "-source" "1.7" "-Xlint:-options"])
