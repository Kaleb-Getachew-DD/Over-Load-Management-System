package otapp;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import static otapp.database.connect;

public class LoginMainController implements Initializable {

    @FXML
    private BorderPane login_form;
    @FXML
    private TextField user_username;
    @FXML
    private PasswordField user_password;
    @FXML
    private Button user_loginBtn;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    public String U;
    
    

    @FXML
    public void loginAccount() throws SQLException, IOException {
        String sql = "SELECT username, password, position FROM user_db WHERE username= ? AND password= ?";

        connect = (Connection) database.connect();
        
        try {
            Alert alert;
            if (!user_password.getText().isEmpty() && !user_username.getText().isEmpty()) {
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, user_username.getText());
                prepare.setString(2, user_password.getText());

                result = prepare.executeQuery();

                if (result.next()) {

                   

                    // if correct username and password
                    String position = result.getString("position");
                    String username = result.getString("username");
                    
                    // Save the username to UserSession
                    UserSession.getInstance().setUsername(username);

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Logged In");
                    // alert.showAndWait();

                    // To hide the login page
                    user_loginBtn.getScene().getWindow().hide();

                    // To Display the respective page based on position
                    String fxmlFile = "";
                    switch (position) {
                        case "Coordinator":
                            fxmlFile = "admin.fxml";
                            break;
                        case "Teacher":
                            fxmlFile = "teacher.fxml";
                            break;
                        case "DepHead":
                            fxmlFile = "Head.fxml";
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + position);
                    }

                    Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);

                    stage.setScene(scene);
                    stage.setTitle(position + " Page");
                    stage.show();

                } else {
                    // if incorrect username and password
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Incorrect Username/Password");
                    alert.showAndWait();
                }

            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please Fill All Blank Fields");
                alert.showAndWait();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (result != null) {
                try {
                    result.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (prepare != null) {
                try {
                    prepare.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connect != null) {
                try {
                    connect.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
