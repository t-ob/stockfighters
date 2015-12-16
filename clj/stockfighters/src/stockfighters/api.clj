(ns stockfighters.api
  (:require [org.httpkit.client :as http]
            [cheshire.core :as json]
            [cemerick.url :as url]))

(def api-url "https://api.stockfighter.io/ob/api")

(defn callback [resp]
  (-> resp
      :body
      (json/parse-string true)))


(defn heartbeat []
  (let [endpoint (str (url/url api-url "heartbeat"))]
    (http/get endpoint)))

(defn venue-heartbeat [venue]
  (let [endpoint (str (url/url api-url "venues" venue "heartbeat"))]
    (http/get endpoint
              {}
              callback)))

(defn stocks [venue]
  (let [endpoint (str (url/url api-url "venues" venue "stocks"))]
    (http/get endpoint
              {}
              callback)))

(defn order-book [venue stock]
  (let [endpoint (str (url/url api-url "venues" venue "stocks" stock))]
    (http/get endpoint
              {}
              callback)))

(defn place-order [api-key opts]
  (let [{:keys [account venue symbol price qty direction order-type]} opts
        endpoint (str (url/url api-url "venues" venue "stocks" symbol "orders"))]
    (prn opts)
    (http/post endpoint
               {:headers {"X-Starfighter-Authorization" api-key}
                :query-params {"account" account
                               "venue" venue
                               "symbol" symbol
                               "qty" qty
                               "direction" direction
                               "orderType" order-type}}
               callback)))

;; account: "WWS88063421"
;; direction: "buy"
;; orderType: "market"
;; price: 1
;; qty: 100
;; symbol: "CRE"
;; venue: "WFOEX"
