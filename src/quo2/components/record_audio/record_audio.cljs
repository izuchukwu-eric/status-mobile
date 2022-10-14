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

(defn signal-circle-apply-animations [scale opacity color]
  (reanimated/apply-animations-to-style
   {:transform [{:scale scale}]
    :opacity opacity}
   {:width  56
    :height 56
    :border-width 1
    :border-color color
    :border-radius 28
    :position :absolute
    :justify-content :center
    :align-items :center}))

(def scale-to 2)
(def opacity-from-lock 1)
(def opacity-from-default 0.5)
(def signal-anim-duration 2200)

(def recording-state (reagent/atom nil))

(defn record-button [frame on-record-start on-press-release]
  [:f>
   (let [recording? (reagent/atom false)]
     (fn []
       (let [scale-1 (reanimated/use-shared-value 1)
             opacity-1 (reanimated/use-shared-value (if (= @recording-state :pause) opacity-from-lock opacity-from-default))
             scale-2 (reanimated/use-shared-value 1)
             opacity-2 (reanimated/use-shared-value (if (= @recording-state :pause) opacity-from-lock opacity-from-default))
             scale-3 (reanimated/use-shared-value 1)
             opacity-3 (reanimated/use-shared-value (if (= @recording-state :pause) opacity-from-lock opacity-from-default))
             scale-4 (reanimated/use-shared-value 1)
             opacity-4 (reanimated/use-shared-value (if (= @recording-state :pause) opacity-from-lock opacity-from-default))
             scale-5 (reanimated/use-shared-value 1)
             opacity-5 (reanimated/use-shared-value (if (= @recording-state :pause) opacity-from-lock opacity-from-default))
          ;;  circle-animation (reanimated/use-animated-style )
             scale-animation-1 (reagent/atom nil)
             opacity-animation-1 (reagent/atom nil)
             scale-animation-2 (reagent/atom nil)
             opacity-animation-2 (reagent/atom nil)
             scale-animation-3 (reagent/atom nil)
             opacity-animation-3 (reagent/atom nil)
             scale-animation-4 (reagent/atom nil)
             opacity-animation-4 (reagent/atom nil)
             scale-animation-5 (reagent/atom nil)
             opacity-animation-5 (reagent/atom nil)
             hit-slop-area (if @recording? 100 0)
             button-color (if (= @recording-state :pause) colors/neutral-20 colors/primary-50)
             icon-color (if (= @recording-state :pause) colors/black colors/white)]
         [rn/view {:style {:position :absolute
                           :bottom 0
                           :right 0
                          ;;  :background-color :red
                           :width 96
                           :height 96
                           :align-items :center
                           :justify-content :center}
                   :pointer-events :none
                   :on-layout (fn [^js e]
                                (reset! frame (js->clj (-> e .-nativeEvent.layout) :keywordize-keys true)))}
          [:<>
           [reanimated/view {:style (signal-circle-apply-animations scale-1 opacity-1 button-color)}]
           [reanimated/view {:style (signal-circle-apply-animations scale-2 opacity-2 button-color)}]
           [reanimated/view {:style (signal-circle-apply-animations scale-3 opacity-3 button-color)}]
           [reanimated/view {:style (signal-circle-apply-animations scale-4 opacity-4 button-color)}]
           [reanimated/view {:style (signal-circle-apply-animations scale-5 opacity-5 button-color)}]]
          [rn/view {:style {:width  56
                                         :height 56
                                         :background-color button-color
                                         :border-radius 28
                                         :justify-content :center
                                         :align-items :center}
                                 :active-opacity 1
                                 :hit-slop {:top    hit-slop-area
                                            :left   hit-slop-area
                                            :right  hit-slop-area
                                            :bottom hit-slop-area}
                                 :on-press-outt #(do
                                                  (println "finish animation" @scale-animation-1 @recording?)
                                                  (reset! recording? false)
                                                  (reanimated/cancel-animation scale-1)
                                                  (reanimated/set-shared-value scale-1 1)
                                                  (reanimated/cancel-animation opacity-1)
                                                  (reanimated/set-shared-value opacity-1 (if (= @recording-state :pause) opacity-from-lock opacity-from-default))

                                                  (reanimated/cancel-animation scale-2)
                                                  (reanimated/set-shared-value scale-2 1)
                                                  (reanimated/cancel-animation opacity-2)
                                                  (reanimated/set-shared-value opacity-2 (if (= @recording-state :pause) opacity-from-lock opacity-from-default))

                                                  (reanimated/cancel-animation scale-3)
                                                  (reanimated/set-shared-value scale-3 1)
                                                  (reanimated/cancel-animation opacity-3)
                                                  (reanimated/set-shared-value opacity-3 (if (= @recording-state :pause) opacity-from-lock opacity-from-default))

                                                  (reanimated/cancel-animation scale-4)
                                                  (reanimated/set-shared-value scale-4 1)
                                                  (reanimated/cancel-animation opacity-4)
                                                  (reanimated/set-shared-value opacity-4 (if (= @recording-state :pause) opacity-from-lock opacity-from-default))

                                                  (reanimated/cancel-animation scale-5)
                                                  (reanimated/set-shared-value scale-5 1)
                                                  (reanimated/cancel-animation opacity-5)
                                                  (reanimated/set-shared-value opacity-5 (if (= @recording-state :pause) opacity-from-lock opacity-from-default)))
                                 :on-press-inn #(do
                                                 (println "start animation", @recording?)
                                                 (reset! recording? true)
                                                 (reset! scale-animation-1 (reanimated/animate-shared-value-repeat
                                                                            scale-1
                                                                            scale-to
                                                                            signal-anim-duration
                                                                            :linear
                                                                            -1))
                                                 (reset! opacity-animation-1 (reanimated/animate-shared-value-repeat
                                                                              opacity-1
                                                                              0
                                                                              signal-anim-duration
                                                                              :linear
                                                                              -1))
                                                 (reset! scale-animation-2 (reanimated/animate-shared-value-with-delay-repeat
                                                                            scale-2
                                                                            scale-to
                                                                            signal-anim-duration
                                                                            :linear
                                                                            400
                                                                            -1))
                                                 (reset! opacity-animation-2 (reanimated/animate-shared-value-with-delay-repeat
                                                                              opacity-2
                                                                              0
                                                                              signal-anim-duration
                                                                              :linear
                                                                              400
                                                                              -1))
                                                 (reset! scale-animation-3 (reanimated/animate-shared-value-with-delay-repeat
                                                                            scale-3
                                                                            scale-to
                                                                            signal-anim-duration
                                                                            :linear
                                                                            800
                                                                            -1))
                                                 (reset! opacity-animation-3 (reanimated/animate-shared-value-with-delay-repeat
                                                                              opacity-3
                                                                              0
                                                                              signal-anim-duration
                                                                              :linear
                                                                              800
                                                                              -1))
                                                 (reset! scale-animation-4 (reanimated/animate-shared-value-with-delay-repeat
                                                                            scale-4
                                                                            scale-to
                                                                            signal-anim-duration
                                                                            :linear
                                                                            1200
                                                                            -1))
                                                 (reset! opacity-animation-4 (reanimated/animate-shared-value-with-delay-repeat
                                                                              opacity-4
                                                                              0
                                                                              signal-anim-duration
                                                                              :linear
                                                                              1200
                                                                              -1))
                                                 (reset! scale-animation-5 (reanimated/animate-shared-value-with-delay-repeat
                                                                            scale-5
                                                                            scale-to
                                                                            signal-anim-duration
                                                                            :linear
                                                                            1600
                                                                            -1))
                                                 (reset! opacity-animation-5 (reanimated/animate-shared-value-with-delay-repeat
                                                                              opacity-5
                                                                              0
                                                                              signal-anim-duration
                                                                              :linear
                                                                              1600
                                                                              -1)))}
           [icons/icon :main-icons2/audio {:color icon-color}]]])))])

