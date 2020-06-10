import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.List;

public class Minidock extends Application {

    String Location;
    float Opacity;
    int Red;
    int Green;
    int Blue;
    List<DockApplication> DockApps;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Set Minidock Properties
        ReadMiniDockProperties();

        // Create dock
        primaryStage.setTitle("minidock");
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setAlwaysOnTop(true);

        // Create 1 row grid
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setId("grid");

        String grid_style = "-fx-background-color: rgba("+Red + ","+Green+","+Blue+","+ Opacity+ ");";
        grid_style += " -fx-border-radius: 5px";
        grid.setStyle(grid_style);

        // Insert applications
        LoadDockApplications(grid);

        // Set Dock Position
        Scene scene = SetDockPositionAndSize(grid, primaryStage);

        // launch stage
        primaryStage.setScene(scene);
        scene.getStylesheets().add
                (Minidock.class.getResource("Dock.css").toExternalForm());
        primaryStage.show();
    }

    public void ReadMiniDockProperties(){
        // Do some logic to read in a local JSOn file

        // Setting temp values for now
        Location = "UP";
        Opacity = 0.2f;
        DockApps = new ArrayList<DockApplication>();
        Red = 223;
        Green = 223;
        Blue = 222;

        // Add some dock apps
        DockApplication vivaldi = new DockApplication("Vivaldi", "Q:\\programs\\Vivaldi\\Application\\vivaldi.exe");
        DockApplication idle = new DockApplication("IDLE", "Q:\\programs\\Python\\pythonw.exe \"Q:\\programs\\Python\\Lib\\idlelib\\idle.pyw");
        DockApplication ghost = new DockApplication("Ghost", "C:\\Users\\carlo\\AppData\\Local\\Ghost\\Ghost.exe");
        DockApps.add(vivaldi);
        DockApps.add(idle);
        DockApps.add(ghost);
    }

    public void LoadDockApplications(GridPane grid){
        int currColumn = 1;

        for (DockApplication a: DockApps) {
            // Design the Button
            Button btn = new Button();
            HBox hbBtn = new HBox(0);
            hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
            hbBtn.getChildren().add(btn);
            grid.add(hbBtn, currColumn++, 1);
            btn.setOpacity(1);
            btn.setStyle("-fx-background-color: " + a.accent + ";");

            if(!a.UseGenericImage){
                ImageView v = new ImageView((Image) a.ico);
                btn.setGraphic(v);
            }
            else{
                btn.setText(a.name);
            }

            // Add the event handler
            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                   a.Run();
                }
            });
        }
    }

    public Scene SetDockPositionAndSize(GridPane grid, Stage primary){
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        int MinUnit = 50;
        int width = 0;
        int height = 0;

        // Calculate Size of dock
        if(Location.equals("UP") || Location.equals("DOWN")){
            height = MinUnit;
            width = DockApps.size() * MinUnit;
        }
        else{
            height = DockApps.size() * MinUnit;
            width = MinUnit;
        }
        Scene scene = new Scene(grid, width, height);

        // Calculate Dock Location
        if(Location.equals("UP")){
            primary.setX(bounds.getWidth() / 2 - width);
            primary.setY(0);
        }
        else if(Location.equals("DOWN")){
            primary.setX(bounds.getWidth() / 2 - width);
            primary.setY(bounds.getHeight() - MinUnit);
        }
        else if(Location.equals("RIGHT")){
            primary.setX(bounds.getWidth() - MinUnit);
            primary.setY(bounds.getHeight() / 2 - height);
        }
        else{
            primary.setX(0);
            primary.setY(bounds.getHeight() / 2 - height);
        }

        scene.setFill(Color.TRANSPARENT);
        return scene;
    }

}
