(ns chip8.cpu-operations
  (:require [chip8.util :as util]))

(defn change-opcode [cpu opcode]
  "put new opcode in cpu for dynamic var"
  (assoc-in cpu [:opcode] opcode))

(defn dec-timers [cpu]
  "decrement the delay and sound timers if their value is > 0"
  (let [d-timer (:delay-timer cpu)
        s-timer (:sound-timer cpu)]
    (cond
     (= d-timer s-timer 0) cpu
     (not= d-timer s-timer 0) (-> cpu
               (assoc-in [:delay-timer] (dec d-timer))
               (assoc-in [:sound-timer] (dec s-timer)))
     (not= d-timer 0) (-> cpu (assoc-in [:delay-timer] (dec d-timer)))
     (not= s-timer 0) (-> cpu (assoc-in [:sound-timer] (dec s-timer))))))

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
  (let [pc (:pc cpu)
        memory (:memory cpu)]
    (util/int->hex-str (bit-or (bit-shift-left (memory pc) 8) (memory (+ pc 1))))))

(defn pc-inc [cpu]
  "increments the pc by two since two spots in memory are needed to form an opcode"
  (let [curr-pc (:pc cpu)]
    (assoc-in cpu [:pc] (+ curr-pc 2))))

(defn pc-set [cpu val]
  (assoc-in cpu [:pc] val))

(defn stack-push [cpu val]
  (let [stack (:stack cpu)]
    (assoc-in cpu [:stack] (conj stack val))))

(defn stack-pop [cpu]
  "returns cpu with item on top of stack removed"
  (let [stack (:stack cpu)]
    (assoc-in cpu [:stack] (pop stack))))

(defn stack-get [cpu]
  "get top item off stack"
  (first (:stack cpu)))

(defn sp-dec [cpu]
  (let [sp (:sp cpu)]
    (if (= sp 0)
      sp
      (assoc-in cpu [:sp] (dec sp)))))

(defn sp-inc [cpu]
  (let [sp (:sp cpu)]
    (assoc-in cpu [:sp] (inc sp))))

(defn sp-set [cpu val]
  (assoc-in cpu [:sp] val))

(defn Vreg-get [cpu vx]
  (get (:Vreg cpu) vx))

(defn Vreg-set [cpu vx val]
  (assoc-in cpu [:Vreg vx] val))
