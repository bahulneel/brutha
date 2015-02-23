(ns brutha.om
  (:require [om.core :as om :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [brutha.svg.hiccup :as h]))

(defn svg-component [svg owner]
  (let [rendered (h/render (om/value svg))]
    (om/component
     (html rendered))))
