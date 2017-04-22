package Classes;

import Model.ScoredMelody;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by oğuz on 22.04.2017.
 */
public class GeneticClass {
    public GeneticClass(){
        Parser parser = new Parser();
        DBClass db = new DBClass();
        WekaClass wk = new WekaClass();
        wk.multiLayerPerception();
        ArrayList<ScoredMelody> list = db.getData(Variables.geneticPoolLenght);
        String res = db.listToString(list,true);
        String[] arr = res.split("\n");
        String[] arr2 = new String[arr.length];

        System.out.println("1.gen ortalama :"+getAvgScoreForGen(arr,wk));
        for (String str:arr) {
            if(wk.getScore(str)==1)
                System.out.println(str);
        }
        System.out.println("------------");


        for (int j = 0; j < Variables.geneticIterationCount; j++) {


            //initia values
            double totalScore = 0;
            double[] scores = new double[arr.length];
            //total score'u bul
            totalScore = 0;
            for (int i = 0; i < arr.length; i++) {
                scores[i] = wk.getScore(arr[i]);
                totalScore+=scores[i];
            }
            double randomValue = 0;
            int secondIndex=0;//ikinci listenin indexi
            int index = 0;
            int selection =0;
            String crossMatch = null;
            double crossScore = 0;



            //2.listeyi doldur
            while (secondIndex < arr.length) {

                //score'un karşılık geldiği elemanı bul
                randomValue = totalScore * Math.random();
                for (int i = 0; i < scores.length; i++) {
                    randomValue -= scores[i];
                    if (randomValue <= 0) {
                        index = i;
                        break;
                    }
                }

                //hangi işlemin yapılacağını seç
                //0 direk ekle , 1 crossover , 2 do nothing
                //1 çıkınca başka bir 1 çıkan örneğe kadar beklet
                selection = (int) Math.round(Math.random() * 100);
                if (selection == 0) {
                    arr2[secondIndex] = arr[index];
                    secondIndex++;
                } else  {
                    if (crossMatch == null) {
                        crossMatch = arr[index];
                        crossScore = wk.getScore(crossMatch);
                    } else {
                        double tempScore = wk.getScore(arr[index]);
                        tempScore = (tempScore + crossScore) / 2;
                        arr2[secondIndex] = crossover1(arr[index], crossMatch, tempScore);
                        crossMatch = null;
                        crossScore = 0;
                        secondIndex++;
                    }
                }

            }
            double avgScore = getAvgScoreForGen(arr2, wk);
            System.out.println("yeni gen ortalama :" +avgScore );
            if(avgScore == 1) break;
            for (int i = 0; i < arr.length; i++) {
                arr[i] = arr2[i];
            }
            arr2 = new String[arr.length];
        }


        for (String str:arr) {
            System.out.println(str);
            System.out.println(parser.parse(str)+"\n");
        }



    }

    public String crossover1(String data1, String data2,double score){

        String result= "";

        if (score >= 1)
            return data1;

        int crossoverPartNumber = (int) Math.round( ((1.0-score)*20));
        if (crossoverPartNumber == 0 ){
            crossoverPartNumber = 1;
        }
        int datalength = Variables.inputLenght*2;
        String[] crossoverParts1 = data1.split(",");
        String[] crossoverParts2 = data2.split(",");
            int prevIndex  = 0;
        for (int i = 0; i < crossoverPartNumber; i++) {
            double r =  Math.random();
            int random = (int) (r * (datalength / crossoverPartNumber));
            int index = i == crossoverPartNumber - 1 ? 100 : random + (i * datalength / crossoverPartNumber);
            result += getSubArrayToString(Arrays.copyOfRange(i % 2 == 0 ? crossoverParts1 : crossoverParts2 , prevIndex, index));
            prevIndex = index;
        }
        return result+"?";
    }

    public static void main(String[] args) {
        new GeneticClass();
    }

    public String getSubArrayToString(String[] strings) {

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i <strings.length ; i++) {
                builder.append(strings[i]+",");
        }


        return builder.toString();
    }

    public double getAvgScoreForGen(String[] arr,WekaClass wk){
        double avg =0;
        for (int i = 0; i < arr.length ; i++) {
            avg += wk.getScore(arr[i]);
        }

        return avg/arr.length;
    }


}
