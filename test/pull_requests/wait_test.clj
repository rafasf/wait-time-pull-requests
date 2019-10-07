(ns pull-requests.wait-test
  (:use org.httpkit.fake)
  (:require [clojure.test :refer :all]
            [java-time :refer [local-date-time]]
            [pull-requests.wait :refer :all]))

(def github-pull-requests
  [{:number 123
    :title "some title"
    :other-field "not important"
    :created_at "2011-04-10T20:09:31Z"
    :closed_at "2011-04-10T20:09:31Z"}])

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
  (is (= [{:id "123"
           :title "some title"
           :created_at (local-date-time 2011 04 10 20 9 31)
           :closed_at (local-date-time 2011 04 10 20 9 31)}]
         (gh-select-fields github-pull-requests))))

(deftest returns-review-time
  (let [pr-with-review-time (review-time-of {:closed_at (local-date-time 2011 04 10 20 9 31)
                                             :created_at (local-date-time 2011 04 10 19 8 31)})
        review-time (select-keys pr-with-review-time [:review-time])]
    (is (= {:review-time 61}
           review-time))))
