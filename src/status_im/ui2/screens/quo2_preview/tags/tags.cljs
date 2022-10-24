(ns status-im.ui2.screens.quo2-preview.tags.tags
  (:require [react-native.core :as rn]
            [status-im.ui2.screens.quo2-preview.preview :as preview]
            [status-im.ui.components.react :as react]
            [quo2.foundations.colors :as colors]
            [quo2.components.tags.tags :as tags]
            [status-im.react-native.resources :as resources]
            [quo2.components.tags.scrollable-view :as scrollable-view]
            [reagent.core :as reagent]))

(def descriptor [{:label   "Size:"
                  :key     :size
                  :type    :select
                  :options [{:key   32
                             :value "32"}
                            {:key   24
                             :value "24"}]}
                 {:label   "Type:"
                  :key     :type
                  :type    :select
                  :options [{:key   :emoji
                             :value "Emoji"}
                            {:key   :icon
                             :value "Icons"}
                            {:key   :label
                             :value "Label"}]}
                 {:label "Scrollable:"
                  :key   :scrollable?
                  :type  :boolean}
                 {:label   "Fade Out:"
                  :key     :fade-end-percentage
                  :type    :select
                  :options [{:key   1
                             :value "1%"}
                            {:key   0.4
                             :value "0.4%"}]}
                 {:label "Labelled:"
                  :key   :labelled?
                  :type  :boolean}
                 {:label "Disabled:"
                  :key   :disabled?
                  :type  :boolean}
                 {:label "Blurred background:"
                  :key   :blurred?
                  :type  :boolean}])

(defn cool-preview []
  (let [state  (reagent/atom {:size                32
                              :labelled?           true
                              :type                :emoji
                              :fade-end-percentage 0.4})]
    (fn []
      [rn/touchable-without-feedback {:on-press rn/dismiss-keyboard!}
       [rn/view {:style {:padding-bottom 150
                         :padding-top    60}}
        [rn/view {:style {:flex 1}}
         [preview/customizer state descriptor]]
        [rn/view {:style {:flex               1
                          :justify-content    :center
                          :top                60
                          :padding-horizontal 16}}
         (when (:blurred? @state)
           [rn/view {:align-items        :center
                     :height             100
                     :border-radius      16}
            [react/image {:source (resources/get-image :community-cover)
                          :style  {:flex               1
                                   :width              "100%"
                                   :border-radius      16}}]
            [react/blur-view {:flex               1
                              :style              {:border-radius      16
                                                   :height             100
                                                   :position           :absolute
                                                   :left               0
                                                   :right              0}
                              :blur-amount         20
                              :overlay-color      (colors/theme-colors
                                                   colors/white-opa-70
                                                   colors/neutral-80-opa-80)}]])
         [rn/view {:style {:position       :absolute
                           :align-self     :center}}
          (if (:scrollable? @state)
            [scrollable-view/view  (merge @state
                                          {:scroll-on-press?    true
                                           :fade-end?           true
                                           :default-active      1
                                           :component           :tags
                                           :labelled?           (if (= :label type) true (:labelled? @state))
                                           :resource            (when (= type :icon)
                                                                  :main-icons2/placeholder)
                                           :data                [{:id 1 :label "Music"     :resource (resources/get-image :music)}
                                                                 {:id 2 :label "Lifestyle" :resource (resources/get-image :lifestyle)}
                                                                 {:id 2 :label "Podcasts"  :resource (resources/get-image :podcasts)}
                                                                 {:id 2 :label "Music"     :resource (resources/get-image :music)}
                                                                 {:id 3 :label "Lifestyle" :resource (resources/get-image :lifestyle)}]})]
            [tags/tags (merge @state
                              {:default-active      1
                               :resource            (if (= type :emoji)
                                                      (:resource @state)
                                                      :main-icons2/placeholder)
                               :labelled?           (if (= :label type) true (:labelled? @state))
                               :data                [{:id 1 :label "Music" :resource (resources/get-image :music)}
                                                     {:id 2 :label "Lifestyle" :resource (resources/get-image :lifestyle)}
                                                     {:id 3 :label "Podcasts" :resource (resources/get-image :podcasts)}]})])]]]])))
(defn preview-tags []
  [rn/view {:flex             1
            :background-color (colors/theme-colors
                               colors/white
                               colors/neutral-90)}
   [rn/flat-list {:flex                      1
                  :keyboardShouldPersistTaps :always
                  :header                    [cool-preview]
                  :key-fn                    str}]])
