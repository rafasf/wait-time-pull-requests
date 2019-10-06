(ns pull-requests.client-test
  (:use org.httpkit.fake)
  (:require [clojure.test :refer :all]
            [pull-requests.client :refer :all]))

(deftest fetch-all-pages
  (with-fake-http [{:url "https://gh2.com/pulls?page=2"
                    :method :get}
                   {:status 200
                    :body "[{\"number\": 3}]"}

                   {:url "https://api.github.com/the-owner/a-repo/pulls"
                    :query-params {"state" "something"}
                    :method :get}
                   {:status 200
                    :headers {:link "<https://gh2.com/pulls?page=2>; rel=\"next\", <https://gh.com/pulls?page=2>; rel=\"last\""}
                    :body "[{\"number\": 1}, {\"number\": 2}]"}]
    (let [prs (fetch-all "https://api.github.com/the-owner/a-repo/pulls" {"state" "something"})]
      (is (= [{:number 1} {:number 2} {:number 3}]
             prs)))))
