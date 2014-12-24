(ns chip8.cpu-atom
  :require [chip8.util :refer [sized-vec]])

(defrecord CPU [opcode memory Vreg Ireg pc gfx delay-timer sound-timer stack sp key])

(defn build-cpu-atom []
  (atom (map->CPU {:opcode nil,
                   :memory (sized-vec 4096),
                   :Vreg (sized-vec 16),
                   :Ireg nil,
                   :pc nil,
                   :gfx (sized-vec (* 64 32)),
                   :delay-timer nil,
                   :sound-timer nil,
                   :stack (sized-vec 16),
                   :sp nil,
                   :key (sized-vec 16)})))

(defn change-opcode-atom [cpu opcode]
  "put new opcode in cpu for atom"
  (swap! cpu assoc :opcode opcode))
