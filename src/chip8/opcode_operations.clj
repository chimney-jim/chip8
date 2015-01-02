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

(defn skip-if-not-eq [cpu vx val]
  "The interpreter compares register Vx to kk, and if they are not equal, increments the program counter by 2."
  (let [vx-val (Vreg-get vx)]
    (if (not= vx-val val)
      (pc-inc cpu)
      cpu)))

(defn skip-if-vx-vy [cpu vx vy]
  "The interpreter compares register Vx to register Vy, and if they are equal, increments the program counter by 2."
  (let [vx-val (Vreg-get vx)
        vy-val (Vreg-get vy)]
    (if (= vx-val vy-val)
      (pc-inc)
      cpu)))

(defn vreg-set [cpu vx val]
  "The interpreter puts the value kk into register Vx."
  (Vreg-set cpu vx val))

(defn vreg-add [cpu vx val]
  "Adds the value kk to the value of register Vx, then stores the result in Vx."
  (let [vx-val (Vreg-get cpu vx)]
    (Vreg-set cpu vx (conj vx val))))

(defn vreg-get [cpu vx]
  (Vreg-get cpu vx))
