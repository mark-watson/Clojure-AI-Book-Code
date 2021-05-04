(ns core-logic-examples-clj.core)

(use 'clojure.core.logic)

;;  (run* [q] (== q true))

(comment
  (run* [r] (fresh [x y] (== (lcons x (lcons y 'salad)) r)))
  )

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
