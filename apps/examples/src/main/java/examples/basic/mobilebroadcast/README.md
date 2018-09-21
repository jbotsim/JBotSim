Mobile Broadcast
================

This example combines mobility and broadcasting.
Each node selects a direction at start time,
then in each step (i.e. onClock()), it moves in this direction (toroidally) and,
if informed, transmits to the surrounding neighborhood (possibly empty).

