(ns brutha.geo
  (:require [brutha.math :refer [sqrt]]))

(defprotocol IPoint
  (-x [p])
  (-y [p])
  (-vec [p]))

(defrecord Point [x y]
  IPoint
  (-x [_]
    x)
  (-y [_]
    y)
  (-vec [_]
    [x y]))

(defn point? [p]
  (satisfies? IPoint p))

(extend-protocol IPoint
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
  (-top-right [b])
  (-bottom-left [b])
  (-bottom-right [b])
  (-dims [b])
  (-center [b]))

(defn box? [b]
  (satisfies? IBox b))

(defrecord Box [tl br]
  IBox
  (-top-left [_]
    tl)
  (-bottom-right [_]
    br)
  (-top-right [_]
    (let [[t l] (-vec tl)
          [b r] (-vec br)]
      (point t r)))
  (-bottom-left [_]
    (let [[t l] (-vec tl)
          [b r] (-vec br)]
      (point b l)))
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
  (-top-right [_]
    (let [[t l] (-vec tl)
          [b r] (-vec (p+ tl dims))]
      (point t r)))
  (-bottom-left [_]
    (let [[t l] (-vec tl)
          [b r] (-vec (p+ tl dims))]
      (point b l)))
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

(defn p-inside? [p b]
  (let [tl (-top-left b)
        br (-bottom-right b)]
    (and (p<= tl p)
         (p>= br p))))

(defn b-inside? [b1 b2]
  (let [tl (-top-left b1)
        br (-bottom-right b1)]
    (and (p-inside? b2 tl)
         (p-inside? b2 br))))

(defn inside? [x b]
  (cond
   (point? x) (p-inside? x b)
   (box? x) (b-inside? x b)
   :else false))

(defn corners [b]
  [(-top-left b)
   (-top-right b)
   (-bottom-right b)
   (-bottom-left b)])

(defn intersects? [b1 b2]
  (->> b2
       corners
       (some #(inside? % b1))
       some?))
