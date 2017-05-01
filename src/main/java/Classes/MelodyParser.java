package Classes;

import java.util.HashMap;

public class MelodyParser {



    public MelodyParser() {
    }

    public String parse(String data){
        String[] splittedArray = data.split(",");
        int[] pitchArray = new int[splittedArray.length/2];
        int[] durationArray = new int[splittedArray.length/2];




        String pitch = "";

        String tempPitch;
        String tempduration = null;
        int  tempoctave;

        for (int i = 0; i < splittedArray.length/2; i++) {
                pitchArray[i] = Integer.parseInt(splittedArray[2*i]);
                durationArray[i] = Integer.parseInt(splittedArray[(2*i)+1]);
        }


       double bb = getPopularElement(durationArray);
        int b;
        b = (int) (60000/bb);
        //b = b/2;
        String rtttl = "deneme:d=4,o=6,b="+b+":";
        for (int i = 0; i < pitchArray.length; i++) {

            pitch = "";
            if (pitchArray[i] > 1){
                switch (pitchArray[i] % 12){
                    case 2: pitch="c";break;
                    case 3: pitch="c#";break;
                    case 4: pitch="d";break;
                    case 5: pitch="d#";break;
                    case 6: pitch="e";break;
                    case 7: pitch="f";break;
                    case 8: pitch="f#";break;
                    case 9: pitch="g";break;
                    case 10: pitch="g#";break;
                    case 11: pitch="a";break;
                    case 0: pitch="a#";break;
                    case 1: pitch="b";break;
                }
            }
            else {
                pitch = "p";
            }

            tempoctave = ((pitchArray[i]-2) /12 )+4;

            if (durationArray[i] < bb*0.15){
                tempduration = "32";
            } else if(durationArray[i] < bb*0.20){
                tempduration = "32";pitch+=".";
            }else if(durationArray[i] < bb*0.31){
                tempduration = "16";
            }else if(durationArray[i] < bb*0.42){
                tempduration = "16";pitch+=".";
            }else if(durationArray[i] < bb*0.62){
                tempduration = "8";
            }else if(durationArray[i] < bb*0.88){
                tempduration = "8";pitch+=".";
            }else if(durationArray[i] < bb*1.2){
                tempduration = "4";
            }else if(durationArray[i] < bb*1.7){
                tempduration = "4";pitch+=".";
            }else if(durationArray[i] < bb*2.5){
                tempduration = "2";
            }else if(durationArray[i] < bb*3.5){
                tempduration = "2";pitch+=".";
            }else if(durationArray[i] < bb*5){
                tempduration = "1";
            } else {
                tempduration = "1";pitch+=".";
            }

            if (pitch.equals("p")){
                rtttl += tempduration+pitch+",";
            }
            else rtttl += tempduration+pitch+tempoctave+",";

        }

        return rtttl;
    }

    public int getPopularElement(int []array) {HashMap<Integer,Integer> hm=new HashMap<Integer,Integer>();
        int max=1,temp = 0;
        for(int i=0;i<array.length;i++)
        {

            if (array[i] == 0) continue;
            if(hm.get(array[i])!=null)
            {int count=hm.get(array[i]);
                count=count+1;
                hm.put(array[i],count);
                if(count>max)
                {max=count;
                    temp=array[i];}
            }
            else
            {hm.put(array[i],1);}
        }
        return temp;
    }

    public static void main(String[] args) {
    }
}