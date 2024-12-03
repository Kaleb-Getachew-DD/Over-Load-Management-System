package otapp;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class TeacherController implements Initializable {

    @FXML
    private TextField username_update;
    @FXML
    private TextField username_password;
    @FXML
    private Button username_updatebtn1;
    @FXML
    private Button username_clearbtn1;
    @FXML
    private TableView<UserData> user_tableView;
    @FXML
    private TableColumn<UserData, String> user_col_id;
    @FXML
    private TableColumn<UserData, String> user_col_fullname;
    @FXML
    private TableColumn<UserData, String> user_col_gender;
    @FXML
    private TableColumn<UserData, String> user_col_department;
    @FXML
    private TableColumn<UserData, String> user_col_position;
    @FXML
    private TableColumn<UserData, Integer> user_col_salary;
    @FXML
    private TableColumn<UserData, String> user_col_username;
    @FXML
    private TableColumn<UserData, String> user_col_password;

    @FXML
    private TableView<ScheduleData> sch_tableview;
    @FXML
    private TableColumn<ScheduleData, String> sch_col_day;
    @FXML
    private TableColumn<ScheduleData, String> sch_col_time;
    @FXML
    private TableColumn<ScheduleData, String> sch_col_course;
    @FXML
    private TableColumn<ScheduleData, String> sch_col_instructor;
    @FXML
    private Button backToLoginBtn;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    private ObservableList<UserData> userDataList = FXCollections.observableArrayList();
//    private ObservableList<ScheduleData> scheduleDataList = FXCollections.observableArrayList(); 
    
    @FXML
    private void handleBackToLogin(ActionEvent event) {
        Stage stage = (Stage) backToLoginBtn.getScene().getWindow();
        NavigationUtils.goToLoginPage(stage);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String username = UserSession.getInstance().getUsername();
        try {
            loadUserData(username);
            scheduleShowData();
            user_tableView.setOnMouseClicked(event -> handleRowSelect());
            username_updatebtn1.setOnAction(event -> handleUpdate());
            username_clearbtn1.setOnAction(event -> clearFields());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadUserData(String username) throws SQLException {
        String sql = "SELECT * FROM user_db WHERE username = ?";

        connect = database.connect();
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, username);

            result = prepare.executeQuery();

            while (result.next()) {
                userDataList.add(new UserData(
                    result.getString("user_id"),
                    result.getString("full_name"),
                    result.getString("department"),
                    result.getString("gender"),
                    result.getString("position"),
                    result.getInt("salary"),
                    result.getString("username"),
                    result.getString("password")
                ));
            }

            user_col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            user_col_fullname.setCellValueFactory(new PropertyValueFactory<>("fullName"));
            user_col_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
            user_col_department.setCellValueFactory(new PropertyValueFactory<>("department"));
            user_col_position.setCellValueFactory(new PropertyValueFactory<>("position"));
            user_col_salary.setCellValueFactory(new PropertyValueFactory<>("salary"));
            user_col_username.setCellValueFactory(new PropertyValueFactory<>("username"));
            user_col_password.setCellValueFactory(new PropertyValueFactory<>("password"));

            user_tableView.setItems(userDataList);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabaseResources();
        }
    }

    //    #########################################################
    
     public ObservableList<ScheduleData> scheduleListData() throws SQLException {

        ObservableList<ScheduleData> listData = FXCollections.observableArrayList();

        String selectData = "SELECT * FROM schedule_db";

        connect = database.connect();

        try {

            prepare = connect.prepareStatement(selectData);
            result = prepare.executeQuery();

            ScheduleData sData;

            while (result.next()) {
                sData = new ScheduleData( result.getString("day"),
                        result.getString("time"),
                        result.getString("instructor"),
                        result.getString("course"),
                        result.getInt("id"));

                listData.add(sData);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (result != null) {
                result.close();
            }
            if (prepare != null) {
                prepare.close();
            }
            if (connect != null) {
                connect.close();
            }
        }
        return listData;
    }

    private ObservableList<ScheduleData> ScheduleData;

    public void scheduleShowData() throws SQLException {
        ScheduleData = scheduleListData();

        sch_col_day.setCellValueFactory(new PropertyValueFactory<>("Sday"));
        sch_col_time.setCellValueFactory(new PropertyValueFactory<>("Stime"));
        sch_col_course.setCellValueFactory(new PropertyValueFactory<>("Scourse"));
        sch_col_instructor.setCellValueFactory(new PropertyValueFactory<>("Sinstructor"));

        sch_tableview.setItems(ScheduleData);
    }
    
//    #######################################################

    private void handleRowSelect() {
        UserData selectedUser = user_tableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            username_update.setText(selectedUser.getUsername());
            username_password.setText(selectedUser.getPassword());
        }
    }

    private void handleUpdate() {
        UserData selectedUser = user_tableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            String newUsername = username_update.getText();
            String newPassword = username_password.getText();

            if (!newUsername.isEmpty() && !newPassword.isEmpty()) {
                try {
                    updateUserData(selectedUser.getId(), newUsername, newPassword);
                    selectedUser.setUsername(newUsername);
                    selectedUser.setPassword(newPassword);
                    user_tableView.refresh();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Update Successful");
                    alert.setHeaderText(null);
                    alert.setContentText("Username and password updated successfully.");
                    alert.showAndWait();
                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert("Update Failed", "Failed to update username and password.");
                }
            } else {
                showAlert("Input Error", "Username and password cannot be empty.");
            }
        }
    }

    private void updateUserData(String userId, String newUsername, String newPassword) throws SQLException {
        String sql = "UPDATE user_db SET username = ?, password = ? WHERE user_id = ?";

        connect = database.connect();
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, newUsername);
            prepare.setString(2, newPassword);
            prepare.setString(3, userId);

            prepare.executeUpdate();
        } finally {
            closeDatabaseResources();
        }
    }

    private void clearFields() {
        username_update.clear();
        username_password.clear();
        user_tableView.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeDatabaseResources() {
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
