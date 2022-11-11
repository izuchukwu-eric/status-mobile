(ns status-im.navigation2.roots
  (:require [quo2.foundations.colors :as colors]
            [status-im.utils.platform :as platform]))

(defn status-bar-options []
  (if platform/android?
    {:navigationBar {:backgroundColor colors/neutral-100}
     :statusBar     {:backgroundColor :transparent
                     :style           (if (colors/dark?) :light :dark)
                     :drawBehind      true}}
    {:statusBar {:style (if (colors/dark?) :light :dark)}}))

(defn roots []
  {:shell-stack
   {:root
    {:stack {:id       :shell-stack
             :children [{:component {:name    :chat-stack
                                     :id      :chat-stack
                                     :options (merge (status-bar-options)
                                                     {:topBar {:visible false}})}}]}}}})
