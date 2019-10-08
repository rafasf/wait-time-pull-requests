(ns pull-requests.wait-test
  (:use org.httpkit.fake)
  (:require [clojure.test :refer :all]
            [java-time :refer [local-date-time plus days]]
            [pull-requests.wait :refer :all]))

(def created-at (local-date-time 2011 04 10 20 9 31))
(def prs [{:review-time 30
           :closed_at (plus created-at (days 101))}
          {:review-time 1}
          {:review-time 30
           :created_at created-at}])

(deftest returns-review-time
  (let [pr-with-review-time (review-time-of {:closed_at (local-date-time 2011 04 10 20 9 31)
                                             :created_at (local-date-time 2011 04 10 19 8 31)})
        review-time (select-keys pr-with-review-time [:review-time])]
    (is (= {:review-time 61}
           review-time))))

(deftest returns-aggregated-wait-time
  (is (= {:days 101 :number-of-prs 3 :wait-time-minutes 61 :wait-time-days 0}
         (wait-time-for prs))))
