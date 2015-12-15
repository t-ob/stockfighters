(ns stockfighters.gm
  (:require [org.httpkit.client :as http]
            [cemerick.url :refer [url]]
            [cheshire.core :as json]))

(def api-url
  "https://api.stockfighter.io/gm")

(defn start [api-key level-name]
  (let [endpoint (str (url api-url "levels" level-name))]
    @(http/post endpoint
                {:headers {"X-Starfighter-Authorization" api-key}})))
