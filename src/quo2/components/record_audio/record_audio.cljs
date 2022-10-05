(ns quo2.components.record-audio.record-audio
  (:require [quo2.components.markdown.text :as text]
            [quo.react-native :as rn]
            [quo.theme :as theme]
            [quo2.foundations.colors :as colors]
            [quo2.components.icon :as icons]
            [quo2.reanimated :as reanimated]
            [quo2.gesture :as gesture]))

(def themes
  {:light {:icon-color           colors/white
           :label                {:style    {:color colors/white}}
           :background-color     {:default  colors/primary-50
                                  :pressed  colors/primary-60
                                  :disabled colors/primary-50}}
   :dark  {:icon-color          colors/white
           :label               {:style    {:color colors/white}}
           :background-color    {:default  colors/primary-60
                                 :pressed  colors/primary-50
                                 :disabled colors/primary-60}}})

(defn record-button [on-long-press on-press-release]
  [rn/touchable-opacity {:style {:width  56
                                 :height 56
                                 :background-color colors/primary-50
                                 :border-radius 28}}
   [icons/icon :main-icons/audio20 {:container-style {:margin-bottom 2}
                                    :color           colors/white
                                    :size            20}]])

(defn send-button []
  [rn/view {:style {:width  32
                    :height 32
                    :background-color colors/primary-50
                    :border-radius 16}}
   [icons/icon :main-icons/arrow-up20 {:color           colors/white
                                       :size            20}]])

(defn lock-button []
  [rn/view {:style {:width  32
                    :height 32
                    :background-color colors/neutral-80-opa-5
                    :border-radius 16}}
   [icons/icon :main-icons/unlocked20 {:color           colors/white
                                       :size            20}]])

(defn delete-button []
  [rn/view {:style {:width  32
                    :height 32
                    :background-color colors/danger-50
                    :border-radius 16}}
   [icons/icon :main-icons/delete20 {:color           colors/white
                                     :size            20}]])

(defn input-view []
  [rn/view
   [delete-button]
   [lock-button]
   [send-button]
   [record-button #(println "Long press start") #(println "Long press release")]])