(ns chip8.opcode-operations-spec
  (:require [speclj.core :refer :all]
            [chip8.cpu :as cpu]
            [chip8.opcode-operations :refer :all]))

(describe "chip8 opcode operations test"
  (it "should set a V register given a hex code"
      (let [cpu (cpu/build-cpu)
            cpu-vreg-set (-> cpu
                             (vreg-set "04" "a2")
                             (vreg-set "07" "f0"))]
        (should= 162 (cpu/vreg-get cpu-vreg-set 4))
        (should= 240 (cpu/vreg-get cpu-vreg-set 7))))
  
  (it "should get the value from a V register"
      (let [cpu (-> (cpu/build-cpu)
                    (vreg-set "04" "a2"))]
        (should= 162 (vreg-get cpu "04"))))
  
  (it "should return from a subroutine"
      (let [cpu (-> (cpu/build-cpu)
                    (cpu/stack-push 123)
                    (cpu/stack-push 456)
                    (cpu/sp-set 1))
            cpu-after-sub (return-from-subroutine cpu)]
        (should= 456 (:pc cpu-after-sub))
        (should= '(123) (:stack cpu-after-sub))
        (should= 0 (:sp cpu-after-sub))))
  
  (it "should jump to address"
      (let [cpu (cpu/build-cpu)
            cpu-after-jump (jump cpu "2f0")]
        (should= 752 (:pc cpu-after-jump))))
  
  (it "should call a subroutine"
      (let [cpu (-> (cpu/build-cpu)
                    (cpu/pc-set 123))
            cpu-call-sub (call-subroutine cpu "2f0")]
        (should= 1 (:sp cpu-call-sub))
        (should= '(123) (:stack cpu-call-sub))
        (should= 752 (:pc cpu-call-sub))))
  
  (it "should skip if the value at Vx is equal to the value passed in"
      (let [cpu (-> (cpu/build-cpu)
                    (cpu/vreg-set 4 752))
            cpu-skipped (skip-if-eq cpu 4 "2f0")]
        (should= 514 (:pc cpu-skipped))))

  (it "should not skip if the value at Vx is not equal to the value passed in"
      (let [cpu (-> (cpu/build-cpu)
                    (cpu/vreg-set 4 752))
            cpu-not-skipped (skip-if-eq cpu 4 "2f")]
        (should= 512 (:pc cpu-not-skipped))))
  
  (it "should skip if the value at Vx is not equal to the val passed in"
      (let [cpu (-> (cpu/build-cpu)
                    (cpu/vreg-set 4 752))
            cpu-skipped (skip-if-not-eq cpu 4 "2f")]
        (should= 514 (:pc cpu-skipped))))
  
  (it "should not skip if the value at Vx is equal to the val passed in"
      (let [cpu (-> (cpu/build-cpu)
                    (cpu/vreg-set 4 752))
            cpu-not-skipped (skip-if-not-eq cpu 4 "2f0")]
        (should= 512 (:pc cpu-not-skipped))))
  
  (it "should skip if the value in Vx and the value in Vy are equal"
      (let [cpu (-> (cpu/build-cpu)
                    (cpu/vreg-set 4 123)
                    (cpu/vreg-set 7 123))
            cpu-skipped (skip-if-vx-vy-eq cpu 4 7)]
        (should= 514 (:pc cpu-skipped))))
  
  (it "should not skip if Vx and Vy are not equal"
      (let [cpu (-> (cpu/build-cpu)
                    (cpu/vreg-set 4 123)
                    (cpu/vreg-set 7 456))
            cpu-not-skipped (skip-if-vx-vy-eq cpu 4 7)]
        (should= 512 (:pc cpu-not-skipped))))
  
  (it "should skip an instruction"
      (let [cpu (cpu/build-cpu)
            cpu-skipped (skip-instruction cpu)]
        (should= 514 (:pc cpu-skipped))))
  
;  (it "should set the I register"
;      (let [cpu (cpu/build-cpu)
;            cpu-ireg-set (ireg-set )]))
)