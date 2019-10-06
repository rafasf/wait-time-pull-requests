(ns pull-requests.client
  (:require [org.httpkit.client :as client]
            [cheshire.core :refer [parse-string]]
            [pull-requests.links :refer [parse-links]]))

(defn parse-to-map [text]
  (parse-string text true))

(defn next-page-in [headers]
  (-> headers parse-links (get-in [:links :next]) :href))

(defn fetch-all
  ([url query-params] (fetch-all url query-params []))
  ([url query-params prs] (let [{:keys [headers body]} @(client/get url {:query-params query-params})]
                            (let [next-page-url (next-page-in headers)]
                              (if-not next-page-url
                                (concat prs (parse-to-map body))
                                (recur next-page-url {} (concat prs (parse-to-map body))))))))

(defn closed-pull-requests-for [owner repository]
  (fetch-all (str "https://api.github.com/repos/" owner "/" repository "/pulls")
             {"state" "closed"}))
