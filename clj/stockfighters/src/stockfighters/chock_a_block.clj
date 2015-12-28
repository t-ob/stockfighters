(ns stockfighters.chock-a-block
  (:require [stockfighters.gm :as gm]
            [stockfighters.api :as api]
            [clojure.core.async
             :as a
             :refer [>! <! >!! <!! go chan buffer close! thread alts! alts!!
                     timeout]]))

(def api-key "ADDME")

(defn aaa [in]
  (let [out (chan)]
    (go
      (while true
        (let [{:keys [ok symbols] :as resp} (<! in)]
          (>! out (map :symbol symbols)))))
    out))

(defn bbb [api-key account venue in]
  (let [out (chan)]
    (go
      (while true
        (>! out
            (api/place-order api-key
                             {:account account
                              :venue venue
                              :symbol (first (<! in))
                              :price 10
                              :qty 100
                              :direction "buy"
                              :order-type "market"}))))
    out))

(defn ccc [venue in]
  (let [out (chan)]
    (go
      (while true
        (>! out
            (api/order-book venue (first (<! in))))))
    out))

(defn ddd [in]
  (let [out (chan)]
    (go
      (while true
        (>! out (str (<! in) "_lol"))))
    out))

#_(let [
      account "CMS65877929"
      venue "YXBEX"
        sym "FAL"]
  (let [stocks (api/ticker account venue sym)
        bids (chan (a/sliding-buffer 1))]
    (go
      (loop [n 0]
        (let [data (<! stocks)
              ok (get data "ok")
              quote (get data "quote")
              ask (get quote "ask")
              ask-size (get quote "askSize")]
          (when (= 0 (mod n 50))
             (println "♥" n))
          (when ask
            (>! bids [ask ask-size]))
          (if ok
            (recur (inc n))
            (println data)))))
    (while true
      (let [[bid bid-size] (<!! bids)
            a (int (* 0.7 bid))
            b (int (* 1.1 bid))
            c (+ a (rand-int (- b a)))]
        (println [bid bid-size])
        (println
         (api/place-order api-key
                           {:account account
                            :venue venue
                            :symbol sym
                            :price c
                            :qty 500
                            :direction "buy"
                            :order-type "limit"}))
        
        )
      )))
