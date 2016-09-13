(ns app.main
  "Entry point for main application."
  (:require
   [core.config :refer-macros [when-production]]
   [core.devtools :as devtools]
   [core.reload :as reload]
   [datascript.core :as datascript]
   [reagent.core :as reagent]))

(def schema
  "DataScript DB schema for application DB."
  nil)

(def container-style
  "Style attributes applied to the CLJS application container"
  {:position "fixed"
   :top 0
   :bottom 0
   :left 0
   :right 0
   :overflow "auto"})

(defn render
  "Render the application."
  []
  (set! js/container.style.cssText nil)
  (doseq [[key val] container-style]
    (aset js/container.style (clj->js key) (clj->js val)))
  (reagent/render [:div "Cool"] js/container))

(defn init
  "Configure and bootstrap the application."
  []
  (let [connection (datascript/create-conn schema)]
    (when-production false
      (enable-console-print!)
      (reload/add-handler render)
      (devtools/datascript-connect connection))))
