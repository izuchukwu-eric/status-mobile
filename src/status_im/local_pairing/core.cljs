(ns status-im.local-pairing.core
  (:require [clojure.string :as string]
            [re-frame.core :as re-frame]
            [status-im.utils.fx :as fx]
            [taoensso.timbre :as log]
            [status-im.utils.types :as types]
            [status-im.native-module.core :as status]))

(fx/defn initiate-local-pairing-with-connection-string
  {:events [::initiate-local-pairing-with-connection-string]}
  [{:keys [data]}]
  (let [config-map         (.stringify js/JSON (clj->js {:keyUID ""
                                                        :keystorePath ""
                                                        :password ""}))
        connection-string  data
        ]
      (status/input-connection-string-for-bootstrapping connection-string config-map #(log/debug "this is response from initiate-local-pairing-with-connection-string " %))
    )
)

