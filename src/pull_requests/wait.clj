(ns pull-requests.wait
  (:require [java-time :refer [as local-date-time minutes time-between]]
            [pull-requests.github-client :refer [fetch-pull-requests]]))

(defn review-time-of [pr]
  (assoc pr :review-time (time-between (pr :created_at) (pr :closed_at) :minutes)))

(defn pick-review-time [pull-request]
  (get pull-request :review-time))

(defn wait-time-for [pull-requests]
  (let [wait-time (->> pull-requests
                       (map :review-time)
                       (reduce +))]
    {:days (time-between ((last pull-requests) :created_at) ((first pull-requests) :closed_at) :days)
     :number-of-prs (count pull-requests)
     :wait-time-minutes wait-time
     :wait-time-days (as (minutes wait-time) :days)}))

(defn wait-times-in [owner repository]
  (let [pull-requests (->> (fetch-pull-requests owner repository)
                           (map review-time-of))
        summary (wait-time-for pull-requests)]
    {:source pull-requests
     :summary summary}))
