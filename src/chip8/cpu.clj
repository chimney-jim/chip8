(ns chip8.cpu
  (:require [chip8.util :as util]
            [clojure.java.io :refer [input-stream]]))

(defprotocol Chip8
  (load-game [this game-path] "loads a game into the system")
  (change-opcode [this opcode] "put new opcode in cpu for dynamic var")
  (dec-timer [this timer])
  (dec-timers [this] "decrement the delay and sound timers if their value is > 0")
  (set-timer [this timer val])
  (mem-insert [this pos val])
  (mem-get [this pos])
  (get-next-opcode [this])
  (pc-inc [this] "increments the pc by two since two spots in memory are needed to form an opcode")
  (pc-set [this val])
  (stack-push [this val])
  (stack-pop [this] "returns cpu with item on top of stack removed")
  (stack-get [this])
  (sp-inc [this])
  (sp-dec [this])
  (sp-set [this val])
  (vreg-get [this vreg])
  (vreg-set [this vreg val])
  (ireg-get [this])
  (ireg-set [this val]))

(defrecord chip8-cpu [opcode memory Vreg Ireg pc gfx delay-timer sound-timer stack sp key]
  Chip8
  (load-game [this game-path]
    (with-open [in (input-stream game-path)]
      (loop [c (.read in)
             i 512
             inner-cpu this]
        (if (not= c -1)
          (do
            (recur (.read in) (+ i 1) (mem-insert inner-cpu i c)))
          inner-cpu))))
  (change-opcode [this opcode]
    (assoc-in this [:opcode] opcode))
  (dec-timer [this timer]
    (assoc-in this [(keyword timer)] (dec ((keyword timer) this))))
  (dec-timers [this]
    (let [dt (:delay-timer this)
          st (:sound-timer this)]
      (cond
        (not= dt st 0) (-> this
                           (dec-timer "delay-timer")
                           (dec-timer "sound-timer"))
        (= dt st 0) this
        (not= dt 0) (dec-timer this "delay-timer")
        (not= st 0) (dec-timer this "sound-timer"))))
  (set-timer [this timer val]
    (assoc-in this [(keyword timer)] val))
  (mem-insert [this pos val]
    (let [memory (:memory this)]
      (assoc-in this [:memory] (assoc memory pos val))))
  (mem-get [this pos]
    (get (:memory this) pos))
  (get-next-opcode [this]
    (let [pc (:pc this)
          memory (:memory this)]
      (bit-or (bit-shift-left (memory pc) 8) (memory (+ pc 1)))))
  (pc-inc [this]
    (let [curr-pc (:pc this)]
      (assoc-in this [:pc] (+ curr-pc 2))))
  (pc-set [this val]
    (assoc-in this [:pc] val))
  (stack-push [this val]
    (let [stack (:stack this)]
      (assoc-in this [:stack] (conj stack val))))
  (stack-pop [this]
    (let [stack (:stack this)]
      (assoc-in this [:stack] (pop stack))))
  (stack-get [this]
    (first (:stack this)))
  (sp-inc [this]
    (let [sp (:sp this)]
      (assoc-in this [:sp] (inc sp))))
  (sp-dec [this]
    (let [sp (:sp this)]
      (if (= sp 0)
        this
        (assoc-in this [:sp] (dec sp)))))
  (sp-set [this val]
    (assoc-in this [:sp] val))
  (vreg-get [this vreg]
    (get (:Vreg this) vreg))
  (vreg-set [this vreg val]
    (assoc-in this [:Vreg vreg] val))
  (ireg-get [this]
    (:Ireg this))
  (ireg-set [this val]
    (assoc-in this [:Ireg] val)))

(defn build-cpu []
  "build a prepped cpu"
  (map->chip8-cpu {:opcode 0,
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

(defn emulate-cycle [cpu]
  "emulates one cycle of the cpu"
  (let [opcode (get-next-opcode cpu)])
  ;;decode opcode
  ;;execute opcode

  ;;update timers
  )

(defn set-keys [cpu]
  "stores a keypress for controls")

(defn load-fontset [cpu]
  "loads the font set into the first 80 slots of memory")
