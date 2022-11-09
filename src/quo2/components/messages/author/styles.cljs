(ns quo2.components.messages.author.styles
  (:require [quo2.foundations.colors :as colors]))

(def container
  {:flex   1
   :width  "100%"
   :height 18
   :flex-direction :row
   :align-items    :center})

(def ens-text
  {:color (colors/theme-colors colors/neutral-100 colors/white)})

(def nickname-text
  {:color (colors/theme-colors colors/neutral-100 colors/white)})

(def middle-dot-nickname
  {:color             colors/neutral-50
   :margin-horizontal 4})

(def public-key-text
  {:color       colors/neutral-50
   :margin-left 8})

(def middle-dot-public-key
  {:color       colors/neutral-50
   :margin-left 4})

(defn profile-name-text [has-nickname?]
  {:color (if has-nickname?
            (colors/theme-colors colors/neutral-60 colors/neutral-40)
            (colors/theme-colors colors/neutral-100 colors/white))})

(def icon-container
  {:margin-left 4})

(defn time-text [is-ens?]
  {:color       colors/neutral-50
   :margin-left (if is-ens? 8 4)})