(defn send-button [frame]
  [rn/view {:style {:width  32
                    :height 32
                    :justify-content :center
                    :align-items :center
                    :background-color colors/primary-50
                    :border-radius 16
                    :position :absolute
                    :top 0
                    :right 32}
            :pointer-events :none
            ::on-layout (fn [^js e]
                          (reset! frame (js->clj (-> e .-nativeEvent.layout) :keywordize-keys true)))}
   [icons/icon :main-icons2/arrow-up {:color           colors/white
                                      :size            20}]])

(defn lock-button [frame]
  [rn/view {:style {:width  32
                    :height 32
                    :justify-content :center
                    :align-items :center
                    :background-color colors/neutral-80-opa-5
                    :border-radius 16
                    :position :absolute
                    :top 24
                    :left 24}
            :pointer-events :none
            :on-layout (fn [^js e]
                         (reset! frame (js->clj (-> e .-nativeEvent.layout) :keywordize-keys true)))}
   [icons/icon :main-icons2/unlocked {:color           colors/black
                                      :size            20}]])

(defn delete-button [frame]
  [rn/view {:style {:width  32
                    :height 32
                    :justify-content :center
                    :align-items :center
                    :background-color colors/danger-50
                    :border-radius 16
                    :position :absolute
                    :top 76
                    :left 0}
            :pointer-events :none
            :on-layout (fn [^js e]
                         (reset! frame (js->clj (-> e .-nativeEvent.layout) :keywordize-keys true)))}
   [icons/icon :main-icons2/delete-context {:color colors/white
                                            :size  20}]])

