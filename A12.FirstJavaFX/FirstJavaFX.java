import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

public class FirstJavaFX extends Application {

  public void start(Stage stage) {
    Group group = new Group();
    Scene scene = new Scene(group, 480, 320);
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    Application.launch(args);
    System.out.println("Goodbye.");
  }
}
