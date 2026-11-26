import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

public class FirstJavaFX extends Application {

  public void start(Stage stage) {
    stage.setTitle("CS400");

    Label dijkstra =
        new Label(
            "If debugging is the process of removing software bugs,\n"
                + "then programming must be the process of putting them in.\n"
                + "-- Edsger W. Dijkstra");

    Circle circle = new Circle(160, 120, 60);
    Polygon polygon = new Polygon(160, 120, 220, 280, 100, 280);

    Group group = new Group(circle, polygon, dijkstra);
    Scene scene = new Scene(group, 480, 320);
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    Application.launch(args);
    System.out.println("Goodbye.");
  }
}
