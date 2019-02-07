package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

public class CreateWIndow {
    static DatabaseHandler handler = new DatabaseHandler();
    public  void display(int k,String name){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Создание таблицы");
        window.setMinWidth(700);
        window.setMinHeight(500);
        ArrayList<TextField> textList = new ArrayList<>();
        ArrayList<ComboBox<String>> comboBoxes = new ArrayList<>();
        ArrayList<CheckBox> checkBoxListNull = new ArrayList<>();
        ArrayList<CheckBox> checkBoxListAI = new ArrayList<>();
        ArrayList<CheckBox> checkBoxListPK = new ArrayList<>();
        ArrayList<HBox> hBoxList = new ArrayList<>();
        VBox vBox = new VBox(5);
        ObservableList<String> rec = FXCollections.observableArrayList();
        rec.add("VARCHAR(45)");
        rec.add("INT");
        for (int i=0; i<k;i++){
            TextField textField = new TextField();
            ComboBox<String> comboBox = new ComboBox<>();
            CheckBox checkBoxNull = new CheckBox("NOT NULL");
            CheckBox checkBoxAI = new CheckBox("AI");
            CheckBox checkBoxPK = new CheckBox("PK");
            comboBox.setItems(rec);
            textList.add(textField);
            comboBoxes.add(comboBox);
            checkBoxListNull.add(checkBoxNull);
            checkBoxListAI.add(checkBoxAI);
            checkBoxListPK.add(checkBoxPK);
            HBox layout = new HBox(4);
            layout.getChildren().addAll(textField,comboBox,checkBoxNull,checkBoxAI,checkBoxPK);
            hBoxList.add(layout);
            vBox.getChildren().addAll(layout);
        }
        Button button = new Button("Создать Таблицу");
        button.setOnAction(event -> {
            String sql = "CREATE TABLE `"+name+"`(";
            for (int i=0; i<k;i++){
                sql+="`"+textList.get(i).getText()+"` "+comboBoxes.get(i).getValue()+" ";
                if(checkBoxListNull.get(i).isSelected()){
                    sql+="NOT NULL";
                }
                if(checkBoxListAI.get(i).isSelected()){
                    sql+=" AUTO_INCREMENT, ";
                }else{
                    sql+=", ";
                }
            }
            sql+=" PRIMARY KEY(";
            for (int i=0; i<k;i++){
                if(checkBoxListPK.get(i).isSelected()){
                    sql+="`"+textList.get(i).getText()+"`)";
                    break;
                }
            }
            sql+=")";
            System.out.println(sql);
            try {
                handler.signUpUser(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/sample/sample.fxml"));

            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();
        });
        vBox.getChildren().addAll(button);

        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.show();
    }
    public  void insertDisplay(ArrayList<String> list,String name){
        Boolean flag = true;
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Вставка");
        window.setMinWidth(700);
        window.setMinHeight(500);
        HBox hBox = new HBox(5);
        ArrayList<TextField> textFields = new ArrayList<>();
        try {
         ResultSet  rs = handler.selectSQL("SELECT `AUTO_INCREMENT` FROM information_schema.tables where `TABLE_NAME` = '"+name+"'");
         rs.next();
         if(rs.getString(1) == null){
             flag =false;
         }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Button button = new Button("Вставить");
        if(!flag) {
            for (int i = 0; i < list.size(); i++) {
                TextField textField = new TextField();
                textField.setPromptText(list.get(i));
                textFields.add(textField);
                hBox.getChildren().add(textField);
            }
            button.setOnAction(event -> {
                String sql = "INSERT INTO `" + name + "` (";
                for (int i = 0; i < list.size(); i++) {
                    if (i + 1 != list.size()) {
                        sql += "`" + list.get(i) + "`,";
                    } else {
                        sql += "`" + list.get(i) + "`) VALUES (";
                    }
                }
                for (int i = 0; i < list.size(); i++) {
                    if ((i + 1 != list.size())) {
                        sql += "'" + textFields.get(i).getText() + "',";
                    } else {
                        sql += "'" + textFields.get(i).getText() + "')";
                    }
                }
                try {
                    handler.signUpUser(sql);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                System.out.println(sql);
            });
        }
        else {
            for (int i = 1; i < list.size(); i++) {
                TextField textField = new TextField();
                textField.setPromptText(list.get(i));
                textFields.add(textField);
                hBox.getChildren().add(textField);
            }
            button.setOnAction(event -> {
                String sql = "INSERT INTO `" + name + "` (";
                for (int i = 1; i < list.size(); i++) {
                    if (i + 1 != list.size()) {
                        sql += "`" + list.get(i) + "`,";
                    } else {
                        sql += "`" + list.get(i) + "`) VALUES (";
                    }
                }
                for (int i = 0; i < list.size()-1; i++) {
                    if ((i + 1 != list.size()-1)) {
                        sql += "'" + textFields.get(i).getText() + "',";
                    } else {
                        sql += "'" + textFields.get(i).getText() + "')";
                    }
                }
                try {
                    System.out.println(sql);
                    handler.signUpUser(sql);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                System.out.println(sql);
            });
        }
        hBox.getChildren().addAll(button);
        Scene scene = new Scene(hBox);
        window.setScene(scene);
        window.show();
    }
    public  void updateDisplay(String name,int key) throws SQLException, ClassNotFoundException {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Обновление данных");
        window.setMinWidth(700);
        window.setMinHeight(500);
        HBox hBox = new HBox(5);
        ArrayList<TextField> textFields = new ArrayList<>();
        String s = "SELECT * from `"+name+"` WHERE `id` = "+key;
        System.out.println(s);
        ResultSet rs =handler.selectSQL(s);
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        rs.next();
        ArrayList<String> list = new ArrayList<>();
        for(int i=0;i<columns;i++){
            TextField textField = new TextField(rs.getString(i+1));
            textFields.add(textField);
            hBox.getChildren().add(textField);
            list.add(rs.getString(i+1));
        }
        Button button = new Button("Обновить");
        button.setOnAction(event -> {
            String sql = "UPDATE TABLE `"+name+"` SET ";
            for(int i=0;i<columns;i++){
                try {
                    sql+="`"+md.getColumnName(i+1)+"` = "+textFields.get(i)+",";
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

                }
        );
        hBox.getChildren().add(button);
        Scene scene = new Scene(hBox);
        window.setScene(scene);
        window.show();
    }
    public  void createViewDisplay(){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Создание представления");
        window.setMinWidth(300);
        window.setMinHeight(100);
        VBox vBox = new VBox(5);
        TextField name = new TextField();
        name.setPromptText("Имя представления");
        TextField selectText = new TextField();
        selectText.setPromptText("Тело представления");
        Button button = new Button("Создать представление");
        button.setOnAction(event -> {
           String sql = "CREATE VIEW `"+name.getText()+"` AS "+selectText.getText();
           System.out.println(sql);
            try {
                handler.signUpUser(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        vBox.getChildren().addAll(name,selectText,button);

        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.show();
    }
}
