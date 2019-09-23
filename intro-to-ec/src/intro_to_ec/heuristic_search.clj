(ns intro-to-ec.heuristic-search
  (:require [clojure.set :as cset]
    [shams.priority-queue :as pq]))

(defn remove-previous-states
  [new-states frontier visited]
  (remove (cset/union (set frontier) (set visited)) new-states))

(def depth-first-search
  {:get-next-node first
   :add-children concat})

(def breadth-first-search
  {:get-next-node first
   :add-children #(concat %2 %1)})

(def random-search
  {:get-next-node rand-nth
   :add-children concat})

(defn generate-path
  [came-from node]
  (if (= :start-node (get came-from node))
    [node]
    (conj (generate-path came-from (get came-from node)) node)))

;; Priority Queue
;; https://github.com/shamsimam/clj-priority-queue
(defn search
  [{:keys [get-next-node add-children]}
   {:keys [goal? make-children heuristic]}
   start-node max-calls]
  (loop [frontier [start-node]
         came-from {start-node :start-node}
         num-calls 0
         cost-so-far {start-node 0}]
    (println num-calls ": " frontier)
    (println came-from)
    (let [current-node (get-next-node frontier)]
      (cond
        (goal? current-node) (generate-path came-from current-node)
        (= num-calls max-calls) :max-calls-reached
        :else
        (let [kids (if (< (cost-so-far current-node) (cost-so-far (first frontier))) (= (cost-so-far (first frontier)) (cost-so-far current-node))
                    (remove-previous-states
                    (make-children current-node) frontier (keys came-from)))]
          (recur
            (reverse (pq/priority-queue #(heuristic % (if (nil? (get cost-so-far %)) (+ (get cost-so-far current-node) 1) (get cost-so-far %))) :elements 
              (add-children
            kids
            (rest frontier))))
           (reduce (fn [cf child] (assoc cf child current-node)) came-from kids)
           (inc num-calls)
          ;; this is for eventually adding to cost so far
          (reduce (fn [cost child] (assoc cost child (+ 1 (get cost-so-far current-node )))) cost-so-far kids)))))))