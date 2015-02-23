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

(defprotocol IContainer
  (-content [s]))

(defn content [s]
  (if (satisfies? IContainer s)
    (-content s)
    s))

(defprotocol ILink
  (-link [s]))

(defn link [s]
  (-link s))

(defrecord RefShape [id p s]
  IShape
  (-id [_]
    nil)
  (-position [_]
    p)
  (-scale [_]
    s)
  ILink
  (-link [_]
    id))

(defn ref-shape
  ([id]
     (ref-shape id [0 0] [1 1]))
  ([id p s]
     (->RefShape id p s)))

(defrecord IdShape [id shape]
  IShape
  (-id [_]
    id)
  (-position [_]
    (position shape))
  (-scale [_]
    (scale shape))
  IContainer
  (-content [_]
    (content shape)))

(defn id= [shape id]
  (->IdShape id shape))

(defrecord TransformedShape [shape p s]
  IShape
  (-id [_]
    (-id shape))
  (-position [_]
    (geo/p+ p (-position shape)))
  (-scale [_]
    (geo/dot s (-scale shape)))
  IContainer
  (-content [_]
    (content shape)))

(defn x-shape [shape p s]
  (->TransformedShape shape p s))

(defn s+ [shape v]
  (x-shape shape v [1 1]))

(defn s* [shape s]
  (x-shape shape [0 0] s))
