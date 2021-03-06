package Classes;

import Model.ScoredMelody;

import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by mahmut on 04.04.2017.
 */
public class DBClass {
    static DBClass dbClass;
     public static ScoredMelody tempSM;
    int bb=0;

    public static void main(String[] args){
        DBClass db = new DBClass();
        ArrayList<ScoredMelody> list  = db.getData(0);
        db.makeArff(list);
    }

    public ArrayList<ScoredMelody> getData(int limit) {
        ArrayList<ScoredMelody> list = new ArrayList<ScoredMelody>();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:mysql://93.89.225.82:3306/fbgartif_artificialmelody", "fbgartif_mahmut", "12119885768");
            Statement komut = conn.createStatement();
            String querry = Variables.selected;
            if(limit != 0){
                querry = querry+ " LIMIT "+limit;
            }
            ResultSet rs = komut.executeQuery(querry);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (rs.next()) {
                String name="";
                double score = 0;
                int id =0;
                String content = "";
                for (int i = 1; i <= columnsNumber; i++) {
                    String columnValue = rs.getString(i);
                    if(i==1)
                        name=columnValue;
                    else if(i==2)
                        score=Double.parseDouble(columnValue);
                    else if(i==3)
                        content = columnValue;
                    else if(i==4)
                        id = Integer.parseInt(columnValue);
                }
                content = content.replaceAll("\\s+","");
                ScoredMelody sm = new ScoredMelody(name,score,content,id);
                list.add(sm);
            }
            conn.close();
            return list;
        }catch (Exception e){
            System.out.println("mysql connection error :"+e);
            return null;
        }
    }


    public static DBClass getDBClass(){
        if(dbClass == null)
            dbClass = new DBClass();

        return dbClass;
    }

    public void makeArff(ArrayList<ScoredMelody> list){
        try{
            String fileName = "file2.arff";
            PrintWriter writer = new PrintWriter(fileName, "UTF-8");
            writer.println("@RELATION artificialMelody");
            writer.println();

            writer.println("    @ATTRIBUTE ID NUMERIC");
            for(int i=0;i<Variables.inputLenght;i++){
                writer.println("    @ATTRIBUTE pitch"+i+" NUMERIC");
                writer.println("    @ATTRIBUTE duration"+i+" NUMERIC");
            }

            writer.println("    @ATTRIBUTE score NUMERIC");
            writer.println();
            writer.println("@DATA");
            writer.print(listToString(list,false));

            writer.close();
            Path currentRelativePath = Paths.get("");
            String s = currentRelativePath.toAbsolutePath().toString();
            JOptionPane.showMessageDialog(null,"Dosya "+s+"/"+fileName+" adresine yazıldı");
        } catch (IOException e) {
            // do something
        }
    }

    public String listToString(ArrayList<ScoredMelody> list,boolean forGeneric) {

        String result="";
        for (ScoredMelody sm : list){
            //System.out.println(sm.getContent());
            tempSM = sm;
            if(sm.getContent().equals(""))continue;
            int[] notes = new int[Variables.inputLenght];
            int[] durations = new int[Variables.inputLenght];
            for(int i=0;i<Variables.inputLenght;i++){notes[i]=0;durations[i]=0;}
            int d=0,b=0,o=0; double score=0;
            String data = sm.getContent().split(":")[1];
            String[] data1 = data.split(",");
            d=Integer.parseInt(data1[0].split("=")[1]);
            b=Integer.parseInt(data1[2].split("=")[1]);
            o=Integer.parseInt(data1[1].split("=")[1]);
            //writer.print(sm.getContent().split(":")[0]+","); //file adını yazıyor
            String line = "";
            if(!forGeneric) {
                score = sm.calculateScore();
                line = sm.getId() + ",";
            }
            String arr[] = sm.getContent().split(":");
            if(arr.length<3)continue;
            String iters[] = arr[2].split(",");
            for(int i=0;i<Variables.inputLenght && i<iters.length;i++){
                bb=60000/b;
                notes[i]=parseNode(iters[i],d,o);
                durations[i]=bb;
            }
            for(int i=0;i<Variables.inputLenght;i++){
                line=line+notes[i]+","+durations[i];
                if(i<(Variables.inputLenght-1))line=line+",";
            }
            if(!forGeneric)
                line = line+","+score;
            else
                line = line+",?";
            result += line+"\n";
        }
        return result;
    }
    public String createRandomString(WekaClass wk){

        int testLenght = Variables.testLenght;
        String res = "";
        for(int i=0;i<testLenght;i++){
            int b = (int) ((Math.random()*100)+100);
            String text = "";
            for(int j=0;j<Variables.inputLenght;j++){
                int d = (int) (Math.random()*6);
                int bb=0;
                switch (d){
                    case 0: bb=(60000/b)*4;break;
                    case 1: bb=(60000/b)*2;break;
                    case 2: bb=(60000/b);break;
                    case 3: bb=(60000/b)/2;break;
                    case 4: bb=(60000/b)/4;break;
                    case 5: bb=(60000/b)/8;break;
                }
                int resultA = (int) (Math.random()*50);
                int rand = (int) (Math.random()*4);
                if(rand==0) {
                    bb = bb * 3 / 2;
                }
                text+= resultA+","+bb+",";

            }
            text+="?";
            if(wk.getScore(text)> 0.4){
                i--;
            }else
                res += text+"\n";
        }

        return res;
    }

    public int parseNode(String text,int d,int o){
        int val = 0;
        text = text.toLowerCase();
        text = text.replaceAll("#a","a#");
        text = text.replaceAll("#c","c#");
        text = text.replaceAll("#d","d#");
        text = text.replaceAll("#f","f#");
        text = text.replaceAll("#g","g#");
        text = text.replaceAll("b#","b");
        text = text.replaceAll("e#","e");
        if(text.contains("p")){
            val = getEncodedValue(text,"p",1,d,o);
        }else if(text.contains("c#")){
            val = getEncodedValue(text,"c#",3,d,o);
        }else if(text.contains("c")){
            val = getEncodedValue(text,"c",2,d,o);
        }else if(text.contains("d#")){
            val = getEncodedValue(text,"d#",5,d,o);
        }else if(text.contains("d")){
            val = getEncodedValue(text,"d",4,d,o);
        }else if(text.contains("e")){
            val = getEncodedValue(text,"e",6,d,o);
        }else if(text.contains("f#")){
            val = getEncodedValue(text,"f#",8,d,o);
        }else if(text.contains("f")){
            val = getEncodedValue(text,"f",7,d,o);
        }else if(text.contains("g#")){
            val = getEncodedValue(text,"g#",10,d,o);
        }else if(text.contains("g")){
            val = getEncodedValue(text,"g",9,d,o);
        }else if(text.contains("a#")){
            val = getEncodedValue(text,"a#",12,d,o);
        }else if(text.contains("a")){
            val = getEncodedValue(text,"a",11,d,o);
        }else if(text.contains("b")){
            val = getEncodedValue(text,"b",13,d,o);
        }
        return val;
    }
    public int getEncodedValue(String text,String key,int mult,int d,int o){
        //0 la 50 arası değer döndürür.
        //mult notanın katsayısı(octav ile genişletiliyor)
        //d ve o rtttl nin default değerleri
        //pitch değerini döndürür
        //duration değeri global bb variableında
        //return mult;
        if(text.contains("_")){text = text.replace("_","#");key=key+"#";}
        boolean isDot = false;
        if(text.contains(".")){isDot = true;text = text.replace(".","");}
        String[] values = text.split(key);
        if(values.length==2 && values[0].equals("")){
            String[] arr = new String[1];
            arr[0] = values[1];
            values = arr;
        }
        if(text.equals(key)){//sadece nota
            updateBB(d,isDot);
            return mult+((o-4)*12);
        }
        else if(values.length == 1){
            if(text.substring(0,key.length()).equals(key)){//octav ve nota
                updateBB(d,isDot);
                int tempOctave = Integer.parseInt(values[0]);
                if(tempOctave>7 || tempOctave<4)
                    tempOctave = o;
                return mult+((tempOctave-4)*12);
            }
            else{//uzunluk ve nota
                d=Integer.parseInt(values[0]);
                updateBB(d,isDot);
                return mult+((o-4)*12);
            }
        }
        else if(values.length == 2){//hepsi
            try{d=Integer.parseInt(values[0]);}catch (Exception e){
                System.out.println("error " +
                        "values[0]:'"+values[0]+"'  values[1]:'"+values[1]+
                        " text:"+tempSM);
            }
            updateBB(d,isDot);
            int tempOctave = Integer.parseInt(values[1]);
            if(tempOctave>7 || tempOctave<4)
                tempOctave = o;
            return mult+(tempOctave-4)*12;
        }
        return 0;
    }
    public void updateBB(int duration,boolean isDot){
        switch (duration){
            case 1: bb=bb*4; break;
            case 2: bb=bb*2; break;
            case 4: break;
            case 8: bb=bb/2; break;
            case 16: bb=bb/4; break;
            case 32: bb=bb/8; break;
        }
        if(isDot) bb=bb*3/2;
    }
}
