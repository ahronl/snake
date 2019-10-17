(ns snake.game)

(def width 75)
(def height 50)
(def win-lenght 5)

(comment
  (add-points [10 10] [-1 0]) ==> [9 10])

(defn- add-points [& pts]
  (vec (apply map + pts)))


(comment (gen-apple))

(defn- gen-apple []
  {:location [(rand-int width) (rand-int height)]
   :color {:r 210 :g 50 :b 90}
   :type :apple})

(defn create-apple []
  (ref (gen-apple)))

(comment (create-snake))

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

(comment (move (gen-snake)) (move (gen-snake) :grow))

(defn- move [{:keys [body dir] :as snake} & grow]
  (assoc snake :body
         (cons (add-points (first body) dir)
               (if grow body (butlast body)))))

(comment (win? {:body [[1 1] [1 2] [1 3] [1 4] [1 5]]}) ==> true)

(defn win? [{body :body}]
  (>= (count body) win-lenght))

(defn- head-overlaps-body? [{[head & body] :body}]
  (contains? (set body) head))

(def lose? head-overlaps-body?)

(defn- eats? [{[snake-head] :body} {apple :location}]
  (= snake-head apple))

(comment (turn (gen-snake) [0 -1]))

(defn- turn [snake newdir]
  (assoc snake :dir newdir))

(defn reset-game [snake apple]
  (dosync
   (ref-set apple (gen-apple))
   (ref-set snake (gen-snake)))
  nil)

(defn update-direction [snake newdir]
  (when newdir (dosync (alter snake turn newdir))))


(defn update-positions [snake apple]
  (dosync
   (if (eats? @snake @apple)
     (do
       (ref-set apple (gen-apple))
       (alter snake move :grow))
     (alter snake move)))
  nil)

(comment
  (def test-snake (ref nil))
  (def test-apple (ref nil))

  (reset-game test-snake test-apple)
  (dosync (alter test-apple assoc :location [1 1]))
  (update-positions test-snake test-apple))