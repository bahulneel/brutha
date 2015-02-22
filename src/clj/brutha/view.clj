(ns brutha.view
  (:require [brutha.geo :as geo]))

(defprotocol IViewport
  (-bounds [v])
  (-project [v p])
  (-scale [v p])
  (-visible? [v p]))

(defn viewport? [v]
  (satisfies? IViewport v))

(defn bounds [v]
  (-bounds [v]))

(defn project [v p]
  (-project v p))

(defn scale [v p]
  (-scale v p))

(defn visible? [v p]
  (-visible? v p))

(defrecord Viewport [real internal]
  IViewport
  (-bounds [_]
    real)
  (-visible? [_ p]
    (geo/inside? p internal))
  (-scale [_ p]
    (let [i (geo/dims internal)
          r (geo/dims real)]
      (->> i
           geo/p-inv
           (geo/dot r)
           (geo/dot p))))
  (-project [v p]
    (let [p' (-scale v p)
          tl (geo/top-left real)]
      (geo/p+ p' tl))))

(defn viewport [r i]
  {:pre [(geo/box? r)
         (geo/box? i)]}
  (->Viewport r i))
