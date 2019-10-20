(ns snake.ui
  (:import (java.awt Color Dimension)
           (javax.swing JPanel JFrame Timer JOptionPane)
           (java.awt.event ActionListener KeyListener))
  (:use snake.import-static)
  (:require [snake.game :as game]))
(import-static java.awt.event.KeyEvent VK_LEFT VK_RIGHT VK_UP VK_DOWN)

(def dirs
  {VK_LEFT [-1 0]
   VK_RIGHT [1 0]
   VK_UP [0 -1]
   VK_DOWN [0 1]})

(def point-size 10)

(defn- point-to-screen-rect [pt]
  (map #(* % point-size) [(pt 0) (pt 1) 1 1]))


(defn- fill-point [g pt color]
  (let [[x y width height] (point-to-screen-rect pt)
        c (Color. (:r color) (:g color) (:b color))]
    (.setColor g c)
    (.fillRect g x y width height)))

(defmulti paint (fn [g object & _] (:type object)))

(defmethod paint :apple [g {:keys [location color]}]
  (fill-point g location color))

(defmethod paint :snake [g {:keys [body color]}]
  (doseq [point body]
    (fill-point g point color)))

(defn- paint-game [g {:keys [snake apple]}]
  (do (paint g @apple)
      (paint g @snake)))

(defn- game-panel [frame game]
  (proxy [JPanel ActionListener KeyListener] []
    (paintComponent [g]
      (proxy-super paintComponent g)
      (paint-game g game))
    (actionPerformed [e]
      (game/update-positions game)
      (when (game/lose? game)
        (game/reset-game game)
        (JOptionPane/showMessageDialog frame "You lose!"))
      (when (game/win? game)
        (game/reset-game game)
        (JOptionPane/showMessageDialog frame "You win!"))
      (.repaint this))
    (keyPressed [e]
      (game/update-direction game (dirs (.getKeyCode e))))
    (getPreferredSize []
      (Dimension. (* (inc game/width) point-size)
                  (* (inc game/height) point-size))) (keyReleased [e])
    (keyTyped [e])))

(def turn-millis 75)

(defn game []
  (let [game  (game/make)
        frame (JFrame. "Snake")
        panel (game-panel frame game)
        timer (Timer. turn-millis panel)]
    (doto panel
      (.setFocusable true)
      (.addKeyListener panel))
    (doto frame
      (.add panel)
      (.pack)
      (.setVisible true)
      (.setDefaultCloseOperation JFrame/EXIT_ON_CLOSE))
    (.start timer)
    [(game :snake), (game :apple), timer]))