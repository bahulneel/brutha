(ns brutha.math)

(def lang
  #+clj :java
  #+cljs :js)

(defn sqrt [x]
  #+clj (Math/sqrt x)
  #+cljs (. js/Math (sqrt x)))
