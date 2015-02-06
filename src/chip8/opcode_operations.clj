(ns chip8.opcode-operations
  (:require [chip8.cpu :as chip8]
            [chip8.util :refer :all]))

(defn vreg-set [cpu vx val]
  "The interpreter puts the value kk into register Vx."
  (let [int-vx (hex-str->int vx)
        int-val (hex-str->int val)]
    (chip8/vreg-set cpu int-vx int-val)))

(defn vreg-get [cpu vx]
  (let [int-vx (hex-str->int vx)]
    (chip8/vreg-get cpu int-vx)))

(defn return-from-subroutine [cpu]
  "The interpreter sets the program counter to the address at the top of the stack,then subtracts 1 from the stack pointer."
  (let [addr (chip8/stack-peek cpu)]
    (-> cpu
        (chip8/pc-set addr)
        (chip8/stack-pop)
        (chip8/sp-dec))))

(defn jump [cpu addr]
  "The interpreter sets the program counter to nnn."
  (let [int-addr (hex-str->int addr)] 
    (chip8/pc-set cpu int-addr)))

(defn call-subroutine [cpu addr]
  "The interpreter increments the stack pointer, then puts the current PC on the top of the stack. The PC is then set to nnn."
  (let [pc-addr (:pc cpu)
        int-addr (hex-str->int addr)]
    (-> cpu
       (chip8/sp-inc)
       (chip8/stack-push pc-addr)
       (chip8/pc-set int-addr))))

(defn skip-if-eq [cpu vx hex-val]
  "The interpreter compares register Vx to kk, and if they are equal, increments the program counter by 2."
  (let [vx-val (vreg-get cpu vx)
        int-val (hex-str->int hex-val)]
    (if (= vx-val int-val)
      (chip8/pc-inc cpu)
      cpu)))

(defn skip-if-not-eq [cpu vx hex-val]
  "The interpreter compares register Vx to kk, and if they are not equal, increments the program counter by 2."
  (let [vx-val (vreg-get cpu vx)
        int-val (hex-str->int hex-val)]
    (if (not= vx-val int-val)
      (chip8/pc-inc cpu)
      cpu)))

(defn skip-if-vx-vy-eq [cpu vx vy]
  "The interpreter compares register Vx to register Vy, and if they are equal, increments the program counter by 2."
  (let [vx-val (vreg-get cpu vx)
        vy-val (vreg-get cpu vy)]
    (if (= vx-val vy-val)
      (chip8/pc-inc cpu)
      cpu)))

(defn skip-instruction [cpu]
  (chip8/pc-inc cpu))

(defn ireg-set [cpu val]
  (chip8/ireg-set cpu (hex-str->int val)))

(defn ireg-get [cpu]
  (chip8/ireg-get cpu))

(defn set-delay-timer [cpu val]
  (chip8/set-timer cpu "delay-timer" (hex-str->int val)))

(defn set-sound-timer [cpu val]
  (chip8/set-timer cpu "sound-timer" (hex-str->int val)))

(defn mem-set [cpu pos val]
  (let [memory (:memory cpu)]
    (chip8/mem-insert cpu pos val)))

(defn write-vxs-to-mem [cpu ireg-val]
  (loop [vxs-list (reverse (into '() (:Vreg cpu)))
         vx-val (vreg-get cpu (first vxs-list))
         pos ireg-val]
    ()))