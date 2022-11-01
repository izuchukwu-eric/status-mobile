(ns quo2.components.record-audio.record-audio
  (:require [quo.react :as react]
            [quo.react-native :as rn]
            [quo2.foundations.colors :as colors]
            [quo2.components.icon :as icons]
            [react-native.reanimated :as reanimated]
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

(def recording? (reagent/atom false))
(def locked? (reagent/atom false))
(def ready-to-send? (reagent/atom false))
(def ready-to-lock? (reagent/atom false))
(def ready-to-delete? (reagent/atom false))
(def clear-timeout (atom nil))

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

(defn record-button [scale]
  [:f>
   (fn []
     (let [opacity-from (if @ready-to-lock? opacity-from-lock opacity-from-default)
           scale-1 (ring-scale scale 0)
           opacity-1 (reanimated/interpolate scale-1 [1 scale-to-each] [opacity-from 0])
           scale-2 (ring-scale scale scale-padding)
           opacity-2 (reanimated/interpolate scale-2 [1 scale-to-each] [opacity-from 0])
           scale-3 (ring-scale scale (* scale-padding 2))
           opacity-3 (reanimated/interpolate scale-3 [1 scale-to-each] [opacity-from 0])
           scale-4 (ring-scale scale (* scale-padding 3))
           opacity-4 (reanimated/interpolate scale-4 [1 scale-to-each] [opacity-from 0])
           scale-5 (ring-scale scale (* scale-padding 4))
           opacity-5 (reanimated/interpolate scale-5 [1 scale-to-each] [opacity-from 0])
           rings-color (cond
                         @ready-to-lock? colors/neutral-80-opa-5-opaque
                         @ready-to-delete? colors/danger-50
                         :else colors/primary-50)
           ring-style-1 (reanimated/apply-animations-to-style
                         {:transform [{:scale scale-1}]
                          :opacity   opacity-1}
                         {:width           56
                          :height          56
                          :border-width    1
                          :border-color    rings-color
                          :border-radius   28
                          :position        :absolute
                          :left            20
                          :top             20
                          :justify-content :center
                          :align-items     :center
                          :z-index         -1})
           ring-style-2 (reanimated/apply-animations-to-style
                         {:transform [{:scale scale-2}]
                          :opacity   opacity-2}
                         {:width           56
                          :height          56
                          :border-width    1
                          :border-color    rings-color
                          :border-radius   28
                          :position        :absolute
                          :left            20
                          :top             20
                          :justify-content :center
                          :align-items     :center
                          :z-index         -1})
           ring-style-3 (reanimated/apply-animations-to-style
                         {:transform [{:scale scale-3}]
                          :opacity   opacity-3}
                         {:width           56
                          :height          56
                          :border-width    1
                          :border-color    rings-color
                          :border-radius   28
                          :position        :absolute
                          :left            20
                          :top             20
                          :justify-content :center
                          :align-items     :center
                          :z-index         -1})
           ring-style-4 (reanimated/apply-animations-to-style
                         {:transform [{:scale scale-4}]
                          :opacity   opacity-4}
                         {:width           56
                          :height          56
                          :border-width    1
                          :border-color    rings-color
                          :border-radius   28
                          :position        :absolute
                          :left            20
                          :top             20
                          :justify-content :center
                          :align-items     :center
                          :z-index         -1})
           ring-style-5 (reanimated/apply-animations-to-style
                         {:transform [{:scale scale-5}]
                          :opacity   opacity-5}
                         {:width           56
                          :height          56
                          :border-width    1
                          :border-color    rings-color
                          :border-radius   28
                          :position        :absolute
                          :left            20
                          :top             20
                          :justify-content :center
                          :align-items     :center
                          :z-index         -1})
           translate-y (reanimated/use-shared-value 0)
           translate-x (reanimated/use-shared-value 0)
           button-color colors/primary-50
           icon-color (if @ready-to-lock? colors/black colors/white)
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
                                (reanimated/animate-shared-value-with-timing translate-y -64 1500 :easing1)
                                (reanimated/animate-shared-value-with-delay icon-opacity 0 200 :linear 700))
           reset-y-animation #(do
                                (reanimated/animate-shared-value-with-timing translate-y 0 300 :easing1)
                                (reanimated/animate-shared-value-with-timing icon-opacity 1 500 :linear))
           start-x-animation #(do
                                (reanimated/animate-shared-value-with-timing translate-x -64 1500 :easing1)
                                (reanimated/animate-shared-value-with-delay icon-opacity 0 200 :linear 700)
                                (reanimated/animate-shared-value-with-timing red-overlay-opacity 1 200 :linear))
           reset-x-animation #(do
                                (reanimated/animate-shared-value-with-timing translate-x 0 300 :easing1)
                                (reanimated/animate-shared-value-with-timing icon-opacity 1 500 :linear)
                                (reanimated/animate-shared-value-with-timing red-overlay-opacity 0 100 :linear))
           start-x-y-animation #(do
                                  (reanimated/animate-shared-value-with-timing translate-y -44 1200 :easing1)
                                  (reanimated/animate-shared-value-with-timing translate-x -44 1200 :easing1)
                                  (reanimated/animate-shared-value-with-delay icon-opacity 0 200 :linear 300)
                                  (reanimated/animate-shared-value-with-timing gray-overlay-opacity 1 200 :linear))
           reset-x-y-animation #(do
                                  (reanimated/animate-shared-value-with-timing translate-y 0 300 :easing1)
                                  (reanimated/animate-shared-value-with-timing translate-x 0 300 :easing1)
                                  (reanimated/animate-shared-value-with-timing icon-opacity 1 500 :linear)
                                  (reanimated/animate-shared-value-with-timing gray-overlay-opacity 0 800 :linear))]
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
         [reanimated/view {:style ring-style-1}]
         [reanimated/view {:style ring-style-2}]
         [reanimated/view {:style ring-style-3}]
         [reanimated/view {:style ring-style-4}]
         [reanimated/view {:style ring-style-5}]]
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
                                    :background-color colors/neutral-80-opa-5-opaque})}]
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
           connector-opacity (reanimated/use-shared-value 1)
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
        [rn/view {:shouldRasterizeIOS true
                  :style {:transform       [{:rotate "45deg"}]
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
                                    :background-color colors/neutral-80-opa-5-opaque
                                    :overflow         :hidden})}]]
        [reanimated/view {:style (reanimated/apply-animations-to-style
                                  {:transform   [{:translateX translate-x-y}
                                                 {:translateY translate-x-y}]
                                   :opacity      opacity}
                                  {:width            32
                                   :height           32

                                   :justify-content  :center
                                   :align-items      :center
                                   :background-color colors/neutral-80-opa-5-opaque
                                   :border-radius    16
                                   :position         :absolute
                                   :top              24
                                   :left             24
                                   :overflow         :hidden
                                   :z-index          12})
                          :pointer-events :none}
         [icons/icon :main-icons2/unlocked {:color           colors/black
                                            :size            20}]]]))])

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
         [icons/icon :main-icons2/delete-context {:color colors/white
                                                  :size  20}]]]))])

