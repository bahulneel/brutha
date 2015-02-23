(ns brutha.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [brutha.svg.scene :as ss]
            [brutha.om :as bom]))

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

(defonce app-state (atom {:text "Hello Chestnut!"
                          :scene (scene [-100 -100] [100 100])}))

(defn main []
  (om/root
   (fn [app owner]
     (reify
       om/IRender
       (render [_]
         (dom/div nil
                  (dom/h1 nil (:text app))
                  (om/build bom/svg-component (:scene app))))))
   app-state
   {:target (. js/document (getElementById "app"))}))
