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

(defn signal-circle [scale opacity]
  (let [circle-style (reanimated/apply-animations-to-style
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
    [reanimated/view {:style circle-style}]))

(defn signal-circle-apply-animations [scale opacity]
  (reanimated/apply-animations-to-style
   {:transform [{:scale scale}]
    :opacity opacity}
   {:width  56
    :height 56
    :border-width 1
    :border-color colors/primary-50
    :border-radius 28
    :position :absolute
    :top 0
    :left 0}))

(def scale-to 2)
(def opacity-from 0.5)
(def signal-anim-duration 1600)

(defn record-button [on-record-start on-press-release]
  [:f>
   (fn []
     (let [recording? (reagent/atom false)
           scale-1 (reanimated/use-shared-value 1)
           opacity-1 (reanimated/use-shared-value opacity-from)
           scale-2 (reanimated/use-shared-value 1)
           opacity-2 (reanimated/use-shared-value opacity-from)
           scale-3 (reanimated/use-shared-value 1)
           opacity-3 (reanimated/use-shared-value opacity-from)
          ;;  circle-animation (reanimated/use-animated-style )
           scale-animation-1 (reagent/atom nil)
           opacity-animation-1 (reagent/atom nil)
           scale-animation-2 (reagent/atom nil)
           opacity-animation-2 (reagent/atom nil)
           scale-animation-3 (reagent/atom nil)
           opacity-animation-3 (reagent/atom nil)]
       [rn/view {:style {:width  56
                         :height 56}}
        ;; (when @recording?
          [:<>
           [reanimated/view {:style (signal-circle-apply-animations scale-1 opacity-1)}]
           [reanimated/view {:style (signal-circle-apply-animations scale-2 opacity-2)}]
           [reanimated/view {:style (signal-circle-apply-animations scale-3 opacity-3)}]]
          ;; )
        [rn/touchable-opacity {:style {:position :absolute
                                       :top 0
                                       :left 0
                                       :width  56
                                       :height 56
                                       :background-color colors/primary-50
                                       :border-radius 28
                                       :justify-content :center
                                       :align-items :center}
                               :active-opacity 1
                               :on-press-out #(do
                                                (println "finish animation" @scale-animation-1 @recording?)
                                                (reset! recording? false)
                                                (reanimated/cancel-animation scale-1)
                                                (reanimated/set-shared-value scale-1 1)
                                                (reanimated/cancel-animation opacity-1)
                                                (reanimated/set-shared-value opacity-1 opacity-from)

                                                (reanimated/cancel-animation scale-2)
                                                (reanimated/set-shared-value scale-2 1)
                                                (reanimated/cancel-animation opacity-2)
                                                (reanimated/set-shared-value opacity-2 opacity-from)

                                                (reanimated/cancel-animation scale-3)
                                                (reanimated/set-shared-value scale-3 1)
                                                (reanimated/cancel-animation opacity-3)
                                                (reanimated/set-shared-value opacity-3 opacity-from)) 
                               :on-long-press #(do
                                                 (println "start animation", @recording?)
                                                 (reset! recording? true)
                                                 (reset! scale-animation-1 (reanimated/animate-shared-value-with-delay-repeat
                                                                            scale-1
                                                                            scale-to
                                                                            signal-anim-duration
                                                                            :linear
                                                                            0
                                                                            -1))
                                                 (reset! opacity-animation-1 (reanimated/animate-shared-value-with-delay-repeat
                                                                              opacity-1
                                                                              0
                                                                              signal-anim-duration
                                                                              :linear
                                                                              0
                                                                              -1))
                                                 (reset! scale-animation-2 (reanimated/animate-shared-value-with-delay-repeat
                                                                            scale-2
                                                                            scale-to
                                                                            signal-anim-duration
                                                                            :linear
                                                                            500
                                                                            -1))
                                                 (reset! opacity-animation-2 (reanimated/animate-shared-value-with-delay-repeat
                                                                              opacity-2
                                                                              0
                                                                              signal-anim-duration
                                                                              :linear
                                                                              500
                                                                              -1))
                                                 (reset! scale-animation-3 (reanimated/animate-shared-value-with-delay-repeat
                                                                            scale-3
                                                                            scale-to
                                                                            signal-anim-duration
                                                                            :linear
                                                                            1000
                                                                            -1))
                                                 (reset! opacity-animation-3 (reanimated/animate-shared-value-with-delay-repeat
                                                                              opacity-3
                                                                              0
                                                                              signal-anim-duration
                                                                              :linear
                                                                              1000
                                                                              -1)))}
         [icons/icon :main-icons/audio20 {:color           colors/white
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