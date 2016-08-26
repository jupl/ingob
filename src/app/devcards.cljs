(ns app.devcards
  (:require [app.components.page-test]
            [color.handler-test]
            [color.components.page-test]
            [color.sub-test]
            [core.db :as db]
            [devcards.core :refer [start-devcard-ui!]]))

(defn init
  "Configure and bootstrap devcards."
  []
  (db/init!)
  (start-devcard-ui!))
