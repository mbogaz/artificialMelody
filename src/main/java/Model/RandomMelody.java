package Model;

import Classes.MelodyParser;
import Classes.WekaClass;

import java.util.ArrayList;

/**
 * Created by mahmut on 29.04.2017.
 */
public class RandomMelody {
    int crrPitch;
    ArrayList<Integer> scaleArr = new ArrayList<Integer>();
    int[] measures = new int[2];
    int bb;
    //scaleType 0 -> major scale
    public RandomMelody(int pitch, int scaleType, String measure, int bpm) {
        this.crrPitch = pitch;
        measures[0] =Integer.parseInt(measure.split("/")[0]);
        measures[1] =Integer.parseInt(measure.split("/")[1]);
        this.bb = 60000/bpm;
        if(scaleType == 0){
            int i = 0;
            while(pitch < 50){
                scaleArr.add(pitch);
                if(i%7==2 || i%7==6){
                    pitch+=2;
                }else pitch++;
                i++;
            }

        }
    }
    public String createRandomMelody(){
        String melody = "";
        int index = randomWithRange(0,scaleArr.size()-2);
        for (int i = 0; i < 50; i++) {
            int dur = 0;
            switch (randomWithRange(2,5)){
                case 1: dur = bb/8; break;
                case 2: dur = bb/4; break;
                case 3: dur = bb/2; break;
                case 4: dur = bb; break;
                case 5: dur = bb*2; break;
                case 6: dur = bb*4;  break;
            }

            melody += scaleArr.get(index)+","+dur+",";

            if(index == 0){
                int random = randomWithRange(1,2);
                index = random==1 ? index : index+1;
            }else if(index == scaleArr.size()-2){
                int random = randomWithRange(1,2);
                index = random==1 ? index : index-1;
            }else{
                int random = randomWithRange(1,3);
                index = random==2 ? index : (random==1 ? index-1 : index+1);
            }

        }
        return melody+"?";
    }

    public String mutation(int index,String data){
        String melody = "";
        String[] datas=data.split(",");
        int scaleIndex = getScaleIndex(Integer.parseInt(datas[index*2]));

        for (int i = 0; i < index; i++) {
            melody += datas[i*2]+","+datas[(i*2)+1]+",";
        }
        int a = index;
        index = scaleIndex;
        for (int i = a; i < 50; i++) {
            int dur = 0;
            switch (randomWithRange(2,5)){
                case 1: dur = bb/8; break;
                case 2: dur = bb/4; break;
                case 3: dur = bb/2; break;
                case 4: dur = bb; break;
                case 5: dur = bb*2; break;
                case 6: dur = bb*4;  break;
            }


            melody += scaleArr.get(index)+","+dur+",";

            if(scaleArr.get(index) == Integer.parseInt(datas[i*2])){
                i++;
                while(i<50){
                    melody += datas[i*2]+","+datas[(i*2)+1]+",";
                    i++;
                }
            }

            if(index == 0){
                int random = randomWithRange(1,2);
                index = random==1 ? index : index+1;
            }else if(index == scaleArr.size()-2){
                int random = randomWithRange(1,2);
                index = random==1 ? index : index-1;
            }else{
                int random = randomWithRange(1,3);
                index = random==2 ? index : (random==1 ? index-1 : index+1);
            }


        }
        return melody+"?";

    }

    int getScaleIndex(int pitch){
        for (int i = 0; i < scaleArr.size(); i++) {
            if(scaleArr.get(i)==pitch)
                return i;
        }
            return -1;
    }

    public static int randomWithRange(int min, int max) {
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }

    public static void main(String[] args) {
        WekaClass wk = new WekaClass();
        wk.multiLayerPerception();
        MelodyParser mp = new MelodyParser();
        RandomMelody rm = new RandomMelody(2,0,"4/8",120);
        double score = 0;
        double max = 0;
        /*while (true){
            String text = rm.createRandomMelody();
            score = wk.getScore(text);
            if(score > max){
                max = score;
                System.out.println(score);
                System.out.println(mp.parse(text));
                System.out.println(text);
            }
            if(score>0.7){
                System.out.println(mp.parse(text));
                break;
            }
        }*/
        String str = rm.createRandomMelody();
        System.out.println(str);
        System.out.println(rm.mutation(5,str));
    }
}
