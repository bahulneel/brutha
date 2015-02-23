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
                      :scene (scene [-100 -100] [100 100])}))

(defn main []
  (om/root
   (fn [app owner]
     (reify
       om/IRender
       (render [_]
         (om/build bom/svg-component (:scene app)))))
   app-state
   {:target (. js/document (getElementById "app"))}))

(swap! app-state update-in [:scene] ss/add-shape (el/line [0 0] [0 50]
                                                          {:stroke-width 1
                                                           :class "line"
                                                           :stroke :black}))
