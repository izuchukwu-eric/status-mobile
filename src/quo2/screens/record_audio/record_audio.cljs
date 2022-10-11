(ns quo2.screens.record-audio.record-audio
  (:require [quo.components.text :as text]
            [quo.previews.preview :as preview]
            [quo.react-native :as rn]
            [quo2.components.record-audio.record-audio :as record-audio]
            [quo2.foundations.colors :as colors]
            [reagent.core :as reagent]))

(def descriptor [])

(defn cool-preview []
  (let [items         ["Banana"
                       "Apple"
                       "COVID +18"
                       "Orange"
                       "Kryptonite"
                       "BMW"
                       "Meh"]
        state         (reagent/atom {:icon         :main-icons/placeholder
                                     :default-item "item1"
                                     :use-border? false
                                     :dd-color     (colors/custom-color :purple 50)
                                     :size         :big})
        selected-item (reagent/cursor state [:default-item])
        on-select     #(reset! selected-item %)]
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
