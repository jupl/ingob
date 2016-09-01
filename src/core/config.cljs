(ns core.config
  "Configuration information used across the project."
  (:require
   [cljs.nodejs :refer [process require]]))

;; If true then application is a production environment
(goog-define production false)

(def base-url
  "Base URL for application assets."
  (if (identical? production true)
    (str "file://" js/__dirname "/")
    (let [os (require "os")]
      (str "http://" (.hostname os) ":3000/"))))

(def osx
  "if true then platform is os x."
  (= (.-platform process) "darwin"))
