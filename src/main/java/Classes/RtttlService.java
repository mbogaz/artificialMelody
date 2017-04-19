package Classes;
import javax.sound.sampled.*;
class RtttlService{
    public static void main(String[]arg)throws Exception{
        Integer k=0;
        String a[]="Ilk deneme:d=1,o=5,b=429:8f7,1f#7,4f5,1f7,1d#4,4f5,4f5,4f#5,32f7,16e5,1g7,8b5,1a#5,16f4,1c#7,16c4".split(":");
        System.out.println(a[0]);
        int[]X=new int[3],N={9,11,0,2,4,5,7};
        while(k<3)X[k]=k.valueOf(a[1].split(",")[k++].split("=")[1].trim());
        SourceDataLine l=AudioSystem.getSourceDataLine(new AudioFormat(48000,8,1,1>0,1<0));
        l.open();
        l.start();
        for(String t:a[2].toLowerCase().split(",")){
            a[k=0]=a[1]=a[2]="";
            for(char c:t.trim().toCharArray())
                if(c!=46)a[k+=(c<48|c>57?1:0)^k]+=c;
            int D=32/(a[k=0]==""?X[0]:k.valueOf(a[0]))*(t.contains(".")?3:2),m=a[1].charAt(0)-97,P=m>6?0:N[m]+12*(a[2]==""?X[1]:k.valueOf(a[2]))+(t.contains("#")?1:0);
            int n=180000*D/X[2];
            for(;k<n;++k)l.write(new byte[]{(byte)(P>0&k<n-n/D?k*Math.pow(2,P/12.)/22.93:0)},0,1);
        }
        l.drain();
    }
}