(ns pull-requests.github-client
  (:require [clojure.set :refer [rename-keys]]
            [java-time :refer [local-date-time time-between]]
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

(defn parse-date-in [date-key pull-request]
  (update pull-request date-key (partial local-date-time "yyyy-MM-dd'T'HH:mm:ss'Z'")))

(def parse-dates
  (comp
   (partial parse-date-in :created_at)
   (partial parse-date-in :closed_at)))

(def to-pull-request
  (comp parse-dates id-to-string adjust-key-names gh-pr-fields))

(defn fetch-pull-requests [owner repository]
  (->> (closed-pull-requests-for owner repository)
       (map to-pull-request)))