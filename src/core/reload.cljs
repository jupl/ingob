(ns core.reload
  "Logic for reload event handling during development.")

;; Set of handlers called when a hot reload occurs in a dev environment.
(defonce handlers (atom #{}))

(defn add-handler
  "Add a callback function to reload handlers."
  [handler]
  (swap! handlers conj handler))

(defn- handle
  "Invoke all registered reload handlers."
  []
  (doseq [handler @handlers]
    (js/setTimeout handler 0)))
