(ns core.devtools
  "Development tools."
  (:require
   [datascript.core :as datascript]))

(defn- add-datom
  "Given a hash and datom, add datom entry to hash."
  [obj datom]
  (let [[entity-id attribute value] datom]
    (assoc-in obj [entity-id attribute] value)))

(defn- db->js
  "Convert a DataScript DB into plain CLJ."
  [db]
  (->> db
       :eavt
       (reduce add-datom {})
       clj->js))

(defn- create-devtools-listener
  "Create a listener for changes by Redux DevTools to update DataScript DB."
  [connection history]
  (fn on-devtools-event
    [message-raw]
    (let [message (js->clj message-raw :keywordize-keys true)
          {type :type {payload-type :type index :index} :payload} message]
      (when (and (= type "DISPATCH") (= payload-type "JUMP_TO_STATE"))
        (datascript/reset-conn! connection (get @history index) :devtools)))))

(defn- create-datascript-listener
  "Create a listener for changes by DataScript DB to update Redux DevTools."
  [devtools history]
  (fn on-datascript-transaction
    [{:keys [db-after tx-data tx-meta]}]
    (when-not (= tx-meta :devtools)
      (let [state (db->js db-after)]
        (if (= tx-meta :init)
          (do
            (reset! history [db-after])
            (.init devtools state))
          (let [type (print-str tx-meta)
                payload (->> tx-data
                             (map (partial into []))
                             clj->js)]
            (swap! history conj db-after)
            (.send devtools #js {:type type :payload payload} state)))))))

(defn- devtools-redux-devtools
  "Set up integration between DataScript DB and Redux DevTools."
  [connection]
  (let [devtools (js/devToolsExtension.connect)
        history (atom [])
        datascript-listener (create-datascript-listener devtools history)
        devtools-listener (create-devtools-listener connection history)
        unsubscribe-devtools (.subscribe devtools devtools-listener)]
    (datascript/listen! connection :devtools datascript-listener)
    (fn []
      (datascript/unlisten! connection :devtools)
      (unsubscribe-devtools))))

(defn datascript-connect
  "Set up integration with DataScript and Redux DevTools."
  [connection]
  (when js/window.devToolsExtension
    (devtools-redux-devtools connection)))
