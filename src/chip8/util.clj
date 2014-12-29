(ns chip8.util)

(defn sized-vec
  ([size] (vec (repeat size nil)))
  ([size val] (vec (repeat size val))))

(defn dim-vec
  ([x y] (vec (repeat x (vec (repeat y nil)))))
  ([x y val] (vec (repeat x (vec (repeat y val))))))

(defn int->hex-str [val]
  "turn an int or long into a hex string without losing the leading zero"
  (let [hex-str (format "%X" val)]
    (if (< (.length hex-str) 2) (str "0" hex-str) hex-str)))
