(ns quo2.components.record-audio.record-audio
  (:require [quo2.components.markdown.text :as text]
            [quo.react :as react]
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


;; (defn apply-anim [dd-height val]
;;   (reanimated/use-shared-value 1))

;; (defn signal-circle [scale opacity]
;;   (let [circle-style (reanimated/apply-animations-to-style
;;                       {:transform [{:scale scale}]
;;                        :opacity opacity}
;;                       {:width  56
;;                        :height 56
;;                        :border-width 2
;;                        :border-color colors/primary-50
;;                        :border-radius 28
;;                        :position :absolute
;;                        :top 0
;;                        :left 0})
;;         scale-animation (reagent/atom nil)
;;         opacity-animation (reagent/atom nil)]
;;     [reanimated/view {:style circle-style}]))

(defn signal-circle-apply-animations [scale opacity color]
  (reanimated/apply-animations-to-style
   {:transform [{:scale scale}]
    :opacity   opacity}
   {:width           56
    :height          56
    :border-width    1
    :border-color    color
    :border-radius   28
    :position        :absolute
    :justify-content :center
    :align-items     :center
    :z-index         0}))

(def scale-to 2)
(def opacity-from-lock 1)
(def opacity-from-default 0.5)
(def signal-anim-duration 2200)

(def recording-state (reagent/atom nil))

(defn record-button [frame recording? ready-to-send? ready-to-delete? lock-recording?]
  [:f>
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
           translate-y-animation (reagent/atom nil)
           translate-x-animation (reagent/atom nil)
           translate-y (reanimated/use-shared-value 0)
           translate-x (reanimated/use-shared-value 0)
           button-color (if (= @recording-state :pause) colors/neutral-20 colors/primary-50)
           icon-color (if (= @recording-state :pause) colors/black colors/white)
           start-animation #(do
                              ;;  (reset! recording? true)
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
                                                           -1)))
           stop-animation #(do
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
           start-y-animation #(reset! translate-y-animation (reanimated/animate-shared-value-with-timing
                                                             translate-y
                                                             -72
                                                             1000
                                                             :easing1))
           reset-y-animation #(reset! translate-y-animation (reanimated/animate-shared-value-with-timing
                                                             translate-y
                                                             0
                                                             200
                                                             :easing1))
           start-x-animation #(reset! translate-x-animation (reanimated/animate-shared-value-with-timing
                                                             translate-x
                                                             -72
                                                             1000
                                                             :easing1))
           reset-x-animation #(reset! translate-x-animation (reanimated/animate-shared-value-with-timing
                                                             translate-x
                                                             0
                                                             200
                                                             :easing1))
           start-x-y-animation #(do
                                  (reset! translate-y-animation (reanimated/animate-shared-value-with-timing
                                                                 translate-y
                                                                 -52
                                                                 1000
                                                                 :easing1))
                                  (reset! translate-x-animation (reanimated/animate-shared-value-with-timing
                                                                 translate-x
                                                                 -52
                                                                 1000
                                                                 :easing1)))
           reset-x-y-animation #(do
                                  (reset! translate-y-animation (reanimated/animate-shared-value-with-timing
                                                                 translate-y
                                                                 0
                                                                 200
                                                                 :easing1))
                                  (reset! translate-x-animation (reanimated/animate-shared-value-with-timing
                                                                 translate-x
                                                                 0
                                                                 200
                                                                 :easing1)))]
       (quo.react/effect! #(if @recording? (start-animation) (do (stop-animation) (reset-y-animation))) [@recording?])
       (quo.react/effect! #(if @lock-recording? (start-x-y-animation) (reset-x-y-animation)) [@lock-recording?])
       (quo.react/effect! #(if @ready-to-send? (start-y-animation) (reset-y-animation)) [@ready-to-send?])
       (quo.react/effect! #(if @ready-to-delete? (start-x-animation) (reset-x-animation)) [@ready-to-delete?])
       [reanimated/view {:style (reanimated/apply-animations-to-style
                                 {:transform   [{:translateY translate-y}
                                                {:translateX translate-x}]}
                                 {:position        :absolute
                                  :bottom          0
                                  :right           0
                                  :width           96
                                  :height          96
                                  :align-items     :center
                                  :justify-content :center})
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
                  :active-opacity 1}
         [icons/icon :main-icons2/audio {:color icon-color}]]]))])

