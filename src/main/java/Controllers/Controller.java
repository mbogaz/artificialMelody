package Controllers;

import Classes.DBClass;
import Model.ScoredMelody;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML Label totalLabel;
    @FXML Button createArffBtn;
    ArrayList<ScoredMelody> list;
    public void initialize(URL location, ResourceBundle resources) {
        list = new ArrayList<ScoredMelody>();
        list = DBClass.getDBClass().getData(0);
        totalLabel.setText("total scored \nmelody count :\n"+list.size());

        createArffBtn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                DBClass.getDBClass().makeArff(list);
            }
        });
    }
}
