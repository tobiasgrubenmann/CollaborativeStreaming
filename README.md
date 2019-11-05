# CollaborativeStreaming
Source code to reproduce the results of the paper "Collaborative Streaming: Trust Requirements for Price Sharing" at the 4th Workshop on Real-time and Stream Analytics in Big Data & Stream Data Management.

Usage:

1. Compile all classes: 
    
    javac -cp ./libraries/\* ./costSharing/\*.java

2. (Optional) run tests:

    Windows: java -cp ".;./libraries/\*" org.junit.runner.JUnitCore costSharing.AllocationUnitTest
    
    Other: java -cp ".:./libraries/\*" org.junit.runner.JUnitCore costSharing.AllocationUnitTest
    
3. Run evaluation:

    java -cp . costSharing.Evaluation

The code will generate two new files:
- "results_allocated_runtime.csv" holds the average allocated runtime.
- "results_exection_time.csv" holds the average execution time.
