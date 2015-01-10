(ns chip8.opcode-operations
  (:require [chip8.cpu-operations :refer :all]
            [chip8.util           :refer :all]))

(defn vreg-set [cpu vx val]
  "The interpreter puts the value kk into register Vx."
  (let [int-vx (hex-str->int vx)
        int-val (hex-str->int val)]
    (Vreg-set cpu int-vx int-val)))

(defn vreg-get [cpu vx]
  (let [int-vx (hex-str->int vx)]
    (Vreg-get cpu int-vx)))

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

(defn call-subroutine [cpu hex-addr]
  "The interpreter increments the stack pointer, then puts the current PC on the top of the stack. The PC is then set to nnn."
  (let [pc-addr (:pc cpu)
        int-addr (hex-str->int hex-addr)]
    (-> cpu
       (sp-inc)
       (stack-push pc-addr)
       (pc-set int-addr))))

(defn skip-if-eq [cpu vx hex-val]
  "The interpreter compares register Vx to kk, and if they are equal, increments the program counter by 2."
  (let [vx-val (vreg-get cpu vx)
        int-val (hex-str->int hex-val)]
    (if (= vx-val val)
      (pc-inc cpu)
      cpu)))

(defn skip-if-not-eq [cpu vx hex-val]
  "The interpreter compares register Vx to kk, and if they are not equal, increments the program counter by 2."
  (let [vx-val (vreg-get vx)
        int-val (hex-str->int hex-val)]
    (if (not= vx-val int-val)
      (pc-inc cpu)
      cpu)))

(defn skip-if-vx-vy [cpu vx vy]
  "The interpreter compares register Vx to register Vy, and if they are equal, increments the program counter by 2."
  (let [vx-val (vreg-get vx)
        vy-val (vreg-get vy)]
    (if (= vx-val vy-val)
      (pc-inc)
      cpu)))

(defn skip-instruction [cpu]
  (pc-inc cpu))

(defn ireg-set [cpu val]
  (Ireg-set cpu (hex-str->int val)))

(defn ireg-get [cpu]
  (Ireg-get cpu))
