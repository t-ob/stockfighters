(ns stockfighters.first-steps
  (:require [stockfighters.gm :as gm]
            [stockfighters.api :as api]))

(def api-key "ADD ME")

(defn strategy [account venue]
  (let [{:keys [symbols]} @(api/stocks venue)
        [{:keys [sym]} & _] symbols]
    #_symbol
    @(api/place-order api-key
                      {:account account
                       :venue venue
                       :symbol sym
                       :price 10
                       :qty 100
                       :direction "buy"
                       :order-type "market"})))
