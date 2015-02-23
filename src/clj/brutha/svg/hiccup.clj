(ns brutha.svg.hiccup
  (:require [brutha.svg.element :as el]))

(declare render-element)

(defn render [e]
  (cond
   (sequential? e) (map render e)
   (satisfies? el/IElement e) (render-element e)
   :else e))

(defn xform-str
  [s p]
  (when p
    (let [[x y] p]
      (str s "(" x "," y ")"))))

(defn xform [e]
  (let [s (xform-str "scale"
                     (el/scale e))
        p (xform-str "translate"
                     (el/position e))]
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