(defn touch-inside-layout? [{:keys [locationX locationY]} {:keys [width height x y]}]
  (let [max-x (+ x width)
        max-y (+ y height)]
    (and
     (and
      (>= locationX x)
      (<= locationX max-x))
     (and
      (>= locationY y)
      (<= locationY max-y)))))

(defn input-view []
  (let [delete-button-frame (reagent/atom nil)
        lock-button-frame (reagent/atom nil)
        send-button-frame (reagent/atom nil)
        record-button-frame (reagent/atom nil)
        recording? (reagent/atom false)]
   [rn/view {:style {:width 140 :height 140}
             :on-start-should-set-responder (fn [^js e]
                                              (println (js->clj (-> e .-nativeEvent.locationX) :keywordize-keys true))
                                              (println "DELETE BUTTON PRESSED" (touch-inside-layout?
                                                                                {:locationX (-> e .-nativeEvent.locationX) :locationY (-> e .-nativeEvent.locationY)}
                                                                                @delete-button-frame))
                                              (println "LOCK BUTTON PRESSED" (touch-inside-layout?
                                                                                {:locationX (-> e .-nativeEvent.locationX) :locationY (-> e .-nativeEvent.locationY)}
                                                                                @lock-button-frame))
                                              (println "SEND BUTTON PRESSED" (touch-inside-layout?
                                                                                {:locationX (-> e .-nativeEvent.locationX) :locationY (-> e .-nativeEvent.locationY)}
                                                                                @send-button-frame))
                                              (println "RECORD BUTTON PRESSED" (touch-inside-layout?
                                                                                {:locationX (-> e .-nativeEvent.locationX) :locationY (-> e .-nativeEvent.locationY)}
                                                                                @record-button-frame))
                                              (reset! recording? (touch-inside-layout?
                                                                  {:locationX (-> e .-nativeEvent.locationX) :locationY (-> e .-nativeEvent.locationY)}
                                                                  @record-button-frame))
                                              true)
             :on-responder-move (fn [^js e]
                                  (println "MOVED TO LOCK BUTTON" (touch-inside-layout?
                                                                  {:locationX (-> e .-nativeEvent.locationX) :locationY (-> e .-nativeEvent.locationY)}
                                                                  @lock-button-frame))
                                  (println "MOVED TO SEND BUTTON" (touch-inside-layout?
                                                                  {:locationX (-> e .-nativeEvent.locationX) :locationY (-> e .-nativeEvent.locationY)}
                                                                  @send-button-frame))
                                  (println "MOVED TO DELETE BUTTON" (touch-inside-layout?
                                                                    {:locationX (-> e .-nativeEvent.locationX) :locationY (-> e .-nativeEvent.locationY)}
                                                                    @delete-button-frame))
                                  (println (js->clj (-> e .-nativeEvent.locationX) :keywordize-keys true)))}
   [delete-button delete-button-frame]
   [lock-button lock-button-frame]
   [send-button send-button-frame]
   [record-button record-button-frame recording? #(println "Long press start") #(println "Long press release")]]))