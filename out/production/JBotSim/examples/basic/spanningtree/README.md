Basic broadcasting
==================

Implementation of a basic spanning tree algorithm

    1. [The user add some nodes]
    2. The user selects the root (middle click on a node)
    3. This node sends a message to all neighbors
    4. Upon first reception, take sender as parent and retransmits

The user can restart the process (right click, restart),
which re-executes the onStart() method on each node as well as on the
centralized StartListener from the main method (for reinitializing link width).
