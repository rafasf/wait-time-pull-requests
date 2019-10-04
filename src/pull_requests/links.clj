(ns pull-requests.links
  "This is pretty much clj-http's link header parser, slightly change to look
  at headers. Check the real source here:
  https://github.com/dakrone/clj-http/blob/master/src/clj_http/links.clj")

(def ^:private quoted-string
  #"\"((?:[^\"]|\\\")*)\"")

(def ^:private token
  #"([^,\";]*)")

(def ^:private link-param
  (re-pattern (str "(\\w+)=(?:" quoted-string "|" token ")")))

(def ^:private uri-reference
  #"<([^>]*)>")

(def ^:private link-value
  (re-pattern (str uri-reference "((?:\\s*;\\s*" link-param ")*)")))

(def ^:private link-header
  (re-pattern (str "(?:\\s*(" link-value ")\\s*,?\\s*)")))

(defn read-link-params [params]
  (into {}
        (for [[_ name quot tok] (re-seq link-param params)]
          [(keyword name) (or quot tok)])))

(defn read-link-value [value]
  (let [[_ uri params] (re-matches link-value value)
        param-map      (read-link-params params)]
    [(keyword (:rel param-map))
     (-> param-map
         (assoc :href uri)
         (dissoc :rel))]))

(defn read-link-headers [header]
  (->> (re-seq link-header header)
       (map second)
       (map read-link-value)
       (into {})))

(defn- links-response
  [headers]
  (if-let [link-headers (headers :link)]
    (let [link-headers (if (coll? link-headers)
                         link-headers
                         [link-headers])]
      (assoc headers
             :links
             (into {} (map read-link-headers link-headers))))
    headers))

(defn parse-links [headers]
  (links-response headers))
