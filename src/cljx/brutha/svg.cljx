(ns brutha.svg
  (:require [brutha.svg.element :as el]
            [brutha.scene :as bs]
            [brutha.layer :as bl]
            [brutha.view :as bv]
            [brutha.geo :as geo]
            [brutha.shape :as bsh]))

(defprotocol ISvg
  (-dims [s])
  (-defs [s])
  (-shapes [s]))

(defn svg? [s]
  (satisfies? ISvg s))

(defrecord SvgGroup [body]
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

(extend-type SvgGroup
  bsh/IShape
  (-position [_]
    [0 0])
  (-scale [_]
    [1 1])
  (-id [_]
    nil))

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
    (let [v (bs/view scene)
          xlate (bv/project v [0 0])
          scale (bv/scale v [1 1])]
      (prn xlate)
      (->> scene
           bs/layers
           (map bl/shapes)
           (map group)
           (map (fn [s]
                  (-> s
                      (bsh/s+ xlate)
                      (bsh/s* scale)))))))
  el/IElement
  (-tag [_]
    :svg)
  (-attrs [s]
    (let [[w h] (-dims s)]
      {:width w
       :height h}))
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
