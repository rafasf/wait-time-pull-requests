(ns pull-requests.github-client-test
  (:use org.httpkit.fake)
  (:require [clojure.test :refer :all]
            [java-time :refer [local-date-time]]
            [pull-requests.github-client :refer :all]))

(def gh-pull-request
  {:number 123
   :title "some title"
   :other-field "not important"
   :created_at "2011-04-10T20:09:31Z"
   :closed_at "2011-04-10T20:09:31Z"})

(def github-pull-requests
  [gh-pull-request])

(deftest fetch-closed-pull-requests-for-given-repository
  (with-fake-http [{:url "https://api.github.com/repos/the-owner/a-repo/pulls"
                    :query-params {"state" "closed"}
                    :method :get}
                   {:status 200
                    :body "[{\"number\": 123}]"}]
    (let [prs (closed-pull-requests-for "the-owner" "a-repo")]
      (is (= [{:number 123}]
             prs)))))

(deftest returns-relevant-fields
  (is (= [:number :title :created_at :closed_at]
         (keys (gh-pr-fields gh-pull-request)))))

(deftest returns-date-for-created-and-closed-at
  (is (= {:created_at (local-date-time 2011 04 10 20 9 31)
          :closed_at (local-date-time 2011 04 10 20 9 31)}
         (select-keys (parse-dates gh-pull-request) [:created_at :closed_at]))))

(deftest returns-pull-request
  (is (= {:id "123"
          :title "some title"
          :created_at (local-date-time 2011 04 10 20 9 31)
          :closed_at (local-date-time 2011 04 10 20 9 31)}
         (to-pull-request gh-pull-request))))
