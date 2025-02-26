(ns intro-to-ec.grid-problem-with-walls)

(defn origin-goal?
  "A goal checking function that assumes the target
   position is the origin, i.e., [0 0]."
  [[x y]]
  (and (zero? x) (zero? y)))

;; The possible moves in this lattice world. Each
;; move is represented by a vector indicating the
;; change in both x and y coordinates associated
;; with this move.
(def up    [0  1])
(def down  [0 -1])
(def left  [-1  0])
(def right [1  0])
(def all-moves [up down left right])

(defn apply-move
  "Apply a move to a given position, yielding the new position"
  [position move]
  (vec (map + position move)))

(defn legal-coordinate
  "Limit our search to the space where the given coordinate
   is in the range [0, max-range)."
  [min-range max-range x]
  (and (>= x min-range) (< x max-range)))

(defn legal-state
  "Return true if both coordinates are legal and this position isn't
   in the 'wall' set."
  [min-range max-range wall-set position]
  (and (every? (partial legal-coordinate min-range max-range) position)
       (not (contains? wall-set position))))

(defn grid-children
  "Generate a list of all the possible child states
   reachable from the given initial position."
  [min-range max-range wall-set position]
  (filter (partial legal-state min-range max-range wall-set)
          (map (partial apply-move position) all-moves)))

(def min-range -10)
(def max-range 10)
(def no-walls #{})
(def low-vertical-wall
  (set
   (for [y (range -2 8)]
     [1 y])))
(def u-shape
  (set
   (concat
    (for [x (range -5 3)]
      [x 1])
    (for [x (range -5 3)]
      [x -1])
    [[2 0]])))

(defn heuristic 
  [a & args]
(+ (Math/abs (first a)) (Math/abs (second a)))
  )

(defn even-heuristic
  "This heuristic adds a cost of 2 to each even y value"
  [a cost]
(if (even? (second a)) (+ (heuristic a) 2 cost) (+ (heuristic a) cost)))

(defn odd-and-even-heuristic
  "This heuristic avoids coorindates where x is even and y is odd"
  [a cost]
(if (and (even? (first a)) (odd? (second a))) (+ (heuristic a) 2 cost) (+ (heuristic a) cost)))

(defn reverse-astar-heuristic
  "This heuristic is reverse astar, it avoids the solution"
  [a cost]
(- (- 0 (heuristic a)) cost))

(defn random-heuristic
  "This heuristic adds a random value between 0 and 100 to the cost"
  [a cost]
(+ (heuristic a) (rand-int 100)))

(defn astar [a cost] (+ (heuristic a) cost))

(defn make-grid-problem
  "Create an instance of a simple problem of moving on a grid towards
   the origin. The ranges specify the bounds on the grid world, and the
   `wall-set` is a (possibly empty) set of positions that can't be entered
   or crossed."
  [min-range max-range wall-set search]
  {:goal? origin-goal?
   :make-children (partial grid-children min-range max-range wall-set)
   :heuristic search})
