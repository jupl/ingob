(ns color.sub-test
  "Testing/devcards for color re-frame subscriptions."
  (:require
   [cljs.test :refer-macros [is]]
   [color.sub :as sub]
   [devcards.core :refer-macros [deftest]]))

(deftest color-test
  (is (= (sub/color {:color "acolor"}) "acolor")))
