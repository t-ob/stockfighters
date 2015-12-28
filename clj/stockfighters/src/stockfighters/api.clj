(ns stockfighters.api
  (:require [org.httpkit.client :as http]
            [cheshire.core :as json]
            [cemerick.url :as url]

            [aleph.http]
            [byte-streams]
            [manifold.stream]
            [clojure.core.async]))

(def api-url "https://api.stockfighter.io/ob/api")
(def ws-url "https://api.stockfighter.io/ob/api/ws")

(defn callback [resp]
  (-> resp
      :body
      (json/parse-string true)))


#_(-> @(aleph.http/get (str api-url "/heartbeat"))
      :body
      byte-streams/to-string
      json/parse-string)


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
    @(http/get endpoint
               {}
               callback)))

(defn order-book [venue stock]
  (let [endpoint (str (url/url api-url "venues" venue "stocks" stock))]
    @(http/get endpoint
               {}
               callback)))

(defn place-order [api-key opts]
  (let [{:keys [account venue symbol price qty direction order-type]} opts
        endpoint (str (url/url api-url "venues" venue "stocks" symbol "orders"))
        params {"account" account
                "venue" venue
                "symbol" symbol
                "qty" qty
                "price" price
                "direction" direction
                "orderType" order-type}]
    @(http/post endpoint
                {:headers {"X-Starfighter-Authorization" api-key
                           "Content-Type" "application/json"}
                 :body (json/generate-string params)}
                callback)))

(defn ticker [account venue stock]
  (let [endpoint (-> (str (url/url ws-url account "venues" venue "tickertape"
                                   "stocks" stock))
                     (clojure.string/replace-first "http" "ws"))
        ws @(aleph.http/websocket-client endpoint)
        out (clojure.core.async/chan 1
                                     (map json/parse-string))]
    (manifold.stream/connect ws out)
    out))

(comment
  (let [stocks (ticker "BB42524257" "SQPEX" "FDH")]
    (while true
      (println (clojure.core.async/<!! stocks)))))
