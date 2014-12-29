(ns chip8.cpu
  (:require [chip8.util :as util]
            [chip8.cpu-operations :as cpu-ops]
            [clojure.java.io :refer [input-stream]]))

(defrecord CPU [opcode memory Vreg Ireg pc gfx delay-timer sound-timer stack sp key])

(defn build-cpu []
  "build a prepped cpu"
  (map->CPU {:opcode 0,
             :memory (util/sized-vec 4096),
             :Vreg (util/sized-vec 16),
             :Ireg 0,
             :pc 0x200,
             :gfx (util/sized-vec (* 64 32)),
             :delay-timer 0,
             :sound-timer 0,
             :stack '(),
             :sp 0,
             :key (util/sized-vec 16)}))


(defn load-game [cpu game-path]
  "loads a game into the system"
  (with-open [in (input-stream game-path)]
    (loop [c (.read in)
           i 512
           inner-cpu cpu]
      (if (not= c -1)
        (do
          (recur (.read in) (+ i 1) (cpu-ops/mem-insert inner-cpu i (util/int->hex-str c))))
        inner-cpu))))

(defn emulate-cycle [cpu]
  "emulates one cycle of the cpu"
  (let [opcode (cpu-ops/get-next-opcode cpu)])
  ;;decode opcode
  ;;execute opcode

  ;;update timers
  )

(defn set-keys [cpu]
  "stores a keypress for controls")

(defn load-fontset [cpu]
  "loads the font set into the first 80 slots of memory")
