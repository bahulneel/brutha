(ns brutha.shape
  (:require [brutha.geo :as geo]))

(defprotocol IShape
  (-position [s])
  (-id [s])
  (-scale [s]))

(defn shape? [s]
  (satisfies? IShape s))

(defn position [s]
  (-position s))

(defn id [s]
  (-id s))

(defn scale [s]
  (-scale s))

(defrecord RefShape [id p s]
  IShape
  (-id [_]
    id)
  (-position [_]
    p)
  (-scale [_]
    s))

(defn ref-shape
  ([id]
     (ref-shape id [0 0] [1 1]))
  ([id p s]
     (->RefShape id p s)))

(defrecord TransformedShape [shape p s]
  IShape
  (-id [_]
    (-id shape))
  (-position [_]
    (geo/p+ p (-position shape)))
  (-scale [_]
    (geo/dot s (-scale shape))))

(defn x-shape [shape p s]
  (->TransformedShape shape p s))

(defn s+ [shape v]
  (x-shape shape v [1 1]))

(defn s* [shape s]
  (x-shape shape [0 0] s))
