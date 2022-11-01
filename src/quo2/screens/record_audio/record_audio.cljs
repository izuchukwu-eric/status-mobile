(ns quo2.screens.record-audio.record-audio
  (:require [quo.react-native :as rn]
            [quo2.components.record-audio.record-audio :as record-audio]))

(defn cool-preview []
  [rn/view {:padding-bottom 150}
   [rn/view {:padding-vertical 60
             :align-items      :center
             :background-color :transparent}
    [record-audio/input-view]]])

(defn preview-record-audio []
  [rn/view {:flex 1}
   [cool-preview]])
