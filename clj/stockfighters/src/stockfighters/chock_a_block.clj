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



