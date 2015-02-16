(ns chip8.cpu-spec
    (:require [speclj.core :refer :all]
              [chip8.cpu :refer :all]))

(describe "chip8 cpu tests"
  (it "should change the opcode to a specific value"
      (let [cpu (build-cpu)
            cpu-updated-opcode (change-opcode cpu "0x1234")]
          (should= "0x1234" (:opcode cpu-updated-opcode))))

  (it "should decrement the delay timer"
      (let [cpu (set-timer (build-cpu) "delay-timer" 5)
            timer-dec-cpu (dec-timer cpu "delay-timer")]
        (should= 4 (:delay-timer timer-dec-cpu))))

  (it "should decrement the sound timer"
      (let [cpu (set-timer (build-cpu) "sound-timer" 5)
            timer-dec-cpu (dec-timer cpu "sound-timer")]
        (should= 4 (:sound-timer timer-dec-cpu))))

  (it "should decrement both timers"
      (let [cpu (-> (build-cpu)
                          (set-timer "delay-timer" 5)
                          (set-timer "sound-timer" 3))
            timer-dec-cpu (dec-timers cpu)]
        (should= 4 (:delay-timer timer-dec-cpu))
        (should= 2 (:sound-timer timer-dec-cpu))))

  (it "should decrement delay timer"
      (let [cpu (-> (build-cpu)
                          (set-timer "delay-timer" 5))
            timer-dec-cpu (dec-timers cpu)]
        (should= 4 (:delay-timer timer-dec-cpu))
        (should= 0 (:sound-timer timer-dec-cpu))))

  (it "should decrement delay timer"
      (let [cpu (-> (build-cpu)
                          (set-timer "sound-timer" 5))
            timer-dec-cpu (dec-timers cpu)]
        (should= 0 (:delay-timer timer-dec-cpu))
        (should= 4 (:sound-timer timer-dec-cpu))))

  (it "should not decrement timer"
      (let [cpu (-> (build-cpu))
            timer-dec-cpu (dec-timers cpu)]
        (should= 0 (:delay-timer timer-dec-cpu))
        (should= 0 (:sound-timer timer-dec-cpu))))

  (it "should insert item into memory"
      (let [cpu (build-cpu)
            cpu-with-mem (mem-insert cpu 3 123)]
        (should= 123 (get (:memory cpu-with-mem) 3))))

  (it "should get from memory"
      (let [cpu (build-cpu)
            cpu-with-mem (mem-insert cpu 3 123)]
        (should= 123 (mem-get cpu-with-mem 3))))

  (it "should get a range from memory"
      (let [cpu (-> (build-cpu)
                    (mem-insert 0 0)
                    (mem-insert 1 1)
                    (mem-insert 2 2))]
        (should= [0 1 2] (mem-get cpu 0 2))))

  (it "should get next opcode"
      (let [basic-cpu (-> (build-cpu)
                          (mem-insert 512 0xa2)
                          (mem-insert 513 0xf0))]
        (should= 41712 (get-next-opcode basic-cpu))))

  (it "should increment the program counter (pc)"
      (let [cpu (build-cpu)
            cpu-inc-pc (pc-inc cpu)]
        (should= 514 (:pc cpu-inc-pc))))

  (it "should set the program counter (pc)"
      (let [cpu (build-cpu)
            cpu-set-pc (pc-set cpu 600)]
        (should= 600 (:pc cpu-set-pc))))

  (it "should push onto the stack"
      (let [cpu (build-cpu)
            cpu-added-stack (-> cpu
                              (stack-push 123)
                              (stack-push 456))]
        (should= '(456 123) (:stack cpu-added-stack))))

  (it "should pop from the stack"
      (let [cpu (-> (build-cpu)
                         (stack-push 123)
                         (stack-push 456))
            cpu-stack-popped (stack-pop cpu)]
        (should= '(123) (:stack cpu-stack-popped))))

  (it "should increment the stack pointer"
      (let [cpu (build-cpu)
            cpu-sp-inc (sp-inc cpu)]
        (should= 1 (:sp cpu-sp-inc))))

  (it "should decrement the stack pointer"
      (let [cpu (-> (build-cpu)
                    (sp-inc)
                    (sp-inc))
            cpu-sp-dec (sp-dec cpu)]
        (should= 1 (:sp cpu-sp-dec))))

  (it "should set the stack pointer"
      (let [cpu (build-cpu)
            cpu-sp-set (sp-set cpu 7)]
        (should= 7 (:sp cpu-sp-set))))

  (it "should set a V register"
      (let [cpu (build-cpu)
            cpu-vreg-set (-> cpu
                             (vreg-set 4 123)
                             (vreg-set 7 456))]
        (should= 123 (get (:Vreg cpu-vreg-set) 4))
        (should= 456 (get (:Vreg cpu-vreg-set) 7))))

  (it "should get a V register's value"
      (let [cpu (-> (build-cpu)
                    (vreg-set 4 123))]
        (should= 123 (vreg-get cpu 4))))

  (it "should get a subset of the V registers"
      (let [cpu (-> (build-cpu)
                    (vreg-set 0 1)
                    (vreg-set 1 2)
                    (vreg-set 2 3))
            vregs (vreg-get cpu 0 2)]
        (should= [1 2 3] vregs)))

  (it "should set the I register"
      (let [cpu (build-cpu)
            cpu-ireg-set (ireg-set cpu 123)]
        (should= 123 (:Ireg cpu-ireg-set))))

  (it "should get the value from the I register"
      (let [cpu (-> (build-cpu)
                    (ireg-set 123))]
        (should= 123 (ireg-get cpu)))))

(run-specs)
