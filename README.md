# Calculate Region Size Function
## Description

This project provides a function that calculates the size of a Region in bytes.

For each input region name, the **CalculateRegionSizeFunction**:

- gets the Region
- invokes ObjectGraphSizer to calculate the size in bytes of the Region in that member
- invokes ObjectGraphSizer to create a histogram of the Region in that member
- writes the size and histogram in the server's log
- returns the size and histogram in a SingleMemberRegionSize

The **RegionObjectFilter**:

- implements ObjectFilter to determine which objects to include in and which to reject from the Region size

A **SingleMemberRegionSize** is created for each sized Region and returned to the client. It encapsulates:

- regionName
- size
- histogram

## Initialization
Modify the **GEODE** environment variable in the *setenv.sh* script to point to a Geode installation directory.
## Build
Build the Spring Boot Client Application and Geode Server Function and logger classes using gradle like:

```
./gradlew clean jar bootJar
```
## Run Example
### Start and Configure Locator and Servers
Start and configure the locator and 3 servers using the *startandconfigure.sh* script like:

```
./startandconfigure.sh
```
### Load Entries
Run the client to load N Trade instances into the PartitionedTrade and ReplicatedTrade Regions using the *runclient.sh* script like below.

The parameters are:

- operation (load-regions)
- number of entries (10000)

```
./runclient.sh load-regions 10000
```
### Calculate Region Size
Execute the **CalculateRegionSizeFunction** to calculate the region size and histogram using the *runclient.sh* script like below.

The parameters are:

- operation (calculate-region-size)
- region name (ReplicatedTrade,PartitionedTrade)

```
./runclient.sh calculate-region-size ReplicatedTrade,PartitionedTrade
```
### Shutdown Locator and Servers
Execute the *shutdownall.sh* script to shutdown the servers and locators like:

```
./shutdownall.sh
```
### Remove Locator and Server Files
Execute the *cleanupfiles.sh* script to remove the server and locator files like:

```
./cleanupfiles.sh
```
## Example Sample Output
### Start and Configure Locator and Servers
Sample output from the *startandconfigure.sh* script is:

