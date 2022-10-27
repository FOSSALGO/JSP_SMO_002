package jobshopschedulingproblem;

import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;

public class DataReader {
    private Machine[][] data = null;

    public DataReader(File f) {
         try {
            FileInputStream fis = new FileInputStream(f);
            Scanner sc = new Scanner(fis, "UTF-8");

            //4 baris pertama pada dataset tidak digunakan
            sc.nextLine(); //baca baris 1 tetapi tidak perlu disimpan
            sc.nextLine(); //baca baris 2 tetapi tidak perlu disimpan
            sc.nextLine(); //baca baris 3 tetapi tidak perlu disimpan
            sc.nextLine(); //baca baris 4 tetapi tidak perlu disimpan

            String baris = sc.nextLine(); //baca baris ke 5 dan menyimpan kedalam variabel baris dalam bentuk String
            String[] segmen = baris.split("\\s+"); //melakukan split pada baris dengan menggunakan pemisah "spasi"
            //segmen ini bertipe array String, sehingga mudah diakses menggunakan index
            //contohnya:
            //System.out.println(segmen[0]); //untuk menampilkan value pada segmen index 0
            //System.out.println(segmen[1]); //untuk menampilkan value pada segmen index 1

            //s0 dan s1 adalah variabel String yang menyimpan nilai dari array
            String s0 = segmen[0];
            String s1 = segmen[1];

            int nJob = Integer.parseInt(s0); //jumlah job pada dataset
            int nProc = Integer.parseInt(s1); //jumlah proses pada dataset
            int nMac = Integer.parseInt(s1); //jumlah mesin pada dataset

            //dataset memiliki 10 job dengan 5 proses, dimana jumlah proses = jumlah mesin
            //cetak job, proses, dan mesin
            //System.out.println("--------------------------------------------------------");
            //System.out.println("nJOB   : " + nJob);
            //System.out.println("nProc  : " + nProc);
            //System.out.println("nMac   : " + nMac);
            //System.out.println("--------------------------------------------------------");

            //selanjutnya kita membaca dataset untuk melakukan pemisahan/split kolom data
            //index pada java dimulai dari 0
            //kolom genap sebagai data mesin
            //kolom ganjil sebagai data waktu
            //baris dan kolom berturut-turut menyatakan job dan proses
            //misalnya kita menyatakan data baris ke-2 dan kolom ke-3 berarti data yang dimaksud adalah job kedua dan proses ke 3
            //menyiapkan tabel mesin dan waktu
            data = new Machine[nJob][nProc];

            //baca data mesin dan waktu baris demi baris
            //System.out.println("ISI DATASET ");
            for (int i = 0; i < nJob; i++) {
                baris = sc.nextLine();
                //coba cetak baris
                //System.out.println(baris);

                //split data string baris kedalam array segmen bertipe string juga
                segmen = baris.split("\\s+");
                for (int k = 0; k < nProc; k++) {
                    String sMac = segmen[2 * k];
                    String sDur = segmen[2 * k + 1];

                    int iMachine = Integer.parseInt(sMac);
                    int duration = Integer.parseInt(sDur);
                    data[i][k] = new Machine(iMachine, duration);
                }
            }
            //System.out.println("---------------------------------------------");
            //System.out.println();
            //System.out.println("TABEL MESIN");
            for (int i = 0; i < nJob; i++) {
                for (int j = 0; j < nProc; j++) {
                    //System.out.print(data[i][j].getIndex()+ " ");
                }
                //System.out.println();
            }
            //System.out.println();
            //System.out.println("TABEL WAKTU");
            for (int i = 0; i < nJob; i++) {
                for (int j = 0; j < nProc; j++) {
                    //System.out.print(data[i][j].getDuration() + " ");
                }
                //System.out.println();
            }
            //System.out.println("---------------------------------------------");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Machine[][] getData() {
        return data;
    }
    
}
