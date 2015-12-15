(ns stockfighters.core
  (:require [org.httpkit.client :as http]
            [cheshire.core :as json]
            [cemerick.url :as url]))

(def api-url "https://api.stockfighter.io/ob/api")

(def api-key "ADDME")

#_@(http/post "https://api.stockfighter.io/gm/instances/3506/restart"
              {:headers {"X-Starfighter-Authorization" api-key}})


(defn heartbeat []
  (let [endpoint (str (url/url api-url "heartbeat"))]
    (http/get endpoint)))

(defn exchange-heartbeat [exchange]
  (let [endpoint (str (url/url api-url "venues" exchange "heartbeat"))]
    (http/get endpoint
              {}
              (fn [resp]
                (-> resp
                    (update-in [:body] json/parse-string true))))))


;; Synchronous
#_(def foo @(http/get "https://api.stockfighter.io/ob/api/heartbeat"))

;; Async
#_(def bar
  (let [opts {:start-time (System/currentTimeMillis)}]
    (http/get "https://api.stockfighter.io/ob/api/heartbeat"
              opts
              (fn [{:keys [status headers body error opts]}]
                (let [{:keys [method url start-time]} opts]
                  (println method url "status" status "takes time"
                           (- (System/currentTimeMillis) start-time) "ms"))))))


(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))
