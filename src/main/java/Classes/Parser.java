package Classes;

import java.util.HashMap;

class Parser {



    public Parser() {
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
        String rtttl = "deneme:d=4,o=6,b="+b+":";
        for (int i = 0; i < pitchArray.length; i++) {

            pitch = "";
            if (pitchArray[i] > 1){
                switch (pitchArray[i] % 12){
                    case 0: pitch="g";break;
                    case 1: pitch="g#";break;
                    case 2:pitch="a"; break;
                    case 3: pitch="a#";break;
                    case 4: pitch="b";break;
                    case 5: pitch="c";break;
                    case 6:pitch="c#"; break;
                    case 7: pitch="d";break;
                    case 8: pitch="d#";break;
                    case 9: pitch="e";break;
                    case 10:pitch="f"; break;
                    case 11:pitch="f#"; break;
                }
            }
            else {
                pitch = "p";
            }

            tempoctave = ((pitchArray[i]-2) /12 )+4;

            if (durationArray[i] < bb*0.150){
                tempduration = "32";
            } else if(durationArray[i] < bb*0.200){
                tempduration = "32.";
            }else if(durationArray[i] < bb*0.310){
                tempduration = "16";
            }else if(durationArray[i] < bb*0.420){
                tempduration = "16.";
            }else if(durationArray[i] < bb*0.620){
                tempduration = "8";
            }else if(durationArray[i] < bb*0.880){
                tempduration = "8.";
            }else if(durationArray[i] < bb*1.250){
                tempduration = "4";
            }else if(durationArray[i] < bb*1.750){
                tempduration = "4.";
            }else if(durationArray[i] < bb*2.500){
                tempduration = "2";
            }else if(durationArray[i] < bb*3.500){
                tempduration = "2.";
            }else if(durationArray[i] < bb*5.000){
                tempduration = "1";
            } else {
                tempduration = "1.";
            }

            if (pitch.equals("p")){
                rtttl += tempduration+pitch+",";
            }
            else rtttl += tempduration+pitch+tempoctave+",";

        }

        return rtttl;
    }

    public int getPopularElement(int []array)
    {HashMap<Integer,Integer> hm=new HashMap<Integer,Integer>();
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


}