```
./startandconfigure.sh 
1. Executing - start locator --name=locator

...............
Locator in <working-directory>/locator on xxx.xxx.x.xx[10334] as locator is currently online.
Process ID: 37314
Uptime: 16 seconds
Geode Version: 1.12.0
Java Version: 1.8.0_151
Log File: <working-directory>/locator/locator.log
JVM Arguments: <jvm-arguments>
Class-Path: <classpath>

Successfully connected to: JMX Manager [host=xxx.xxx.x.xx, port=1099]

Cluster configuration service is up and running.

2. Executing - set variable --name=APP_RESULT_VIEWER --value=any

Value for variable APP_RESULT_VIEWER is now: any.

3. Executing - configure pdx --read-serialized=true --disk-store=DEFAULT --auto-serializable-classes=.*

read-serialized = true
ignore-unread-fields = false
persistent = true
disk-store = DEFAULT
PDX Serializer = org.apache.geode.pdx.ReflectionBasedAutoSerializer
Non Portable Classes = [.*]
Cluster configuration for group 'cluster' is updated.

4. Executing - start server --name=server-1 --server-port=0 --statistic-archive-file=cacheserver.gfs --J=-Dgemfire.log-file=cacheserver.log --J=-Dgemfire.conserve-sockets=false

........
Server in <working-directory>/server-1 on xxx.xxx.x.xx[52917] as server-1 is currently online.
Process ID: 37320
Uptime: 6 seconds
Geode Version: 1.12.0
Java Version: 1.8.0_151
Log File: <working-directory>/server-1/cacheserver.log
JVM Arguments: <jvm-arguments>
Class-Path: <classpath>

5. Executing - start server --name=server-2 --server-port=0 --statistic-archive-file=cacheserver.gfs --J=-Dgemfire.log-file=cacheserver.log --J=-Dgemfire.conserve-sockets=false

.........
Server in <working-directory>/server-2 on xxx.xxx.x.xx[52939] as server-2 is currently online.
Process ID: 37322
Uptime: 7 seconds
Geode Version: 1.12.0
Java Version: 1.8.0_151
Log File: <working-directory>/server-2/cacheserver.log
JVM Arguments: <jvm-arguments>
Class-Path: <classpath>

6. Executing - start server --name=server-3 --server-port=0 --statistic-archive-file=cacheserver.gfs --J=-Dgemfire.log-file=cacheserver.log --J=-Dgemfire.conserve-sockets=false

........
Server in <working-directory>/server-3 on xxx.xxx.x.xx[52967] as server-3 is currently online.
Process ID: 37323
Uptime: 6 seconds
Geode Version: 1.12.0
Java Version: 1.8.0_151
Log File: <working-directory>/server-3/cacheserver.log
JVM Arguments: <jvm-arguments>
Class-Path: <classpath>

7. Executing - list members

Member Count : 4

  Name   | Id
-------- | ---------------------------------------------------------------
locator  | xxx.xxx.x.xx(locator:37314:locator)<ec><v0>:41000 [Coordinator]
server-1 | xxx.xxx.x.xx(server-1:37320)<v1>:41001
server-2 | xxx.xxx.x.xx(server-2:37322)<v2>:41002
server-3 | xxx.xxx.x.xx(server-3:37323)<v3>:41003

8. Executing - create region --name=PartitionedTrade --type=PARTITION_REDUNDANT_PERSISTENT

 Member  | Status | Message
-------- | ------ | ------------------------------------------------
server-1 | OK     | Region "/PartitionedTrade" created on "server-1"
server-2 | OK     | Region "/PartitionedTrade" created on "server-2"
server-3 | OK     | Region "/PartitionedTrade" created on "server-3"

Cluster configuration for group 'cluster' is updated.

9. Executing - create region --name=ReplicatedTrade --type=REPLICATE_PERSISTENT

 Member  | Status | Message
-------- | ------ | -----------------------------------------------
server-1 | OK     | Region "/ReplicatedTrade" created on "server-1"
server-2 | OK     | Region "/ReplicatedTrade" created on "server-2"
server-3 | OK     | Region "/ReplicatedTrade" created on "server-3"

Cluster configuration for group 'cluster' is updated.

10. Executing - list regions

List of regions
----------------
PartitionedTrade
ReplicatedTrade

11. Executing - deploy --jar=server/build/libs/server-0.0.1-SNAPSHOT.jar

 Member  |       Deployed JAR        | Deployed JAR Location
-------- | ------------------------- | ---------------------------------------------------------
server-1 | server-0.0.1-SNAPSHOT.jar | <working-directory>/server-1/server-0.0.1-SNAPSHOT.v1.jar
server-2 | server-0.0.1-SNAPSHOT.jar | <working-directory>/server-2/server-0.0.1-SNAPSHOT.v1.jar
server-3 | server-0.0.1-SNAPSHOT.jar | <working-directory>/server-3/server-0.0.1-SNAPSHOT.v1.jar

12. Executing - list functions

 Member  | Function
-------- | ---------------------------
server-1 | CalculateRegionSizeFunction
server-2 | CalculateRegionSizeFunction
server-3 | CalculateRegionSizeFunction

************************* Execution Summary ***********************
Script file: startandconfigure.gfsh

Command-1 : start locator --name=locator
Status    : PASSED

Command-2 : set variable --name=APP_RESULT_VIEWER --value=any
Status    : PASSED

Command-3 : configure pdx --read-serialized=true --disk-store=DEFAULT --auto-serializable-classes=.*
Status    : PASSED

Command-4 : start server --name=server-1 --server-port=0 --statistic-archive-file=cacheserver.gfs --J=-Dgemfire.log-file=cacheserver.log --J=-Dgemfire.conserve-sockets=false
Status    : PASSED

Command-5 : start server --name=server-2 --server-port=0 --statistic-archive-file=cacheserver.gfs --J=-Dgemfire.log-file=cacheserver.log --J=-Dgemfire.conserve-sockets=false
Status    : PASSED

Command-6 : start server --name=server-3 --server-port=0 --statistic-archive-file=cacheserver.gfs --J=-Dgemfire.log-file=cacheserver.log --J=-Dgemfire.conserve-sockets=false
Status    : PASSED

Command-7 : list members
Status    : PASSED

Command-8 : create region --name=PartitionedTrade --type=PARTITION_REDUNDANT_PERSISTENT
Status    : PASSED

Command-9 : create region --name=ReplicatedTrade --type=REPLICATE_PERSISTENT
Status    : PASSED

Command-10 : list regions
Status     : PASSED

Command-11 : deploy --jar=server/build/libs/server-0.0.1-SNAPSHOT.jar
Status     : PASSED

Command-12 : list functions
Status     : PASSED
```
### Load Entries
Sample output from the *runclient.sh* script is:

