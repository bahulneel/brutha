(ns brutha.svg.elements
  (:require [brutha.svg :as svg]
            [brutha.svg.element :as el]
            [brutha.shape :as bsh]
            [brutha.geo :as geo]))

(defn p->props
  [m p x-name y-name]
  (let [[x y] p]
    (-> m
        (assoc x-name x)
        (assoc y-name y))))

(defn line
  ([p1 p2]
     (line p1 p2 {}))
  ([p1 p2 attrs]
     (let [attrs (-> attrs
                     (p->props p1 :x1 :y1)
                     (p->props p2 :x2 :y2))]
       (el/element :line attrs nil))))

(defn rect
  ([tl wh]
     (rect tl wh {}))
  ([tl wh attrs]
     (let [b (geo/rect tl wh)
           p (geo/top-left b)
           d (geo/dims b)
           attrs (-> attrs
                     (p->props p :x :y)
                     (p->props d :width :height))]
       (el/element :rect attrs nil))))

(defn circle
  ([c r]
     (circle c r {}))
  ([c r attrs]
     (let [attrs (-> attrs
                     (p->props c :cx :cy)
                     (assoc :r r))]
       (el/element :circle attrs nil))))

(defn ellipse
  ([c r]
     (ellipse c r {}))
  ([c r attrs]
     (let [attrs (-> attrs
                     (p->props c :cx :cy)
                     (p->props c :rx :ry))]
       (el/element :ellipse attrs nil))))

(defn text
  ([p t]
     (text p t {}))
  ([p t attrs]
     (let [attrs (-> attrs
                     (p->props p :x :y))]
       (el/element :text attrs t))))

(defn g
  [& es]
  (svg/group es))

(def id bsh/id=)

(def shape bsh/ref-shape)

(def s+ bsh/s+)

(def s* bsh/s*)
