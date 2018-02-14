(ns rocks.clj.configuron.core
  (:require [config.core :as config]
            [hawk.core :as hawk]
            [rocks.clj.transit.core :refer [to-transit]]))

(defn get-project-env [mode project]
  (try
    (let [profiles (->> project
                        (drop-while #(not (= :profiles %)))
                        (drop 1)
                        (first))
          figwheel (->> project
                        (drop-while #(not (= :figwheel %)))
                        (drop 1)
                        (first))
          env (-> (get-in profiles [mode :env])
                  (dissoc :mode)
                  (assoc :figwheel figwheel))]
      env)
    (catch Throwable e
      {})))

(def env config/env)

(defonce watcher (when (= :dev (:mode config/env))
                   (println "starting project.clj watcher")
                   (hawk/watch! [{:paths [(-> (System/getProperty "user.dir")
                                              (str "/project.clj"))]
                                  :filter hawk/file?
                                  :handler (fn [_ {:keys [file]}]
                                             (println "reloading environ config")
                                             (alter-var-root #'env
                                                             (fn [old-env]
                                                               (->> file
                                                                    slurp
                                                                    read-string
                                                                    (get-project-env :dev)
                                                                    (merge old-env)))))}])))

(defn get-client-config []
  (->> (:client-config-keys env)
       (reduce (fn [client-config path]
                 (->> (get-in env path)
                      (assoc-in client-config path)))
               {})
       to-transit))

(defn config-handler [_]
  {:status 200
   :headers {"Content-Type" "application/transit+json"}
   :body (get-client-config)})
