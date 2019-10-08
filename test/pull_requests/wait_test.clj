(ns pull-requests.wait-test
  (:use org.httpkit.fake)
  (:require [clojure.test :refer :all]
            [java-time :refer [local-date-time]]
            [pull-requests.wait :refer :all]))

(deftest returns-review-time
  (let [pr-with-review-time (review-time-of {:closed_at (local-date-time 2011 04 10 20 9 31)
                                             :created_at (local-date-time 2011 04 10 19 8 31)})
        review-time (select-keys pr-with-review-time [:review-time])]
    (is (= {:review-time 61}
           review-time))))
