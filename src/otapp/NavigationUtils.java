
package otapp;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class NavigationUtils {
     public static void goToLoginPage(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationUtils.class.getResource("loginMain.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
