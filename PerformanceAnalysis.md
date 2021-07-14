# Overview

This document analysis the performance of this application from both time consumption and memory consumption.

# Code Analysis

### Time complexity

The current implementation relies on the HashMap to work as index, both PUT and GET operations of HashMap is O(1), thus search response
times should not increase linearly as the number of documents grows.

### Memory Usage

#### File Processing

The GSON stream reader is used to parse JSON file, the streams operate on one token at a time, thus impose minimal memory
overhead(https://sites.google.com/site/gson/streaming). Thus file parsing shouldn't introduce high memory usage.

#### Index

There are 3 separate indexes for Users, Tickets and Organizations in this implementation. The indexes store the occurrence information of
all values of all fields and is kept in memory, thus it may lead to high memory usage.

# Performance Test

To test the performance, I created some big JSON files
using [PressureTest](src/test/java/com/zendesk/datasearcher/searcher/PressureTest.java). The files that are generated are:

* pressure-test-tickets.json: contains 1 million records, about 539MB.
* pressure-test-users.json: contains 100k records, about 67MB
* pressure-test-organizations.json: contains 50k records, about 21MB.

I ran the jar with JVM option `JAVA_OPTS="-Xms1G -Xmx2G" `, and check how long does it take to finish the search operation.

I firstly search on the tickets.tags field and this run takes 42.692 seconds to complete.(If I run the jar with `-Xms2g -Xmx4g`, the first
run takes 38.570 s) \
This is because all 3 indexes are built in this stage, which is time-consuming.

```
2021-07-05T23:23:30.678+1000 INFO No available data for searched term tags with value '238g7r'.
2021-07-05T23:23:30.679+1000 DEBUG Search takes 42692 ms
```

Then I search on the same field again, but this time is much faster - 0ms. Which make sense, because the indexes is fully ready to use and
GET operation of HashMap is O(1) time complexity.

```
2021-07-05T23:24:06.625+1000 INFO No available data for searched term tags with value '3rg827'.
2021-07-05T23:24:06.625+1000 DEBUG Search takes 0 ms
```

Search on organization._id is also fast - 1ms:

```
2021-07-05T23:24:41.831+1000 INFO Found 1 Organizations whose id is 4:
2021-07-05T23:24:41.832+1000 DEBUG Search takes 1 ms
```

Therefore, we can draw the conclusion that once the index has been built, search time will be acceptable even if the data size is big.

# JVM Memory Usage Investigation

I used the Eclipse Memory Analyzer tool to acquire the Heap Dump and then analysis the dump file.

The `com.zendesk.datasearcher.model.InvertedIndex` takes the most memory, as shown in the top consumers pie chart:
![Biggest Objects](screenshot/performance/top-consumers.png?raw=true)

According to the dominant tree, 99.74% of the memory was allocated to the inverted index, the 3 big HashMap (which are the index for 3 data
sets) and and the 3 big ArrayList (which store all entities parsed from the JSON file) take the majority of memory.
![Dominant Tree](screenshot/performance/dominant-tree.png?raw=true)

# Conclusion

Given the current implementation, all entities parsed from the JSON file and all 3 inverted indexes are all maintained in the memory, which
leads to the high memory usage. We need to allocate higher memory to the JVM if large file processing is required.

In terms of the time consumption, the first search will take longer as the indexes are built in this stage. Once the indexes are ready to
use, the following search will be fast, no matter how big is the data set. 