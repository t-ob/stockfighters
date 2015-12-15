(ns stockfighters.first-steps
  (:require [stockfighters.gm :as gm]
            [stockfighters.core :as api]))

(def api-key "ADDME")

#_@(api/stocks "HMWBEX")
#_@(api/order-book "HMWBEX" "TJIY")
#_@(api/place-order api-key "HMWBEX" "TJIY"
                 "HAA35569327"
                 10
                 100
                 "buy"
                 "market")








