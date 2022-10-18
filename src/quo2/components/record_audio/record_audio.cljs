(ns quo2.components.record-audio.record-audio
  (:require [quo.react :as react]
            [quo.react-native :as rn]
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

(def scale-to 1.8)
(def opacity-from-lock 1)
(def opacity-from-default 0.5)
(def signal-anim-duration 2200)

(defn record-button [frame recording? ready-to-send? ready-to-delete? ready-to-lock? locked?]
  [:f>
   (fn []
     (let [scale-1 (reanimated/use-shared-value 1)
           opacity-1 (reanimated/use-shared-value (if @ready-to-lock? opacity-from-lock opacity-from-default))
           scale-2 (reanimated/use-shared-value 1)
           opacity-2 (reanimated/use-shared-value (if @ready-to-lock? opacity-from-lock opacity-from-default))
           scale-3 (reanimated/use-shared-value 1)
           opacity-3 (reanimated/use-shared-value (if @ready-to-lock? opacity-from-lock opacity-from-default))
           scale-4 (reanimated/use-shared-value 1)
           opacity-4 (reanimated/use-shared-value (if @ready-to-lock? opacity-from-lock opacity-from-default))
           scale-5 (reanimated/use-shared-value 1)
           opacity-5 (reanimated/use-shared-value (if @ready-to-lock? opacity-from-lock opacity-from-default))
          ;;  scale-animation-1 (reagent/atom nil)
          ;;  opacity-animation-1 (reagent/atom nil)
          ;;  scale-animation-2 (reagent/atom nil)
          ;;  opacity-animation-2 (reagent/atom nil)
          ;;  scale-animation-3 (reagent/atom nil)
          ;;  opacity-animation-3 (reagent/atom nil)
          ;;  scale-animation-4 (reagent/atom nil)
          ;;  opacity-animation-4 (reagent/atom nil)
          ;;  scale-animation-5 (reagent/atom nil)
          ;;  opacity-animation-5 (reagent/atom nil)
          ;;  translate-y-animation (reagent/atom nil)
          ;;  translate-x-animation (reagent/atom nil)
           translate-y (reanimated/use-shared-value 0)
           translate-x (reanimated/use-shared-value 0)
           rings-color (cond
                         @ready-to-lock? colors/neutral-30
                         @ready-to-delete? colors/danger-50
                         :else colors/primary-50)
           button-color colors/primary-50
           icon-color (if @ready-to-lock? colors/black colors/white)
           icon-opacity (reanimated/use-shared-value 1)
           red-overlay-opacity (reanimated/use-shared-value 0)
           gray-overlay-opacity (reanimated/use-shared-value 0)
           start-animation (fn []
                             (println "SIGNAL ANIM")
                             (reanimated/animate-shared-value-repeat scale-1 scale-to signal-anim-duration :linear -1)
                             (reanimated/animate-shared-value-repeat opacity-1 0 signal-anim-duration :linear -1)
                             (reanimated/animate-shared-value-with-delay-repeat scale-2 scale-to signal-anim-duration :linear 400 -1)
                             (reanimated/animate-shared-value-with-delay-repeat opacity-2 0 signal-anim-duration :linear 400 -1)
                             (reanimated/animate-shared-value-with-delay-repeat scale-3 scale-to signal-anim-duration :linear 800 -1)
                             (reanimated/animate-shared-value-with-delay-repeat opacity-3 0 signal-anim-duration :linear 800 -1)
                             (reanimated/animate-shared-value-with-delay-repeat scale-4 scale-to signal-anim-duration :linear 1200 -1)
                             (reanimated/animate-shared-value-with-delay-repeat opacity-4 0 signal-anim-duration :linear 1200 -1)
                             (reanimated/animate-shared-value-with-delay-repeat scale-5 scale-to signal-anim-duration :linear 1600 -1)
                             (reanimated/animate-shared-value-with-delay-repeat opacity-5 0 signal-anim-duration :linear 1600 -1))
           stop-animation (fn []
                            (println "CANCEL SIGN ANIM")
                            (reanimated/cancel-animation scale-1)
                            (reanimated/set-shared-value scale-1 1)
                            (reanimated/cancel-animation opacity-1)
                            (reanimated/set-shared-value opacity-1 (if @ready-to-lock? opacity-from-lock opacity-from-default))
                            (reanimated/cancel-animation scale-2)
                            (reanimated/set-shared-value scale-2 1)
                            (reanimated/cancel-animation opacity-2)
                            (reanimated/set-shared-value opacity-2 (if @ready-to-lock? opacity-from-lock opacity-from-default))
                            (reanimated/cancel-animation scale-3)
                            (reanimated/set-shared-value scale-3 1)
                            (reanimated/cancel-animation opacity-3)
                            (reanimated/set-shared-value opacity-3 (if @ready-to-lock? opacity-from-lock opacity-from-default))
                            (reanimated/cancel-animation scale-4)
                            (reanimated/set-shared-value scale-4 1)
                            (reanimated/cancel-animation opacity-4)
                            (reanimated/set-shared-value opacity-4 (if @ready-to-lock? opacity-from-lock opacity-from-default))
                            (reanimated/cancel-animation scale-5)
                            (reanimated/set-shared-value scale-5 1)
                            (reanimated/cancel-animation opacity-5)
                            (reanimated/set-shared-value opacity-5 (if @ready-to-lock? opacity-from-lock opacity-from-default)))
           start-y-animation (fn []
                               (reanimated/animate-shared-value-with-timing translate-y -64 1000 :easing1)
                               (reanimated/animate-shared-value-with-delay icon-opacity 0 200 :linear 400))
           reset-y-animation (fn []
                               (reanimated/animate-shared-value-with-timing translate-y 0 300 :easing1)
                               (reanimated/animate-shared-value-with-timing icon-opacity 1 500 :linear))
           start-x-animation (fn []
                               (reanimated/animate-shared-value-with-timing translate-x -64 1500 :easing1)
                               (reanimated/animate-shared-value-with-delay icon-opacity 0 200 :linear 700)
                               (reanimated/animate-shared-value-with-timing red-overlay-opacity 1 200 :linear))
           reset-x-animation (fn []
                               (reanimated/animate-shared-value-with-timing translate-x 0 300 :easing1)
                               (reanimated/animate-shared-value-with-timing icon-opacity 1 500 :linear)
                               (reanimated/animate-shared-value-with-timing red-overlay-opacity 0 100 :linear))
           start-x-y-animation (fn []
                                 (reanimated/animate-shared-value-with-timing translate-y -44 1000 :easing1)
                                 (reanimated/animate-shared-value-with-timing translate-x -44 1000 :easing1)
                                 (reanimated/animate-shared-value-with-delay icon-opacity 0 200 :linear 300)
                                 (reanimated/animate-shared-value-with-timing gray-overlay-opacity 1 200 :linear))
           reset-x-y-animation (fn []
                                 (reanimated/animate-shared-value-with-timing translate-y 0 300 :easing1)
                                 (reanimated/animate-shared-value-with-timing translate-x 0 300 :easing1)
                                 (reanimated/animate-shared-value-with-timing icon-opacity 1 500 :linear)
                                 (reanimated/animate-shared-value-with-timing gray-overlay-opacity 0 800 :linear))]
       (quo.react/effect! #(do (println "FDSFDSFDSFDSDS") (if @recording? (start-animation) (when-not @ready-to-lock? (stop-animation)))) [@recording?])
       (quo.react/effect! #(if @ready-to-lock? (start-x-y-animation) (reset-x-y-animation)) [@ready-to-lock?])
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
                                  :justify-content :center
                                  :z-index         0})
                         :pointer-events :none
                         :on-layout (fn [^js e]
                                      (reset! frame (js->clj (-> e .-nativeEvent.layout) :keywordize-keys true)))}
        [:<>
         [reanimated/view {:style (signal-circle-apply-animations scale-1 opacity-1 rings-color)}]
         [reanimated/view {:style (signal-circle-apply-animations scale-2 opacity-2 rings-color)}]
         [reanimated/view {:style (signal-circle-apply-animations scale-3 opacity-3 rings-color)}]
         [reanimated/view {:style (signal-circle-apply-animations scale-4 opacity-4 rings-color)}]
         [reanimated/view {:style (signal-circle-apply-animations scale-5 opacity-5 rings-color)}]]
        [rn/view {:style {:width  56
                          :height 56
                          :border-radius 28
                          :justify-content :center
                          :align-items :center
                          :background-color button-color
                          :overflow         :hidden}}
         [reanimated/view {:style (reanimated/apply-animations-to-style
                                   {:opacity red-overlay-opacity}
                                   {:position :absolute
                                    :top 0
                                    :left 0
                                    :right 0
                                    :bottom 0
                                    :background-color colors/danger-50})}]
         [reanimated/view {:style (reanimated/apply-animations-to-style
                                   {:opacity gray-overlay-opacity}
                                   {:position :absolute
                                    :top 0
                                    :left 0
                                    :right 0
                                    :bottom 0
                                    :background-color (colors/theme-colors colors/white colors/neutral-90)})}
          [rn/view {:style {:position :absolute :width "100%" :height "100%" :background-color colors/neutral-80-opa-5}}]]
         [reanimated/view {:style (reanimated/apply-animations-to-style {:opacity icon-opacity} {})}
          (if @locked?
            [rn/view {:style {:width 13 :height 13 :border-radius 4 :background-color colors/white}}]
            [icons/icon :main-icons2/audio {:color icon-color}])]]]))])

