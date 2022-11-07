(ns status-im.ui.screens.communities.community-overview-redesign
  (:require  [status-im.i18n.i18n :as i18n]
             [quo.react-native :as rn]
             [status-im.ui.components.react :as react]
             [reagent.core :as reagent]
             [status-im.react-native.resources :as resources]
             [quo2.components.markdown.text :as text]
             [quo2.components.buttons.button :as button]
             [quo2.components.list-items.preview-list :as preview-list]
             [quo2.components.list-items.channel :as channel]
             [quo2.components.dividers.divider-label :as divider-label]
             [quo2.components.community.community-view :as community-view]
             [quo2.components.tags.status-tags :as status-tags]
             [status-im.ui.screens.communities.request-to-join-bottom-sheet-redesign :as request-to-join]
             [status-im.ui.screens.communities.community-options-bottom-sheet :as options-menu]
             [status-im.utils.handlers :refer [<sub >evt]]
             [status-im.ui.screens.communities.styles :as styles]
             [quo2.foundations.colors :as colors]
             [quo2.components.navigation.page-nav :as page-nav]
             [status-im.ui.screens.communities.icon :as communities.icon]))

;; Mocked list items
(def user-list
  [{:full-name "Alicia K"}
   {:full-name "Marcus C"}
   {:full-name "MNO PQR"}
   {:full-name "STU VWX"}])

(defn preview-user-list []
  [rn/view {:style {:flex-direction :row
                    :align-items :center
                    :margin-top 20}}
   [preview-list/preview-list {:type :user
                               :user user-list :list-size 4 :size 24}]
   [text/text {:accessibility-label :communities-screen-title
               :style {:margin-left 8}
               :size                :label}
    "Join Alicia, Marcus and 2 more"]]) ;; TODO remove mocked data and use from contacts list/communities members

(def list-of-channels {:Welcome [{:name "welcome"
                                  :emoji "🤝"}
                                 {:name  "onboarding"
                                  :emoji "🍑"}
                                 {:name "intro"
                                  :emoji "🦄"}]
                       :General [{:name  "general"
                                  :emoji "🐷"}
                                 {:name  "people-ops"
                                  :emoji "🌏"}
                                 {:name "announcements"
                                  :emoji "🎺"}]
                       :Mobile [{:name "mobile"
                                 :emoji "👽"}
                                {:name "mobile-ui"
                                 :emoji "👽"}
                                {:name "mobile-ui-reviews"
                                 :emoji "👽"}]
                       :Desktop [{:name "desktop"
                                  :emoji "👽"}
                                 {:name "desktop-ui"
                                  :emoji "👽"}
                                 {:name "desktop-ui-reviews"
                                  :emoji "👽"}
                                 {:name "desktop2"
                                  :emoji "👽"}
                                 {:name "desktop-ui2"
                                  :emoji "👽"}
                                 {:name "desktop-ui2-reviews"
                                  :emoji "👽"}]})

(defn channel-list-component [channel-heights first-channel-height]
  [rn/view {:on-layout #(swap! first-channel-height
                               (fn [] (int (Math/ceil (-> % .-nativeEvent.layout.y)))))
            :style {:margin-top 20 :flex 1}}
   (map (fn [category]
          ^{:key (first category)}
          [rn/view
           {:flex 1
            :on-layout #(swap! channel-heights
                               (fn []
                                 (sort-by :height
                                          (conj @channel-heights
                                                {:height (int (-> % .-nativeEvent.layout.y))
                                                 :label (first category)}))))}

           [divider-label/divider-label
            {:label (first category)
             :chevron-position :left}]
           [rn/view
            {:margin-left   8
             :margin-top    10
             :margin-bottom 8}
            (map (fn [channel-data] [rn/view {:margin-top 4}
                                     [channel/list-item channel-data]]) ((first category) list-of-channels))]])
        list-of-channels)])

(defn icon-color []
  (colors/theme-colors
   colors/white-opa-40
   colors/neutral-80-opa-40))

(def scroll-0 -44)
(def scroll0 44)
(def scroll1 86)

(def max-image-size 80)
(def min-image-size 32)

(defn diff-with-max-min [value maximum minimum]
  (min maximum (max minimum (- maximum (+ value scroll0)))))

(defn get-header-size [scroll-height]
  (if (<= scroll-height -26)
    0
    (min (max 0 (* (+ 27 scroll-height) 3)) 100)))

