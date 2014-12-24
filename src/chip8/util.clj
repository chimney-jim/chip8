(ns chip8.util)

(defn sized-vec
  ([size] (vec (repeat size nil)))
  ([size value] (vec (repeat size value))))