(defn send-button [frame ready-to-send?]
  [:f>
   (fn []
     (let [translate-y (reanimated/use-shared-value 0)
           start-y-animation #(do
                                (reanimated/animate-shared-value-with-delay
                                 translate-y
                                 12
                                 200
                                 :linear
                                 400))
           reset-y-animation #(do
                                (reanimated/animate-shared-value-with-timing
                                 translate-y
                                 0
                                 200
                                 :linear))]
       (quo.react/effect! #(if @ready-to-send? (start-y-animation) (reset-y-animation)) [@ready-to-send?])
       [reanimated/view {:style (reanimated/apply-animations-to-style
                                 {:transform   [{:translateY translate-y}]}
                                 {:justify-content  :center
                                  :align-items      :center
                                  :background-color colors/primary-50
                                  :width            32
                                  :height           32
                                  :border-radius    16
                                  :position         :absolute
                                  :top              0
                                  :right            32
                                  :z-index 10})
                         :pointer-events :none
                         ::on-layout (fn [^js e]
                                       (reset! frame (js->clj (-> e .-nativeEvent.layout) :keywordize-keys true)))}
        [icons/icon :main-icons2/arrow-up {:color           colors/white
                                           :size            20
                                           :container-style {:z-index 10}}]]))])

(defn lock-button [frame ready-to-lock? locked?]
  [:f>
   (fn []
     (let [translate-x-y (reanimated/use-shared-value 0)
           opacity (reanimated/use-shared-value 1)
           start-x-y-animation #(do
                                  (reanimated/animate-shared-value-with-delay
                                   translate-x-y
                                   8
                                   200
                                   :linear
                                   400))
           reset-x-y-animation #(reanimated/animate-shared-value-with-timing
                                 translate-x-y
                                 0
                                 100
                                 :linear)
           fade-in-animation #(reanimated/animate-shared-value-with-timing
                               opacity
                               1
                               100
                               :linear)
           fade-out-animation #(reanimated/animate-shared-value-with-timing
                                opacity
                                0
                                100
                                :linear)]
       (quo.react/effect! #(if @ready-to-lock? (start-x-y-animation) (when-not @locked? (reset-x-y-animation))) [@ready-to-lock?])
       (quo.react/effect! #(if @locked? (fade-out-animation) (do (fade-in-animation) (reset-x-y-animation))) [@locked?])
       [reanimated/view {:style (reanimated/apply-animations-to-style
                                 {:transform   [{:translateX translate-x-y}
                                                {:translateY translate-x-y}]
                                  :opacity      opacity}
                                 {:width            32
                                  :height           32
                                  :justify-content  :center
                                  :align-items      :center
                                  :background-color (colors/theme-colors colors/white colors/neutral-90)
                                  :border-radius    16
                                  :position         :absolute
                                  :top              24
                                  :left             24
                                  :overflow         :hidden
                                  :z-index          12})
                         :pointer-events :none
                         :on-layout (fn [^js e]
                                      (reset! frame (js->clj (-> e .-nativeEvent.layout) :keywordize-keys true)))}
        ;; Background color with opacity gets broken when merging with the record button, so we put it in a separate view
        [rn/view {:style {:position :absolute :width "100%" :height "100%" :background-color colors/neutral-80-opa-5}}]
        [icons/icon :main-icons2/unlocked {:color           colors/black
                                           :size            20}]]))])

