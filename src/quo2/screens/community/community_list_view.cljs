(ns quo2.screens.community.community-list-view
  (:require [quo.react-native :as rn]
            [quo.previews.preview :as preview]
            [reagent.core :as reagent]
            [status-im.constants :as constants]
            [quo.design-system.colors :as quo.colors]
            [quo2.foundations.colors :as colors]
            [quo2.components.community.community-list-view :as community-list-view]
            [status-im.i18n.i18n :as i18n]
            [status-im.react-native.resources :as resources]))

(def community-data
  {:id             constants/status-community-id
   :name           "Status"
   :description    "Status is a secure messaging app, crypto wallet and web3 browser built with the state of the art technology"
   :status         :gated
   :section        :popular
   :permissions    true
   :cover          (resources/get-image :community-cover)
   :community-icon (resources/get-image :status-logo)
   :color          (rand-nth quo.colors/chat-colors)
   :tokens         [{:id 1 :group [{:id 1 :token-icon (resources/get-image :status-logo)}]}]
   :tags           [{:id 1 :tag-label (i18n/label :t/music) :resource (resources/get-image :music)}
                    {:id 2 :tag-label (i18n/label :t/lifestyle) :resource (resources/get-image :lifestyle)}
                    {:id 3 :tag-label (i18n/label :t/podcasts) :resource (resources/get-image :podcasts)}]})

(def descriptor [{:label   "Community list view"
                  :key     :view-style
                  :type    :select
                  :options [{:key   :gated
                             :value "gated"}
                            {:key   :gated
                             :value "open"}
                            {:key   :gated
                             :value "muted"}
                            {:key   :gated
                             :value "mentions"}
                             {:key   :gated
                              :value "unread-messages"}]}
                  {:label "locked:"
                   :key   :locked
                   :type  :boolean}])

(defn cool-preview []
  (let [state (reagent/atom {:view-style :gated})]
    (fn []
      [rn/touchable-without-feedback {:on-press rn/dismiss-keyboard!}
       [rn/view {:padding-bottom 150}
        [rn/view {:flex 1
                  :padding       16}
         [preview/customizer state descriptor]]
        [rn/view {:padding-vertical 60
                  :justify-content  :center}
         [community-list-view/communities-list-view-item community-data]]]])))

(defn preview-community-list-view []
  [rn/view {:background-color (colors/theme-colors colors/neutral-5
                                                   colors/neutral-95)
            :flex             1}
   [rn/flat-list {:flex                      1
                  :keyboardShouldPersistTaps :always
                  :header                    [cool-preview]
                  :key-fn                    str}]])

