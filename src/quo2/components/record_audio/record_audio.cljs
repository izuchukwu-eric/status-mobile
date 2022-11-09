(ns quo2.components.record-audio.record-audio
  (:require [quo.react :as react]
            [quo.react-native :as rn]
            [quo2.foundations.colors :as colors]
            [quo2.components.icon :as icons]
            [react-native.reanimated :as reanimated]
            [reagent.core :as reagent]
            [cljs-bean.core :as bean]))

(def recording? (reagent/atom false))
(def locked? (reagent/atom false))
(def ready-to-send? (reagent/atom false))
(def ready-to-lock? (reagent/atom false))
(def ready-to-delete? (reagent/atom false))
(def clear-timeout (atom nil))
(def record-button-at-initial-position? (atom true))
(def record-button-is-animating? (atom false))

(def scale-to-each 1.8)
(def scale-to-total 2.6)
(def scale-padding 0.16)
(def opacity-from-lock 1)
(def opacity-from-default 0.5)
(def signal-anim-duration 3900)
(def signal-anim-duration-2 1950)

(defn ring-scale [scale substract]
  (.ringScale ^js reanimated/worklet-factory
              scale
              substract))

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

(def animated-ring
  (reagent/adapt-react-class
   (react/memo
    (fn [props]
      (let [{:keys [scale opacity color]} (bean/bean props)]
        (reagent/as-element
         [reanimated/view {:style (signal-circle-apply-animations scale opacity color)}]))))))

