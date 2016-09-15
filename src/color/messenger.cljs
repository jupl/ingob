(ns color.messenger
  "Message handler for color specifics."
  (:require
   [cljs.core.async :refer [<! alts! timeout]]
   [color.db :as db]
   [core.messenger :as messenger])
  (:require-macros
   [cljs.core.async.macros :refer [go go-loop]]))

(defn register-previous
  "Hook up to messenger to handle color change to previous."
  [m connection]
  (let [[handler] (messenger/subscribe m :color-previous)]
    (go-loop []
      (<! handler)
      (db/previous-color connection)
      (recur))))

(defn register-next
  "Hook up to messenger to handle color change to next."
  [m connection]
  (let [[handler] (messenger/subscribe m :color-next)]
    (go-loop []
      (<! handler)
      (db/next-color connection)
      (recur))))

(defn register-auto
  "Hook up to messenger to auto change color until manual change."
  [m connection]
  (let [[previous unsubscribe-previous] (messenger/subscribe m :color-previous)
        [next unsubscribe-next] (messenger/subscribe m :color-next)]
    (go-loop []
      (if (nil? (-> [previous next (timeout 1000)] alts! (get 0)))
        (do
          (db/next-color connection)
          (recur))
        (do
          (unsubscribe-previous)
          (unsubscribe-next))))))

(defn register
  "Hook up to messenger for color events."
  [m connection]
  (let [[handler unsubscribe] (messenger/subscribe m :initialize)]
    (db/initialize connection)
    (go
      (<! handler)
      (doseq [func [register-previous register-next register-auto]]
        (func m connection))
      (unsubscribe))))
