(ns chip8.core
  (:require [chip8.cpu :as chip8])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [game ("some game")
        cpu (atom (chip8/load-game chip8/build-cpu game))])
  ;;(setup-graphics)
  ;;(setup-input)

  (while true
    (swap! cpu chip8/emulate-cycle)
    ;;(if (cpu :draw-flag) (draw-graphics))
    ;;(set-keys)
    )
)
