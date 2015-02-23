(ns brutha.svg.hiccup
  (:require [brutha.svg.element :as el]))

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
   (satisfies? el/IElement e) (render-element e)
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
    (->> [s p]
         (remove nil?)
         (interpose ",")
         (apply str))))

(defn render-element [e]
  (let [t (el/tag e)
        a (el/attrs e)
        xf (xform e)
        a (if (empty? xf)
            a
            (assoc a :transform xf))
        b (render (el/body e))]
    (if (keyword? (first b))
      [t a b]
      (apply vector t a b))))
