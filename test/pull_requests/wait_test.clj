(ns pull-requests.wait-test
  (:use org.httpkit.fake)
  (:require [clojure.test :refer :all]
            [pull-requests.wait :refer :all]))

(deftest fetch-closed-pull-requests-for-given-repository
  (with-fake-http [{:url "https://api.github.com/repos/the-owner/a-repo/pulls"
                    :query-params {"state" "closed"}
                    :method :get}
                   {:status 200
                    :body "[{\"number\": 123}]"}]
    (let [prs (closed-pull-requests-for "the-owner" "a-repo")]
      (is (= [{:number 123}]
             prs)))))
