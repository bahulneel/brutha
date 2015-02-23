(ns brutha.layer)

(defprotocol ILayer
  (-z [l])
  (-shapes [l])
  (-add-shape [l s]))

(defn layer? [l]
  (satisfies? ILayer l))

(defrecord Layer [z shapes]
  ILayer
  (-z [_]
    z)
  (-shapes [_]
    shapes)
  (-add-shape [l s]
    (update-in l [:shapes] conj s)))

(defn z [l]
  (-z l))

(defn shapes [l]
  (-shapes l))

(defn add-shape [l s]
  (-add-shape l s))

(defn layer
  ([]
     (layer 0))
  ([z]
     (layer z []))
  ([z s]
     (->Layer z s)))
