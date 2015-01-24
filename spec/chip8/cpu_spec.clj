(ns chip8.cpu-spec
    (:require [speclj.core :refer :all]
              [chip8.cpu :refer :all]))

(describe "chip8 cpu tests"
  (it "should change the opcode to a specific value"
      (let [base-cpu (build-cpu)
            cpu (change-opcode base-cpu "0x1234")]
          (should= "0x1234" (:opcode cpu))))
  
  (it "should decrement the delay timer"
      (let [base-cpu (set-timer (build-cpu) "delay-timer" 5)
            timer-dec-cpu (dec-timer base-cpu "delay-timer")]
        (should= 4 (:delay-timer timer-dec-cpu))))

  (it "should decrement the sound timer"
      (let [base-cpu (set-timer (build-cpu) "sound-timer" 5)
            timer-dec-cpu (dec-timer base-cpu "sound-timer")]
        (should= 4 (:sound-timer timer-dec-cpu))))
  
  (it "should decrement both timers"
      (let [base-cpu (-> (build-cpu)
                          (set-timer "delay-timer" 5)
                          (set-timer "sound-timer" 3))
            timer-dec-cpu (dec-timers base-cpu)]
        (should= 4 (:delay-timer timer-dec-cpu))
        (should= 2 (:sound-timer timer-dec-cpu))))  
  
  (it "should decrement delay timer"
      (let [base-cpu (-> (build-cpu)
                          (set-timer "delay-timer" 5))
            timer-dec-cpu (dec-timers base-cpu)]
        (should= 4 (:delay-timer timer-dec-cpu))
        (should= 0 (:sound-timer timer-dec-cpu))))  
  
  (it "should decrement delay timer"
      (let [base-cpu (-> (build-cpu)
                          (set-timer "sound-timer" 5))
            timer-dec-cpu (dec-timers base-cpu)]
        (should= 0 (:delay-timer timer-dec-cpu))
        (should= 4 (:sound-timer timer-dec-cpu))))  
  
  (it "should not decrement timer"
      (let [base-cpu (-> (build-cpu))
            timer-dec-cpu (dec-timers base-cpu)]
        (should= 0 (:delay-timer timer-dec-cpu))
        (should= 0 (:sound-timer timer-dec-cpu))))
  
  (it "should insert item into memory"
      (let [base-cpu (build-cpu)
            cpu-with-mem (mem-insert base-cpu 3 123)]
        (should= 123 (get (:memory cpu-with-mem) 3))))
  
  (it "should get from memory"
      (let [base-cpu (build-cpu)
            cpu-with-mem (mem-insert base-cpu 3 123)]
        (should= 123 (mem-get cpu-with-mem 3))))
  
  (it "should get next opcode"
      (let [basic-cpu (-> (build-cpu)
                          (mem-insert 512 0xa2)
                          (mem-insert 513 0xf0))]
        (should= 41712 (get-next-opcode basic-cpu))))
)

(run-specs)