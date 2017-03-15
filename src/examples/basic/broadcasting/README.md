Basic broadcasting
==================

Implementation of a basic broadcasting algorithm

    1. [The user add some nodes]
    2. The user selects the initiator (middle click on a node)
    3. This node sends a message to all neighbors
    4. Upon first reception, a node retransmits

The user can restart the process (right click, restart),
which re-executes the onStart() method on each node.
