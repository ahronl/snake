(ns snake.game)

(def width 75)
(def height 50)
(def win-lenght 5)

(defn- add-points [& pts]
  (vec (apply map + pts)))

(defn- gen-apple []
  {:location [(rand-int width) (rand-int height)]
   :color {:r 210 :g 50 :b 90}
   :type :apple})

(defn create-apple []
  (ref (gen-apple)))

(defn- gen-snake []
  {:body '([1 1])
   :dir [1 0]
   :color {:r 15 :g 160 :b 70}
   :type :snake})

(defn create-snake []
  (ref (gen-snake)))

(defn make []
  {:snake (create-snake)
   :apple (create-apple)})

(defn- move [{:keys [body dir] :as snake} & grow]
  (assoc snake :body
         (cons (add-points (first body) dir)
               (if grow body (butlast body)))))

(defn win? [{snake :snake}]
  (let [body (:body @snake)]
    (>= (count body) win-lenght)))

(defn- head-overlaps-body? [game]
  (let [body (:body @(:snake game))
        head (first body)
        rb (rest body)]
    (contains? (set rb) head)))

(def lose? head-overlaps-body?)

(defn out-of-frame? [{:keys [snake]}]
  (let [body (:body @snake)
        head (first body)
        [w h] head]
    (or (or (< width w) (< w 0)) (or (< height h) (< h 0)))))

(defn- eats? [{[snake-head] :body} {apple :location}]
  (= snake-head apple))

(defn- turn [snake newdir]
  (assoc snake :dir newdir))

(defn reset-game [{:keys [snake apple]}]
  (dosync
   (ref-set apple (gen-apple))
   (ref-set snake (gen-snake)))
  nil)

(defn update-direction [game newdir]
  (let [snake (:snake game)]
    (when newdir (dosync (alter snake turn newdir)))))

(defn update-positions [{:keys [apple snake]}]
  (dosync
   (if (eats? @snake @apple)
     (do
       (ref-set apple (gen-apple))
       (alter snake move :grow))
     (alter snake move)))
  nil)