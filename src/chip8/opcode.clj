(ns chip8.opcode
  (:require [chip8.opcode-operations :as op-ops]))

(defn handle-opcode [cpu opcode]
  (let [first (subs opcode 0)
        rest (subs opcode 1 3)]
    (case first
      "0" (case rest
            "0E0" (println "clear screen")
            "0EE" (op-ops/return-from-subroutine cpu)
            ("call RCA 1802 program at address NNN"))
      "1" (op-ops/jump cpu rest)
      "2" (op-ops/call-subroutine cpu rest)
      "3" (op-ops/skip-if-eq)
      "4" (println "skips the next subroutine if VX doesn't equal NN")
      "5" (println "skips the next instruction if VX equals VY")
      "6" (println "Sets VX to NN")
      "7" (println "Adds NN to VX")
      "8" (let [last (opcode 4)]
            (case last
              "0" (println "set VX to VY")
              "1" (println "set VX to VX or VY")
              "2" (println "set VX to VX and VY")
              "3" (println "set VX to VX xor VY")
              "4" (println "adds VY to VX. VF is set to 1 when there's a carry, and to 0 when there isn't.")
              "5" (println "VY is subtracted from VX. VF is set to 0 when there's a borrow, and 1 when there isn't")
              "6" (println "Shifts VX right by one. VF is set to the value of the least significant bit of VX before the shift")
              "7" (println "Sets VX to VY minus VX. VF is set to 0 when there's a borrow, and 1 when there isn't.")
              "E" (println "Shifts VX left by one. VF is set to the value of the most significant bit of VX before the shift")))
      "9" (println "Skips the next instruction if VX doesn't equal VY.")
      "A" (println "Sets I to the address NNN.")
      "B" (println "Jumps to the address NNN plus V0.")
      "C" (println "Sets VX to a random number and NN.")
      "D" (println "Sprites stored in memory at location in index register (I), maximum 8bits wide. Wraps around the screen. If when drawn, clears a pixel, register VF is set to 1 otherwise it is zero. All drawing is XOR drawing (i.e. it toggles the screen pixels)")
      "E" (let [last-two (subs opcode 2 4)]
            (case last-two
              "9E" (println "Skips the next instruction if the key stored in VX is pressed.")
              "A1" (println "Skips the next instruction if the key stored in VX isn't pressed.")))
      "F" (let [last-two (subs opcode 2 4)]
            (case last-two
              "07" (println "Sets VX to the value of the delay timer.")
              "0A" (println "A key press is awaited, and then stored in VX.")
              "15" (println "Sets the delay timer to VX.")
              "18" (println "Sets the sound timer to VX.")
              "1E" (println "Adds VX to I.")
              "29" (println "Sets I to the location of the sprite for the character in VX. Characters 0-F (in hexadecimal) are represented by a 4x5 font.")
              "33" (println "Stores the Binary-coded decimal representation of VX, with the most significant of three digits at the address in I, the middle digit at I plus 1, and the least significant digit at I plus 2. (In other words, take the decimal representation of VX, place the hundreds digit in memory at location in I, the tens digit at location I+1, and the ones digit at location I+2.)")
              "55" (println "Stores V0 to VX in memory starting at address I.")
              "65" (println "Fills V0 to VX with values from memory starting at address I"))))))
