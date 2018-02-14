(ns rocks.clj.configuron.core
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<! >! alts! timeout]]
            [rocks.clj.transit.core :refer [from-transit]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defn get-encoded-data [id]
  (-> (.getElementById js/document id)
      (.getAttribute "transit")
      (from-transit)))

(def env (get-encoded-data "config"))

(def fetcher (go
               (if (seq env)
                 env
                 (do
                   (println "requesting config")
                   (let [{:keys [body] :as r} (<! (http/get "/environ"))]
                     (set! env body)
                     body)))))
