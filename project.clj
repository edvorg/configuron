(defproject rocks.clj/configuron "0.1.0-SNAPSHOT"
  :description "Simple environ based config that reloads itself in dev mode"
  :url "https://github.com/edvorg/configuron"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [yogthos/config "0.9"]
                 [hawk "0.2.11"]]
  :javac-options ["-target" "1.7" "-source" "1.7" "-Xlint:-options"])
