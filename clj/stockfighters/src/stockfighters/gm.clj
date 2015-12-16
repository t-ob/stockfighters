(ns stockfighters.gm
  (:require [org.httpkit.client :as http]
            [cemerick.url :refer [url]]
            [cheshire.core :as json]))

(def api-url
  "https://api.stockfighter.io/gm")

(defn status [api-key instance-id]
  (let [endpoint (str (url api-url "instances" instance-id))]
    @(http/get endpoint
               {:headers {"X-Starfighter-Authorization" api-key}})))

(defn start [api-key level-name]
  (let [endpoint (str (url api-url "levels" level-name))]
    @(http/post endpoint
                {:headers {"X-Starfighter-Authorization" api-key}})))

(defn restart [api-key instance-id]
  (let [endpoint (str (url api-url "instances" instance-id "restart"))]
    @(http/post endpoint
                {:headers {"X-Starfighter-Authorization" api-key}})))
