(defproject pull-requests "0.1.0-SNAPSHOT"
  :description "Pull Requests Wait Times"
  :url "http://github.com/rafasf/wait-time-pull-requests"
  :license {:name "MIT"
            :url "https://opensource.org/licenses/MIT"}

  :plugins [[lein-cljfmt "0.6.4"]]

  :dependencies [[org.clojure/clojure "1.10.0"]
                 [http-kit "2.4.0-alpha4"]
                 [cheshire "5.9.0"]
                 [cljfmt "0.6.4"]]

  :main ^:skip-aot pull-requests.core
  :target-path "target/%s"

  :profiles
  {
   :uberjar {:aot :all}
   :dev {:plugins [[com.jakemccrary/lein-test-refresh "0.24.1"]]}
   :test {:plugins [[lein-test-report-junit-xml "0.2.0"]]
          :dependencies [[pjstadig/humane-test-output "0.9.0"]
                         [http-kit.fake "0.2.1"]]
          :injections [(require 'pjstadig.humane-test-output)
                       (pjstadig.humane-test-output/activate!)]}})
