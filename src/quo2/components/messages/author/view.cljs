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
                      :style  styles/ens-text}
           ens-name]
          [:<>
           (when has-nickname?
             [:<>
              [text/text {:weight :semi-bold
                          :size   :paragraph-2
                          :style  styles/nickname-text}
               nickname]
              [text/text {:size  :paragraph-2
                          :style styles/middle-dot-nickname}
               "·"]])
           [text/text {:weight (if has-nickname? :medium :semi-bold)
                       :size   :paragraph-2
                       :style  (styles/profile-name-text has-nickname?)}
            profile-name]])
        (when is-contact?
          [icons/icon :main-icons2/contact {:size            12
                                            :no-color        true
                                            :container-style styles/icon-container}])
        (cond
          is-verified?
          [icons/icon :main-icons2/verified {:size            12
                                             :no-color        true
                                             :container-style styles/icon-container}]
          is-untrustworthy?
          [icons/icon :main-icons2/untrustworthy {:size            12
                                                  :no-color        true
                                                  :container-style styles/icon-container}])
        (when-not is-ens?
          [text/text {:monospace true
                      :size      :paragraph-2
                      :style     styles/public-key-text}
           public-key])
        (when-not is-ens?
          [text/text {:monospace true
                      :size      :paragraph-2
                      :style     styles/middle-dot-public-key}
           "·"])
        [text/text {:monospace true
                    :size      :paragraph-2
                    :style     (styles/time-text is-ens?)}
         time-str]]))])