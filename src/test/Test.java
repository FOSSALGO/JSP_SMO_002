package test;

import java.io.File;
import jobshopschedulingproblem.SpiderMonkeyOptimization;

public class Test {

    public static void main(String[] args) {
        File f = new File("la01");
        int I = 1000;
        int N = 400;
        int MG = 14;
        double pr = 0.2;
        int LLL = 10;
        int GLL = 10;
        SpiderMonkeyOptimization smo = new SpiderMonkeyOptimization(f, I, N, MG, pr, LLL, GLL);
        
        System.out.println("Result");
        System.out.println("Global Leader");
        System.out.println(smo.getGlobalLeader());
    }
}
