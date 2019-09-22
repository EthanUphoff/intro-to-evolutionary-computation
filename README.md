# intro-to-evolutionary-computation
Code and text introducing folks to the basic ideas of evolutionary computation

# How to Run
1. `cd intro-to-ec`
2. `lein repl`
3. `(require '[intro-to-ec.heuristic-search :as s])`
4. `(require '[intro-to-ec.grid-problem-with-walls :as grid])`
5. `(s/search s/<Search Algorithm> (grid/make-grid-problem <Lower Bound> <Upper Bound> <Walls> grid/<heurisitic search>) <Starting Node> <Max Calls>)`