(ns nav-example.ios.core
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [cljs-react-navigation.reagent :refer [stack-navigator stack-screen]]
            [nav-example.events]
            [nav-example.subs]))

(def ReactNative (js/require "react-native"))

(def app-registry (.-AppRegistry ReactNative))
(def text (r/adapt-react-class (.-Text ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def image (r/adapt-react-class (.-Image ReactNative)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight ReactNative)))

(def logo-img (js/require "./images/cljs.png"))

(defn alert [title]
  (.alert (.-Alert ReactNative) title))

(defn first-screen
  []
  (let [greeting (subscribe [:get-greeting])]
    (fn [{:keys [navigation]}]
      (let [{navigate :navigate} navigation]
        [view {:style {:flex-direction "column" :margin 40 :align-items "center"}}
         [text {:style {:font-size 30 :font-weight "100" :margin-bottom 20 :text-align "center"}} @greeting]
         [image {:source logo-img
                 :style  {:width 80 :height 80 :margin-bottom 30}}]
         [touchable-highlight {:style {:background-color "#999" :padding 10 :border-radius 5}
                               :on-press #(navigate "AnotherScreen" {})}
          [text {:style {:color "white" :text-align "center" :font-weight "bold"}} "press me to navigate"]]]))))

(defn another-screen
  []
  [view {:style {:margin 40}}
   [text {:style {:font-size 30}} "You are on another screen"]])

(def stack-nav
  (stack-navigator {:FirstScreen {:screen (stack-screen first-screen {:title "First Screen"})}
                    :AnotherScreen {:screen (stack-screen another-screen {:title "Another Screen"})}}))

(defn app-root
  []
  [:> stack-nav {}])

(defn init []
  (dispatch-sync [:initialize-db])
  (.registerComponent app-registry "NavExample" #(r/reactify-component app-root)))
