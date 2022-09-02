(ns status-im.ui.screens.syncing.sheets.enter-password.views
  (:require [quo.react-native :as rn]
            [status-im.ui.screens.syncing.sheets.enter-password.styles :as styles]
            [re-frame.core :as re-frame]
            [quo.core :as quo]
            [status-im.i18n.i18n :as i18n]
            [status-im.ui.components.react :as react]
            [status-im.ui.components.toolbar :as toolbar]
            [taoensso.timbre :as log]
            [status-im.utils.types :as types]
            [status-im.utils.fx :as fx]
            [status-im.native-module.core :as status]))

(def entered-password (atom ""))

;(defn connection-string-callback [response]
;  (log/debug "we have a call back from status-go --->" response))

(fx/defn preperations-for-connection-string
  {:events [:preperations-for-connection-string]}
  [{:keys [db]}]
  (let [key-uid            (get-in db [:multiaccount :key-uid])
        config-map         (.stringify js/JSON (clj->js {:keyUID key-uid
                            :keystorePath ""
                            :password @entered-password}))]
    (log/debug "config-json ---> " config-map)
    (status/get-connection-string-for-bootstrapping-another-device config-map #(log/debug "we have a call back from status-go --->" %))
    ))

(defn views []
  [:<>
   [rn/view {:style styles/body-container}
    [rn/text {:style styles/header-text} "Enter your password"]
    [react/view {:flex 1}
     [quo/text-input
      {:placeholder         (i18n/label :t/enter-your-password)
       :auto-focus          true
       :accessibility-label :password-input
       :show-cancel         false
       :on-change-text      #(reset! entered-password %)
       :secure-text-entry   true}]]
    [toolbar/toolbar
     {:size :large
      :center
      [react/view {:padding-horizontal 8}
       [quo/button
        {:on-press #(re-frame/dispatch [:preperations-for-connection-string])}
        "Generate Scan Sync Code"]]}]]])
