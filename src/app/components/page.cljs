(ns app.components.page
  "Top level UI component."
  (:require
   [color.components.page :as color.page]))

;; ---------- Template

(def page-style
  "Application page styling."
  {:width "100%" :height "100%"})

(defn template
  "Application page template."
  []
  [color.page/component {:style page-style}])

;; ---------- Component

(def component
  "Application page component."
  template)
