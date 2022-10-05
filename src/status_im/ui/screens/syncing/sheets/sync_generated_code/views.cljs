(ns status-im.ui.screens.syncing.sheets.sync-generated-code.views
  (:require [quo.react-native :as rn]
            [status-im.ui.screens.syncing.sheets.sync-generated-code.styles :as styles]
            [re-frame.core :as re-frame]
            [quo2.components.info.information-box :as information-box]
            [status-im.ui.components.qr-code-viewer.views :as qr-code-viewer]))

(def example-key "2:5vd6SL:KFC:26gAouU6D6A4dCs9LK7jHmXZ3gjVdPczvX7yeusZRHTeR:3HxJ9Qr4H351dPoXjQYsdPX4tK6tV6TkdsHk1xMZEZmL:3")

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
