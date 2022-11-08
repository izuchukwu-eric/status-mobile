(ns quo2.components.messages.author.view
  (:require
   [quo2.components.messages.author.styles :as styles]
   [quo.react-native :as rn]
   [quo2.components.markdown.text :as text]
   [quo2.foundations.colors :as colors]
   [quo2.components.icon :as icons]))

(defn author [{:keys [profile-name nickname public-key ens-name time-str is-contact? is-verified? is-untrustworthy?]}]
  [:f>
   (fn []
     (let [is-ens? (> (count ens-name) 0)
           has-nickname? (> (count nickname) 0)]
       [rn/view {:style styles/container}
        (if is-ens?
          [text/text {:weight :semi-bold
                      :size   :paragraph-2
                      :color  (colors/theme-colors colors/neutral-100 colors/white)
                      :style  {:color (colors/theme-colors colors/neutral-100 colors/white)}}
           ens-name]
          [:<>
           (when has-nickname?
             [:<>
              [text/text {:weight :semi-bold
                          :size      :paragraph-2
                          :style     {:color (colors/theme-colors colors/neutral-100 colors/white)}}
               nickname]
              [text/text {:size      :paragraph-2
                          :style     {:color       colors/neutral-50
                                      :margin-horizontal 4}}
               "·"]])
           [text/text {:weight (if has-nickname? :medium :semi-bold)
                       :size   :paragraph-2
                       :style {:color (if has-nickname? (colors/theme-colors colors/neutral-60 colors/neutral-40) (colors/theme-colors colors/neutral-100 colors/white))}}
            profile-name]])
        (when is-contact?
          [icons/icon :main-icons2/contact {:size 12
                                            :no-color true
                                            :container-style {:margin-left 4}}])
        (cond
          is-verified?
          [icons/icon :main-icons2/verified {:size 12
                                             :no-color true
                                             :container-style {:margin-left 4}}]
          is-untrustworthy?
          [icons/icon :main-icons2/untrustworthy {:size 12
                                                  :no-color true
                                                  :container-style {:margin-left 4}}])
        (when-not is-ens?
          [text/text {:monospace true
                      :size      :paragraph-2
                      :style     {:color       colors/neutral-50
                                  :margin-left 8}}
           public-key])
        (when-not is-ens?
          [text/text {:monospace true
                      :size      :paragraph-2
                      :style     {:color       colors/neutral-50
                                  :margin-left 4}}
           "·"])
        [text/text {:monospace true
                    :size      :paragraph-2
                    :style     {:color       colors/neutral-50
                                :margin-left (if is-ens? 8 4)}}
         time-str]]))])