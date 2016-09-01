(ns app.devcards
  "Entry point for devcards application, referencing all devcards."
  (:require
   [app.components.page-test]
   [color.handler-test]
   [color.components.page-test]
   [color.sub-test]))

(defn init
  "Configure and bootstrap devcards."
  []
  (core.db/init!)
  (devcards.core/start-devcard-ui!))
