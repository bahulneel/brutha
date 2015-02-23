(ns brutha.svg.hiccup
  (:require [brutha.svg.element :as el]
            [brutha.utils :refer [map-vals apply-if apply-unless]]))

(declare render-element)

(defprotocol IRaw
  (-content [r]))

(defn content [r]
  (-content r))

(defrecord Raw [h]
  IRaw
  (-content [_]
    h))

(defn raw [h]
  (->Raw h))

(defn render [e]
  (cond
   (sequential? e) (map render e)
   (el/element? e) (render-element e)
   (satisfies? IRaw e) (content e)
   :else e))

(defn xform-str
  [s p]
  (when-let [[x y] p]
    (str s "(" x "," y ")")))

(defn norm-scale
  [s]
  (when-let [[x y] s]
    (when-not (and (= 1 x) (= 1 y))
      [(float x) (float y)])))

(defn norm-pos
  [s]
  (when-let [[x y] s]
    (when-not (and (= 0 x) (= 0 y))
      [(float x) (float y)])))

(defn xform [e]
  (let [s (->> e
               el/scale
               norm-scale
               (xform-str "scale"))
        p (->> e
               el/position
               norm-pos
               (xform-str "translate"))]
    (->> [p s]
         (remove nil?)
         (interpose ",")
         (apply str))))

(defn norm-num [n]
  (apply-if number?
            float n))

(defn render-element [e]
  (let [t (el/tag e)
        a (map-vals norm-num (el/attrs e))
        xf (xform e)
        a (apply-unless (constantly (empty? xf))
                        assoc a :transform xf)
        b (render (el/body e))]
    (if (or (string? b) (keyword? (first b)))
      [t a b]
      (apply vector t a b))))
