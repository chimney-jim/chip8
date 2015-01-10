(ns chip8.opcode-operations
  (:require [chip8.cpu-operations :as cpu-ops]
            [chip8.util           :refer :all]))

(defn vreg-set [cpu vx val]
  "The interpreter puts the value kk into register Vx."
  (let [int-vx (hex-str->int vx)
        int-val (hex-str->int val)]
    (cpu-ops/Vreg-set cpu int-vx int-val)))

(defn vreg-get [cpu vx]
  (let [int-vx (hex-str->int vx)]
    (cpu-ops/Vreg-get cpu int-vx)))

(defn return-from-subroutine [cpu]
  "The interpreter sets the program counter to the address at the top of the stack,then subtracts 1 from the stack pointer."
  (let [addr (cpu-ops/stack-get cpu)]
    (-> cpu
        (cpu-ops/sp-set addr)
        (cpu-ops/stack-pop)
        (cpu-ops/sp-dec))))

(defn jump [cpu addr]
  "The interpreter sets the program counter to nnn."
  (cpu-ops/pc-set cpu addr))

(defn call-subroutine [cpu hex-addr]
  "The interpreter increments the stack pointer, then puts the current PC on the top of the stack. The PC is then set to nnn."
  (let [pc-addr (:pc cpu)
        int-addr (hex-str->int hex-addr)]
    (-> cpu
       (cpu-ops/sp-inc)
       (cpu-ops/stack-push pc-addr)
       (cpu-ops/pc-set int-addr))))

(defn skip-if-eq [cpu vx hex-val]
  "The interpreter compares register Vx to kk, and if they are equal, increments the program counter by 2."
  (let [vx-val (vreg-get cpu vx)
        int-val (hex-str->int hex-val)]
    (if (= vx-val val)
      (cpu-ops/pc-inc cpu)
      cpu)))

(defn skip-if-not-eq [cpu vx hex-val]
  "The interpreter compares register Vx to kk, and if they are not equal, increments the program counter by 2."
  (let [vx-val (vreg-get cpu vx)
        int-val (hex-str->int hex-val)]
    (if (not= vx-val int-val)
      (cpu-ops/pc-inc cpu)
      cpu)))

(defn skip-if-vx-vy [cpu vx vy]
  "The interpreter compares register Vx to register Vy, and if they are equal, increments the program counter by 2."
  (let [vx-val (vreg-get cpu vx)
        vy-val (vreg-get cpu vy)]
    (if (= vx-val vy-val)
      (cpu-ops/pc-inc cpu)
      cpu)))

(defn skip-instruction [cpu]
  (cpu-ops/pc-inc cpu))

(defn ireg-set [cpu val]
  (cpu-ops/Ireg-set cpu (hex-str->int val)))

(defn ireg-get [cpu]
  (cpu-ops/Ireg-get cpu))

(defn set-delay-t [cpu val]
  (cpu-ops/set-delay-timer cpu (hex-str->int val)))