(defn delete-button [frame ready-to-delete?]
  [:f>
   (fn []
     (let [translate-x (reanimated/use-shared-value 0)
           connector-opacity (reanimated/use-shared-value 0)
           width (reanimated/use-shared-value 24)
           height (reanimated/use-shared-value 12)
           border-radius (reanimated/use-shared-value 8)
           border-radius-2 (reanimated/use-shared-value 8)
           start-x-animation #((reanimated/animate-shared-value-with-delay translate-x 12 300 :linear 800)
                               (reanimated/animate-shared-value-with-delay connector-opacity 1 0 :easing1 480)
                               (reanimated/animate-shared-value-with-delay width 56 500 :easing1 480)
                               (reanimated/animate-shared-value-with-delay height 56 500 :easing1 480)
                               (reanimated/animate-shared-value-with-delay border-radius 28 500 :easing1 480)
                               (reanimated/animate-shared-value-with-delay border-radius-2 28 500 :easing1 480))
           reset-x-animation #(do
                                (reanimated/animate-shared-value-with-timing translate-x 0 100 :linear)
                                (reanimated/set-shared-value connector-opacity 0)
                                (reanimated/set-shared-value width 24)
                                (reanimated/set-shared-value height 12)
                                (reanimated/set-shared-value border-radius 8)
                                (reanimated/set-shared-value border-radius-2 16))]
       (quo.react/effect! #(do (reset-x-animation) (when @ready-to-delete? (start-x-animation))) [@ready-to-delete?])
       [:<>
        [rn/view {:style {:justify-content :center
                          :align-items :center
                          :position         :absolute
                          :width 56
                          :height 56
                          :bottom 20
                          :left 0}}
         [reanimated/view {:style (reanimated/apply-animations-to-style
                                   {:opacity                    connector-opacity
                                    :width                      width
                                    :height                     height
                                    :border-bottom-left-radius  border-radius
                                    :border-top-left-radius     border-radius
                                    :border-top-right-radius    border-radius-2
                                    :border-bottom-right-radius border-radius-2}
                                   {:justify-content  :center
                                    :align-items      :center
                                    :align-self :center
                                    :background-color colors/danger-50
                                    :z-index          0})}]]
        [reanimated/view {:style (reanimated/apply-animations-to-style
                                  {:transform        [{:translateX translate-x}]}
                                  {:width            32
                                   :height           32
                                   :justify-content  :center
                                   :align-items      :center
                                   :background-color colors/danger-50
                                   :border-radius    16
                                   :position         :absolute
                                   :top              76
                                   :left             0
                                   :z-index          11})
                          :pointer-events :none
                          :on-layout (fn [^js e]
                                       (reset! frame (js->clj (-> e .-nativeEvent.layout) :keywordize-keys true)))}
         [icons/icon :main-icons2/delete-context {:color colors/white
                                                  :size  20}]]]))])

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

