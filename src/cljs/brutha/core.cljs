(ns brutha.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [brutha.svg.scene :as ss]
            [brutha.svg.elements :as el]
            [brutha.om :as bom]))

(enable-console-print!)

(defn scene
  [tl br]
  (let [width (-> js/window
                  .-innerWidth
                  (max 240)
                  (- 5))
        height (-> js/window
                   .-innerHeight
                   (max 240)
                   (- 5))]
    (ss/scene [width height] tl br)))

(def app-state (atom {:text "Hello Chestnut!"
                      :scene (scene [-20 -20] [20 20])}))

(defn main []
  (om/root
   (fn [app owner]
     (reify
       om/IRender
       (render [_]
         (om/build bom/svg-component (:scene app)))))
   app-state
   {:target (. js/document (getElementById "app"))}))

(defn grid [size step]
  (let [attrs {:stroke-width 1
               :class "line"
               :stroke :grey}]
    (-> (concat (for [x (range (/ step 2) size step)]
                  (el/line [x 0] [x size] attrs))
                (for [y (range (/ step 2) size step)]
                  (el/line [0 y] [size y] attrs)))
        el/g)))

(defn tile [c r size]
  (let [g (grid size (/ size 10))
        x (* c size)
        y (* r size)]
    (el/s+ g [x y])))

(doseq [c [-2 -1 0 1]
        r [-2 -1 0 1]]
  (swap! app-state update-in [:scene] ss/add-shape (tile c r 10) -2))

(swap! app-state update-in [:scene] ss/add-shape (el/circle [0 0] 0.5 {:fill :red}))

(swap! app-state update-in [:scene] ss/add-shape (el/circle [-5 3] 0.5 {:fill :red}))

(swap! app-state update-in [:scene] ss/add-shape (el/line [0 0] [-5 3]
                                                          {:class :line
                                                           :stroke-width 1
                                                           :stroke :green}) -1)
