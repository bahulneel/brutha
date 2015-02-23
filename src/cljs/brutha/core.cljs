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
                  (max 240))
        height (-> js/window
                   .-innerHeight
                   (max 240))]
    (ss/scene [width height] tl br)))

(def app-state (atom {:text "Hello Chestnut!"
                      :scene (scene [-15 -15] [15 15])}))

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
               :stroke "#2b778b"
               :fill :none
               :stroke-opacity 0.5}
        r (range 0 size step)]
    (-> (concat (for [x r]
                  (el/line [x 0] [x size] attrs))
                (for [y r]
                  (el/line [0 y] [size y] attrs))
                [(el/rect [0 0] [size size] attrs)])
        el/g)))

(defn tile [c r size]
  (let [g (grid size (/ size 10))
        x (* c size)
        y (* r size)]
    (el/s+ g [x y])))

(doseq [c [-2 -1 0 1]
        r [-2 -1 0 1]]
  (swap! app-state update-in [:scene] ss/add-shape (tile c r 10) -2))

(swap! app-state update-in [:scene] ss/add-shape (el/rect [-100 -100] [200 200] {:fill :black}) -3)

(swap! app-state update-in [:scene] ss/add-shape (el/circle [-5 0] 0.5 {:fill "#465b63" :stroke "#4dadd0" :stroke-width 1}))

(swap! app-state update-in [:scene] ss/add-shape (el/circle [0 5] 0.5 {:fill "#4dadd0" :stroke "#2b778b" :stroke-width 1}))

(swap! app-state update-in [:scene] ss/add-shape (el/circle [5 0] 0.5 {:fill "#2b778b" :stroke "#465b63" :stroke-width 1}))

(swap! app-state update-in [:scene] ss/add-shape (el/circle [0 -5] 0.5 {:fill "#465b63" :stroke "#2b778b" :stroke-width 1}))

(swap! app-state update-in [:scene] ss/add-shape (el/line [0 -5] [0 5]
                                                          {:stroke-width 5
                                                           :stroke "#2b778b"
                                                           :stroke-opacity 0.50}) -1)

(swap! app-state update-in [:scene] ss/add-shape (el/line [0 -5] [0 5]
                                                          {:stroke-width 1
                                                           :stroke "#4dadd0"
                                                           :stroke-opacity 0.50}) -1)

(swap! app-state update-in [:scene] ss/add-shape (el/line [-5 0] [0 5]
                                                          {:stroke-width 5
                                                           :stroke "#2b778b"
                                                           :stroke-opacity 0.75}) -1)

(swap! app-state update-in [:scene] ss/add-shape (el/line [-5 0] [0 5]
                                                          {:stroke-width 1
                                                           :stroke "#4dadd0"
                                                           :stroke-opacity 1}) -1)

(swap! app-state update-in [:scene] ss/add-shape (el/line [0 -5] [5 0]
                                                          {:stroke-width 3
                                                           :stroke "#2b778b"
                                                           :stroke-opacity 0.50}) -1)

(swap! app-state update-in [:scene] ss/add-shape (el/circle [10 10] 3 {:fill "#713032" :opacity 0.8}) 100)

(swap! app-state update-in [:scene] ss/add-shape (el/circle [10 10] 1.5 {:fill "#526d5e" :stroke "#465b63" :stroke-width 1}) 100)

(swap! app-state update-in [:scene] ss/add-shape (el/rect [-14.5 9.5] [20 5] {:fill "#0e3143" :opacity 0.7
                                                                              :stroke "#4dadd0" :stroke-width 5 :stroke-opacity 0.75}) 99)

(swap! app-state update-in [:scene] ss/add-shape (el/rect [-14.5 9.5] [20 5] {:fill :none :stroke "#4dadd0" :stroke-width 1 :stroke-opacity 0.75}) 99)

(swap! app-state update-in [:scene] ss/add-shape (el/text [-14 11] "STATUS:" {:fill "#4dadd0"}) 99)

(swap! app-state update-in [:scene] ss/add-shape (el/text [-14 12] "things still work" {:fill "#4dadd0"}) 99)
