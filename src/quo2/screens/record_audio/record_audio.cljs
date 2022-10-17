(ns quo2.screens.record-audio.record-audio
  (:require [quo.previews.preview :as preview]
            [quo.react-native :as rn]
            [quo2.components.record-audio.record-audio :as record-audio]
            [quo2.foundations.colors :as colors]
            [reagent.core :as reagent]))

(def descriptor [])

(defn cool-preview []
  (let [state (reagent/atom {})]
    (fn []
      [rn/touchable-without-feedback {:on-press rn/dismiss-keyboard!}
       [rn/view {:padding-bottom 150}
        [preview/customizer state descriptor]
        [rn/view {:padding-vertical 60
                  :align-items      :center}
         [record-audio/input-view]]]])))

(defn preview-record-audio []
  [rn/view {:background-color (colors/theme-colors colors/white colors/neutral-90)
            :flex             1}
   [cool-preview]])
