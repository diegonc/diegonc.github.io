(ns ^:figwheel-always snapsvg-example.core
  (:require [cljsjs.snapsvg]))

(enable-console-print!)

(defn- log [x] (println x) x)

(def s (js/Snap 600 600))
(def nums (.. s (text 300 300
                      (clj->js (take 12 (iterate inc 1))))
              (attr #js {"font" "300 40px Helvetica Neue"
                         "textAnchor" "middle"})))
(def path
  (apply str
         (for [i (take 72 (iterate inc 0))]
           (let [r (if-not (= 0 (rem i 6))
                     (if-not (= 0 (rem i 3))
                       247
                       240)
                     230)
                 sin (.sin js/Math (.rad js/Snap (* 5 i)))
                 cos (.cos js/Math (.rad js/Snap (* 5 i)))]
             (str "M" #js [(+ 300 (* 250 cos)) (+ 300 (* 250 sin))]
                  "L" #js [(+ 300 (* r cos)) (+ 300 (* r sin))])))))

;; used vec to force eager evaluation for the side-effects
(vec (for [i (filter #(= 0 (rem % 6)) (take 72 (iterate inc 0)))]
       (let [alfa (.rad js/Snap (- (* 5 i) 60))
             cos (.cos js/Math alfa)
             sin (.sin js/Math alfa)
             x (+ 300 (* 200 cos))
             y (+ 315 (* 200 sin))]
         (.. nums
             (select (str "tspan:nth-child(" (+ 1 (/ i 6)) ")"))
             (attr #js {"x" x "y" y})))))

(def table
  (let [attred-path (.. s
                        (path path)
                        (attr #js {"fill" "none"
                                   "stroke" "#000"
                                   "strokeWidth" 2}))]
    (.. s
        (g nums attred-path)
        (attr #js {"transform" "t0,210"}))))

(.. s (g table) (attr #js {"clip" (.circle s 300 300 100)}))

(def hand
  (.. s (line 300 200 300 400)
      (attr #js {"fill" "none"
                 "stroke" "#f63"
                 "strokeWidth" 2})))

(defn animate-clock [val]
  (let [alfa (.rad js/Snap (+ val 90))
        tx (* 210 (.cos js/Math alfa))
        ty (* 210 (.sin js/Math alfa))
        ]
    (.transform table (str "t" #js [tx ty]))
    (.transform hand (str "r" #js [val 300 300]))))

(.. s
    (circle 300 300 100)
    (attr #js {"stroke" "#000"
               "strokeWidth" 10
               "fillOpacity" 0})
    (click #(.animate js/Snap 0 360 animate-clock 12000)))

