package Model;

/**
 * Created by mahmut on 13.04.2017.
 */
public class SimpleNote {
    int pitch ;
    int duration;

    public SimpleNote(){
        pitch=0;
        duration=0;
    }
    public SimpleNote(int pitch,int duration){
        this.pitch = pitch;
        this.duration = duration;
    }
    public void setPitch(int pitch){this.pitch=pitch;}
    public void setDuration(int duration){this.duration=duration;}

    public int getPitch(){return pitch;}
    public int getDuration(){return duration;}
}
