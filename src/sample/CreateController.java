package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CreateController {

    @FXML
    private Button makeButton;

    @FXML
    private TextField countText;

    @FXML
    void initialize() {
        DatabaseHandler handler = new DatabaseHandler();
        makeButton.setOnAction(event -> {
            ArrayList<TextField> list = new ArrayList<>();
            ArrayList<ChoiceBox>notNULL = new ArrayList<>();
            for(int i=0;i< Integer.parseInt(countText.getText());i++){
                TextField field= new TextField();
                ChoiceBox choiceBox = new ChoiceBox();
                list.add(field);
                notNULL.add(choiceBox);
            }
        });
    }
}