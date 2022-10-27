package jobshopschedulingproblem;

public class JobProcess {
    private int indexJob;
    private int indexProcess;
    private int startTime;
    private int endTime;

    public JobProcess(int indexJob, int indexProcess, int startTime, int endTime) {
        this.indexJob = indexJob;
        this.indexProcess = indexProcess;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getIndexJob() {
        return indexJob;
    }

    public int getIndexProcess() {
        return indexProcess;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }
    
    public int getDuration() {
        return Math.abs(endTime-startTime);
    }
}
