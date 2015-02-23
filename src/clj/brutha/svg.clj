(ns brutha.svg
  (:require [brutha.scene :as bs]
            [brutha.layer :as bl]
            [brutha.view :as bv]
            [brutha.geo :as geo]
            [brutha.shape :as bsh]
            [brutha.svg.element :as el])
  (:import [brutha.svg.element SvgElement]))

(defprotocol ISvg
  (-dims [s])
  (-defs [s])
  (-shapes [s]))

(defn svg? [s]
  (satisfies? ISvg s))

(defrecord SvgGroup [body]
  bsh/IShape
  (-position [_]
    [0 0])
  (-scale [_]
    [1 1])
  (-id [_]
    nil)
  el/IElement
  (-tag [_]
    :g)
  (-position [_]
    nil)
  (-scale [_]
    nil)
  (-attrs [_]
    nil)
  (-body [_]
    body))

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
  el/IElement
  (-tag [_]
    :svg)
  (-attrs [s]
    (let [[w h] (dims s)])
    {:width w
     :height h})
  (-position [_]
    nil)
  (-scale [_]
    nil)
  (-body [s]
    (apply vector
           (el/element :defs (-defs s))
           (-shapes s)))
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
