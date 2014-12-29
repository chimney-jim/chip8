(ns chip8.cpu-operations
  (:require [chip8.opcode :as opcode]))

(defn handle-opcode [cpu opcode]
  (opcode/handle-opcode cpu opcode))

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
  "grab the next opcode form memory"
  (let [pc (:pc cpu)
        memory (:memory cpu)]
    (str (memory pc) (memory (+ pc 1)))))

(defn inc-pc [cpu]
  "increments the pc by two since two spots in memory are needed to form an opcode"
  (let [curr-pc (:pc cpu)]
    (assoc-in cpu [:pc] (+ curr-pc 2))))

(defn set-pc [cpu val]
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
  (first (:stack @cpu)))

(defn dec-sp [cpu]
  (let [sp (:sp cpu)]
    (if (= sp 0)
      sp
      (assoc-in cpu [:sp] (dec sp)))))

(defn set-sp [cpu val]
  (assoc-in cpu [:sp] val))
