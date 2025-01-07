//PROG2 VT2023, Inlämningsuppgift, del1
//Grupp 022
//Palina Boczkowska pabo5408
//Anna Strömberg anst5816

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

class ConnectionAlert extends Alert {

    private TextField nameField = new TextField();
    private TextField timeField = new TextField();

    protected ConnectionAlert(String fromName, String toName) {
        super(AlertType.CONFIRMATION);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setHgap(5);
        grid.setVgap(10);

        Label nameLabel = new Label("Name: ");
        Label timeLabel = new Label("Time: ");

        grid.addRow(0, nameLabel, nameField);
        grid.addRow(1, timeLabel, timeField);
        GridPane.setHalignment(nameLabel, HPos.RIGHT);
        GridPane.setHalignment(timeLabel, HPos.RIGHT);

        setTitle("Connection");
        setHeaderText("Connection from " + fromName + " to " + toName);
        getDialogPane().setContent(grid);
    }

    public String getName(){
        return nameField.getText();
    }
    public int getTime(){
        return Integer.parseInt(timeField.getText());
    }

    public TextField getNameField() {
        return nameField;
    }
    public TextField getTimeField(){
        return timeField;
    }
}