(defn record-button [scale]
  [:f>
   (fn []
     (let [opacity-from (if @ready-to-lock? opacity-from-lock opacity-from-default)
           animations (map
                       (fn [index]
                         (let [ring-scale (ring-scale scale (* scale-padding index))]
                           {:scale ring-scale
                            :opacity (reanimated/interpolate ring-scale [1 scale-to-each] [opacity-from 0])}))
                       (range 0 5))
           rings-color (cond
                         @ready-to-lock? (colors/theme-colors colors/neutral-80-opa-5-opaque colors/neutral-80)
                         @ready-to-delete? colors/danger-50
                         :else colors/primary-50)
           translate-y (reanimated/use-shared-value 0)
           translate-x (reanimated/use-shared-value 0)
           button-color colors/primary-50
           icon-color (if (and (not (colors/dark?)) @ready-to-lock?) colors/black colors/white)
           icon-opacity (reanimated/use-shared-value 1)
           red-overlay-opacity (reanimated/use-shared-value 0)
           gray-overlay-opacity (reanimated/use-shared-value 0)
           start-animation (fn []
                             (reanimated/animate-shared-value-with-timing scale 2.6 signal-anim-duration :linear)
                             ;; TODO: Research if we can implement this with withSequence method from Reanimated 2
                             (reset! clear-timeout (js/setTimeout #(do (reanimated/set-shared-value scale scale-to-each)
                                                                       (reanimated/animate-shared-value-with-delay-repeat scale scale-to-total signal-anim-duration-2 :linear 0 -1))
                                                                  signal-anim-duration)))
           stop-animation #(do
                             (reanimated/cancel-animation scale)
                             (reanimated/set-shared-value scale 1)
                             (when @clear-timeout (js/clearTimeout @clear-timeout)))
           start-y-animation #(do
                                (reset! record-button-at-initial-position? false)
                                (reanimated/animate-shared-value-with-timing translate-y -64 1500 :easing1)
                                (reanimated/animate-shared-value-with-delay icon-opacity 0 200 :linear 700))
           reset-y-animation #(do
                                (reanimated/animate-shared-value-with-timing translate-y 0 300 :easing1)
                                (reanimated/animate-shared-value-with-timing icon-opacity 1 500 :linear)
                                (js/setTimeout (fn [] (reset! record-button-at-initial-position? true)) 500))
           start-x-animation #(do
                                (reset! record-button-at-initial-position? false)
                                (reanimated/animate-shared-value-with-timing translate-x -64 1500 :easing1)
                                (reanimated/animate-shared-value-with-delay icon-opacity 0 200 :linear 700)
                                (reanimated/animate-shared-value-with-timing red-overlay-opacity 1 200 :linear))
           reset-x-animation #(do
                                (reanimated/animate-shared-value-with-timing translate-x 0 300 :easing1)
                                (reanimated/animate-shared-value-with-timing icon-opacity 1 500 :linear)
                                (reanimated/animate-shared-value-with-timing red-overlay-opacity 0 100 :linear)
                                (js/setTimeout (fn [] (reset! record-button-at-initial-position? true)) 500))
           start-x-y-animation #(do
                                  (reset! record-button-at-initial-position? false)
                                  (reset! record-button-is-animating? true)
                                  (reanimated/animate-shared-value-with-timing translate-y -44 1200 :easing1)
                                  (reanimated/animate-shared-value-with-timing translate-x -44 1200 :easing1)
                                  (reanimated/animate-shared-value-with-delay icon-opacity 0 200 :linear 300)
                                  (reanimated/animate-shared-value-with-timing gray-overlay-opacity 1 200 :linear)
                                  (js/setTimeout (fn [] (reset! record-button-is-animating? false)) 1200))
           reset-x-y-animation #(do
                                  (reanimated/animate-shared-value-with-timing translate-y 0 300 :easing1)
                                  (reanimated/animate-shared-value-with-timing translate-x 0 300 :easing1)
                                  (reanimated/animate-shared-value-with-timing icon-opacity 1 500 :linear)
                                  (reanimated/animate-shared-value-with-timing gray-overlay-opacity 0 800 :linear)
                                  (js/setTimeout (fn [] (reset! record-button-at-initial-position? true)) 800))]
       (quo.react/effect! #(if @recording? (start-animation) (when-not @ready-to-lock? (stop-animation))) [@recording?])
       (quo.react/effect! #(if @ready-to-lock? (start-x-y-animation) (reset-x-y-animation)) [@ready-to-lock?])
       (quo.react/effect! #(if @ready-to-send? (start-y-animation) (reset-y-animation)) [@ready-to-send?])
       (quo.react/effect! #(if @ready-to-delete? (start-x-animation) (reset-x-animation)) [@ready-to-delete?])
       [reanimated/view {:style (reanimated/apply-animations-to-style
                                 {:transform [{:translateY translate-y}
                                              {:translateX translate-x}]}
                                 {:position        :absolute
                                  :bottom          0
                                  :right           0
                                  :width           96
                                  :height          96
                                  :align-items     :center
                                  :justify-content :center
                                  :z-index         0})
                         :pointer-events :none}
        [:<>
         (map-indexed
          (fn [id animation]
            ^{:key id}
            [animated-ring {:scale   (:scale animation)
                            :opacity (:opacity animation)
                            :color   rings-color}])
          animations)]
        [rn/view {:style {:width            56
                          :height           56
                          :border-radius    28
                          :justify-content  :center
                          :align-items      :center
                          :background-color button-color
                          :overflow         :hidden}}
         [reanimated/view {:style (reanimated/apply-animations-to-style
                                   {:opacity red-overlay-opacity}
                                   {:position :absolute
                                    :top              0
                                    :left             0
                                    :right            0
                                    :bottom           0
                                    :background-color colors/danger-50})}]
         [reanimated/view {:style (reanimated/apply-animations-to-style
                                   {:opacity gray-overlay-opacity}
                                   {:position         :absolute
                                    :top              0
                                    :left             0
                                    :right            0
                                    :bottom           0
                                    :background-color (colors/theme-colors colors/neutral-80-opa-5-opaque colors/neutral-80)})}]
         [reanimated/view {:style (reanimated/apply-animations-to-style {:opacity icon-opacity} {})}
          (if @locked?
            [rn/view {:style {:width            13
                              :height           13
                              :border-radius    4
                              :background-color colors/white}}]
            [icons/icon :main-icons2/audio {:color icon-color}])]]]))])

(defn send-button []
  [:f>
   (fn []
     (let [translate-y (reanimated/use-shared-value 0)
           connector-opacity (reanimated/use-shared-value 0)
           width (reanimated/use-shared-value 12)
           height (reanimated/use-shared-value 24)
           border-radius (reanimated/use-shared-value 16)
           border-radius-2 (reanimated/use-shared-value 8)
           start-y-animation #(do
                                (reanimated/animate-shared-value-with-delay translate-y 12 300 :linear 800)
                                (reanimated/animate-shared-value-with-delay connector-opacity 1 0 :easing1 480)
                                (reanimated/animate-shared-value-with-delay width 56 500 :easing1 480)
                                (reanimated/animate-shared-value-with-delay height 56 500 :easing1 480)
                                (reanimated/animate-shared-value-with-delay border-radius 28 500 :easing1 480)
                                (reanimated/animate-shared-value-with-delay border-radius-2 28 500 :easing1 480))
           reset-y-animation #(do
                                (reanimated/animate-shared-value-with-timing translate-y 0 100 :linear)
                                (reanimated/set-shared-value connector-opacity 0)
                                (reanimated/set-shared-value width 12)
                                (reanimated/set-shared-value height 24)
                                (reanimated/set-shared-value border-radius 16)
                                (reanimated/set-shared-value border-radius-2 8))]
       (quo.react/effect! #(if @ready-to-send? (start-y-animation) (reset-y-animation)) [@ready-to-send?])
       [:<>
        [rn/view {:style {:justify-content :center
                          :align-items     :center
                          :position        :absolute
                          :width           56
                          :height          56
                          :top             0
                          :right           20}}
         [reanimated/view {:style (reanimated/apply-animations-to-style
                                   {:opacity                    connector-opacity
                                    :width                      width
                                    :height                     height
                                    :border-bottom-left-radius  border-radius-2
                                    :border-top-left-radius     border-radius
                                    :border-top-right-radius    border-radius
                                    :border-bottom-right-radius border-radius-2}
                                   {:justify-content  :center
                                    :align-items      :center
                                    :align-self       :center
                                    :background-color colors/primary-50
                                    :z-index          0})}]]
        [reanimated/view {:style (reanimated/apply-animations-to-style
                                  {:transform [{:translateY translate-y}]}
                                  {:justify-content  :center
                                   :align-items      :center
                                   :background-color colors/primary-50
                                   :width            32
                                   :height           32
                                   :border-radius    16
                                   :position         :absolute
                                   :top              0
                                   :right            32
                                   :z-index          10})
                          :pointer-events :none}
         [icons/icon :main-icons2/arrow-up {:color           colors/white
                                            :size            20
                                            :container-style {:z-index 10}}]]]))])

