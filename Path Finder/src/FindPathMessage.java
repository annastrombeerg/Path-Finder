//PROG2 VT2023, Inlämningsuppgift, del1
//Grupp 022
//Palina Boczkowska pabo5408
//Anna Strömberg anst5816

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

class FindPathMessage extends Alert {

    protected TextArea pathInfo = new TextArea();

    protected FindPathMessage(String fromName, String toName) {
        super(AlertType.INFORMATION);
        GridPane grid = new GridPane();

        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setHgap(5);
        grid.setVgap(10);
        grid.addRow(0, pathInfo);
        setTitle("Message");
        setHeaderText("The Path from " +  fromName + " to " + toName);
        getDialogPane().setContent(grid);
        pathInfo.setEditable(false); //klistra in i VPL

    }
    public TextArea getPathInfo(){
        return pathInfo;
    }
}
