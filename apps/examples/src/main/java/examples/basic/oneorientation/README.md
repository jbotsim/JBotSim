Centralized construction of a 1-orientation
===========================================

The code does the following

    1. Add a command to the context menu for triggering the computation
    2. The orientation is computed for each connected component
    3. Each orientation is computed using a DFS (or BFS)
    4. Upon start, the nodes take their own ID as colors.

The fourth point is not directly related to orientation, 
but coloring algorithms based on an initial 1-orientation 
are a classical use case.