(def record-button-area
  {:width  56
   :height 56
   :x      76
   :y      76})

(defn delete-button-area [active?]
  {:width  (if active? 56 32)
   :height (if active? 56 32)
   :x      0
   :y      (if active? 64 76)})

(defn lock-button-area [active?]
  {:width  (if active? 56 32)
   :height (if active? 56 32)
   :x      24
   :y      24})

(defn send-button-area [active?]
  {:width  (if active? 56 32)
   :height (if active? 56 32)
   :x      (if active? 64 76)
   :y      0})

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
                   :renderToHardwareTextureAndroid true
                   :shouldRasterizeIOS true
                   :on-start-should-set-responder (fn [^js e]
                                                    (let [pressed-record-button? (touch-inside-layout?
                                                                                  {:locationX (-> e .-nativeEvent.locationX)
                                                                                   :locationY (-> e .-nativeEvent.locationY)}
                                                                                  record-button-area)]
                                                      (reset! recording? pressed-record-button?)
                                                      true))
                   :on-responder-move (fn [^js e]
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
                                          (when (and (not= @ready-to-delete? moved-to-delete-button?) @recording?) (reset! ready-to-delete? moved-to-delete-button?))
                                          (when (and (not= @ready-to-send? moved-to-send-button?) @recording?) (reset! ready-to-send? moved-to-send-button?))
                                          (when (and (not= @ready-to-lock? moved-to-lock-button?) @recording?) (reset! ready-to-lock? moved-to-lock-button?))))
                   :on-responder-release (fn [^js e]
                                           (let [on-record-button? (touch-inside-layout?
                                                                    {:locationX (-> e .-nativeEvent.locationX)
                                                                     :locationY (-> e .-nativeEvent.locationY)}
                                                                    (lock-button-area @ready-to-lock?))]
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
          [delete-button]
          [lock-button]
          [send-button]
          [record-button scale]])))])