(defn input-view [on-send]
  (let [delete-button-frame (reagent/atom nil)
        lock-button-frame (reagent/atom nil)
        send-button-frame (reagent/atom nil)
        record-button-frame (reagent/atom nil)
        recording? (reagent/atom false)
        locked? (reagent/atom false)
        ready-to-send? (reagent/atom false)
        ready-to-lock? (reagent/atom false)
        ready-to-delete? (reagent/atom false)]
    [rn/view {:style {:width 140 :height 140}
              :on-start-should-set-responder (fn [^js e]
                                               (let [pressed-record-button? (touch-inside-layout?
                                                                             {:locationX (-> e .-nativeEvent.locationX)
                                                                              :locationY (-> e .-nativeEvent.locationY)}
                                                                             @record-button-frame)]
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
                                     (reset! ready-to-send? moved-to-send-button?)
                                     (reset! ready-to-delete? moved-to-delete-button?)
                                     (reset! ready-to-lock? moved-to-lock-button?)))
              :on-responder-release (fn [^js e]
                                      (let [on-record-button? (touch-inside-layout?
                                                               {:locationX (-> e .-nativeEvent.locationX)
                                                                :locationY (-> e .-nativeEvent.locationY)}
                                                               @record-button-frame)]
                                        (cond
                                          @ready-to-lock? (do
                                                            (reset! locked? true)
                                                            (reset! ready-to-lock? false))
                                          on-record-button? (do
                                                              (when on-send (on-send))
                                                              (reset! locked? false)
                                                              (reset! recording? false)
                                                              (reset! ready-to-lock? false))
                                          :else (do (reset! recording? false)
                                                    (reset! locked? false)
                                                    (reset! ready-to-send? false)
                                                    (reset! ready-to-delete? false)
                                                    (reset! ready-to-lock? false)))))}
     [delete-button delete-button-frame ready-to-delete?]
     [lock-button lock-button-frame ready-to-lock? locked?]
     [send-button send-button-frame ready-to-send?]
     [record-button record-button-frame recording? ready-to-send? ready-to-delete? ready-to-lock? locked?]]))