(ns brutha.svg.scene
  (:require [brutha.view :as bv]
            [brutha.geo :as geo]
            [brutha.scene :as bs]
            [brutha.svg :as svg]))

(defn scene [dims tl br]
  (let [r (geo/rect [0 0] dims)
        i (geo/box tl br)
        view (bv/viewport r i)
        scene (bs/scene view)]
    (svg/svg scene)))

(def add-shape bs/add-shape)
