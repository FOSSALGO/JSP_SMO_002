package jobshopschedulingproblem;

import java.util.ArrayList;

public class Gantt {

    private ArrayList<JobProcess> machineAllocation = null;

    public Gantt() {
        this.machineAllocation = new ArrayList<>();
    }

    public void add(JobProcess jobProcess) {
        if (machineAllocation != null) {
            machineAllocation.add(jobProcess);
        }
    }

    public ArrayList<JobProcess> getMachineAllocation() {
        return machineAllocation;
    }

    public String toString() {
        String result = null;
        if (machineAllocation != null) {
            StringBuilder sb = new StringBuilder();
            int pointer = 0;
            for (int i = 0; i < machineAllocation.size(); i++) {
                JobProcess jobProcess = machineAllocation.get(i);
                int indexJob = jobProcess.getIndexJob();
                int indexProcess = jobProcess.getIndexProcess();
                int duration = jobProcess.getDuration();
                int startTime = jobProcess.getStartTime();
                int endTime = jobProcess.getEndTime();
                if(pointer<startTime){
                    for (int j = pointer; j < startTime; j++) {
                        sb.append("-");
                    }
                    pointer = startTime;
                }
                for (int j = pointer; j < endTime; j++) {
                    //sb.append("#");
                    sb.append(indexJob);
                }
                pointer = endTime;
            }
            result = sb.toString();
        }
        return result;
    }

}
