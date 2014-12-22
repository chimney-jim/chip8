(ns chip8.cpu)

(defrecord CPU [opcode memory Vreg Ireg pc gfx delay-timer sound-timer stack sp key])

(defn build-cpu []
  (map->CPU {:opcode nil,
             :memory (sized-vec 4096),
             :Vreg (sized-vec 16),
             :Ireg nil,
             :pc nil,
             :gfx nil,
             :delay-timer nil,
             :sound-timer nil,
             :stack (sized-vec 16),
             :sp nil,
             :key (sized-vec 16)}))

(defn change-opcode [cpu opcode]
  "put new opcode in cpu"
  (-> cpu
      (assoc :opcode opcode)))

(defn mem-insert [cpu position value]
  "insert value into memory"
  (-> cpu
      (assoc :memory (assoc (:memory cpu) position value))))

(defn sized-vec
  ([size] (vec (repeat size nil)))
  ([size value] (vec (repeat size value))))
