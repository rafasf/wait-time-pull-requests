(ns pull-requests.core
  (:gen-class)
  (:require [org.httpkit.client :as client]
            [cheshire.core :refer [parse-string]]
            [pull-requests.wait :refer [closed-pull-requests-for]]))

(defn -main
  [& args]
  (println (closed-pull-requests-for "dylanaraps" "pure-bash-bible")))
