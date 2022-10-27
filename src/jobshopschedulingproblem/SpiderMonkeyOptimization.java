package jobshopschedulingproblem;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class SpiderMonkeyOptimization {

    //DATASET
    private File f;// = new File("la01");
    private DataReader dr;// = new DataReader(f);
    private Machine[][] data;// = dr.getData();

    //INPUT
    private int I;// = 100;//Total Number of Iterations
    private int N;// = 100;//Total Number of Spider Monkey
    private int MG;// = 6;//Allowed Maximum Group
    private double pr;// = 0.5;//Parturbation Rate
    private int LLL;// = 5;//Local Leader Limit
    private int GLL;// = 4;//Global Leader Limit

    //VARIABLES
    private int t = 0;//iteration counter
    private int g = 0;//Current Number of Group
    private int groupSize = 1;//banyaknya spider monkey di setiap group
    private Individu[] spiderMonkey = null;//SM = Population of Spider Monkey
    private Individu globalLeader = null;
    private int globalLeaderLimitCounter = 0;//GLLc = Global Leader Limit Counter
    private Individu[] localLeader = null;//LL = List of Local Leader
    private int[] localLeaderLimitCounter = new int[g];//LLLc = Local Leader Limit Counter of kth Group

    public SpiderMonkeyOptimization(File f, int I, int N, int MG, double pr, int LLL, int GLL) {
        this.f = f;
        this.dr = new DataReader(this.f);
        this.data = this.dr.getData();
        this.I = I;
        this.N = N;
        this.MG = MG;
        if(this.MG<(N/2)){
            this.MG = N/2;
        }
        if(this.MG<=0){
            this.MG = 1;
        }
        this.pr = pr;
        this.LLL = LLL;
        this.GLL = GLL;
        this.mainOperation();
    }

    public Individu getGlobalLeader() {
        return this.globalLeader;
    }

    private void mainOperation() {
        this.initialization();

        Random random = new Random();
        Algorithm algorithm = new Algorithm();

        while (t <= I) {
            //==================================================================
            //[1] Update of Spider Monkeys
            //==================================================================
            //[1.1] UPDATE Spider Monkeys base on local Leader 
            for (int k = 0; k < g; k++) {
                //set lower and upper bound
                int lowerBound = k * groupSize;
                int upperBound = lowerBound + groupSize - 1;
                if (k == g - 1) {
                    upperBound = N - 1;
                }

                //update spider monkey
                for (int i = lowerBound; i <= upperBound; i++) {
                    double u = random.nextDouble();//U(0,1)
                    if (u >= pr) {
                        int[] chromosomeLLk = algorithm.cloneChromosome(localLeader[k].getChromosome());
                        int[] chromosomeSMi = algorithm.cloneChromosome(spiderMonkey[i].getChromosome());
                        int r = algorithm.randomBetweenInteger(lowerBound, upperBound);
                        int[] chromosomeRSMr = algorithm.cloneChromosome(spiderMonkey[r].getChromosome());

                        ArrayList<SwapOperator> ss1 = algorithm.substract(chromosomeLLk, chromosomeSMi);
                        ArrayList<SwapOperator> ss2 = algorithm.substract(chromosomeRSMr, chromosomeSMi);
                        ArrayList<SwapOperator> ss = algorithm.union(ss1, ss2);

                        //Apply SS into SMi to calculate newSM
                        Individu newSM = spiderMonkey[i].clone();
                        int[] chromosome = chromosomeSMi;
                        for (int j = 0; j < ss.size(); j++) {
                            SwapOperator so = ss.get(j);
                            chromosome = algorithm.add(chromosome, so);
                            Individu individu = new Individu(chromosome);
                            individu.calculateFitness(data);
                            if (individu.getFitness() > newSM.getFitness()) {
                                newSM = individu.clone();
                            }
                        }

                        if (newSM.getFitness() > spiderMonkey[i].getFitness()) {
                            spiderMonkey[i] = newSM;
                        }

                    }//end of if (u >= pr)                     
                }//end of for (int i = lowerBound; i <= upperBound; i++)
            }//end of for (int k = 0; k < g; k++)

            //[1.2] UPDATE Spider Monkeys base on global Leader 
            for (int k = 0; k < g; k++) {
                //set lower and upper bound
                int lowerBound = k * groupSize;
                int upperBound = lowerBound + groupSize - 1;
                if (k == g - 1) {
                    upperBound = N - 1;
                }

                //update spider monkey
                for (int i = lowerBound; i <= upperBound; i++) {
                    double u = random.nextDouble();//U(0,1)
                    double prob = 0.9 * ((double) globalLeader.getMaxDuration() / (double) spiderMonkey[i].getMaxDuration()) + 0.1;//prob(i)
                    if (u <= prob) {
                        int[] chromosomeGl = algorithm.cloneChromosome(globalLeader.getChromosome());
                        int[] chromosomeSMi = algorithm.cloneChromosome(spiderMonkey[i].getChromosome());
                        int r = algorithm.randomBetweenInteger(lowerBound, upperBound);
                        int[] chromosomeRSMr = algorithm.cloneChromosome(spiderMonkey[r].getChromosome());

                        ArrayList<SwapOperator> ss1 = algorithm.substract(chromosomeGl, chromosomeSMi);
                        ArrayList<SwapOperator> ss2 = algorithm.substract(chromosomeRSMr, chromosomeSMi);
                        ArrayList<SwapOperator> ss = algorithm.union(ss1, ss2);

                        //Apply SS into SMi to calculate newSM
                        Individu newSM = spiderMonkey[i].clone();
                        int[] chromosome = chromosomeSMi;
                        for (int j = 0; j < ss.size(); j++) {
                            SwapOperator so = ss.get(j);
                            chromosome = algorithm.add(chromosome, so);
                            Individu individu = new Individu(chromosome);
                            individu.calculateFitness(data);
                            if (individu.getFitness() > newSM.getFitness()) {
                                newSM = individu.clone();
                            }
                        }

                        if (newSM.getFitness() > spiderMonkey[i].getFitness()) {
                            spiderMonkey[i] = newSM;
                        }

                    }//end of if (u >= pr)                     
                }//end of for (int i = lowerBound; i <= upperBound; i++)
            }//end of for (int k = 0; k < g; k++)

            //==================================================================
            //[2] Update of Local Leaders and Global Leader
            //==================================================================
            //[2.1] check new local leader
            Individu newGlobalLeader = globalLeader.clone();
            for (int k = 0; k < g; k++) {
                //set lower and upper bound
                int lowerBound = k * groupSize;
                int upperBound = lowerBound + groupSize - 1;
                if (k == g - 1) {
                    upperBound = N - 1;
                }

                Individu newLocalLeader = localLeader[k].clone();
                for (int i = lowerBound; i <= upperBound; i++) {
                    if (spiderMonkey[i].getFitness() > newLocalLeader.getFitness()) {
                        newLocalLeader = spiderMonkey[i];
                    }
                }

                if (newLocalLeader.getFitness() > localLeader[k].getFitness()) {
                    localLeader[k] = newLocalLeader.clone();
                    localLeaderLimitCounter[k] = 0;
                } else {
                    localLeaderLimitCounter[k]++;//localLeaderLimitCounter[k] = localLeaderLimitCounter[k] + 1;
                }

                if (localLeader[k].getFitness() > newGlobalLeader.getFitness()) {
                    newGlobalLeader = localLeader[k];
                }

            }//end of for (int k = 0; k < g; k++)

            //check new global leader
            if (newGlobalLeader.getFitness() > globalLeader.getFitness()) {
                globalLeader = newGlobalLeader.clone();
                globalLeaderLimitCounter = 0;
            } else {
                globalLeaderLimitCounter++;
            }

            //==================================================================
            //[3] Decision Phase of Local Leader and Global Leader
            //==================================================================
            //[3.1] Decision Phase of Local Leader
            for (int k = 0; k < g; k++) {
                if (localLeaderLimitCounter[k] > LLL) {
                    localLeaderLimitCounter[k] = 0;//LLLk ← 0

                    //set lower and upper bound
                    int lowerBound = k * groupSize;
                    int upperBound = lowerBound + groupSize - 1;
                    if (k == g - 1) {
                        upperBound = N - 1;
                    }

                    for (int i = lowerBound; i <= upperBound; i++) {
                        double u = random.nextDouble();//U(0,1)
                        if (u >= pr) {
                            //Initialize SMi randomly
                            Individu newSM = new Individu(data);
                            newSM.calculateFitness(data);
                            spiderMonkey[i] = newSM;
                        } else {
                            //Initialize SMi by interacting with the GL and LL
                            int[] chromosomeGL = algorithm.cloneChromosome(globalLeader.getChromosome());
                            int[] chromosomeLLk = algorithm.cloneChromosome(localLeader[k].getChromosome());
                            int[] chromosomeSMi = algorithm.cloneChromosome(spiderMonkey[i].getChromosome());

                            ArrayList<SwapOperator> ss2 = algorithm.substract(chromosomeGL, chromosomeSMi);
                            ArrayList<SwapOperator> ss1 = algorithm.substract(chromosomeSMi, chromosomeLLk);
                            ArrayList<SwapOperator> ss = algorithm.union(ss1, ss2);

                            //Apply SS into SMi to calculate newSM
                            Individu newSM = spiderMonkey[i].clone();
                            int[] chromosome = chromosomeSMi;
                            for (int j = 0; j < ss.size(); j++) {
                                SwapOperator so = ss.get(j);
                                chromosome = algorithm.add(chromosome, so);
                                Individu individu = new Individu(chromosome);
                                individu.calculateFitness(data);
                                if (individu.getFitness() > newSM.getFitness()) {
                                    newSM = individu.clone();
                                }
                            }

                            if (newSM.getFitness() > spiderMonkey[i].getFitness()) {
                                spiderMonkey[i] = newSM;
                            }

                        }
                    }
                }
            }//end of for (int k = 0; k < g; k++)

            //[3.1] Decision Phase of Global Leader
            if (globalLeaderLimitCounter > GLL) {
                globalLeaderLimitCounter = 0;
                if (g < MG) {
                    //Divide the spider monkeys into g + 1 number of groups
                    g++;// g = g + 1
                    groupSize = (int) Math.floor((double) N / (double) g);
                    this.localLeader = new Individu[g];
                    this.localLeaderLimitCounter = new int[g];//LLLc

                    int indexGlobalLeader = -1;
                    double globalFitness = 0;

                    //check new local leader and global leader
                    for (int k = 0; k < g; k++) {
                        //set lower and upper bound
                        int lowerBound = k * groupSize;
                        int upperBound = lowerBound + groupSize - 1;
                        if (k == g - 1) {
                            upperBound = N - 1;
                        }

                        //find new local leader
                        Individu newLocalLeader = spiderMonkey[lowerBound];
                        for (int i = lowerBound + 1; i <= upperBound; i++) {
                            if (spiderMonkey[i].getFitness() > newLocalLeader.getFitness()) {
                                newLocalLeader = spiderMonkey[i];
                            }

                        }
                        this.localLeader[k] = newLocalLeader.clone();

                        //find new global leader
                        if (globalFitness < this.localLeader[k].getFitness()) {
                            globalFitness = this.localLeader[k].getFitness();
                            indexGlobalLeader = k;
                        }                        
                    }//end of for (int k = 0; k < g; k++)
                    
                    //update GLOBAL LEADER
                    if(this.localLeader[indexGlobalLeader].getFitness()>this.globalLeader.getFitness()){
                        this.globalLeader = this.localLeader[indexGlobalLeader].clone();
                    }                    
                    
                } else {
                    //Disband all the groups and Form a single group.
                    g = 1;
                    groupSize = (int) Math.floor((double) N / (double) g);
                    this.localLeader = new Individu[g];
                    this.localLeader[0] = this.globalLeader.clone();
                    this.localLeaderLimitCounter = new int[g];//LLLc
                }
            }

            //INCREMENT of t====================================================
            t++;//increment t = t+1
        }//end of while while(t<=I);
    }//end of mainOperation

    public void initialization() {
        this.t = 1;//(1) t ← 1
        this.spiderMonkey = new Individu[N];//(2) create N spider moneys and append them to SM

        //(3)Assign each SMi in SM with a random solution
        int indexGlobalLeader = -1;
        double globalFitness = 0;
        for (int i = 0; i < N; i++) {
            spiderMonkey[i] = new Individu(data);
            spiderMonkey[i].calculateFitness(data);
            //System.out.println(spiderMonkey[i]);
            if (globalFitness < spiderMonkey[i].getFitness()) {
                globalFitness = spiderMonkey[i].getFitness();
                indexGlobalLeader = i;
            }
        }

        //(4) g = 1 initially consider all spiderMonkey into one group
        this.g = 1;
        this.groupSize = (int) Math.floor((double) N / (double) g);

        //(5) Select Local Leader and Global Leader // Both leaders are same due to single group
        //GLOBAL GLOBAL
        this.globalLeader = spiderMonkey[indexGlobalLeader].clone();
        this.globalLeaderLimitCounter = 0;//GLLc

        //LOCAL LEADER
        this.localLeader = new Individu[g];
        this.localLeader[0] = this.globalLeader.clone();
        this.localLeaderLimitCounter = new int[g];//LLLc
    }

}
