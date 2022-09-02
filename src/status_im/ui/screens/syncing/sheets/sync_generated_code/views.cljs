(ns status-im.ui.screens.syncing.sheets.sync-generated-code.views
  (:require [quo.react-native :as rn]
            [status-im.ui.screens.syncing.sheets.sync-generated-code.styles :as styles]
            [re-frame.core :as re-frame]
            [quo2.components.info.information-box :as information-box]
            [status-im.ui.components.qr-code-viewer.views :as qr-code-viewer]))

(def example-key "2:4FHRnp:Q4:uqnnMwVUfJc2Fkcaojet8F1ufKC3hZdGEt47joyBx9yd:BbnZ7Gc66t54a9kEFCf7FW8SGQuYypwHVeNkRYeNoqV6:3")

(defn views []
  (let [window-width @(re-frame/subscribe [:dimensions/window-width])]
    [:<>
     [rn/view {:style styles/body-container}
      [rn/text {:style styles/header-text} "Sync code generated"]
      [qr-code-viewer/qr-code-view (* window-width 0.808) example-key]
      [information-box/information-box {:type      :informative
                                        :closable? false
                                        :icon      :main-icons2/placeholder
                                        :style     {:margin-top 20}} "On your other device, navigate to the Syncing screen and select “Scan sync”"]]]))
