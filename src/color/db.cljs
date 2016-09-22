(ns color.db
  "DataScript functionality relative to colors."
  (:require
   [datascript.core :refer [q transact!]]))

(def color-ids-query
  "DataScript query to get all IDs for colors."
  '[:find [?id ...]
    :where [?id :color/value]])

(def previous-color-ids-query
  "DataScript query to get all IDs that precede selected color."
  '[:find [?id ...]
    :where
    [0 :app/color ?selected]
    [?id :color/value]
    [(< ?id ?selected)]])

(def next-color-ids-query
  "DataScript query to get all IDs that follow selected color."
  '[:find [?id ...]
    :where
    [0 :app/color ?selected]
    [?id :color/value]
    [(> ?id ?selected)]])

(defn previous-color
  "Cycle to the previous color from selected color."
  [connection]
  (let [id (or (->> @connection (q previous-color-ids-query) last)
               (->> @connection (q color-ids-query) last))]
    (transact! connection [{:db/id 0 :app/color id}] :color-previous)))

(defn next-color
  "Cycle to the next color from selected color."
  [connection]
  (let [id (or (->> @connection (q next-color-ids-query) first)
               (->> @connection (q color-ids-query) first))]
    (transact! connection [{:db/id 0 :app/color id}] :color-next)))

(defn initialize
  "Set up colors and selected color."
  [connection]
  (let [colors ["#39cccc" "#2ecc40" "#ffdc00" "#ff851b"]
        transactions (map #(hash-map :color/value %) colors)]
    (transact! connection transactions :color-initialize))
  (next-color connection))