```
./runclient.sh load-regions 10000

> Task :client:bootRun

2020-06-16 14:33:17.573  INFO 66854 --- [           main] example.client.Client                    : Starting Client on ...
...
2020-06-16 14:33:20.531  INFO 66854 --- [           main] example.client.Client                    : Started Client in 3.568 seconds (JVM running for 4.198)
2020-06-16 14:33:20.532  INFO 66854 --- [           main] example.client.service.TradeService      : Putting 10000 trades of size 1024 bytes
2020-06-16 14:33:23.290  INFO 66854 --- [           main] example.client.service.TradeService      : Saved Trade(id=0, cusip=UNP, shares=41, price=531.30, createTime=1592354000532, updateTime=1592354000532)
2020-06-16 14:33:23.791  INFO 66854 --- [           main] example.client.service.TradeService      : Saved Trade(id=1, cusip=HSBC, shares=19, price=337.96, createTime=1592354003290, updateTime=1592354003290)
2020-06-16 14:33:23.965  INFO 66854 --- [           main] example.client.service.TradeService      : Saved Trade(id=2, cusip=COST, shares=57, price=27.56, createTime=1592354003791, updateTime=1592354003791)
2020-06-16 14:33:24.038  INFO 66854 --- [           main] example.client.service.TradeService      : Saved Trade(id=3, cusip=MMM, shares=32, price=395.21, createTime=1592354003966, updateTime=1592354003966)
2020-06-16 14:33:24.134  INFO 66854 --- [           main] example.client.service.TradeService      : Saved Trade(id=4, cusip=HD, shares=30, price=557.12, createTime=1592354004039, updateTime=1592354004039)
...
2020-06-16 14:33:57.957  INFO 66854 --- [           main] example.client.service.TradeService      : Saved Trade(id=9995, cusip=LMT, shares=40, price=142.03, createTime=1592354037956, updateTime=1592354037956)
2020-06-16 14:33:57.958  INFO 66854 --- [           main] example.client.service.TradeService      : Saved Trade(id=9996, cusip=IBM, shares=68, price=456.38, createTime=1592354037957, updateTime=1592354037957)
2020-06-16 14:33:57.959  INFO 66854 --- [           main] example.client.service.TradeService      : Saved Trade(id=9997, cusip=AMZN, shares=55, price=301.48, createTime=1592354037958, updateTime=1592354037958)
2020-06-16 14:33:57.960  INFO 66854 --- [           main] example.client.service.TradeService      : Saved Trade(id=9998, cusip=JPM, shares=64, price=117.23, createTime=1592354037959, updateTime=1592354037959)
2020-06-16 14:33:57.961  INFO 66854 --- [           main] example.client.service.TradeService      : Saved Trade(id=9999, cusip=GE, shares=25, price=213.31, createTime=1592354037960, updateTime=1592354037960)
```
### Calculate Region Size
Sample output from the *runclient.sh* script is:

