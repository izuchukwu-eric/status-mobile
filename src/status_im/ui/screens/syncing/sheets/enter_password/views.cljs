(ns status-im.ui.screens.syncing.sheets.enter-password.views
  (:require [quo.react-native :as rn]
            [status-im.ui.screens.syncing.sheets.enter-password.styles :as styles]
;            [status-im.ui.screens.syncing.sheets.sync-generated-code :as sync-generated-code]
            [re-frame.core :as re-frame]
            [quo.core :as quo]
            [status-im.i18n.i18n :as i18n]
            [status-im.ui.components.react :as react]
            [status-im.ui.components.toolbar :as toolbar]
            [taoensso.timbre :as log]
            [status-im.utils.security :as security]
            [status-im.ethereum.core :as ethereum]
            [status-im.utils.types :as types]
            [status-im.utils.fx :as fx]
            [status-im.native-module.core :as status]
            [status-im.async-storage.core :as async-storage]
            [quo2.components.info.information-box :as information-box]
            [status-im.ui.components.qr-code-viewer.views :as qr-code-viewer]
            [status-im.utils.handlers :refer [>evt]]))

(def entered-password (atom ""))

(defn qr-code-view-with-connection-string [connection-string]
  (let [window-width       @(re-frame/subscribe [:dimensions/window-width])]
    [:<>
     [rn/view {:style styles/body-container}
      [rn/text {:style styles/header-text} "Sync code generated"]
      [qr-code-viewer/qr-code-view (* window-width 0.808) connection-string]
      [information-box/information-box {:type      :informative
                                        :closable? false
                                        :icon      :main-icons2/placeholder
                                        :style     {:margin-top 20}} "On your other device, navigate to the Syncing screen and select “Scan sync”"]]]
    )
  )

(fx/defn preperations-for-connection-string
  {:events [:preperations-for-connection-string]}
  [{:keys [db]}]
  (let [key-uid            (get-in db [:multiaccount :key-uid])
        sha3-pwd           (ethereum/sha3 (security/safe-unmask-data @entered-password))
        config-map         (.stringify js/JSON (clj->js {:keyUID key-uid
                            :keystorePath ""
                            :password sha3-pwd}))
        connection-string  (atom "")

        ]
    (status/get-connection-string-for-bootstrapping-another-device config-map #(>evt [:bottom-sheet/show-sheet
                                                                                                                 {:showHandle? false
                                                                                                                  :content (fn []
                                                                                                                             [qr-code-view-with-connection-string %])}]))
    )
)

(defn views []
  [:<>
   [rn/view {:style styles/body-container}
          [rn/view
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
                "Generate Scan Sync Code"]]}]
           ]
    ]])
