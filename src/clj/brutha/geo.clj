(ns clj.brutha.geo
  (:require [brutha.math :refer [sqrt]]))

(defprotocol IPos
  (-x [p])
  (-y [p])
  (-vec [p]))

(defrecord Point [x y]
  IPos
  (-x [_]
    x)
  (-y [_]
    y)
  (-vec [_]
    [x y]))

(extend-protocol IPos
  clojure.lang.PersistentVector
  (-x [[x _]]
    x)
  (-y [[_ y]]
    y)
  (-vec [v]
    v))

(defn point [x y]
  {:pre [(number? x)
         (number? y)]}
  (->Point x y))

(defn p-map [f p1 p2]
  (let [[x1 y1] (-vec p1)
        [x2 y2] (-vec p2)]
    (point (f x1 x2) (f y1 y2))))

(defn dot [p1 p2]
  (p-map * p1 p2))

(defn p+ [p1 p2]
  (p-map + p1 p2))

(defn p- [p1 p2]
  (p-map + p1 p2))

(defn p* [p1 m]
  (dot p1 [m m]))

(defn mag [p]
  (let [sq (apply + (-vec (dot p p)))]
    (sqrt sq)))

(defn norm [p]
  (let [m (mag p)]
    (p* p (/ 1 m))))

(defn p-comp [c p1 p2]
  (let [[x1 y1] (-vec p1)
        [x2 y2] (-vec p2)]
    (and (c x1 x2)
         (c y1 y2))))

(defn p= [p1 p2]
  (p-comp = p1 p2))

(defn p> [p1 p2]
  (p-comp > p1 p2))

(defn p< [p1 p2]
  (p-comp < p1 p2))

(defn p>= [p1 p2]
  (p-comp >= p1 p2))

(defn p<= [p1 p2]
  (p-comp <= p1 p2))

(defprotocol IBox
  (-top-left [b])
  (-bottom-right [b])
  (-dims [b])
  (-center [b]))

(defrecord Box [tl br]
  IBox
  (-top-left [_]
    tl)
  (-bottom-right [_]
    br)
  (-dims [_]
    (p- br tl))
  (-center [_]
    (-> br
        (p- tl)
        (p* (/ 1 2))
        (p+ tl))))

(defrecord Rect [tl dims]
  IBox
  (-top-left [_]
    tl)
  (-bottom-right [_]
    (p+ tl dims))
  (-dims [_]
    dims)
  (-center [_]
    (-> dims
        (p* (/ 1 2))
        (p+ tl))))

(defn box [tl br]
  {:pre [(p<= tl br)]}
  (->Box tl br))

(defn rect [tl dims]
  {:pre [(p>= dims [0 0])]}
  (->Rect tl dims))