(defn lock-button []
  [:f>
   (fn []
     (let [translate-x-y (reanimated/use-shared-value 0)
           opacity (reanimated/use-shared-value 1)
           connector-opacity (reanimated/use-shared-value 0)
           width (reanimated/use-shared-value 24)
           height (reanimated/use-shared-value 12)
           border-radius (reanimated/use-shared-value 8)
           border-radius-2 (reanimated/use-shared-value 8)
           start-x-y-animation #(do
                                  (reanimated/animate-shared-value-with-delay translate-x-y 8 300 :linear 700)
                                  (reanimated/animate-shared-value-with-delay connector-opacity 1 0 :easing1 380)
                                  (reanimated/animate-shared-value-with-delay width 56 500 :easing1 380)
                                  (reanimated/animate-shared-value-with-delay height 56 500 :easing1 380)
                                  (reanimated/animate-shared-value-with-delay border-radius 28 500 :easing1 380)
                                  (reanimated/animate-shared-value-with-delay border-radius-2 28 500 :easing1 380))
           reset-x-y-animation #(do
                                  (reanimated/animate-shared-value-with-timing translate-x-y 0 100 :linear)
                                  (reanimated/set-shared-value connector-opacity 0)
                                  (reanimated/set-shared-value width 24)
                                  (reanimated/set-shared-value height 12)
                                  (reanimated/set-shared-value border-radius 8)
                                  (reanimated/set-shared-value border-radius-2 16))
           fade-in-animation #(do
                                (reanimated/animate-shared-value-with-timing opacity 1 100 :linear))
           fade-out-animation #(do
                                 (reanimated/animate-shared-value-with-timing opacity 0 100 :linear)
                                 (reanimated/set-shared-value connector-opacity 0)
                                 (reanimated/set-shared-value width 24)
                                 (reanimated/set-shared-value height 12)
                                 (reanimated/set-shared-value border-radius 8)
                                 (reanimated/set-shared-value border-radius-2 16))]
       (quo.react/effect! #(if @ready-to-lock? (start-x-y-animation) (when-not @locked? (reset-x-y-animation))) [@ready-to-lock?])
       (quo.react/effect! #(if @locked? (fade-out-animation) (do (fade-in-animation) (reset-x-y-animation))) [@locked?])
       [:<>
        [rn/view {:style {:transform       [{:rotate "45deg"}]
                          :justify-content :center
                          :align-items     :center
                          :position        :absolute
                          :width           56
                          :height          56
                          :top             20
                          :left            20}}
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
                                    :align-self       :center
                                    :background-color (colors/theme-colors colors/neutral-80-opa-5-opaque colors/neutral-80)
                                    :overflow         :hidden})}]]
        [reanimated/view {:style (reanimated/apply-animations-to-style
                                  {:transform   [{:translateX translate-x-y}
                                                 {:translateY translate-x-y}]
                                   :opacity      opacity}
                                  {:width            32
                                   :height           32
                                   :justify-content  :center
                                   :align-items      :center
                                   :background-color (colors/theme-colors colors/neutral-80-opa-5-opaque colors/neutral-80)
                                   :border-radius    16
                                   :position         :absolute
                                   :top              24
                                   :left             24
                                   :overflow         :hidden
                                   :z-index          12})
                          :pointer-events :none}
         [icons/icon :main-icons2/unlocked {:color (colors/theme-colors colors/black colors/white)
                                            :size  20}]]]))])

