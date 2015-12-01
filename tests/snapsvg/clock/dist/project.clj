(defproject snapsvg-clock-example "0.1.0-SNAPSHOT"
  :description "Snap.svg's clock example's port to ClojureScript"
  :url "http://diegonc.github.io/tests/snapsvg/clock/dist/public"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.170"]
                 [org.omcljs/om "0.9.0"]
                 [prismatic/om-tools "0.3.12"]
                 [cljsjs/snapsvg "0.4.1-0"]]

  :plugins [[lein-cljsbuild "1.1.1"]
            [lein-figwheel "0.5.0-2"]]

  :clean-targets ^{:protect false} ["resources/public/js/compiled"
                                    "target"]

  :profiles {:uberjar {:aot :all
                       :prep-tasks ["compile" ["cljsbuild" "once" "min"]]}}

  :cljsbuild {
    :builds [{:id "dev"
              :source-paths ["src/cljs"]
              :jar true
              :figwheel {:websocket-host :js-client-host}
              :compiler {:main snapsvg-example.core
                         :asset-path "js/compiled/out"
                         :output-to "resources/public/js/compiled/snapsvg_example.js"
                         :output-dir "resources/public/js/compiled/out"
                         :source-map-timestamp true}}
             {:id "min"
              :source-paths ["src/cljs"]
              :jar true
              :compiler {:main snapsvg-example.core
                         :output-to "resources/public/js/compiled/snapsvg_example.js"
                         :optimizations :advanced
                         :pretty-print false
                         :source-map-timestamp true}}]}
  :figwheel {:css-dirs ["resources/public/css"]})
