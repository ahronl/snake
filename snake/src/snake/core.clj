(ns snake.core
  (:require
   [snake.ui :as ui])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (ui/game)
)
