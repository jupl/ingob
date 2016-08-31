(ns app.components.page-test
  "Testing for top level UI component."
  (:require
   [app.components.page :as app.page]
   [cljs.test :refer-macros [is]]
   [color.components.page :as color.page]
   [devcards.core :refer-macros [defcard-rg deftest]]))

(deftest component-test
  (is (= (app.page/component)
         [color.page/component {:style {:width "100%" :height "100%"}}])))
