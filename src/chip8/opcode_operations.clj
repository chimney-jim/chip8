(ns chip8.opcode-operations
  (:require [chip8.cpu-operations :refer :all]))

(defn return-from-subroutine [cpu]
  "The interpreter sets the program counter to the address at the top of the stack,then subtracts 1 from the stack pointer."
  (let [addr (stack-get cpu)]
    (-> cpu
        (sp-set addr)
        (stack-pop)
        (sp-dec))))

(defn jump [cpu addr]
  "The interpreter sets the program counter to nnn."
  (pc-set cpu addr))

(defn call-subroutine [cpu addr]
  "The interpreter increments the stack pointer, then puts the current PC on the top of the stack. The PC is then set to nnn."
  (let [pc-addr (:pc cpu)]
    (-> cpu
       (sp-inc)
       (stack-push pc-addr)
       (pc-set addr))))

(defn skip-if-eq [cpu vx val]
  "The interpreter compares register Vx to kk, and if they are equal, increments the program counter by 2."
  (let [VX-val (Vreg-get vx)]
    (if (= VX-val val)
      (pc-inc cpu)
      cpu)))
