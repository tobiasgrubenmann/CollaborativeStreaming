package costSharing;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Evaluation {

    public static final int NUMBER_OF_SOURCES = 500;

    public static final int ITERATIONS = 10;

    public static final String OUTPUT_FILENAME_EXECUTION_TIME = "output/results_exection_time.csv";
    public static final String OUTPUT_FILENAME_ALLOCATED_RUNTIME = "output/results_allocated_runtime.csv";

    public static final int SEED = 0;

    public static final int LAYERS = 2;

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {

        PrintWriter executionTimeResultsWriter = new PrintWriter(OUTPUT_FILENAME_EXECUTION_TIME, "UTF-8");
        PrintWriter allocatedRuntimeResultsWriter = new PrintWriter(OUTPUT_FILENAME_ALLOCATED_RUNTIME, "UTF-8");

        Random random = new Random(SEED);

        long startTime = 0;

        List<Integer> queryParemters = Arrays.asList( 1, 10, 100, 1000);

        List<Double> probabilityParameters = Arrays.asList(0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0);

        executionTimeResultsWriter.print("n_queries / prob");
        allocatedRuntimeResultsWriter.print("n_queries / prob");

        // write header for output
        for (Double probability : probabilityParameters) {
            executionTimeResultsWriter.print("," + probability);
            allocatedRuntimeResultsWriter.print("," + probability);
        }
        executionTimeResultsWriter.println();
        allocatedRuntimeResultsWriter.println();

        for (Integer numberOfQueries : queryParemters) {

            executionTimeResultsWriter.print(numberOfQueries);
            allocatedRuntimeResultsWriter.print(numberOfQueries);

            for (Double probability : probabilityParameters) {

                Scenario scenario = new RandomOverlapScenario(numberOfQueries, NUMBER_OF_SOURCES, LAYERS, probability, random);

                double aggregatedExecutionTime = 0;

                double aggregatedAllocatedRuntime = 0;

                for (int i = 0; i < ITERATIONS; ++i) {

                    List<Allocation> allocations = new ArrayList<Allocation>();
                    for (Node query : scenario.getLeaves()) {
                        allocations.add(new Allocation(query, Integer.MAX_VALUE, random.nextDouble(), Integer.MAX_VALUE));
                    }

                    startTime = System.nanoTime();

                    AllocationAlgorithm.allocate(scenario.getCostNodes(), allocations);

                    aggregatedExecutionTime += (System.nanoTime() - startTime) / 1e9;

                    for (Allocation allocation : allocations) {
                        aggregatedAllocatedRuntime += allocation.getRuntime();
                    }
                }

                double avgExectionTime = aggregatedExecutionTime / ITERATIONS;

                double avgAllocatedRuntimePerQuery = aggregatedAllocatedRuntime / (ITERATIONS * numberOfQueries);

                System.out.println(numberOfQueries + " queries, " + probability + " prob.: " + avgExectionTime  + " seconds");

                executionTimeResultsWriter.print("," + avgExectionTime);

                allocatedRuntimeResultsWriter.print("," + avgAllocatedRuntimePerQuery);
            }

            executionTimeResultsWriter.println();
            executionTimeResultsWriter.flush();

            allocatedRuntimeResultsWriter.println();
            allocatedRuntimeResultsWriter.flush();
        }

        executionTimeResultsWriter.close();
        allocatedRuntimeResultsWriter.close();
    }
}