//PROG2 VT2023, Inlämningsuppgift, del1
//Grupp 022
//Palina Boczkowska pabo5408
//Anna Strömberg anst5816

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

public class PathFinder extends Application {

    private Stage primaryStage;
    private Pane outputArea = new Pane();
    private Button btnFindPath;
    private Button btnShowConnection;
    private Button btnNewPlace;
    private Button btnNewConnection;
    private Button btnChangeConnection;
    private ListGraph<Place> listGraph = new ListGraph<>();
    private Place place;
    private Place fromNode;
    private Place toNode;
    private Image image;
    private String mapName;
    private ImageView imageView = new ImageView();
    private boolean changed = false;


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        BorderPane root = new BorderPane();
        VBox vbox = new VBox();
        root.setTop(vbox);

        MenuBar menu = new MenuBar();
        vbox.getChildren().add(menu);
        menu.setId("menu");

        Menu menuFile = new Menu("File");
        menu.getMenus().add(menuFile);
        menuFile.setId("menuFile");

        MenuItem menuNewMap = new MenuItem("New Map");
        menuFile.getItems().add(menuNewMap);
        menuNewMap.setOnAction(new NewMapHandler());
        menuNewMap.setId("menuNewMap");


        MenuItem menuOpenFile = new MenuItem("Open");
        menuFile.getItems().add(menuOpenFile);
        menuOpenFile.setOnAction(new OpenFileHandler());
        menuOpenFile.setId("menuOpenFile");

        MenuItem menuSaveFile = new MenuItem("Save");
        menuFile.getItems().add(menuSaveFile);
        menuSaveFile.setOnAction(new SaveFileHandler());
        menuSaveFile.setId("menuSaveFile");

        MenuItem menuSaveImage = new MenuItem("Save Image");
        menuFile.getItems().add(menuSaveImage);
        menuSaveImage.setOnAction(new SaveImageHandler());
        menuSaveImage.setId("menuSaveImage");

        MenuItem menuExit = new MenuItem("Exit");
        menuFile.getItems().add(menuExit);
        menuExit.setOnAction(new ExitHandler());
        menuExit.setId("menuExit");

