(ns pull-requests.wait
  (:require [java-time :refer [local-date-time time-between]]
            [pull-requests.github-client :refer [fetch-pull-requests]]))

(defn review-time-of [pr]
  (assoc pr :review-time (time-between (pr :created_at) (pr :closed_at) :minutes)))

(defn wait-times-in [owner repository]
  (fetch-pull-requests [owner repository]))
