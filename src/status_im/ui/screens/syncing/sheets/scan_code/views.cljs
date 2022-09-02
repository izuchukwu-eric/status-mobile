(ns status-im.ui.screens.syncing.sheets.scan-code.views
  (:require
   [quo.react-native :as rn]
   [status-im.ui.screens.syncing.sheets.sync-generated-code.styles :as styles]))

(defn views []
  [:<>
   [rn/view {:style styles/body-container}
    [rn/text {:style styles/header-text} "Scan code"]]])
