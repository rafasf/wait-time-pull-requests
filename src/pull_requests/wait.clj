(ns pull-requests.wait
  (:require [pull-requests.client :refer [fetch-all]]))

(defn closed-pull-requests-for [owner repository]
  (fetch-all (str "https://api.github.com/repos/" owner "/" repository "/pulls")
             {"state" "closed"}))
