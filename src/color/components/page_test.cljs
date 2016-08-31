(ns color.components.page-test
  "Testing/devcards for color page UI component."
  (:require
   [cljs.test :refer-macros [is]]
   [color.components.page :as color.page]
   [devcards.core :refer-macros [defcard-rg deftest]]))

(defcard-rg component
  "This is the `color/page` component."
  [color.page/component {:style {:height 200}}])

(deftest template-test
  (let [props {:style {:a 1 :b 2} :color "red"}
        [_ {:keys [style]}] (color.page/template props)]
    (is (= style {:transition "background 0.8s ease-out"
                  :a 1
                  :b 2
                  :background-color "red"}))))
