//PROG2 VT2023, Inlämningsuppgift, del1
//Grupp 022
//Palina Boczkowska pabo5408
//Anna Strömberg anst5816

import javafx.scene.control.Alert;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
public class Place extends Circle {

    private String name;
    private boolean selected = false;

    protected Place(double x, double y){
        super(x, y,10);
        setFill(Color.BLUE);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        if(selected) {
            setFill(Color.RED);
        }else {
            setFill(Color.BLUE);
        }
    }


    public boolean selected() {
        return selected;
    }


}
