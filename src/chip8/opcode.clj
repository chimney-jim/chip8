(ns chip8.opcode
  (:require [chip8.opcode-operations :as op-ops]))

(defn remove-carry [val]
  (Integer/parseInt (subs (Integer/toBinaryString val) 1 9) 2))

(defn get-lsb [val]
  (subs (Integer/toBinaryString val) 8 9))

(defn get-msb [val]
  (subs (Integer/toBinaryString val) 0 1))


(defn handle-opcode [cpu opcode]
  (let [first (subs opcode 0)
        rest (subs opcode 1 4)]
    (case first
      "0" (case rest
            "0E0" (println "clear screen")
            "0EE" (op-ops/return-from-subroutine cpu)
            ("call RCA 1802 program at address NNN"))
      "1" (op-ops/jump cpu rest)
      "2" (op-ops/call-subroutine cpu rest)
      "3" (let [vx (subs opcode 1 2)
                val (subs opcode 2 4)]
            (op-ops/skip-if-eq cpu vx val))
      "4" (let [vx (subs opcode 1 2)
                val (subs opcode 2 4)]
            (op-ops/skip-if-not-eq cpu vx val))
      "5" (let [vx (subs opcode 1 2)
                vy (subs opcode 2 3)]
            (op-ops/skip-if-vx-vy cpu vx vy))
      "6" (let [vx (subs opcode 1 2)
                val (subs opcode 2 4)]
            (op-ops/vreg-set cpu vx val))
      "7" (let [vx (subs opcode 1 2)
                vx-val (op-ops/vreg-get cpu vx)
                val (subs opcode 2 4)]
            (op-ops/vreg-set cpu vx (+ vx val)))
      "8" (let [last (subs opcode 3 4)
                vx (subs opcode 1 2)
                vy (subs opcode 2 3)
                vx-val (op-ops/vreg-get cpu vx)
                vy-val (op-ops/vreg-get cpu vy)]
            (case last
              "0" (op-ops/vreg-set cpu vx vy-val)
              "1" (op-ops/vreg-set cpu vx (bit-or vx-val vy-val))
              "2" (op-ops/vreg-set cpu vx (bit-and vx-val vy-val))
              "3" (op-ops/vreg-set cpu vx (bit-xor vx-val vy-val))
              "4" (let [added-val (+ vx-val vy-val)]
                    (if (> added-val 255)
                      (-> cpu
                          (op-ops/vreg-set vx (remove-carry added-val))
                          (op-ops/vreg-set "F" "1"))
                      (-> cpu
                          (op-ops/vreg-set vx added-val)
                          (op-ops/vreg-set "F" "0"))))
              "5" (let [subbed-val (- vx-val vy-val)]
                    (if (> vx-val vy-val)
                      (-> cpu
                          (op-ops/vreg-set "F" "1")
                          (op-ops/vreg-set vx subbed-val))
                      (-> cpu
                          (op-ops/vreg-set "F" "0")
                          (op-ops/vreg-set vx subbed-val))))
              "6" (let [vx-lsb (get-lsb vx-val)]
                    (if (= vx-lsb "1")
                      (-> cpu
                          (op-ops/vreg-set "F" "1")
                          (op-ops/vreg-set vx (bit-shift-right vx-val 1)))
                      (-> cpu
                          (op-ops/vreg-set "F" "0")
                          (op-ops/vreg-set vx (bit-shift-right vx-val 1)))))
              "7" (let [subbed-val (- vy-val vx-val)]
                    (if (> vy-val vx-val)
                      (-> cpu
                          (op-ops/vreg-set "F" "1")
                          (op-ops/vreg-set vx subbed-val))
                      (-> cpu
                          (op-ops/vreg-set "F" "0")
                          (op-ops/vreg-set vx subbed-val))))
              "E" (let [msb (get-msb vx-val)]
                    (do
                      (op-ops/vreg-set cpu vx (bit-shift-left vx-val 1))
                      (if (= msb "1")
                        (op-ops/vreg-set cpu "F" "1")
                        (op-ops/vreg-set cpu "F" "0"))))))
      "9" (let [vx (subs opcode 1 2)
                vy (subs opcode 2 3)
                vx-val (op-ops/vreg-get cpu vx)
                vy-val (op-ops/vreg-get cpu vy)]
            (if (not= vx-val vy-val)
              (op-ops/skip-instruction cpu)
              cpu))
      "A" (op-ops/ireg-set cpu rest)
      "B" (op-ops/vreg-set cpu 0 rest)
      "C" (let [register (subs opcode 1 2)
                kk (subs opcode 2 4)]
            (op-ops/vreg-set cpu register (bit-and kk (rand-int 256))))
      "D" (println "Sprites stored in memory at location in index register (I), maximum 8bits wide. Wraps around the screen. If when drawn, clears a pixel, register VF is set to 1 otherwise it is zero. All drawing is XOR drawing (i.e. it toggles the screen pixels)")
      "E" (let [last-two (subs opcode 2 4)]
            (case last-two
              "9E" (println "Skips the next instruction if the key stored in VX is pressed.")
              "A1" (println "Skips the next instruction if the key stored in VX isn't pressed.")))
      "F" (let [last-two (subs opcode 2 4)]
            (case last-two
              "07" (let [vx (subs opcode 1 2)]
                        (op-ops/vreg-set cpu vx (:delay-timer cpu)))
              "0A" (println "A key press is awaited, and then stored in VX.")
              "15" (let [vx (subs opcode 1 2)
                         vx-val (op-ops/vreg-get cpu vx)]
                     (op-ops/set-delay-t cpu vx-val))
              "18" (println "Sets the sound timer to VX.")
              "1E" (println "Adds VX to I.")
              "29" (println "Sets I to the location of the sprite for the character in VX. Characters 0-F (in hexadecimal) are represented by a 4x5 font.")
              "33" (println "Stores the Binary-coded decimal representation of VX, with the most significant of three digits at the address in I, the middle digit at I plus 1, and the least significant digit at I plus 2. (In other words, take the decimal representation of VX, place the hundreds digit in memory at location in I, the tens digit at location I+1, and the ones digit at location I+2.)")
              "55" (println "Stores V0 to VX in memory starting at address I.")
              "65" (println "Fills V0 to VX with values from memory starting at address I"))))))
