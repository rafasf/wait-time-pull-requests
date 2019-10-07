(ns pull-requests.wait
  (:require [clojure.set :refer [rename-keys]]
            [pull-requests.client :refer [fetch-all]]))

(defn closed-pull-requests-for [owner repository]
  (fetch-all (str "https://api.github.com/repos/" owner "/" repository "/pulls")
             {"state" "closed"}))

(defn gh-pr-fields [pr]
  (select-keys pr [:number :title :created_at :closed_at]))

(defn adjust-key-names [pr]
  (rename-keys pr {:number :id}))

(defn id-to-string [pr]
  (update pr :id str))

(defn gh-select-fields [prs]
  (map (comp id-to-string adjust-key-names gh-pr-fields) prs))
