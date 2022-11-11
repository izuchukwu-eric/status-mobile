(ns status-im.ui2.screens.chat.components.edit
  (:require [re-frame.core :as rf]
            [quo2.foundations.colors :as colors]
            [status-im.ui.components.icons.icons :as icons]
            [quo.react-native :as rn]
            [status-im.constants :as constants]
            [status-im.utils.handlers :refer [<sub]]
            [quo2.components.markdown.text :as quo2.text]
            [status-im.ui.screens.chat.photos :as photos]
            [quo2.components.buttons.button :as quo2.button]
            [clojure.string :as string]
            [status-im.ethereum.stateofus :as stateofus]
            [status-im.i18n.i18n :as i18n]
            [status-im.ui2.screens.chat.composer.style :as styles]))

(defn edit-message []
  [rn/view {:style {:flex-direction      :row
                    :height              24
                    :accessibility-label :edit-message}}
   [rn/view {:style {:padding-horizontal 10
                     :flex               1
                     :flex-direction     :row}}
    [icons/icon :main-icons/edit-connector {:color (colors/theme-colors colors/neutral-40 colors/neutral-60)
                                            :container-style {:position :absolute :left 10 :bottom -4 :width 16 :height 16}}]
    [rn/view {:style {:position :absolute :left 36 :right 54 :top 3 :flex-direction :row :align-items :center}}
     [quo2.text/text {:weight          :medium
                      :size            :paragraph-2}
      (i18n/label :t/editing-message)]]]
   [quo2.button/button {:width               24
                        :size                24
                        :type                :outline
                        :accessibility-label :reply-cancel-button
                        :on-press            #(rf/dispatch [:chat.ui/cancel-message-edit])}
    [icons/icon :main-icons/close {:width 16
                                   :height 16
                                   :color (colors/theme-colors colors/black colors/neutral-40)}]]])