(ns chip8.core
  (:require [chip8.cpu :as cpu])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  ;;(setup-graphics)
  ;;(setup-input)

  (def cpu (atom cpu/build-cpu))
  ;;(init-cpu)
  (swap! cpu cpu/load-game game)
  (while true
    (swap! cpu cpu/emulate-cycle)
    ;;(if (cpu :draw-flag) (draw-graphics))
    ;;(set-keys)
    )
  )
