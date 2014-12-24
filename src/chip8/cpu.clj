(ns chip8.cpu
  (:require [chip8.util :refer [sized-vec]]))

(defrecord CPU [opcode memory Vreg Ireg pc gfx delay-timer sound-timer stack sp key])

(defn build-cpu []
  "build a prepped cpu"
  (map->CPU {:opcode 0,
             :memory (sized-vec 4096),
             :Vreg (sized-vec 16),
             :Ireg 0,
             :pc 0x200,
             :gfx (sized-vec (* 64 32)),
             :delay-timer 0,
             :sound-timer 0,
             :stack (sized-vec 16),
             :sp 0,
             :key (sized-vec 16)}))

(defn change-opcode [cpu opcode]
  "put new opcode in cpu for dynamic var"
  (assoc-in cpu [:opcode] opcode))

(defn dec-delay-sound-timers [cpu]
  "decrement the delay and sound timers if their value is > 0"
  (let [d-timer (:delay-timer cpu)
        s-timer (:sound-timer cpu)]
    (if ((or (= d-timer 0) (= s-timer 0)))
      cpu
      (-> cpu
          (assoc-in [:delay-timer] dec)
          (assoc-in [:sound-timer] dec))))))

(defn set-delay-timer [cpu val]
  (-> cpu (assoc-in [:delay-timer] val)))

(defn set-sound-timer [cpu val]
  (-> cpu (assoc-in [:sound-timer] val)))

(defn mem-insert [cpu pos val]
  "insert value into memory"
  (let [memory (:memory cpu)]
    (assoc-in cpu [:memory] (assoc memory pos val))))

(defn mem-get [cpu pos]
  "grabs the value at the position in memory"
  (-> cpu :memory (get pos)))

(defn get-next-opcode [cpu]
  "grab next opcode from memory at pc"
  (let [pc (:pc cpu)
        memory (:memory cpu)]
    (bit-or
     (bit-shift-left (get memory pc) 8) (get memory (+ pc 1)))))

(defn inc-pc [cpu]
  "increments the pc by two since two spots in memory are needed to form an opcode"
  (let [curr-pc (:pc cpu)]
    (assoc-in cpu [:pc] (+ curr-pc 2))))

(defn load-game [cpu game]
  "loads a game into the system")

(defn emulate-cycle [cpu]
  "emulates one cycle of the cpu"
  (get-next-opcode cpu)
  ;;decode opcode
  ;;execute opcode

  ;;update timers
  )

(defn set-keys [cpu]
  "stores a keypress for controls")
