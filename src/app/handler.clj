(ns app.handler
  "Application ring handler."
  (:require
   [compojure.core :refer [defroutes GET]]
   [compojure.route :refer [resources]]
   [hiccup.page :refer [html5 include-css include-js]]
   [ring.middleware.defaults :refer [api-defaults wrap-defaults]]))

(def home-page
  "Home page."
  (html5
   {:lang "en"}
   [:head
    [:meta {:charset "UTF-8"}]
    [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge,chrome=1"}]
    [:meta {:name "viewport" :content "initial-scale=1.0"}]
    [:title "App"]
    (include-css "/normalize.css")]
   [:body
    [:div {:id "container" :style "display:none"}]
    (include-js "/index.js")]))

(defroutes routes
  (resources "/")
  (GET "/" [] home-page))

(def main
  "Finalized application Ring handler."
  (wrap-defaults routes api-defaults))