```
./runclient.sh calculate-region-size ReplicatedTrade,PartitionedTrade

> Task :client:bootRun

2020-06-16 14:35:34.410  INFO 66913 --- [           main] example.client.Client                    : Starting Client on ...
...
2020-06-16 14:35:37.450  INFO 66913 --- [           main] example.client.Client                    : Started Client in 3.567 seconds (JVM running for 4.259)
2020-06-16 14:35:38.148  INFO 66913 --- [           main] example.client.service.TradeService      : 
Member sizes for regions [ReplicatedTrade, PartitionedTrade]:

member=xxx.xxx.x.xx(server-1:37320)<v1>:41001(version:UNKNOWN[ordinal=115])
        regionName=ReplicatedTrade; size=12,404,384 bytes
        regionName=PartitionedTrade; size=9,716,936 bytes

member=xxx.xxx.x.xx(server-2:37322)<v2>:41002(version:UNKNOWN[ordinal=115])
        regionName=ReplicatedTrade; size=12,404,256 bytes
        regionName=PartitionedTrade; size=9,832,064 bytes

member=xxx.xxx.x.xx(server-3:37323)<v3>:41003(version:UNKNOWN[ordinal=115])
        regionName=ReplicatedTrade; size=12,404,128 bytes
        regionName=PartitionedTrade; size=9,674,160 bytes
```
Each server's log file will contain messages this for each Region being sized:

```
[info 2020/06/16 17:06:17.000 HST <ServerConnection on port 52917 Thread 16> tid=0xe1] Calculating size of Partitioned Region @aba74e9 [path='/PartitionedTrade'; dataPolicy=PERSISTENT_PARTITION; prId=1; isDestroyed=false; isClosed=false; retryTimeout=3600000; serialNumber=8; partition attributes=PartitionAttributes@1561727612[redundantCopies=1;localMaxMemory=1638;totalMaxMemory=2147483647;totalNumBuckets=113;partitionResolver=null;colocatedWith=null;recoveryDelay=-1;startupRecoveryDelay=0;FixedPartitionAttributes=null;partitionListeners=null]; on VM 192.168.1.11(server-1:37320)<v1>:41001]

[info 2020/06/16 17:06:17.252 HST <ServerConnection on port 52917 Thread 16> tid=0xe1] Size of PartitionedTrade is 9,703,832 bytes

[info 2020/06/16 17:06:17.252 HST <ServerConnection on port 52917 Thread 16> tid=0xe1] Histogram for PartitionedTrade is:
clazzclazz	size	count
...
class org.apache.geode.internal.cache.BucketRegion	39600	75
class java.util.concurrent.locks.ReentrantReadWriteLock	42336	1764
class [Ljava.util.HashMap$Node;	42856	642
class java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock	49760	3110
class java.util.concurrent.locks.ReentrantReadWriteLock$Sync$ThreadLocalHoldCounter	49760	3110
class java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock	49760	3110
class [Lorg.apache.geode.internal.util.concurrent.CustomEntryConcurrentHashMap$HashEntry;	58440	1232
class org.apache.geode.internal.util.concurrent.CustomEntryConcurrentHashMap$Segment	68992	1232
class java.util.concurrent.ConcurrentHashMap	91136	1424
class java.util.HashMap	91728	1911
class [Ljava.util.concurrent.ConcurrentHashMap$Node;	100928	377
class [C	101288	1047
class org.apache.geode.internal.cache.PreferBytesCachedDeserializable	106400	6650
class java.util.concurrent.locks.ReentrantReadWriteLock$NonfairSync	145632	3034
class org.apache.geode.internal.cache.DiskId$PersistenceWithIntOffset	319200	6650
class org.apache.geode.internal.cache.entries.VersionedThinDiskRegionEntryHeapStringKey1	425600	6650
class [B	7341728	6653
```
### Shutdown Locator and Servers
Sample output from the *shutdownall.sh* script is:

```
./shutdownall.sh 

(1) Executing - connect

Connecting to Locator at [host=localhost, port=10334] ..
Connecting to Manager at [host=xxx.xxx.x.xx, port=1099] ..
Successfully connected to: [host=xxx.xxx.x.xx, port=1099]


(2) Executing - shutdown --include-locators=true

Shutdown is triggered
```
