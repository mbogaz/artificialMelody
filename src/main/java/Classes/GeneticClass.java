package Classes;

import Model.GeneticItem;
import Model.ScoredMelody;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by oğuz on 22.04.2017.
 */
public class GeneticClass {
    public GeneticClass(){
        MelodyParser melodyParser = new MelodyParser();
        DBClass db = new DBClass();
        WekaClass wk = new WekaClass();
        wk.multiLayerPerception();
        ArrayList<ScoredMelody> list = db.getData(Variables.geneticPoolLenght);
        String res = db.listToString(list,true);
        //String res = db.createRandomString(wk);
        /*String res = "";
        RandomMelody rm = new RandomMelody(1,0,"4/4",120);
        for (int i = 0; i < 100; i++) {
            if(i!=99)
                res+= rm.createRandomMelody()+"\n";
            else
                res+=rm.createRandomMelody();
        }*/
        String[] arr = res.split("\n");

        ArrayList<GeneticItem> gen1 = new ArrayList<GeneticItem>();
        ArrayList<GeneticItem> gen2 = new ArrayList<GeneticItem>();
        ArrayList<GeneticItem> gen3 = new ArrayList<GeneticItem>();
        ArrayList<GeneticItem> gen4 = new ArrayList<GeneticItem>();
        for(String str:arr){
            double score = wk.getScore(str);
            gen1.add(new GeneticItem(score,str));
            //System.out.println(parser.parse(str));
        }

        System.out.println("başlangıç ortalama :"+getAvgScoreForGen(gen1));

        double maxScoreAvg = 0,maxScore=0;


        for (int j = 0; j < Variables.geneticIterationCount; j++) {

            //2.listeyi doldur
            //rastgele 5 taneden en iyisini al
            for (int i = 0; i < Variables.geneticPoolLenght; i++) {
                ArrayList<GeneticItem> smallList = new ArrayList<GeneticItem>();
                for (int k = 0; k < 5; k++) {
                smallList.add(gen1.get((int) (Math.random()*gen1.size())));
                }
                int index=0;double maxScoreFromSmallList=0;
                for (int k = 0; k < 5; k++) {
                    if(smallList.get(k).getScore()>maxScoreFromSmallList){
                        maxScoreFromSmallList = smallList.get(k).getScore();
                        index = k;
                    }
                }
                gen2.add(smallList.get(index));


            }




            //3.listeyi doldur
            //zar salla ihtimalden küçükse cross yap öyle ekle (2 parçayı da ayrı)
            for (int i = 0; i < Variables.geneticPoolLenght; i+=2) {
                if(Math.random()<Variables.crossOverProb){
                    String[] crosseds = crossover2(gen2.get(i).getText(),gen2.get(i+1).getText());
                    gen3.add(new GeneticItem(wk.getScore(crosseds[0]),crosseds[0]));
                    gen3.add(new GeneticItem(wk.getScore(crosseds[1]),crosseds[1]));
                }else{
                    gen3.add(gen2.get(i));
                    gen3.add(gen2.get(i+1));
                }
            }


            //4.listeyi doldur
            //mutation prob
            for (int i = 0; i < Variables.geneticPoolLenght; i++) {
                if(Math.random()<Variables.mutationProb){
                    String str = gen3.get(i).getText();
                    double score = wk.getScore(str);
                    //str = rm.mutation(rm.randomWithRange(1,49),str);
                    str = mutation1(str,score);
                    score = wk.getScore(str);
                    gen4.add(new GeneticItem(score,str));
                }else
                    gen4.add(gen3.get(i));
            }



            double avgScore = getAvgScoreForGen(gen4);

            if(avgScore>maxScoreAvg) {
                System.out.println("iteration :"+(j+1)+" ->yeni gen ortalama :" + avgScore);
                maxScoreAvg = avgScore;
            }
            if(avgScore == 1) break;


            //4 ü bire aktar
            gen1.clear();
            for (int i = 0; i < Variables.geneticPoolLenght; i++) {
                GeneticItem gi = new GeneticItem(gen4.get(i).getScore(),gen4.get(i).getText());
                if(gi.getScore() > maxScore){
                    maxScore = gi.getScore();
                    System.out.println("score :"+gi.getScore()+ melodyParser.parse(gi.getText()));
                }
                gen1.add(gi);
            }
            gen2.clear();
            gen3.clear();
            gen4.clear();
        }


        for (GeneticItem gi:gen1) {
            System.out.println(gi.getText());
            System.out.println("score :"+gi.getScore()+ melodyParser.parse(gi.getText()));
            //if(gi.getScore()>0.98)
                break;
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

    public static String[] crossover2(String data1, String data2){

        String result1= "";
        String result2= "";

        int crossoverPartNumber = (int) ((Math.random()*3)+1);
        int datalength = Variables.inputLenght*2;
        String[] crossoverParts1 = data1.split(",");
        String[] crossoverParts2 = data2.split(",");
        int prevIndex  = 0;
        for (int i = 0; i < crossoverPartNumber; i++) {
            double r =  Math.random();
            int random = (int) (r * (datalength / crossoverPartNumber));
            int index = i == crossoverPartNumber - 1 ? 100 : random + (i * datalength / crossoverPartNumber);
            result1 += getSubArrayToString(Arrays.copyOfRange(i % 2 == 0 ? crossoverParts1 : crossoverParts2 , prevIndex, index));
            result2 += getSubArrayToString(Arrays.copyOfRange(i % 2 == 1 ? crossoverParts1 : crossoverParts2 , prevIndex, index));
            prevIndex = index;
        }
        String[] arr = new String[2];
        arr[0] = result1+"?";
        arr[1] = result2+"?";
        return arr;
    }

    public static String[] crossover3(String data1, String data2){
        String result1= "";
        String result2= "";


        int datalength = Variables.inputLenght;
        String[] crossoverParts1 = data1.split(",");
        String[] crossoverParts2 = data2.split(",");
        int kesismeIndex=0;
        int maxKesisme=1000;
        for (int i = 0; i < datalength; i++) {
            if(crossoverParts1[i*2].equals(crossoverParts2[i*2])){
                if(maxKesisme>Math.abs(25 - (datalength-i))){
                    maxKesisme = Math.abs(25 - (datalength-i));
                    kesismeIndex = i;
                }
            }
        }
        result1 += getSubArrayToString(Arrays.copyOfRange(crossoverParts1 , 0, kesismeIndex*2));
        result1 += getSubArrayToString(Arrays.copyOfRange(crossoverParts2 , (kesismeIndex*2), crossoverParts1.length));
        result2 += getSubArrayToString(Arrays.copyOfRange(crossoverParts2 , 0, kesismeIndex*2));
        result2 += getSubArrayToString(Arrays.copyOfRange(crossoverParts1 , (kesismeIndex*2), crossoverParts1.length));
        String[] arr = new String[2];
        arr[0] = result1;
        arr[1] = result2;
        return arr;
    }

    public static String mutation1(String data,double score){
        int mutationPartNumber = (int) Math.round( ((1.0-score)*10));
        if (mutationPartNumber == 0 ){
            mutationPartNumber = 1;
        }
        String[] datas = data.split(",");
        for (int i = 0; i < mutationPartNumber; i++) {
            int p = (int) (Math.random()*49);
            int d = Integer.parseInt(datas[(int) Math.floor(((Math.random()*49)*2)+1)]);
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
        /*String a ="2,250,2,250,2,500,3,1000,2,250,2,250,3,125,3,250,2,125,3,500,2,500,3,500,4,1000,6,125,4,250,6,500,4,125,6,250,6,1000,4,250,6,250,4,250,4,500,6,500,7,250,8,500,7,250,8,250,7,250,8,500,9,500,8,250,7,500,6,500,4,250,6,1000,4,250,6,500,6,125,6,1000,4,500,4,250,4,500,3,250,3,1000,2,125,2,125,3,125,4,250,6,250,?";
        String b ="2,500,2,250,2,1000,3,500,4,500,4,250,6,125,6,250,7,500,8,125,9,125,11,1000,12,125,11,500,11,250,12,500,12,500,11,125,12,125,12,250,13,1000,13,125,12,500,11,500,9,500,11,125,12,125,12,500,12,500,13,125,13,1000,12,1000,12,125,13,250,12,125,12,250,12,125,12,125,11,500,11,1000,12,125,11,500,9,250,8,1000,9,250,9,500,9,125,9,250,8,125,8,125,?";
        String[] c = crossover3(a,b);
        System.out.println(c[0]);
        System.out.println(c[1]);*/
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
    public double getAvgScoreForGen(ArrayList<GeneticItem> list){
        double avg =0;
        for (int i = 0; i < list.size() ; i++) {
            avg += list.get(i).getScore();
        }

        return avg/list.size();
    }


}
