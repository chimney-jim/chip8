(ns chip8.cpu
  (:require [chip8.util :refer [sized-vec]]))

(defrecord CPU [opcode memory Vreg Ireg pc gfx delay-timer sound-timer stack sp key])

(defn build-cpu []
  (map->CPU {:opcode nil,
             :memory (sized-vec 4096),
             :Vreg (sized-vec 16),
             :Ireg nil,
             :pc nil,
             :gfx (sized-vec (* 64 32)),
             :delay-timer nil,
             :sound-timer nil,
             :stack (sized-vec 16),
             :sp nil,
             :key (sized-vec 16)}))

(defn change-opcode [cpu opcode]
  "put new opcode in cpu for dynamic var"
  (assoc-in cpu [:opcode] opcode))

(defn mem-insert [cpu pos val]
  "insert value into memory"
  (assoc-in cpu [:memory] (assoc (:memory cpu) pos val)))

(defn mem-get [cpu pos]
  "grabs the value at the position in memory"
  (-> cpu :memory (get pos)))

(defn mem-get-next-opcode [cpu]
  (let [pc (:pc cpu)
        memory (:memory cpu)]
    (bit-or (get memory pc) (get memory (+ pc 1)))))

(defn load-game [cpu game]
  "loads a game into the system")

(defn emulate-cycle [cpu]
  "emulates one cycle of the cpu"
  ;;fetch opcode
  ;;decode opcode
  ;;execute opcode

  ;;update timers
  )

(defn set-keys [cpu]
  "stores a keypress for controls")

(defn fetch-opcode [cpu]
  "fetches an opcode from memory of the cpu"
  ())