        HBox buttons = new HBox();
        vbox.getChildren().add(buttons);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(10, 20, 20, 20));
        buttons.setSpacing(10);


        btnFindPath = new Button("Find Path");
        btnFindPath.setOnAction(null);
        btnShowConnection = new Button("Show Connection");
        btnShowConnection.setOnAction(null);
        btnNewPlace = new Button("New place");
        btnNewPlace.setOnAction(null);
        btnNewConnection = new Button("New Connection");
        btnNewConnection.setOnAction(null);
        btnChangeConnection = new Button("Change Connection");
        btnChangeConnection.setOnAction(null);

        btnFindPath.setId("btnFindPath");
        btnShowConnection.setId("btnShowConnection");
        btnNewPlace.setId("btnNewPlace");
        btnNewConnection.setId("btnNewConnection");
        btnChangeConnection.setId("btnChangeConnection");
        buttons.getChildren().addAll(btnFindPath, btnShowConnection, btnNewPlace,
                btnNewConnection, btnChangeConnection);

        outputArea.setId("outputArea");
        root.setCenter(outputArea);
        outputArea.getChildren().add(imageView);
        Scene scene = new Scene(root);
        primaryStage.sizeToScene();
        primaryStage.setTitle("PathFinder");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(new ExitHandlerRequest());
    }


    class NewMapHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            fromNode = null;
            toNode = null;
            outputArea.getChildren().clear();
            listGraph = new ListGraph<>();
            image = new Image("file:europa.gif");
            imageView.setImage(image);
            outputArea.getChildren().add(imageView);
            primaryStage.setWidth(image.getWidth());
            primaryStage.setHeight(image.getHeight());
            btnFindPath.setOnAction(new FindPathHandler());
            btnShowConnection.setOnAction(new ShowConnectionHandler());
            btnNewPlace.setOnAction(new NewPlaceHandler());
            btnNewConnection.setOnAction(new NewConnectionHandler());
            btnChangeConnection.setOnAction(new ChangeConnectionHandler());
            changed = true;
        }
    }


    class OpenFileHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
            if (changed) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Unsaved changes, continue anyway?");
                alert.setTitle("Warning!");
                alert.setHeaderText(null);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() != ButtonType.OK) {
                    open();
                }
            }
            open();
        }
    }

    class SaveFileHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            save();
            changed = false;
        }
    }


    class SaveImageHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            try {
                WritableImage image = outputArea.snapshot(null, null);
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
                ImageIO.write(bufferedImage, "png", new File("capture.png"));
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "IO-error " + e.getMessage());
                alert.showAndWait();
            }
        }
    }

    private void save() {
        try {
            FileWriter writer = new FileWriter("europa.graph");
            PrintWriter out = new PrintWriter(writer);
            out.println(image.getUrl());
            for (Place p : listGraph.getNodes())
                out.print(p.getName() + ";" + p.getCenterX() + ";" + p.getCenterY() + ";");
            out.print("\n");
            for (Place p : listGraph.getNodes()) {
                Collection<Edge<Place>> edgeList = listGraph.getEdgesFrom(p);
                String destinationName = null;
                int weight = 0;
                String name = null;
                for (Edge<Place> e : edgeList) {
                    destinationName = e.getDestination().getName();
                    weight = e.getWeight();
                    name = e.getName();
                }
                out.println(p.getName() + ";" + destinationName + ";" + name + ";" + weight);
            }

            out.close();
            writer.close();
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Can't open file!");
            alert.showAndWait();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "IO-error: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void open() {
        try {
            fromNode = null;
            toNode = null;
            outputArea.getChildren().clear();
            listGraph = new ListGraph<>();
            FileReader reader = new FileReader("europa.graph");
            BufferedReader in = new BufferedReader(reader);
            mapName = in.readLine();
            HashMap<String, Place> map = new HashMap<>();
            Image image = new Image(mapName);
            imageView.setImage(image);
            outputArea.getChildren().add(imageView);
            primaryStage.setWidth(image.getWidth());
            primaryStage.setHeight(image.getHeight());
            String line = in.readLine();
            String[] tokens = line.split(";");
            for (int i = 0; i < tokens.length; i += 3) {
                String nodeName = tokens[i];
                double x = Double.parseDouble(tokens[i + 1]);
                double y = Double.parseDouble(tokens[i + 2]);
                place = new Place(x, y);
                place.setName(nodeName);
                place.setId(place.getName());
                listGraph.add(place);
                outputArea.getChildren().add(place);
                place.setOnMouseClicked(new ColorChanger());
                map.put(nodeName, place);
                Text text = new Text(x + 10, y, nodeName);
                text.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 10));
                text.setBoundsType(TextBoundsType.VISUAL);
                outputArea.getChildren().add(text);
            }

            while ((line = in.readLine()) != null) {
                String[] items = line.split(";");
                String fromName = items[0];
                String toName = items[1];
                String edgeName = items[2];
                int weight = Integer.parseInt(items[3]);
                Place from = map.get(fromName);
                Place to = map.get(toName);
                if(listGraph.getEdgeBetween(from, to)==null) {
                    if (from != null && to != null) {
                        listGraph.connect(from, to, edgeName, weight);
                        Line connection = new Line(from.getCenterX(), from.getCenterY(), to.getCenterX(), to.getCenterY());
                        connection.setStrokeWidth(3);
                        connection.setDisable(true);
                        outputArea.getChildren().add(connection);
                    }
                }
            }
            in.close();
            changed = true;
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Can't open file!");
            alert.showAndWait();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "IO-error " + e.getMessage());
            alert.showAndWait();
        }
    }

    class ExitHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            primaryStage.fireEvent(new WindowEvent(primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST));
        }
    }

    class ExitHandlerRequest implements EventHandler<WindowEvent> {

        @Override
        public void handle(WindowEvent event) {
            if (changed) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Unsaved changes, exit anyway?");
                alert.setTitle("Warning!");
                alert.setHeaderText(null);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() != ButtonType.OK) {
                    event.consume();
                }
            }
        }
    }

    class NewPlaceHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            outputArea.setOnMouseClicked(new ClickHandler());
            outputArea.setCursor(Cursor.CROSSHAIR);
            btnNewPlace.setDisable(true);
        }
    }

    class ClickHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
            double x = event.getX();
            double y = event.getY();
            place = new Place(x, y);
            outputArea.getChildren().add(place);
            place.setOnMouseClicked(new ColorChanger());
            listGraph.add(place);
            outputArea.setOnMouseClicked(null);
            btnNewPlace.setDisable(false);
            outputArea.setCursor(Cursor.DEFAULT);
            TextInputDialog nameBox = new TextInputDialog();
            nameBox.setTitle("Name");
            nameBox.setHeaderText(null);
            nameBox.setContentText("Name of place: ");
            Optional<String> result = nameBox.showAndWait();
            if (result.isPresent()) {
                String nameOfPlace = nameBox.getResult();
                if(nameOfPlace == null || nameOfPlace.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Error: Invalid name!");
                    alert.showAndWait();
                    changed = false;
                    return;
                }
                place.setName(nameOfPlace);
                place.setId(place.getName());
                Text text = new Text(x + 10, y, nameOfPlace);
                text.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 10));
                text.setBoundsType(TextBoundsType.VISUAL);
                outputArea.getChildren().add(text);
            }
            changed = true;
        }
    }

    class ColorChanger implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
            Place p = (Place) event.getSource();
            if (p.selected()) {
                p.setSelected(false);
                if (p.equals(fromNode)) {
                    fromNode = null;
                } else {
                    toNode = null;
                }
            } else if (fromNode == null) {
                fromNode = p;
                fromNode.setSelected(true);
            } else if (toNode == null) {
                toNode = p;
                toNode.setSelected(true);
            }
        }
    }

    class FindPathHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            boolean pathExist = listGraph.pathExists(fromNode, toNode);
            if (fromNode == null || toNode == null || !(fromNode.selected() && toNode.selected())) {
                errorTwoPlacesMustBeSelected();
                return;
            }
            if (!pathExist) {
                errorNoPath();
                return;
            }
            Place startPlace = fromNode;
            Place endPlace = toNode;

            if(toNode.selected()) {
                startPlace = toNode;
                endPlace = fromNode;
            }
            try {
                List<Edge<Place>> edges = listGraph.getPath(startPlace, endPlace);
                FindPathMessage findPathMessage = new FindPathMessage(fromNode.getName(), toNode.getName());
                StringBuilder sb = new StringBuilder();
                int total = 0;
                for (Edge<Place> e : edges) {
                    sb.append("to ").append(e.getDestination().getName()).append(" by ").append(e.getName()).
                            append(" takes ").append(e.getWeight()).append("\n");
                    total += e.getWeight();
                }
                sb.append("Total: ").append(total).append("\n");
                findPathMessage.getPathInfo().setText(sb.toString());

                Optional<ButtonType> result = findPathMessage.showAndWait();
                if (result.isPresent() && result.get() != ButtonType.OK)
                    return;
            } catch (NumberFormatException exception) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error!");
                alert.showAndWait();
            }
        }
    }

    class NewConnectionHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            Edge<Place> edge = null;
            if (fromNode != null && toNode != null) {
                edge = listGraph.getEdgeBetween(fromNode, toNode);
            }
            if (fromNode == null || toNode == null || !(fromNode.selected() && toNode.selected())) {
                errorTwoPlacesMustBeSelected();
                return;
            }
            if (edge != null) {
                errorConnectionAlreadyExist();
                return;
            }
            try {
                ConnectionAlert connectionAlert = new ConnectionAlert(fromNode.getName(), toNode.getName());
                Optional<ButtonType> result = connectionAlert.showAndWait();
                if (result.isPresent() && result.get() != ButtonType.OK)
                    return;
                String name = connectionAlert.getName();
                if(name == null || name.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Error: Invalid name!");
                    alert.showAndWait();
                    return;
                }
                int time = connectionAlert.getTime();
                listGraph.connect(fromNode, toNode, name, time);
                Line line = new Line(fromNode.getCenterX(), fromNode.getCenterY(), toNode.getCenterX(), toNode.getCenterY());
                line.setStrokeWidth(3);
                outputArea.getChildren().add(line);
                line.setDisable(true);
                place.setDisable(false);
            } catch (NumberFormatException exception) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error: Invalid time!");
                alert.showAndWait();
            }
            changed = true;
        }
    }

    class ShowConnectionHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            Edge<Place> edge = null;
            if (fromNode != null && toNode != null)
                edge = listGraph.getEdgeBetween(fromNode, toNode);
            if (fromNode == null || toNode == null || !(fromNode.selected() && toNode.selected())) {
                errorTwoPlacesMustBeSelected();
            } else if (edge == null) {
                errorNoConnection();
            } else {
                ConnectionAlert connectionAlert = new ConnectionAlert(fromNode.getName(), toNode.getName());
                connectionAlert.getNameField().setText(edge.getName());
                connectionAlert.getTimeField().setText(String.valueOf(edge.getWeight()));
                connectionAlert.getNameField().setEditable(false);
                connectionAlert.getTimeField().setEditable(false);
                Optional<ButtonType> result = connectionAlert.showAndWait();
                if (result.isPresent() && result.get() != ButtonType.OK) {
                    return;
                }
            }
        }
    }

    class ChangeConnectionHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            try {
                Edge<Place> edge = null;
                if (fromNode != null && toNode != null)
                    edge = listGraph.getEdgeBetween(fromNode, toNode);
                if (fromNode == null || toNode == null || !(fromNode.selected() && toNode.selected())) {
                    errorTwoPlacesMustBeSelected();
                    return;
                } else if (edge == null) {
                    errorNoConnection();
                    return;
                } else {
                    ConnectionAlert connectionAlert = new ConnectionAlert(fromNode.getName(), toNode.getName());
                    String nameOfEdge = edge.getName();
                    connectionAlert.getNameField().setText(nameOfEdge);
                    connectionAlert.getNameField().setEditable(false);
                    Optional<ButtonType> result = connectionAlert.showAndWait();
                    if (result.isPresent() && result.get() != ButtonType.OK)
                        return;
                    int time = connectionAlert.getTime();
                    listGraph.setConnectionWeight(fromNode, toNode, time);
                }
            } catch (NumberFormatException exception) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error: Invalid time!");
                alert.showAndWait();
            }
            changed = true;
        }
    }

    public void errorTwoPlacesMustBeSelected() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error!");
        alert.setHeaderText(null);
        alert.setContentText("Two places must be selected!");
        alert.showAndWait();
    }

    public void errorNoPath() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error!");
        alert.setHeaderText(null);
        alert.setContentText("There is no path between these places!");
        alert.showAndWait();
    }

    public void errorConnectionAlreadyExist() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error!");
        alert.setHeaderText(null);
        alert.setContentText("Connection between these places already exist!");
        alert.showAndWait();
    }

    public void errorNoConnection() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error!");
        alert.setHeaderText(null);
        alert.setContentText("There is no connection between these places!");
        alert.showAndWait();
    }

}