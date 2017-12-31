(ns rocks.clj.configuron.core
  (:require [config.core :as config]
            [hawk.core :as hawk]))

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

(def watcher (when (= :dev (:mode config/env))
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
