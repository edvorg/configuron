(ns rocks.clj.configuron.core
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<! >! alts! timeout chan]]
            [rocks.clj.transit.core :refer [from-transit]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defn get-encoded-data [id]
  (some-> (.getElementById js/document id)
          (.getAttribute "transit")
          (from-transit)))

(def env (get-encoded-data "config"))

(defn get-env []
  (let [c (chan 1)]
    (go
      (when (nil? env)
        (let [{:keys [body]} (<! (http/get "/environ"))
              new-env        (if (nil? body)
                               {}
                               body)]
          (set! env new-env)))
      (>! c env))
    c))
