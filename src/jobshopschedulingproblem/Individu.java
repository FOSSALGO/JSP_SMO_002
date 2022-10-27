package jobshopschedulingproblem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Individu {

    private int[] chromosome;
    private int max_duration;
    private double fitness;
    private int[][] startJobProcess = null;
    private int[][] endJobProcess = null;
    private Gantt[] ganttDiagram = null;

    public Individu(int[] chromosome) {
        this.resetAll();
        this.chromosome = chromosome;
    }

    public Individu(Machine[][] data) {
        this.resetAll();
        this.generate(data);
    }

    private void resetAll() {
        //reset all fields
        this.chromosome = null;
        this.max_duration = 0;
        this.fitness = 0;
        this.startJobProcess = null;
        this.endJobProcess = null;
        this.ganttDiagram = null;
    }

    public Individu clone() {
        Individu newIndividu = null;
        if (this.chromosome != null) {
            //copy chromosome
            int[] newChromosome = new int[this.chromosome.length];
            for (int i = 0; i < this.chromosome.length; i++) {
                newChromosome[i] = this.chromosome[i];
            }
            newIndividu = new Individu(newChromosome);
            newIndividu.max_duration = this.max_duration;
            newIndividu.fitness = this.fitness;

            //copy startJobProcess
            if (this.startJobProcess != null) {
                int rows = this.startJobProcess.length;
                int cols = this.startJobProcess[0].length;
                newIndividu.startJobProcess = new int[rows][cols];
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        newIndividu.startJobProcess[i][j] = this.startJobProcess[i][j];
                    }
                }
            }

            //copy endJobProcess
            if (this.endJobProcess != null) {
                int rows = this.endJobProcess.length;
                int cols = this.endJobProcess[0].length;
                newIndividu.endJobProcess = new int[rows][cols];
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        newIndividu.endJobProcess[i][j] = this.endJobProcess[i][j];
                    }
                }
            }

            //copy gantt diagram
            if (this.ganttDiagram != null) {
                newIndividu.ganttDiagram = new Gantt[this.ganttDiagram.length];
                for (int i = 0; i < this.ganttDiagram.length; i++) {
                    newIndividu.ganttDiagram[i] = new Gantt();
                    ArrayList<JobProcess> machineAllocation = this.ganttDiagram[i].getMachineAllocation();
                    if (machineAllocation != null) {
                        for (JobProcess jp : machineAllocation) {
                            int indexJob = jp.getIndexJob();
                            int indexProcess = jp.getIndexProcess();
                            int startTime = jp.getStartTime();
                            int endTime = jp.getEndTime();
                            JobProcess newJP = new JobProcess(indexJob, indexProcess, startTime, endTime);
                            newIndividu.ganttDiagram[i].add(newJP);
                        }
                    }
                }
            }
        }
        return newIndividu;
    }

    public int[] generate(Machine[][] data) {
        this.chromosome = null;
        if (data != null) {
            int nJob = data.length;
            int nProc = data[0].length;

            int chromosomeSize = nJob * nProc;
            int[] maxProc = new int[nJob];   //untuk menandai berapa proses yang telah masuk untuk setiap job
            this.chromosome = new int[chromosomeSize];

            //proses pembangkitan kromosom secara random
            int k = 0;
            while (k < chromosomeSize) {
                int value = new Random().nextInt(nJob);
                //validasi ada berapa proses yang telah dimiliki oleh setiap job
                if (maxProc[value] < nProc) {
                    maxProc[value]++;
                    this.chromosome[k] = value;
                    k++;
                }
            }
        }
        return this.chromosome;
    }

    public int calculateTotalDuration(Machine[][] data) {
        if (data != null && this.chromosome != null) {
            int nJob = data.length;
            int nProc = data[0].length;
            int nMac = nProc;

            //inisialisasi array untuk menyimpan start time dan end time
            startJobProcess = new int[nJob][nProc];
            endJobProcess = new int[nJob][nProc];

            int[] endTimeMac = new int[nMac];
            int[] endTimeJob = new int[nJob];
            int[] iProc = new int[nJob];        //untuk menandai proses yang sedang dikerjakan di setiap job

            ganttDiagram = new Gantt[nMac];// initialize ganttDiagram
            for (int i = 0; i < nMac; i++) {
                ganttDiagram[i] = new Gantt();
            }

            for (int i = 0; i < chromosome.length; i++) {
                int job = chromosome[i];
                int proc = iProc[job];
                Machine dataJobProc = data[job][proc];
                int iMachine = dataJobProc.getIndex();
                int duration = dataJobProc.getDuration();

                //masukkan mesin dan durasi ke gantt diagramn
                int etMac = endTimeMac[iMachine];   //end time untuk mesin ke - i
                int etJob = endTimeJob[job];     // end time untuk job
                int start = Math.max(etMac, etJob);
                int finish = start + duration;

                //save start dan finish
                startJobProcess[job][proc] = start;
                endJobProcess[job][proc] = finish;

                //insert new JobProcess to ganttDiagram
                JobProcess jobProcess = new JobProcess(job, proc, start, finish);
                ganttDiagram[iMachine].add(jobProcess);

                endTimeMac[iMachine] = finish;
                endTimeJob[job] = finish;

                if (finish > this.max_duration) {//update MAX_DURATION
                    this.max_duration = finish;
                }
                iProc[job]++; //increment untuk index proses               
            }
        }
        return this.max_duration;
    }

    public double calculateFitness(Machine[][] data) {
        this.fitness = 0;
        this.calculateTotalDuration(data);
        if (this.max_duration > 0) {
            this.fitness = 1.0 / this.max_duration;
        }
        return this.fitness;
    }

    public double getFitness() {
        return fitness;
    }

    public int[] getChromosome() {        
        return chromosome;
    }

    public int getMaxDuration() {
        return max_duration;
    }  
            
    public String toString() {
        String info = null;
        if (chromosome != null) {
            StringBuffer sb = new StringBuffer();
            sb.append("=========================================================\n");
            sb.append("CHROMOSOME: " + Arrays.toString(chromosome) + "\n");
            sb.append("DURATION  : " + max_duration + "\n");
            sb.append("FITNESS   : " + new BigDecimal(fitness).toPlainString() + "\n");
            if (ganttDiagram != null) {
                {
                    sb.append("\n");
                    sb.append("GANTT DIAGRAM:\n");
                    for (int i = 0; i < ganttDiagram.length; i++) {
                        sb.append("M-" + i + " " + ganttDiagram[i].toString() + "\n");
                    }
                }
            }
            sb.append("=========================================================\n");
            info = sb.toString();
        }
        return info;
    }

}
