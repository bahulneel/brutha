(ns brutha.scene
  (:require [brutha.layer :as bl]))

(defprotocol IScene
  (-view [s])
  (-layers [l])
  (-add-shape [s sh z]))

(defn scene? [s]
  (satisfies? IScene s))

(defn view [s]
  (-view s))

(defn layers [s]
  (-layers s))

(defn add-shape
  ([s sh]
     (-add-shape s sh nil))
  ([s sh z]
     (-add-shape s sh z)))

(defrecord Scene [view layers]
  IScene
  (-view [_]
    view)
  (-layers [_]
    (prn (vals layers))
    (sort-by bl/z (vals layers)))
  (-add-shape [s sh z]
    (let [z (or z 0)
          layer (or (get layers z) (bl/layer z))
          layer (bl/add-shape layer sh)]
      (update-in s [:layers] assoc z layer))))

(defn scene
  ([view]
     (scene view {}))
  ([view layers]
     (->Scene view layers)))
