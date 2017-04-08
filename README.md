LRU
== 

A simple scala based LRU with a home brew linked list (why not).

The LRU uses a hash lookup of keys to nodes and leverages the node
accesses in the linked list to manage the cache size.  This means
we can do O(1) drop, add, insert by trading for space.  

BinPackers
==

Also includes a simple bit set packer. Answers the question of "is this position set".

Good for storing binary answers of large 2d sets