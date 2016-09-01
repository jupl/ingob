(ns color.sub
  "re-frame subscriptions relative to color.")

(defn color
  "Extract color from app-db."
  [{:keys [color]}]
  color)
