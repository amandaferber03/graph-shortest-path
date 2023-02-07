# Homework 8

## Discussion
The finding that initially caught my eye was that fact that each set of coordinates took a surprisingly similar 
amount of time to execute. Finding the shortest path coordinate pair hovered between 33 and 36 milliseconds. These 
findings are not outliers as non-outlier results all were within 1-5 milliseconds of each other. Similarly, the 
network loading of the baltimore streets map took around 105 milliseconds, but this similarlity is expected as
all paths and all coordinate pairs are found on the same graph. I was especially suprised by this finding: 
findingv the shortest path between coordinate set #3, which was 16570.49 meters, took just as long as finding the 
shortest path between coordinate set #2, which was only 5827.37 meters. This may be a testament to how powerful the use
of HashSets and HashMaps are in making complex algorithms more efficient. Because HashTables can access any element
in an amortized complexity O(1), the size of the data structure does not have as much bearing when performing operations
involving its elements. From accessing a graph's vertices to ensuring that an insertion or deletion will still result
in a valid graph, the use of a hashtable when implementing the Graph ADT made the size of the data 
structure less of a factor in determining the performance of an algorithm. Similarly, the use of the Priority Queue 
allowed us to avoid iterating through every unexplored vertex in a graph because of its heap property, allowing us to 
always pursue the next explored node that contributed the least distance to the path. 

Coincidentally, I did end up running JMHRuntime test but, to try to save the time I needed to write this reflection, 
I mistakenly thought that running the test using the Campus Paths graph would result in a shorter running time. However, 
as I began to realize that the data structures used in my implementation improved speed, it became clear that the 
Baltimore Streets map would require as much time as the Campus Paths map to run the test. After 25 minutes, it was 
shown that gc.alloc rate for JHU to Druid Lake was 52.681 MB/sec, 7-11 to Druid Lake was 69.959  MB/sec, and 
Inner Harbor to JHU was 57.673  MB/sec. 

It should be noted that there some outliers during the SystemRuntime tests that showed that coordinate set #2,
7-11 to Druid Lake, had the longest running time, even though the shortest path had the smallest distance. This 
may means that there were many possible paths to choose from. 


Below is the output for the SystemRuntime and Memory Monitor Tests for each of the coordinate pairs:
#1
Output for Coordinates [-76.6175,39.3296 to -76.6383,39.3206]
Driver: Shortest Path was 8818.52
SystemRuntimeTest
Config: baltimore.streets.txt from -76.6175,39.3296 to -76.6383,39.3206
Loading network took 103 milliseconds.
Finding shortest path took 36 milliseconds

MemoryMonitorTest
Config: baltimore.streets.txt from -76.6175,39.3296 to -76.6383,39.3206
Used memory: 6265.69 KB (Δ = 0.000)
Instantiating empty Graph data structure
Instantiating empty StreetSearcher object
Used memory: 6416.87 KB (Δ = 151.180)
Loading the network
Used memory: 18788.69 KB (Δ = 12371.820)
Finding the shortest path
Used memory: 19948.23 KB (Δ = 1159.547)
Setting objects to null (so GC does its thing!)
Used memory: 8984.23 KB (Δ = -10964.008)

#2
Output for Coordinates [-76.6214,39.3212 to -76.6383,39.3206]
Driver: Shortest Path was 5827.37
SystemRuntimeTest
Config: baltimore.streets.txt from -76.6214,39.3212 to -76.6383,39.3206
Loading network took 107 milliseconds.
Finding shortest path took 35 milliseconds

MemoryMonitorTest
Config: baltimore.streets.txt from -76.6214,39.3212 to -76.6383,39.3206
Used memory: 6295.52 KB (Δ = 0.000)
Instantiating empty Graph data structure
Instantiating empty StreetSearcher object
Used memory: 6492.53 KB (Δ = 197.016)
Loading the network
Used memory: 18691.75 KB (Δ = 12199.219)
Finding the shortest path
Used memory: 19874.13 KB (Δ = 1182.383)
Setting objects to null (so GC does its thing!)
Used memory: 8918.63 KB (Δ = -10955.508)

#3
Output for Coordinates [-76.6107,39.2866 to -76.6175,39.3296]
Driver: Shortest Path was 16570.49
SystemRuntimeTest
Config: baltimore.streets.txt from -76.6107,39.2866 to -76.6175,39.3296
Loading network took 101 milliseconds.
Finding shortest path took 33 milliseconds

MemoryMonitorTest.
Config: baltimore.streets.txt from -76.6107,39.2866 to -76.6175,39.3296
Used memory: 6297.02 KB (Δ = 0.000)
Instantiating empty Graph data structure
Instantiating empty StreetSearcher object
Used memory: 6489.62 KB (Δ = 192.602)
Loading the network
Used memory: 18785.86 KB (Δ = 12296.242)
Finding the shortest path
Used memory: 19910.31 KB (Δ = 1124.453)
Setting objects to null (so GC does its thing!)
Used memory: 8953.05 KB (Δ = -10957.258)
