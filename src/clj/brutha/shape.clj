(ns brutha.shape)

(defprotocol IShape
  (-position [_])
  (-hash [_]))

(defn shape? [s]
  (satisfies? IShape s))

(defn position [s]
  (-position s))

(defn shash [s]
  (-hash s))
