(ns pull-requests.github-client
  (:require [clojure.set :refer [rename-keys]]
            [java-time :refer [local-date-time time-between]]
            [pull-requests.client :refer [fetch-all provider-for]]))

(defn closed-pull-requests-for [provider owner repository]
  (fetch-all (assoc provider :url (str (provider :url) "/repos/" owner "/" repository "/pulls"))
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

(defn github-provider [auth]
  (let [base-url "https://api.github.com"]
    (if (empty? auth)
      (provider-for base-url)
      (provider-for base-url (auth :user) (auth :secret)))))

(defn fetch-pull-requests [provider owner repository]
  (->> (closed-pull-requests-for provider owner repository)
       (map to-pull-request)))
