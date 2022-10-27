package jobshopschedulingproblem;

import java.util.ArrayList;
import java.util.Random;

public class Algorithm {

    /// SWAP ===================================================================
    public int randomBetweenInteger(int min, int max) {
        if (min > max) {
            int temp = min;
            min = max;
            max = temp;
        }
        Random r = new Random();
        int bound = max - min + 1;
        return min + r.nextInt(bound);
    }

    public int[] cloneChromosome(int[] chromosome) {
        int[] newChromosome = null;
        if (chromosome != null) {
            newChromosome = new int[chromosome.length];
            for (int i = 0; i < chromosome.length; i++) {
                newChromosome[i] = chromosome[i];
            }
        }
        return newChromosome;
    }

    public int[] swap(int[] chromosome, int index1, int index2) {
        if (chromosome != null
                && index1 >= 0 && index1 < chromosome.length
                && index2 >= 0 && index2 < chromosome.length
                && chromosome[index1] != chromosome[index2]) {
            int temp = chromosome[index1];
            chromosome[index1] = chromosome[index2];
            chromosome[index2] = temp;
        }
        return chromosome;
    }

    public int[] swap(int[] chromosome) {
        if (chromosome != null && chromosome.length > 1) {
            int min = 0;
            int max = chromosome.length - 1;
            int index1 = randomBetweenInteger(min, max);
            int index2 = index1;
            while (index1 == index2) {
                index2 = randomBetweenInteger(min, max);
            }
            chromosome = swap(chromosome, index1, index2);
        }
        return chromosome;
    }

    public int[] add(int[] chromosome, SwapOperator so) {
        return swap(chromosome, so.getX(), so.getY());
    }

    public int[] add(int[] chromosome, ArrayList<SwapOperator> ss) {
        for (SwapOperator so : ss) {
            chromosome = add(chromosome, so);
        }
        return chromosome;
    }

    public ArrayList<SwapOperator> union(ArrayList<SwapOperator> ss1, ArrayList<SwapOperator> ss2) {
        ArrayList<SwapOperator> ss = null;
        if (ss1 != null && ss2 != null) {
            ss = new ArrayList<>();
            for (SwapOperator so : ss1) {
                SwapOperator so1 = new SwapOperator(so.getX(), so.getY());
                ss.add(so1);
            }
            for (SwapOperator so : ss2) {
                SwapOperator so2 = new SwapOperator(so.getX(), so.getY());
                ss.add(so2);
            }
        }
        return ss;
    }

    public ArrayList<SwapOperator> substract(int[] chromosome1, int[] chromosome2) {//chromosome1 - chromosome2
        ArrayList<SwapOperator> ss = null;
        if (chromosome1 != null && chromosome2 != null && chromosome1.length == chromosome2.length) {
            ss = new ArrayList<>();
            int[] chromosomeOperation = cloneChromosome(chromosome2);
            for (int i = 0; i < chromosome1.length; i++) {
                int value = chromosome1[i];
                if (chromosomeOperation[i] != value) {
                    for (int j = i + 1; j < chromosomeOperation.length; j++) {
                        if (value == chromosomeOperation[j]) {
                            SwapOperator so = new SwapOperator(i, j);
                            chromosomeOperation = add(chromosomeOperation, so);
                            ss.add(so);
                            break;
                        }
                    }
                }
            }
        }
        return ss;
    }
}