(defn send-button [frame]
  [rn/view {:style {:width            32
                    :height           32
                    :justify-content  :center
                    :align-items      :center
                    :background-color colors/primary-50
                    :border-radius    16
                    :position         :absolute
                    :top              0
                    :right            32}
            :pointer-events :none
            ::on-layout (fn [^js e]
                          (reset! frame (js->clj (-> e .-nativeEvent.layout) :keywordize-keys true)))}
   [icons/icon :main-icons2/arrow-up {:color           colors/white
                                      :size            20}]])

(defn lock-button [frame]
  [rn/view {:style {:width            32
                    :height           32
                    :justify-content  :center
                    :align-items      :center
                    :background-color colors/neutral-80-opa-5
                    :border-radius    16
                    :position         :absolute
                    :top              24
                    :left             24}
            :pointer-events :none
            :on-layout (fn [^js e]
                         (reset! frame (js->clj (-> e .-nativeEvent.layout) :keywordize-keys true)))}
   [icons/icon :main-icons2/unlocked {:color           colors/black
                                      :size            20}]])

(defn delete-button [frame]
  [rn/view {:style {:width            32
                    :height           32
                    :justify-content  :center
                    :align-items      :center
                    :background-color colors/danger-50
                    :border-radius    16
                    :position         :absolute
                    :top              76
                    :left             0}
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
        recording? (reagent/atom false)
        locked? (reagent/atom false)
        ready-to-send? (reagent/atom false)
        ready-to-delete? (reagent/atom false)]
    [rn/view {:style {:width 140 :height 140}
              :on-start-should-set-responder (fn [^js e]
                                               (let [pressed-record-button? (touch-inside-layout?
                                                                             {:locationX (-> e .-nativeEvent.locationX)
                                                                              :locationY (-> e .-nativeEvent.locationY)}
                                                                             @record-button-frame)
                                                     pressed-send-button? (touch-inside-layout?
                                                                           {:locationX (-> e .-nativeEvent.locationX)
                                                                            :locationY (-> e .-nativeEvent.locationY)}
                                                                           @send-button-frame)
                                                     pressed-delete-button? (touch-inside-layout?
                                                                             {:locationX (-> e .-nativeEvent.locationX)
                                                                              :locationY (-> e .-nativeEvent.locationY)}
                                                                             @delete-button-frame)
                                                     pressed-lock-button? (touch-inside-layout?
                                                                           {:locationX (-> e .-nativeEvent.locationX)
                                                                            :locationY (-> e .-nativeEvent.locationY)}
                                                                           @lock-button-frame)]
                                                 (when pressed-record-button? (println "PRESSED RECORD BUTTON"))
                                                 (when pressed-send-button? (println "PRESSED SEND BUTTON"))
                                                 (when pressed-delete-button? (println "PRESSED DELETE BUTTON"))
                                                 (when pressed-lock-button? (println "PRESSED LOCK BUTTON"))
                                                 (reset! recording? pressed-record-button?)
                                                 true))
              :on-responder-move (fn [^js e]
                                   (let [moved-to-send-button? (touch-inside-layout?
                                                                {:locationX (-> e .-nativeEvent.locationX)
                                                                 :locationY (-> e .-nativeEvent.locationY)}
                                                                @send-button-frame)
                                         moved-to-delete-button? (touch-inside-layout?
                                                                  {:locationX (-> e .-nativeEvent.locationX)
                                                                   :locationY (-> e .-nativeEvent.locationY)}
                                                                  @delete-button-frame)
                                         moved-to-lock-button? (touch-inside-layout?
                                                                {:locationX (-> e .-nativeEvent.locationX)
                                                                 :locationY (-> e .-nativeEvent.locationY)}
                                                                @lock-button-frame)]
                                     (when moved-to-send-button? (println "MOVED TO SEND BUTTON"))
                                     (when moved-to-delete-button? (println "MOVED TO DELETE BUTTON"))
                                     (when moved-to-lock-button? (println "MOVED TO LOCK BUTTON"))
                                     (reset! ready-to-send? moved-to-send-button?)
                                     (reset! ready-to-delete? moved-to-delete-button?)
                                     (reset! locked? moved-to-lock-button?)))
              :on-responder-release (fn [^js e]
                                      (reset! recording? false)
                                      (reset! ready-to-send? false)
                                      (reset! ready-to-delete? false)
                                      (reset! locked? false)
                                      (println (js->clj (-> e .-nativeEvent.locationX) :keywordize-keys true)))}

     [delete-button delete-button-frame]
     [lock-button lock-button-frame]
     [send-button send-button-frame]
     [record-button record-button-frame recording? ready-to-send? ready-to-delete? locked?]]))