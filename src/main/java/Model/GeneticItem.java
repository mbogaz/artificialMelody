package Model;

/**
 * Created by mahmut on 27.04.2017.
 */
public class GeneticItem {
    double score;
    String text;

    public GeneticItem(double score, String text) {
        this.score = score;
        this.text = text;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "GeneticItem{" +
                "score=" + score +
                ", text='" + text + '\'' +
                '}';
    }
}