(defn community-card-page-view [{:keys [name description locked joined
                                        status tokens cover tags community-color] :as community}]
  (let [scroll-height (reagent/atom -44)
        channel-heights (reagent/atom [])
        first-channel-height (reagent/atom 0)]
    (fn []
      [:<>
       [:<>
        [rn/image
         {:source      cover
          :position :absolute
          :style  {:height (get-header-size @scroll-height)
                   :z-index 4
                   :flex 1}}]
        [react/blur-view {:blur-amount 32
                          :blur-type :xlight
                          :overlay-color colors/white-opa-70
                          :style {:z-index 5
                                  :position :absolute
                                  :height (get-header-size @scroll-height)
                                  :width "100%"
                                  :flex 1}}]]
       [rn/view {:style {:z-index 6 :margin-top 52}}
        [page-nav/page-nav
         {:horizontal-description?            true
          :one-icon-align-left?               true
          :align-mid?                         false
          :page-nav-color                     :transparent
          :page-nav-background-uri            ""
          :mid-section {:type  :text-with-description
                        :description-user-icon-size 24
                        :main-text (when (>= @scroll-height scroll1) name)
                        :description-user-icon-source (when (>= @scroll-height scroll1) (resources/get-image :status-logo))}
          :right-section-buttons [{:icon :main-icons2/search
                                   :background-color (icon-color)}
                                  {:icon :main-icons2/options
                                   :background-color (icon-color)
                                   :on-press #(>evt [:bottom-sheet/show-sheet
                                                     {:content (constantly [options-menu/options-menu community])
                                                      :content-height 400}])}]
          :left-section {:icon                  :main-icons2/close
                         :icon-background-color (icon-color)
                         :on-press #(>evt [:navigate-back])}}]
        (when (>= @scroll-height @first-channel-height)
          [react/blur-view {:blur-amount 32
                            :blur-type :xlight
                            :overlay-color colors/white-opa-70
                            :style {:position :absolute
                                    :top  48
                                    :height 34
                                    :width "100%"
                                    :flex 1}}

           [divider-label/divider-label
            {:label (:label (last (filter (fn [channel-height]
                                            (>= @scroll-height (+ (:height channel-height) @first-channel-height)))
                                          @channel-heights)))
             :chevron-position :left}]])]
       [rn/scroll-view {:position :absolute
                        :top -48
                        :shows-vertical-scroll-indicator false
                        :scroll-event-throttle 1
                        :overflow :scroll
                        :style {:border-radius (diff-with-max-min @scroll-height 16 0)
                                :height "100%"}
                        :on-scroll #(swap! scroll-height (fn [] (-> % .-nativeEvent.contentOffset.y)))}

        [rn/view {:style {:height 151}}
         [rn/image
          {:source      cover
           :style  {:overflow :visible
                    :flex 1}}]]
        [rn/view {:flex 1
                  :border-radius (diff-with-max-min @scroll-height 16 0)
                  :background-color (colors/theme-colors
                                     colors/white
                                     colors/neutral-90)}
         [rn/view
          [rn/view {:padding-horizontal 20}
           [rn/view {:border-radius    40
                     :border-width     1
                     :border-color     colors/white
                     :position         :absolute
                     :top              (if (<= @scroll-height scroll-0)
                                         -40
                                         (min (+ -40 (*  (+ scroll0 @scroll-height) 3)) 8))
                     :left             17
                     :padding          2
                     :background-color (colors/theme-colors
                                        colors/white
                                        colors/neutral-90)}
            [communities.icon/community-icon-redesign community
             (min max-image-size (max (- max-image-size (* (+ scroll0 @scroll-height) 3)) min-image-size))]]
           (when (and (not joined)
                      (= status :gated))
             [rn/view (styles/permission-tag-styles)
              [community-view/permission-tag-container
               {:locked       locked
                :status       status
                :tokens       tokens}]])

           (when joined
             [rn/view {:position         :absolute
                       :top              12
                       :right            12}
              [status-tags/status-tag {:status {:type :positive} :label (i18n/label :joined)}]])
           [rn/view  {:margin-top  56}
            [text/text
             {:accessibility-label :chat-name-text
              :number-of-lines     1
              :ellipsize-mode      :tail
              :weight              :semi-bold
              :size                :heading-1} name]]

           [text/text
            {:accessibility-label :community-description-text
             :number-of-lines     2
             :ellipsize-mode      :tail
             :weight  :regular
             :size    :paragraph-1
             :style {:margin-top 8}}
            description]

           [community-view/community-stats-column :card-view]
           [community-view/community-tags tags]
           [preview-user-list]
           (when (not joined)
             [button/button
              {:on-press  #(>evt [:bottom-sheet/show-sheet
                                  {:content (constantly [request-to-join/request-to-join community])
                                   :content-height 300}])
               :override-background-color community-color
               :style
               {:width "100%"
                :margin-top 20
                :margin-left :auto
                :margin-right :auto}
               :before :main-icons2/communities}
              (i18n/label :join-open-community)])]
          [channel-list-component channel-heights first-channel-height]]]]])))

(defn overview []
  (let [community-mock (<sub [:get-screen-params :community-overview]) ; TODO stop using mock data and only pass community id 
        community (<sub [:communities/community (:id community-mock)])]
    [rn/view {:style
              {:position :absolute
               :height "110%"}}
     [community-card-page-view
      (merge community-mock {:joined (:joined community)})]
     ]))

