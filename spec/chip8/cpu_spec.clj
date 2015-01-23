(ns chip8.cpu-spec
    (:require [speclj.core :refer :all]
              [chip8.cpu :as cpu]))

(describe "chip8 cpu tests"
  (it "should change the opcode to a specific value"
      (let [basic-cpu (cpu/build-cpu)
            cpu (cpu/change-opcode basic-cpu "0x1234")]
          (should= "0x1234" (:opcode cpu))))
  
  (it "should decrement the delay timer"
      (let [basic-cpu (cpu/set-timer (cpu/build-cpu) "delay-timer" 5)
            timer-dec-cpu (cpu/dec-timer basic-cpu "delay-timer")]
        (should= 4 (:delay-timer timer-dec-cpu))))

  (it "should decrement the sound timer"
      (let [basic-cpu (cpu/set-timer (cpu/build-cpu) "sound-timer" 5)
            timer-dec-cpu (cpu/dec-timer basic-cpu "sound-timer")]
        (should= 4 (:sound-timer timer-dec-cpu))))
  
  (it "should decrement both timers"
      (let [basic-cpu (-> (cpu/build-cpu)
                          (cpu/set-timer "delay-timer" 5)
                          (cpu/set-timer "sound-timer" 3))
            timer-dec-cpu (cpu/dec-timers basic-cpu)]
        (should= 4 (:delay-timer timer-dec-cpu))
        (should= 2 (:sound-timer timer-dec-cpu))))
)

(run-specs)