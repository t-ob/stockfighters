(ns stockfighters.first-steps
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
        (let [{:keys [ok symbols]} (<! in)]
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

(defn s [api-key instance-id account venue]
  (let [status-chan (chan)
        foo (chan)
        bar (aaa foo)
        baz (bbb api-key account venue bar)]
    ;; (go
    ;;   (println (<! status-chan)))
    ;; (go
    ;;   (while true
    ;;     (println (<! foo))))
    ;; (go
    ;;   (while true
    ;;     (println (<! bar))))
    (go
      (while true
        (println (<! baz))))
    (a/put! status-chan (gm/status api-key instance-id))
    (dotimes [n 1]
      (a/put! foo (api/stocks venue))
      (Thread/sleep 5000))))

(comment
  (s api-key 3506 "FOB76171241" "JBAEX"))
