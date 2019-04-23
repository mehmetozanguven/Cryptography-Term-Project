package measurement;

import java.util.concurrent.TimeUnit;

/**
 * This class is responsible for measuring performance, execution time
 *
 */
public class  Measure {
    private static Measure measureInstance = null;

    private Measure(){}

    public static Measure getInstance(){
        if (measureInstance == null)
            measureInstance = new Measure();
        return measureInstance;
    }

    private long executionStartTime;
    private long executionEndTime;

    private long beforeUsedMemory;
    private long afterUsedMemory;

    public void setExecutionStartTime(long executionStartTime) {
        this.executionStartTime = executionStartTime;
    }

    public void setExecutionEndTime(long executionEndTime) {
        this.executionEndTime = executionEndTime;
    }

    public void setBeforeUsedMemory(long beforeUsedMemory) {
        this.beforeUsedMemory = beforeUsedMemory;
    }

    public void setAfterUsedMemory(long afterUsedMemory) {
        this.afterUsedMemory = afterUsedMemory;
    }

    public String toString(){
        String result =
                "\nPerformance \t Before \t\t After\n" +
                "Execution Time\t " + TimeUnit.NANOSECONDS.toMillis(executionStartTime) + "\t\t" + TimeUnit.NANOSECONDS.toMillis(executionEndTime) + "\n" +
                "Memory Usage\t " + beforeUsedMemory + "\t\t" + afterUsedMemory + "\n";


        return result;
    }
}
