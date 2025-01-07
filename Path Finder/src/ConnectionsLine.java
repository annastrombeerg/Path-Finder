
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class ConnectionsLine extends Line {

    public ConnectionsLine(double startX, double startY, double endX, double endY){
        super(startX, startY, endX, endY);
        setFill(Color.BLACK);
    }


}
