(ns quo2.components.messages.author.view
  (:require
   [quo2.components.messages.author.style :as style]
   [quo.react-native :as rn]
   [quo2.components.markdown.text :as text]
   [quo2.components.icon :as icons]
   [status-im.utils.utils :as utils]))

(def middle-dot "·")

(defn author [{:keys [profile-name nickname chat-key ens-name time-str is-contact? is-verified? is-untrustworthy?]}]
  [:f>
   (fn []
     (let [is-ens? (> (count ens-name) 0)
           has-nickname? (> (count nickname) 0)]
       [rn/view {:style style/container}
        (if is-ens?
          [text/text {:weight :semi-bold
                      :size   :paragraph-2
                      :style  (style/ens-text)}
           ens-name]
          [:<>
           (when has-nickname?
             [:<>
              [text/text {:weight :semi-bold
                          :size   :paragraph-2
                          :style  (style/nickname-text)}
               nickname]
              [text/text {:size  :paragraph-2
                          :style style/middle-dot-nickname}
               middle-dot]])
           [text/text {:weight (if has-nickname? :medium :semi-bold)
                       :size   :paragraph-2
                       :style  (style/profile-name-text has-nickname?)}
            profile-name]])
        (when is-contact?
          [icons/icon :main-icons2/contact
           {:size            12
            :no-color        true
            :container-style style/icon-container}])
        (cond
          is-verified?
          [icons/icon :main-icons2/verified
           {:size            12
            :no-color        true
            :container-style style/icon-container}]
          is-untrustworthy?
          [icons/icon :main-icons2/untrustworthy
           {:size            12
            :no-color        true
            :container-style style/icon-container}])
        (when-not is-ens?
          [text/text {:monospace true
                      :size      :paragraph-2
                      :style     style/chat-key-text}
           (utils/get-shortened-address chat-key)])
        (when-not is-ens?
          [text/text {:monospace true
                      :size      :paragraph-2
                      :style     style/middle-dot-chat-key}
           middle-dot])
        [text/text {:monospace true
                    :size      :paragraph-2
                    :style     (style/time-text is-ens?)}
         time-str]]))])