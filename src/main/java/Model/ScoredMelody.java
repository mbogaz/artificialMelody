package Model;

/**
 * Created by mahmut on 04.04.2017.
 */
public class ScoredMelody {
    String name;
    int id;
    String content;
    double score;

    public ScoredMelody(String name, double score, String content,int id){
        this.name = name;
        this.score = score;
        this.content = content;
        this.id = id;
    }

    public double getScore() {
        return score;
    }

    public double calculateScore(){
        return (score-1)/4;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContent(String content){this.content = content;}

    public String getContent(){return content;}

    public void setId(int id){
        this.id = id;
    }

    public int getId(){return id;}

    @Override
    public String toString() {
        return "ScoredMelody{" +
                "id='" + id + '\'' +
                "name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", score=" + score +
                '}';
    }
}
