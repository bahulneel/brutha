(ns brutha.om
  (:require [om.core :as om :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [brutha.svg.hiccup :as h]))

(defn svg-component [svg owner]
  (om/component
   (html (h/render svg))))
