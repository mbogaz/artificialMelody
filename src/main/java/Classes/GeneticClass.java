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
        String[] arr3 = new String[arr.length];

        for (String str:arr)
            System.out.println(wk.getScore(str));

        System.out.println("1.gen ortalama :"+getAvgScoreForGen(arr,wk));

        double maxScore = 0;
        for (int j = 0; j < Variables.geneticIterationCount; j++) {


            //initia values
            double totalScore = 0;
            double[] scores = new double[arr.length];
            //total score'u bul
            totalScore = 0;
            for (int i = 0; i < arr.length; i++) {
                scores[i] = wk.getScore(arr[i]);
                if(scores[i]==0.0)scores[i]=0.01;
                totalScore+=scores[i];
            }
            double randomValue = 0;
            int secondIndex=0;//ikinci listenin indexi
            int thirdIndex=0;//üçüncü listenin indexi
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
                selection = (int) Math.round(Math.random() * 3);
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

            totalScore = 0;
            for (int i = 0; i < arr.length; i++) {
                scores[i] = wk.getScore(arr2[i]);
                if(scores[i]==0.0)scores[i]=0.01;
                totalScore+=scores[i];
            }

            //3.listeyi doldur
            while (thirdIndex < arr.length){
                randomValue = totalScore * Math.random();
                for (int i = 0; i < scores.length; i++) {
                    randomValue -= scores[i];
                    if (randomValue <= 0) {
                        index = i;
                        break;
                    }
                }


                selection = (int) Math.round(Math.random() * 3);
                if (selection == 0) {
                    arr3[thirdIndex] = arr2[index];
                } else  {
                    double score = wk.getScore(arr2[index]);
                    arr3[thirdIndex] = mutation1(arr2[index],score);
                }
                thirdIndex++;


            }



            double avgScore = getAvgScoreForGen(arr3, wk);

            if(avgScore>maxScore) {
                System.out.println("yeni gen ortalama :" + avgScore);
                maxScore = avgScore;
            }
            if(avgScore == 1) break;
            for (int i = 0; i < arr.length; i++) {
                arr[i] = arr3[i];
            }
            arr2 = new String[arr.length];
            arr3 = new String[arr.length];
        }


        for (String str:arr) {
            System.out.println("score :"+wk.getScore(str)+parser.parse(str)+"\n");
        }



    }

    public static String crossover1(String data1, String data2,double score){

        String result= "";

        if (score >= 1)
            return data1;

        int crossoverPartNumber = (int) Math.round( ((1.0-score)*10));
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

    public static String mutation1(String data,double score){
        int mutationPartNumber = (int) Math.round( ((1.0-score)*10));
        if (mutationPartNumber == 0 ){
            mutationPartNumber = 1;
        }
        String[] datas = data.split(",");
        for (int i = 0; i < mutationPartNumber; i++) {
            int p = (int) (Math.random()*49);
            int d = Integer.parseInt(datas[(int) (((Math.random()*49)*2)+1)]);
            int pPlace = (int) ((Math.random()*49)*2);
            int dPlace = (int) (((Math.random()*49)*2)+1);
            datas[pPlace]=p+"";
            datas[dPlace]=d+"";
        }
        String res = "";
        for (int i = 0; i < datas.length-1; i++) {
            res = res+datas[i]+",";
        }
        return res+"?";
    }

    public static void main(String[] args) {
        new GeneticClass();
        /*String a = "9,75,10,75,9,75,7,75,5,75,7,75,5,75,0,200,-22,75,13,75,5,75,13,75,0,200,5,75,0,200,-22,75,-11,75,-22,75,-11,75,-13,75,-15,75,13,75,-13,75,-11,75,-22,150,-11,75,-22,75,0,200,-22,75,0,200,5,150,0,200,5,75,7,150,5,75,7,75,9,300,13,25,9,300,13,300,13,300,5,75,0,200,5,75,7,75,9,150,5,150,0,200,-22,150,5,75";
        String b = "14,133,13,133,33,267,13,133,33,133,13,267,34,535,33,535,17,133,13,133,32,267,13,133,32,133,13,267,34,535,32,535,22,267,29,267,13,133,29,133,13,267,31,133,32,267,29,535,13,133,22,133,13,133,29,267,13,133,29,133,13,267,31,133,32,267,29,400,13,267,14,133,13,133,33,267,13,133,33,133,13,267,34,535,33,535,17,133,13,133,32,267,13,133,32,133,13,267,34,535";
        String c = crossover1(a,b,0.7);
        System.out.println(c);*/
    }

    public static String getSubArrayToString(String[] strings) {

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
