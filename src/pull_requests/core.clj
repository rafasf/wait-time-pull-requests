(ns pull-requests.core
  (:gen-class)
  (:require [clojure.pprint :refer [print-table]]
            [org.httpkit.client :as client]
            [cheshire.core :refer [parse-string]]
            [pull-requests.wait :refer [wait-times-in]]))

(defn -main
  [& args]
  (let [wait-times (wait-times-in "dylanaraps" "pure-bash-bible")]
    (println "")
    (println "Summary")
    (print-table [(wait-times :summary)])
    (println "")
    (println "Source with review time")
    (print-table (wait-times :source))))
