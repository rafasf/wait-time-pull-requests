(ns pull-requests.client
  (:require [org.httpkit.client :as client]
            [cheshire.core :refer [parse-string]]
            [pull-requests.links :refer [parse-links]]))

(defn str-to-pull-requests [text]
  (parse-string text true))

(defn to-pull-requests [{:keys [status headers body error opts]}]
  (str-to-pull-requests body))

(defn good-fields [pull-request]
  (select-keys pull-request [:created_at :closed_at :number :state]))

(defn next-page-in [headers]
  (-> headers parse-links (get-in [:links :next]) :href))

(defn all-prs
  ([url] (all-prs url []))
  ([url prs] (let [{:keys [headers body]} @(client/get url)]
               (let [next-page-url (next-page-in headers)]
                 (if-not next-page-url
                   (concat prs (str-to-pull-requests body))
                   (recur next-page-url (concat prs (str-to-pull-requests body))))))))

(defn pull-requests [owner repository]
  (all-prs (str "https://api.github.com/" owner "/" repository "/pulls")))
