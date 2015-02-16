(ns chip8.display
  (:require [seesaw.core :refer :all]))

(defn make-frame []
  (frame :title "Hello"
         :content (canvas :id :screen
                          :background :black
                          :size [640 :by 320])
         ;:listen []
         :on-close :exit))


