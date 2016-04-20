Write-behind cache was implemented usind queue that contains elements to write to storage. Every put operation adds each element to the queue first.
Then another thread goes through this queue and writes all elements to storage with some delay time.

LFU strategy implemented via additional class called CacheEntry that contains element itself and frequency of this element. 
Every put operation check if cache is full, and if it is then the element that is least frequently used is found. 

For concurrent cache I've used concurrent collections such as ConcurrentHashMap and LinkedBlockingQueue for cache and write-behind queue respectively.
Also ReentrantLock was added for additional syncronization. For testing this functionality there are two tests. 
One of tests puts elements to cache in three threads at the same time and then checks if all of them were cached.
Another test performs get operation in three threads of the same elements, then test checks if all the "gets" were considered.
