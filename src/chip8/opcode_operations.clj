(ns chip8.opcode-operations
  (:require [chip8.cpu-operations :refer :all]))

(defn return-from-subroutine [cpu]
  (let [addr (stack-get cpu)]
    (-> cpu
        (set-pc addr)
        (stack-pop)
        (dec-sp))))
