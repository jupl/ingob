(ns core.messenger
  "Simple pubsub messaging system with helpers."
  (:require
   [cljs.core.async :refer [chan pub put! sub unsub]])
  (:require-macros
   [cljs.core.async.macros :refer [go]]))

(defprotocol IMessenger
  ""
  (dispatch [_ type] [_ type payload]
    "")
  (subscribe [_ type]
    ""))

(defrecord Messenger [broker publisher]
  IMessenger
  (dispatch [_ type]
    (put! broker {:type type}))
  (dispatch [_ type payload]
    (put! broker {:type type :payload payload}))
  (subscribe [_ type]
    (let [handler (chan)
          unsubscribe #(unsub publisher type handler)]
      (sub publisher type handler)
      [handler unsubscribe])))

(defn create-messenger
  "Construct a pubsub system with helper functions and conveniences."
  ([] (create-messenger (chan)))
  ([broker] (Messenger. broker (pub broker :type))))