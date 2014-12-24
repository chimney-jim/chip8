(ns chip8.util)

(defn sized-vec
  ([size] (vec (repeat size nil)))
  ([size val] (vec (repeat size val))))

(defn dim-vec
  ([x y] (vec (repeat x (vec (repeat y nil)))))
  ([x y val] (vec (repeat x (vec (repeat y val))))))
