(ns pull-requests.core
  (:gen-class)
  (:require [clojure.pprint :refer [print-table]]
            [org.httpkit.client :as client]
            [cheshire.core :refer [parse-string]]
            [clojure.tools.cli :refer [parse-opts summarize]]
            [pull-requests.wait :refer [wait-times-in]]))

(def required-opts #{:owner :repository})
(def cli-options
  [["-o" "--owner OWNER" "Repository Owner"]
   ["-r" "--repository REPO" "Repository Name"]])

(defn missing-opts? [opts]
  (cond
    (not-every? opts required-opts) ()))

(defn error-msg [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (clojure.string/join \newline errors)))

(defn check-wait-time [{:keys [owner repository]}]
  (let [wait-times (wait-times-in owner repository)]
    (println "")
    (println "Summary")
    (print-table [(wait-times :summary)])
    (println "")
    (println "Source with review time")
    (print-table (wait-times :source))))

(defn -main
  [& args]
  (let [{:keys [options arguments summary errors]} (parse-opts args cli-options)]
    (cond
      errors (println (error-msg errors))
      (missing-opts? options) (println (map str required-opts) "are required")
      :else (check-wait-time options))))
