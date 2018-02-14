(ns rocks.clj.configuron.core
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<! >! alts! timeout]]
            [cognitect.transit :as t])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defn from-transit [in]
  (let [reader (t/reader :json)]
    (t/read reader in)))

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
