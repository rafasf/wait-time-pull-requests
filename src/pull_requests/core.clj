(ns pull-requests.core
  (:gen-class)
  (:require [clojure.pprint :refer [print-table]]
            [org.httpkit.client :as client]
            [cheshire.core :refer [parse-string]]
            [pull-requests.wait :refer [wait-times-in]]))

(defn -main
  [& args]
  (print-table (wait-times-in "dylanaraps" "pure-bash-bible")))
