(ns brutha.svg.element
  (:require [brutha.shape :as sh]))

(defprotocol IElement
  (-tag [e])
  (-attrs [e])
  (-scale [e])
  (-position [e])
  (-body [e]))

(defn element? [e]
  (satisfies? IElement e))

(defn tag [e]
  (-tag e))

(defn attrs [e]
  (-attrs e))

(defn position [e]
  (-position e))

(defn scale [e]
  (-scale e))

(defn body [e]
  (-body e))

(defrecord SvgElement [tag attrs scale pos body]
  IElement
  (-tag [_]
    tag)
  (-attrs [_]
    attrs)
  (-scale [_]
    scale)
  (-position [_]
    pos)
  (-body [_]
    body))

(extend-protocol sh/IShape
  SvgElement
  (-id [e]
    {:id (attrs e)})
  (-position [e]
    (position e))
  (-scale [e]
    (scale e)))

(defn element
  ([tag body]
     (element nil body))
  ([tag attrs body]
     (element tag attrs nil nil body))
  ([tag attrs scale pos body]
     (->SvgElement tag attrs scale pos body)))

(extend-type brutha.shape.RefShape
  IElement
  (-tag [_]
    :use)
  (-attrs [s]
    {:xlink:href (sh/link s)})
  (-scale [s]
    (sh/scale s))
  (-position [s]
    (sh/position s))
  (-body [_]
    nil))

(extend-type brutha.shape.TransformedShape
   IElement
  (-tag [s]
    (tag (sh/content s)))
  (-attrs [s]
    (attrs (sh/content s)))
  (-scale [s]
    (sh/scale s))
  (-position [s]
    (sh/position s))
  (-body [s]
    (body (sh/content s))))
