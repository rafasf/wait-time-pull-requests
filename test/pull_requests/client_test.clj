(ns pull-requests.client-test
  (:use org.httpkit.fake)
  (:require [clojure.test :refer :all]
            [pull-requests.client :refer :all]))

(deftest pull-requests-client
  (testing "fetch all pages"
    (with-fake-http [{:url "https://gh2.com/pulls?page=2"
                      :method :get}
                     {:status 200
                      :body "[{\"number\": 3}]"}

                     {:url "https://gh.com/pulls"
                      :method :get}
                     {:status 200
                      :headers {:link "<https://gh2.com/pulls?page=2>; rel=\"next\", <https://gh.com/pulls?page=2>; rel=\"last\""}
                      :body "[{\"number\": 1}, {\"number\": 2}]"}]
      (let [prs (pull-requests)]
        (is (= [{:number 1} {:number 2} {:number 3}]
               prs))))))
