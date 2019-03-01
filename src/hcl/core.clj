(ns hcl.core
  (:require [clojure.string :as str]))

(def default-indent "  ")
(def ^:dynamic *indent* "")
(def ^:dynamic *level* 0)

(defn- quote-string [name]
  (format "\"%s\"" (str/replace name "\"" "\\\"")))

(defmacro indented-lines [block]
  `(binding [*level*  (inc *level*)
             *indent* (str/join "" (repeat *level* default-indent))]
     (let [block#   ~block]
       (if (not (empty? block#))
         (let [lines# (str/split block# #"\n")]
           (str (->> lines#
                     (map #(str *indent* %))
                     (str/join "\n"))
                "\n"))
         ""))))

(defn format-kv [k v]
  (format "%s %s\n"
          (cond
            (vector? k)
            (str (name (first k)) " " (str/join " " (map (partial format "\"%s\"") (rest k))))
            :else
            (name k))
          (cond
            (map? v)
            (emit v)
            :else
            (format "= %s" (emit v)))))

(defn emit [value]
  (cond
    (nil? value)
    ""

    (map? value)
    (format (case *level*
              0 "%s"
              "{\n%s}")
            (->> value
                 (map (fn [[k v]]
                        (if (and (keyword? k)
                                 (= "repeated" (namespace k)))
                          (str/join "" (map #(format-kv (name k) %) v))
                          (format-kv k v))))
                 (str/join "")
                 indented-lines))

    (vector? value)
    (format "[\n%s]" (indented-lines (str/join ",\n" (map emit value))))

    :else
    (cond (string? value)
          (quote-string value)
          :else
          (str value))))
