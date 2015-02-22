(ns brutha.svg
  (:require [brutha.scene :as bs]
            [brutha.layer :as bl]
            [brutha.view :as bv]
            [brutha.geo :as geo]
            [brutha.shape :as bsh])
  (:import [brutha.shape RefShape TransformedShape]))

(defprotocol ISvg
  (-dims [s])
  (-defs [s])
  (-shapes [s]))

(defn svg? [s]
  (satisfies? ISvg s))

(defprotocol IShape
  (-shape [s]))

(extend-protocol IShape
  RefShape
  (-shape [s]
    (bsh/id s))
  TransformedShape
  (-shape [s]
    (-shape (:shape s))))

(defn shape? [s]
  (satisfies? IShape s))

(defrecord SvgShape [id shape]
  bsh/IShape
  (-position [_]
    [0 0])
  (-id [_]
    id)
  (-scale [_]
    [1 1])
  IShape
  (-shape [_]
    shape))

(defn shape
  ([s]
     (shape s nil))
  ([s id]
     (->SvgShape id s)))

(defrecord SvgGroup [shapes]
  bsh/IShape
  (-position [_]
    [0 0])
  (-scale [_]
    [1 1])
  (-id [_]
    nil)
  IShape
  (-shape [_]
    shapes))

(defn group [shapes]
  (->SvgGroup shapes))

(defrecord Svg [scene defs]
  ISvg
  (-dims [_]
    (let [v (bs/view scene)
          b (bv/bounds v)]
      (geo/dims b)))
  (-defs [_]
    (vals defs))
  (-shapes [_]
    (->> scene
        bs/layers
        (map bl/shapes)
        group))
  bs/IScene
  (-view [_]
    (bs/view scene))
  (-layers [_]
    (bs/layers scene))
  (-add-shape [s sh z]
    (update-in s [:scene] bs/add-shape sh z)))

(defn svg
  ([s]
     (svg s {}))
  ([s d]
     (->Svg s d)))
