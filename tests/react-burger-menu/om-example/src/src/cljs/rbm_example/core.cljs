(ns ^:figwheel-always rbm-example.core
  (:require [om.core :as om :include-macros true]
            [om-tools.dom :as dom :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]
            [cljsjs.react-burger-menu]))

(enable-console-print!)

(defn- log [x] (println x) x)

(defonce app-state (atom {:current-menu :slide
                          :menus
                          {:slide       {:button-text "Slide"
                                         :items 1}
                           :stack       {:button-text "Stack"
                                         :items 1}
                           :elastic     {:button-text "Elastic"
                                         :items 1}
                           :bubble      {:button-text "Bubble"
                                         :items 1}
                           :push        {:button-text "Push"
                                         :items 1}
                           :pushRotate  {:button-text "Push Rotate"
                                         :items 2}
                           :scaleDown   {:button-text "Scale Down"
                                         :items 2}
                           :scaleRotate {:button-text "Scale Rotate"
                                         :items 2}
                           :fallDown    {:button-text "Fall Down"
                                         :items 2}}}))

(defn react-build [comp props & children]
  (let [React (.-React js/window)]
    (.createElement React comp (clj->js props) children)))

(defn change-menu [cursor menu]
  (om/update! cursor :current-menu menu))

(defn build-item [extra-class text]
  (dom/a
   {:href ""}
   (dom/i
    {:class (str "fa fa-fw " extra-class)})
   (dom/span text)))

(defn build-menu [cursor]
  (let [current-menu (:current-menu cursor)
        items (-> cursor current-menu :items)
        str-current-menu (name current-menu)
        comp (aget (.-BurgerMenu js/window) str-current-menu)]
    (condp = items
      2 (react-build comp
                     {:id str-current-menu
                      :pageWrapId "page-wrap"
                      :outerContainerId "outer-container"}
                     (cons
                      (dom/h2
                       (dom/i {:class "fa fa-fw fa-inbox fa-2x"})
                       (dom/span "Sidebar"))
                      (map build-item
                           ["fa-database" "fa-map-marker" "fa-mortar-board"
                            "fa-picture-o" "fa-money"]
                           ["Data Management" "Location" "Study"
                            "Collections" "Credits"])))
      items (react-build
             comp
             {:id str-current-menu
              :pageWrapId "page-wrap"
              :outerContainerId "outer-container"}
             (map build-item
                  ["fa-star-o" "fa-bell-o" "fa-envelope-o"
                   "fa-comment-o" "fa-bar-chart-o" "fa-newspaper-o"]
                  ["Favorites" "Alerts" "Messages"
                   "Comments" "Analytics" "Reading"])))))

(defcomponent button [data owner]
  (render
   [_]
   (let [cursor (:cursor data)
         menu   (:menu data)
         menus  (:menus cursor)
         current-menu (:current-menu cursor)
         class (if (= menu current-menu)
                 "current-demo"
                 "")]
     (dom/a
      {:class class
       :on-click #(change-menu cursor menu)}
      (-> menus menu :button-text)))))

(defn build-all-buttons [cursor]
  (let [menus (:menus cursor)
        base-data {:cursor cursor}
        datas (->> (log menus)
                   (map first)
                   (map #(assoc base-data :menu %)))]
    (om/build-all button (log datas) {:key :menu})))

(defcomponent demo [data owner]
  (render
   [_]
   (dom/div
    {:id "outer-container"
     :style {:height "100%"}}
    (build-menu data)
    (dom/main
     {:id "page-wrap"}
     (dom/h1
      (dom/a
       {:href "https://github.com/negomi/react-burger-menu"}
       "react-burger-menu"))
     (dom/h2 "An off-canvas sidebar React component with a collection
              of effects and styles using CSS transitions and SVG path
              animations.")
     (dom/nav
      {:class "demo-buttons"}
      (build-all-buttons data))
     "Inspired by "
     (dom/a
      {:href "https://github.com/codrops/OffCanvasMenuEffects"}
      "Off-Canvas Menu Effects")
     " and "
     (dom/a
      {:href "https://github.com/codrops/SidebarTransitions"}
      "Sidebar Transitions")
     " by Codrops"))))

(om/root
 demo
 app-state
 {:target (.-body js/document)})
