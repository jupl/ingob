(ns app.main
  "Entry point for main application."
  (:require
   [app.handler :as handler]
   [immutant.web :as web])
  (:gen-class))

(def defaults
  "Default server options that can be overridden."
  {:port 3000})

(defn- -main
  "Start the web server, leveraging command line arguments."
  [& {:as args}]
  (web/run handler/main (merge defaults args)))
