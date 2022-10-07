(ns quo2.components.record-audio.record-audio
  (:require [quo2.components.markdown.text :as text]
            [quo.react-native :as rn]
            [quo.theme :as theme]
            [quo2.foundations.colors :as colors]
            [quo2.components.icon :as icons]
            [quo2.reanimated :as reanimated]
            [reagent.core :as reagent]))

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


(defn apply-anim [dd-height val]
  (reanimated/use-shared-value 1))

(defn record-button [on-record-start on-press-release]
  [:f>
   (fn []
     (let [recording? (reagent/atom false)
           scale (reanimated/use-shared-value 1)
           opacity (reanimated/use-shared-value 1)
          ;;  circle-animation (reanimated/use-animated-style )
           circle-style (reanimated/apply-animations-to-style
                         {:transform [{:scale scale}]
                          :opacity opacity}
                         {:width  56
                          :height 56
                          :border-width 2
                          :border-color colors/primary-50
                          :border-radius 28
                          :position :absolute
                          :top 0
                          :left 0})
           scale-animation (reagent/atom nil)
           opacity-animation (reagent/atom nil)]
       [rn/view {:style {:width  56
                         :height 56}}
        ;; (when @recording?
          [:<>
           [reanimated/view {:style circle-style}]]
          ;; )
        [rn/touchable-opacity {:style {:position :absolute
                                       :top 0
                                       :left 0
                                       :width  56
                                       :height 56
                                       :background-color colors/primary-50
                                       :border-radius 28}
                               :active-opacity 0.8
                               :on-press-out #(do
                                                (println "finish animation" @scale-animation @recording?)
                                                (reset! recording? false)
                                                (reanimated/cancel-animation scale) ;;(.-value scale))
                                                (reanimated/set-shared-value scale 1)
                                                (reanimated/cancel-animation opacity)
                                                (reanimated/set-shared-value opacity 1)) ;;(.-value opacity)))
                               :on-long-press #(do
                                                 (println "start animation", @recording?)
                                                (reset! recording? true)
                                                 (reset! scale-animation (reanimated/animate-shared-value-with-delay-repeat
                                                                          scale
                                                                          1.5
                                                                          1000
                                                                          :linear
                                                                          0
                                                                          -1))
                                                 (reset! opacity-animation (reanimated/animate-shared-value-with-delay-repeat
                                                                            opacity
                                                                            0
                                                                            1000
                                                                            :linear
                                                                            0
                                                                            -1))
                                                ;;  (reanimated/animate-shared-value-with-delay-repeat
                                                ;;   scale-animation
                                                ;;   1.5
                                                ;;   1000
                                                ;;   :linear
                                                ;;   0
                                                ;;   -1)
                                                ;;  (reanimated/animate-shared-value-with-delay-repeat
                                                ;;   opacity-animation
                                                ;;   0
                                                ;;   1000
                                                ;;   :linear
                                                ;;   0
                                                ;;   -1)
                                                 )}
         [icons/icon :main-icons/audio20 {:container-style {:margin-bottom 2}
                                          :color           colors/white
                                          :size            20}]]]))])

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