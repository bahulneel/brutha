(ns brutha.utils)

(defn apply-if [pred f v & args]
  (if (pred v)
    (apply f v args)
    v))

(defn apply-unless [pred f v & args]
  (apply apply-if (complement pred) f v args))

(defn map-vals [f m]
  (into {} (map (fn [[k v]]
                  [k (f v)])
                m)))
