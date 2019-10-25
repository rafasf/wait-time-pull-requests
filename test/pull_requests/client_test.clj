(ns pull-requests.client-test
  (:use org.httpkit.fake)
  (:require [clojure.test :refer :all]
            [pull-requests.client :refer :all]))

(def paged-gh-responses
  [{:url "https://gh2.com/pulls?page=2"
    :method :get}
   {:status 200
    :body "[{\"number\": 3}]"}

   {:url "https://api.github.com/the-owner/a-repo/pulls"
    :query-params {"state" "something"}
    :method :get}
   {:status 200
    :headers {:link "<https://gh2.com/pulls?page=2>; rel=\"next\", <https://gh.com/pulls?page=2>; rel=\"last\""}
    :body "[{\"number\": 1}, {\"number\": 2}]"}])

(def authenticateed-paged-gh-responses
  [{:url "https://gh2.com/pulls?page=2"
    :basic-auth ["bob" "secret"]
    :method :get}
   {:status 200
    :body "[{\"number\": 3}]"}

   {:url "https://api.github.com/the-owner/a-repo/pulls"
    :query-params {"state" "something"}
    :basic-auth ["bob" "secret"]
    :method :get}
   {:status 200
    :headers {:link "<https://gh2.com/pulls?page=2>; rel=\"next\", <https://gh.com/pulls?page=2>; rel=\"last\""}
    :body "[{\"number\": 1}, {\"number\": 2}]"}])

(deftest fetch-all-pages
  (testing "public repository fetching"
    (with-fake-http paged-gh-responses
      (let [provider (provider-for "https://api.github.com/the-owner/a-repo/pulls")
            prs (fetch-all provider {"state" "something"})]
        (is (= [{:number 1} {:number 2} {:number 3}]
               prs)))))

  (testing "private repository fetching"
    (with-fake-http authenticateed-paged-gh-responses
      (let [provider (provider-for "https://api.github.com/the-owner/a-repo/pulls" "bob" "secret")
            prs (fetch-all provider {"state" "something"})]
        (is (= [{:number 1} {:number 2} {:number 3}]
               prs))))))

(deftest provider-creation
  (testing "non-authenticated provider"
    (is (= {:url "https://someprovider.com"}
           (provider-for "https://someprovider.com"))))

  (testing "authenticated provider"
    (is (= {:url "https://someprovider.com" :basic-auth ["bob" "secret"]}
           (provider-for "https://someprovider.com" "bob" "secret")))))
