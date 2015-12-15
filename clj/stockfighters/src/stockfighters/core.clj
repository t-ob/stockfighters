(ns stockfighters.core
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

(defn place-order [api-key venue stock account price qty direction order-type]
  (let [endpoint (str (url/url api-url "venues" venue "stocks" stock "orders"))]
    (http/post endpoint
               {:headers {"X-Starfighter-Authorization" api-key}
                :query-params {"account" account
                               "venue" venue
                               "stock" stock
                               "qty" qty
                               "direction" direction
                               "orderType" order-type}}
               callback)))
