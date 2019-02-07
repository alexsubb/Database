package sample;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Controller {

    @FXML
    private Button makeButton;

    @FXML
    private TextField sqlText;

    @FXML
    private MenuButton choosBox;

    @FXML
    private Button deleteButton;

    @FXML
    private Button createButton;

    @FXML
    private TextField keyText;

    @FXML
    private TextField tablenameText;

    @FXML
    private Button dropButton;

    @FXML
    private Button trancateButton;
    @FXML
    private Button selectButton;

    @FXML
    private TableView<ObservableList<String>> insertTable;

    @FXML
    private Button insertButton;


    @FXML
    void makeSQL(MouseEvent event) {

    }
    @FXML
    private TableView<ObservableList<String>> tableSelect;

    @FXML
    private TextField atributeName;

    @FXML
    private ComboBox<String> typeText;

    @FXML
    private CheckBox notNull;

    @FXML
    private Button updateButton;

    @FXML
    private TextField newText;

    @FXML
    private MenuButton alterType;

    @FXML
    private TextField countText;
    @FXML
    private Button viewButton;


    private void setAlterMenu(){
        DatabaseHandler hendler = new DatabaseHandler();
        ObservableList<String> rec = FXCollections.observableArrayList();
        rec.add("VARCHAR(45)");
        rec.add("INT");
        typeText.setItems(rec);
        MenuItem add = new MenuItem("ADD");
        MenuItem drop = new MenuItem("DROP");
        MenuItem change = new MenuItem("CHANGE");
        alterType.getItems().clear();
        alterType.getItems().add(add);
        alterType.getItems().add(drop);
        alterType.getItems().add(change);

        add.setOnAction(event -> {
            String sql = "ALTER TABLE `"+tablenameText.getText()+"` ADD `"+atributeName.getText()+"` "+typeText.getValue();
            if(notNull.isSelected()){
                sql+=" NOT NULL";
            }
            sqlText.setText(sql);
            try {
                hendler.signUpUser(sql);
            } catch (SQLException e) {
                showDialog("Ошибка","Введите все атрибуты");
            } catch (ClassNotFoundException e) {
                showDialog("Ошибка","Отсутствует подключение к базе");
            }
        });

        drop.setOnAction(event -> {
            String sql = "ALTER TABLE `"+tablenameText.getText()+"` DROP `"+atributeName.getText()+"`";
            try {
                hendler.signUpUser(sql);
            } catch (SQLException e) {
                showDialog("Ошибка","Введите все атрибуты");
            } catch (ClassNotFoundException e) {
                showDialog("Ошибка","Отсутствует подключение к базе");
            }
        });

        change.setOnAction(event -> {
           String sql = "ALTER TABLE `"+tablenameText.getText()+"` CHANGE `"+atributeName.getText()+"` "+" `"+newText.getText()+"`"+typeText.getValue();
           sqlText.clear();
           sqlText.setText(sql);
            try {
                hendler.signUpUser(sql);
            } catch (SQLException e) {
                showDialog("Ошибка","Введите все атрибуты");
            } catch (ClassNotFoundException e) {
                showDialog("Ошибка","Отсутствует подключение к базе");
            }
        });
    }

    private void setMenu(){
        MenuItem select = new MenuItem("SELECT");
        MenuItem insert = new MenuItem("INSERT");
        MenuItem update = new MenuItem("UPDATE");
        MenuItem delete = new MenuItem("DELETE");
        MenuItem create = new MenuItem("CREATE");
        MenuItem alter = new MenuItem("ALERT");
        MenuItem drop = new MenuItem("DROP");
        MenuItem truncate = new MenuItem("TRUNCATE");

        choosBox.getItems().clear();
        choosBox.getItems().add(select);
        choosBox.getItems().add(insert);
        choosBox.getItems().add(update);
        choosBox.getItems().add(delete);
        choosBox.getItems().add(create);
        choosBox.getItems().add(alter);
        choosBox.getItems().add(drop);
        choosBox.getItems().add(truncate);

        select.setOnAction(event ->
                sqlText.setText("SELECT\n" +
                        "  [DISTINCT | DISTINCTROW | ALL]\n" +
                        "  select_expression,...\n" +
                        "FROM table_references\n" +
                        "[WHERE where_definition]\n" +
                        "[GROUP BY {unsigned_integer | col_name | formula}]\n" +
                        "[HAVING where_definition]\n" +
                        "[ORDER BY {unsigned_integer | col_name | formula} [ASC | DESC], ...]")
        );
        insert.setOnAction(event ->
                sqlText.setText("insert into <название таблицы> ([<Имя столбца>, ... ]) values (<Значение>,...)")
        );
        update.setOnAction(event ->
                sqlText.setText("UPDATE [top(x)] <объект>  " + "SET <присваивание1 [, присваивание2, ...]>  " +
                        "[WHERE <условие>];")
        );
        delete.setOnAction(event ->
                sqlText.setText("DELETE FROM <Имя Таблицы> WHERE <Условие отбора записей>")
        );
        create.setOnAction(event ->
                sqlText.setText("CREATE TABLE `blocklist` (\n" +
                        "`email` text NOT NULL,\n" +
                        "`login` text NOT NULL)")
        );
        alter.setOnAction(event ->
                sqlText.setText("ALTER TABLE t1(pole1 char(10));")
        );
        drop.setOnAction(event ->
                sqlText.setText("DROP TABLE table_name")
        );
        truncate.setOnAction(event ->
                sqlText.setText("TRUNCATE TABLE <Имя Таблицы>")
        );
    }

    private  void LoadTable(ResultSet rs) throws SQLException, ClassNotFoundException {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();

        for(int i=0; i<columns; i++) {
            final int j=i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(md.getColumnName(i+1));
            column.setCellValueFactory( param -> new SimpleStringProperty(param.getValue().get(j).toString()));
            column.setEditable(false);
            tableSelect.getColumns().addAll(column);
        }
    }

    private void FillTable(ResultSet rs, int columns) throws SQLException, ClassNotFoundException {
        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
        while(rs.next()){
            ObservableList<String> rec = FXCollections.observableArrayList();
            for(int i=1; i<= columns; i++) {
                rec.add((rs.getString(i)));
            }
            data.add(rec);
        }
        tableSelect.setItems(data);
    }

    private  void createButtonAction(){
        /*FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/create.fxml"));

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();*/
        CreateWIndow createWIndow = new CreateWIndow();
        createWIndow.display(Integer.parseInt(countText.getText()),tablenameText.getText());

    }

    private void showDialog(String title, String text){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }

    @FXML
    void initialize() {
        DatabaseHandler handler = new DatabaseHandler();
        setMenu();
        setAlterMenu();
        createButton.setOnAction(event -> {
            createButton.getScene().getWindow().hide();
            createButtonAction();
        });
        dropButton.setOnAction(event -> {
            try {
                String table = tablenameText.getText();
                handler.signUpUser("DROP TABLE "+table);
            }catch (SQLException e){
                showDialog("Вы совершили ошибку","Данной таблицы не существует");
            } catch (ClassNotFoundException e) {
                showDialog("Вы совершили ошибку","Проверьте подключение к базе");
            }
        });
        insertButton.setOnAction(event -> {
            ArrayList<String> list = new ArrayList<>();
            ResultSet rs ;
            ResultSetMetaData md = null;
            int columns =0;
            try {
                rs = handler.selectSQL("SELECT * FROM " + tablenameText.getText());
                md = rs.getMetaData();
                columns = md.getColumnCount();
                for(int i=0;i<columns;i++){
                        list.add(md.getColumnName(i+1));
                }
               CreateWIndow createWIndow = new CreateWIndow();
                createWIndow.insertDisplay(list,tablenameText.getText());
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        updateButton.setOnAction(event -> {
          CreateWIndow createWIndow = new CreateWIndow();
            try {
                createWIndow.updateDisplay(tablenameText.getText(),Integer.parseInt(keyText.getText()));
            } catch (SQLException e) {
                System.out.println(e.toString());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        selectButton.setOnAction(event -> {
            try {

                ResultSet rs = handler.selectSQL("SELECT * FROM " + tablenameText.getText());
                int columns = rs.getMetaData().getColumnCount();
                tableSelect.getColumns().clear();
                LoadTable(rs);
                FillTable(rs, columns);
            } catch (SQLException | ClassNotFoundException e) {
                showDialog("Вы совершили ошибку","Данной таблицы не существует");
                e.printStackTrace();
            }
        }
        );
        trancateButton.setOnAction(event -> {
            try {
                String table = tablenameText.getText();
                handler.signUpUser("TRUNCATE TABLE "+table);
            }catch (SQLException e){
                showDialog("Вы совершили ошибку","Данной таблицы не существует");
            } catch (ClassNotFoundException e) {
                showDialog("Вы совершили ошибку","Проверьте подключение к базе");
            }
        });
        deleteButton.setOnAction(event -> {
            String keyString = keyText.getText();
            int key;
            try {
                key = Integer.valueOf(keyString);
                String table = tablenameText.getText();
                if(table.equals("")){
                    showDialog("Вы совершили ошибку","Введите имя таблицы");
                }
                handler.signUpUser("DELETE FROM `database_1`.`" + table + "` WHERE (`id` = '" + key + "');");
            } catch (Exception e) {
                showDialog("Вы совершили ошибку","Элемента с таким ключем не существует");
            }
        });
        viewButton.setOnAction(event -> {
            CreateWIndow createWIndow = new CreateWIndow();
            createWIndow.createViewDisplay();
        });
        makeButton.setOnAction(event -> {
                    try {
                        String sql = sqlText.getText();
                        String[] sqlArray = sql.split(" ");
                        if (sqlArray[0].compareToIgnoreCase("select") == 0) {
                            ResultSet rs = handler.selectSQL(sql);
                            ResultSetMetaData rsmd = rs.getMetaData();
                            int columns = rs.getMetaData().getColumnCount();

                            tableSelect.getColumns().clear();
                            LoadTable(rs);
                            FillTable(rs, columns);
                        } else {
                            if (handler.signUpUser(sql)) {
                                sqlText.setText("Успешно");
                            }
                        }
                    } catch (SQLException e) {
                        sqlText.setText(e.toString());
                    } catch (ClassNotFoundException e) {
                        sqlText.setText(e.toString());
                    }
                }
        );
    }
}
