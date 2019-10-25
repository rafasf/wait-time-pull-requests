(ns pull-requests.client
  (:require [org.httpkit.client :as client]
            [cheshire.core :refer [parse-string]]
            [pull-requests.links :refer [parse-links]]))

(defn parse-to-map [text]
  (parse-string text true))

(defn next-page-in [headers]
  (-> headers parse-links (get-in [:links :next]) :href))

(defn provider-for
  ([url] {:url url})
  ([url username secret] {:url url :basic-auth [username secret]}))

(defn fetch-all
  ([provider query-params] (fetch-all provider query-params []))
  ([provider query-params prs] (let [{:keys [headers body]} @(client/get (provider :url) {:query-params query-params})]
                                 (let [next-page-url (next-page-in headers)]
                                   (if-not next-page-url
                                     (concat prs (parse-to-map body))
                                     (recur (assoc provider :url next-page-url) {} (concat prs (parse-to-map body))))))))