(defn delete-button []
  [:f>
   (fn []
     (let [translate-x (reanimated/use-shared-value 0)
           connector-opacity (reanimated/use-shared-value 0)
           width (reanimated/use-shared-value 24)
           height (reanimated/use-shared-value 12)
           border-radius (reanimated/use-shared-value 8)
           border-radius-2 (reanimated/use-shared-value 8)
           start-x-animation #(do
                                (reanimated/animate-shared-value-with-delay translate-x 12 300 :linear 800)
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
                          :align-items     :center
                          :position        :absolute
                          :width           56
                          :height          56
                          :bottom          20
                          :left            0}}
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
                                    :align-self       :center
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
                          :pointer-events :none}
         [icons/icon :main-icons2/delete {:color colors/white
                                          :size  20}]]]))])

(def record-button-area
  {:width  56
   :height 56
   :x      64
   :y      64})

(defn delete-button-area [active?]
  {:width  (if active? 72 48)
   :height (if active? 56 32)
   :x      -16
   :y      (if active? 64 76)})

(defn lock-button-area [active?]
  {:width  (if active? 72 48)
   :height (if active? 72 48)
   :x      8
   :y      8})

(defn send-button-area [active?]
  {:width  (if active? 56 32)
   :height (if active? 72 48)
   :x      (if active? 64 76)
   :y      -16})

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
  [:f>
   (fn []
     (let [scale (reanimated/use-shared-value 1)]
       (fn []
         [rn/view {:style {:width  140
                           :height 140}
                   :pointer-events :box-only
                   :on-start-should-set-responder (fn [^js e]
                                                    (when-not @locked?
                                                      (let [pressed-record-button? (touch-inside-layout?
                                                                                    {:locationX (-> e .-nativeEvent.locationX)
                                                                                     :locationY (-> e .-nativeEvent.locationY)}
                                                                                    record-button-area)]
                                                        (reset! recording? pressed-record-button?)))
                                                    true)
                   :on-responder-move (fn [^js e]
                                        (when-not @locked?
                                          (let [moved-to-send-button? (touch-inside-layout?
                                                                       {:locationX (-> e .-nativeEvent.locationX)
                                                                        :locationY (-> e .-nativeEvent.locationY)}
                                                                       (send-button-area @ready-to-send?))
                                                moved-to-delete-button? (touch-inside-layout?
                                                                         {:locationX (-> e .-nativeEvent.locationX)
                                                                          :locationY (-> e .-nativeEvent.locationY)}
                                                                         (delete-button-area @ready-to-delete?))
                                                moved-to-lock-button? (touch-inside-layout?
                                                                       {:locationX (-> e .-nativeEvent.locationX)
                                                                        :locationY (-> e .-nativeEvent.locationY)}
                                                                       (lock-button-area @ready-to-lock?))]
                                            (cond
                                              (and
                                               (or
                                                (and (not moved-to-lock-button?) @ready-to-lock?)
                                                (and (not @locked?) moved-to-lock-button? @record-button-at-initial-position?))
                                               @recording?) (reset! ready-to-lock? moved-to-lock-button?)
                                              (and
                                               (or
                                                (and (not moved-to-delete-button?) @ready-to-delete?)
                                                (and moved-to-delete-button? @record-button-at-initial-position?))
                                               @recording?) (reset! ready-to-delete? moved-to-delete-button?)
                                              (and
                                               (or
                                                (and (not moved-to-send-button?) @ready-to-send?)
                                                (and moved-to-send-button? @record-button-at-initial-position?))
                                               @recording?) (reset! ready-to-send? moved-to-send-button?)))))
                   :on-responder-release (fn [^js e]
                                           (let [on-record-button? (touch-inside-layout?
                                                                    {:locationX (-> e .-nativeEvent.locationX)
                                                                     :locationY (-> e .-nativeEvent.locationY)}
                                                                    record-button-area)]
                                             (cond
                                               (and @ready-to-lock? (not @record-button-is-animating?))
                                               (do
                                                 (reset! locked? true)
                                                 (reset! ready-to-lock? false))
                                               (and @locked? on-record-button?)
                                               (do
                                                 (when on-send (on-send))
                                                 (reset! locked? false)
                                                 (reset! recording? false)
                                                 (reset! ready-to-lock? false))
                                               (not @locked?)
                                               (do
                                                 (reset! recording? false)
                                                 (reset! ready-to-send? false)
                                                 (reset! ready-to-delete? false)
                                                 (reset! ready-to-lock? false)))))}
          [delete-button]
          [lock-button]
          [send-button]
          [record-button scale]])))])