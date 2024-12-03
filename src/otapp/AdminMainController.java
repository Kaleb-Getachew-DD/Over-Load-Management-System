package otapp;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class AdminMainController implements Initializable {

    @FXML
    private TextField user_id;
    @FXML
    private TextField user_fullname;
    @FXML
    private ComboBox<String> user_gender;
    @FXML
    private TextField user_department;
    @FXML
    private TextField user_salary;
    @FXML
    private ComboBox<String> user_position;
    @FXML
    private TextField user_username;
    @FXML
    private TextField user_password;
    @FXML
    private Button user_addbtn;
    @FXML
    private Button user_updatebtn;
    @FXML
    private Button user_clearbtn;
    @FXML
    private Button user_deletebtn;
    @FXML
    private Button course_addbtn;

    @FXML
    private Button course_clearbtn;

    @FXML
    private TableColumn<CourseData, String> course_col_cr;

    @FXML
    private TableColumn<CourseData, String> course_col_id;

    @FXML
    private TableColumn<CourseData, String> course_col_name;

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
    private ComboBox<String> course_cr;

    @FXML
    private Button course_deletebtn;

    @FXML
    private TextField course_id;

    @FXML
    private TextField course_name;

    @FXML
    private TableView<CourseData> course_tableview;

    @FXML
    private Button course_updatebtn;

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
    private Button backToLoginBtn;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private Statement statement;

    private Alert alert;

    @FXML
    private void handleBackToLogin(ActionEvent event) {
        Stage stage = (Stage) backToLoginBtn.getScene().getWindow();
        NavigationUtils.goToLoginPage(stage);
    }

    @FXML
    public void user_addbtn(ActionEvent event) throws SQLException {

        connect = database.connect();

        try {

            if (user_id.getText().isEmpty()
                    || user_fullname.getText().isEmpty()
                    || user_department.getText().isEmpty()
                    || user_gender.getSelectionModel().getSelectedItem() == null
                    || user_position.getSelectionModel().getSelectedItem() == null
                    || user_salary.getText().isEmpty()
                    || user_username.getText().isEmpty()
                    || user_password.getText().isEmpty()) {

                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();

            } else {

                String checkId = "SELECT user_id FROM user_db WHERE user_id = ?";
                prepare = connect.prepareStatement(checkId);
                prepare.setString(1, user_id.getText());
                result = prepare.executeQuery();

                if (result.next()) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("User Id: " + user_id.getText() + " is already taken");
                    alert.showAndWait();
                } else {
                    String checkUsername = "SELECT username FROM user_db WHERE username = ?";
                    prepare = connect.prepareStatement(checkUsername);
                    prepare.setString(1, user_username.getText());
                    result = prepare.executeQuery();

                    if (result.next()) {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Username: " + user_username.getText() + " is already taken");
                        alert.showAndWait();
                    } else {
                        String insertData = "INSERT INTO user_db (user_id, full_name, gender, department, position, salary, username, password) VALUES(?,?,?,?,?,?,?,?)";
                        prepare = connect.prepareStatement(insertData);
                        prepare.setString(1, user_id.getText());
                        prepare.setString(2, user_fullname.getText());
                        prepare.setString(3, user_gender.getSelectionModel().getSelectedItem());
                        prepare.setString(4, user_department.getText());
                        prepare.setString(5, user_position.getSelectionModel().getSelectedItem());
                        prepare.setInt(6, Integer.parseInt(user_salary.getText()));
                        prepare.setString(7, user_username.getText());
                        prepare.setString(8, user_password.getText());

                        prepare.executeUpdate();

                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Message");
                        alert.setHeaderText(null);
                        alert.setContentText("User Added Successfully!");
                        alert.showAndWait();

                        // Update table view
                        userShowData();
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Error: " + e.getMessage());
            alert.showAndWait();
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
    }

    public ObservableList<UserData> userListData() throws SQLException {

        ObservableList<UserData> listData = FXCollections.observableArrayList();

        String selectData = "SELECT * FROM user_db";

        connect = database.connect();

        try {

            prepare = connect.prepareStatement(selectData);
            result = prepare.executeQuery();

            UserData uData;

            while (result.next()) {
                uData = new UserData(result.getString("user_id"),
                        result.getString("full_name"),
                        result.getString("department"),
                        result.getString("gender"),
                        result.getString("position"),
                        result.getInt("salary"),
                        result.getString("username"),
                        result.getString("password"));

                listData.add(uData);

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

    private ObservableList<UserData> UserData;

    public void userShowData() throws SQLException {
        UserData = userListData();

        user_col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        user_col_fullname.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        user_col_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        user_col_department.setCellValueFactory(new PropertyValueFactory<>("department"));
        user_col_position.setCellValueFactory(new PropertyValueFactory<>("position"));
        user_col_salary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        user_col_username.setCellValueFactory(new PropertyValueFactory<>("username"));
        user_col_password.setCellValueFactory(new PropertyValueFactory<>("password"));

        user_tableView.setItems(UserData);
    }

    @FXML
    public void userSelectedData() {
        UserData uData = user_tableView.getSelectionModel().getSelectedItem();
        int num = user_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }

        user_id.setText(String.valueOf(uData.getId()));
        user_department.setText(String.valueOf(uData.getDepartment()));
        user_fullname.setText(String.valueOf(uData.getFullName()));
        user_gender.setValue(uData.getGender());
        user_position.setValue(uData.getPosition());
        user_salary.setText(String.valueOf(uData.getSalary()));
        user_username.setText(uData.getUsername());
        user_password.setText(uData.getPassword());
    }

    @FXML
    public void user_updatebtn(ActionEvent event) throws SQLException {
        UserData selectedUser = user_tableView.getSelectionModel().getSelectedItem();

        if (selectedUser == null) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please select a user to update");
            alert.showAndWait();
            return;
        }

        if (user_id.getText().isEmpty()
                || user_fullname.getText().isEmpty()
                || user_department.getText().isEmpty()
                || user_gender.getSelectionModel().getSelectedItem() == null
                || user_position.getSelectionModel().getSelectedItem() == null
                || user_salary.getText().isEmpty()
                || user_username.getText().isEmpty()
                || user_password.getText().isEmpty()) {

            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
            return;
        }

        connect = database.connect();

        try {
            String updateData = "UPDATE user_db SET full_name = ?, gender = ?, department = ?, position = ?, salary = ?, username = ?, password = ? WHERE user_id = ?";
            prepare = connect.prepareStatement(updateData);
            prepare.setString(1, user_fullname.getText());
            prepare.setString(2, user_gender.getSelectionModel().getSelectedItem());
            prepare.setString(3, user_department.getText());
            prepare.setString(4, user_position.getSelectionModel().getSelectedItem());
            prepare.setInt(5, Integer.parseInt(user_salary.getText()));
            prepare.setString(6, user_username.getText());
            prepare.setString(7, user_password.getText());
            prepare.setString(8, user_id.getText());

            int rowsUpdated = prepare.executeUpdate();

            if (rowsUpdated > 0) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("User updated successfully!");
                alert.showAndWait();

                // Refresh the table view to show updated data
                userShowData();
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Failed to update user. Please try again.");
                alert.showAndWait();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Error: " + e.getMessage());
            alert.showAndWait();
        } finally {
            if (prepare != null) {
                prepare.close();
            }
            if (connect != null) {
                connect.close();
            }
        }
    }

    @FXML
public void user_deletebtn(ActionEvent event) throws SQLException {
    // Get the selected user
    UserData selectedUser = user_tableView.getSelectionModel().getSelectedItem();

    if (selectedUser == null) {
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Message");
        alert.setHeaderText(null);
        alert.setContentText("Please select a user to delete");
        alert.showAndWait();
        return;
    }

    // Confirm deletion
    alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Confirmation Message");
    alert.setHeaderText(null);
    alert.setContentText("Are you sure you want to delete user and the associated schedule: " + selectedUser.getFullName() + "?");

    if (alert.showAndWait().get() != ButtonType.OK) {
        return;
    }

    // Delete user data from the database
    connect = database.connect();

    PreparedStatement deleteUserStmt = null;
    PreparedStatement deleteScheduleStmt = null;

    try {
        // Delete the user
        String deleteUserSQL = "DELETE FROM user_db WHERE user_id = ?";
        deleteUserStmt = connect.prepareStatement(deleteUserSQL);
        deleteUserStmt.setString(1, selectedUser.getId());
        int userRowsDeleted = deleteUserStmt.executeUpdate();

        // Delete the schedules associated with the user
        String deleteScheduleSQL = "DELETE FROM schedule_db WHERE instructor = ?";
        deleteScheduleStmt = connect.prepareStatement(deleteScheduleSQL);
        deleteScheduleStmt.setString(1, selectedUser.getFullName());
        deleteScheduleStmt.executeUpdate();

        if (userRowsDeleted > 0) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Message");
            alert.setHeaderText(null);
            alert.setContentText("User and associated schedules deleted successfully!");
            alert.showAndWait();

            // Refresh the table view and Schedule view to remove the deleted data
            userShowData();
            scheduleShowData();
        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Failed to delete user. Please try again.");
            alert.showAndWait();
        }

    } catch (SQLException e) {
        e.printStackTrace();
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Message");
        alert.setHeaderText(null);
        alert.setContentText("Error: " + e.getMessage());
        alert.showAndWait();
    } finally {
        if (deleteUserStmt != null) {
            deleteUserStmt.close();
        }
        if (deleteScheduleStmt != null) {
            deleteScheduleStmt.close();
        }
        if (connect != null) {
            connect.close();
        }
    }
}


    @FXML
    public void user_clearbtn(ActionEvent event) {
        // Clear all text fields
        user_id.clear();
        user_fullname.clear();
        user_department.clear();
        user_salary.clear();
        user_username.clear();
        user_password.clear();

        // Clear all combo boxes
        user_gender.getSelectionModel().clearSelection();
        user_position.getSelectionModel().clearSelection();
    }

    private String[] genderList = {"Male", "Female"};

    @FXML
    public void userGenderList() {
        List<String> GList = new ArrayList<>();

        for (String data : genderList) {
            GList.add(data);
        }

        ObservableList<String> listData = FXCollections.observableArrayList(GList);
        user_gender.setItems(listData);
    }

    private final String[] positionList = {"Teacher", "DepHead", "Coordinator"};

    @FXML
    public void userPositionList() {
        List<String> PList = new ArrayList<>();

        for (String data : positionList) {
            PList.add(data);
        }

        ObservableList<String> listData = FXCollections.observableArrayList(PList);
        user_position.setItems(listData);
    }

    /* ###################################################################*/
    @FXML
    public void course_addbtn(ActionEvent event) throws SQLException {

        connect = database.connect();

        try {

            if (course_id.getText().isEmpty()
                    || course_name.getText().isEmpty()
                    || course_cr.getSelectionModel().getSelectedItem() == null) {

                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();

            } else {

                String checkData = "SELECT course_id FROM course_db WHERE course_id = ?";
                prepare = connect.prepareStatement(checkData);
                prepare.setString(1, course_id.getText());
                result = prepare.executeQuery();

                if (result.next()) {

                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Course Id: " + course_id.getText() + " is already taken");
                    alert.showAndWait();

                } else {

                    String insertData = "INSERT INTO course_db (course_id, course_name, CreditHour) VALUES(?,?,?)";
                    prepare = connect.prepareStatement(insertData);
                    prepare.setString(1, course_id.getText());
                    prepare.setString(2, course_name.getText());
                    prepare.setString(3, course_cr.getSelectionModel().getSelectedItem());

                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Course Added Successfully!");
                    alert.showAndWait();

                    // Update course table view
                    courseShowData();

                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Error: " + e.getMessage());
            alert.showAndWait();
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
    }

    @FXML
    public void course_updatebtn(ActionEvent event) throws SQLException {
        // Get the selected course from the table
        CourseData selectedCourse = course_tableview.getSelectionModel().getSelectedItem();

        if (selectedCourse == null) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please select a course to update");
            alert.showAndWait();
            return;
        }

        if (course_id.getText().isEmpty()
                || course_name.getText().isEmpty()
                || course_cr.getSelectionModel().getSelectedItem() == null) {

            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
            return;
        }

        connect = database.connect();

        try {
            String updateData = "UPDATE course_db SET course_name = ?, creditHour = ? WHERE course_id = ?";
            prepare = connect.prepareStatement(updateData);
            prepare.setString(1, course_name.getText());
            prepare.setString(2, course_cr.getSelectionModel().getSelectedItem());
            prepare.setString(3, course_id.getText());

            int rowsUpdated = prepare.executeUpdate();

            if (rowsUpdated > 0) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Course updated successfully!");
                alert.showAndWait();

                // Refresh the table view to show updated course data
                courseShowData();
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Failed to update course. Please try again.");
                alert.showAndWait();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Error: " + e.getMessage());
            alert.showAndWait();
        } finally {
            if (prepare != null) {
                prepare.close();
            }
            if (connect != null) {
                connect.close();
            }
        }
    }

    @FXML
    public void course_deletebtn(ActionEvent event) throws SQLException {
        // Get the selected course from the table
        CourseData selectedCourse = course_tableview.getSelectionModel().getSelectedItem();

        if (selectedCourse == null) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please select a course to delete");
            alert.showAndWait();
            return;
        }

        // Confirm deletion
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete course: " + selectedCourse.getCname() + "?");

        if (alert.showAndWait().get() != ButtonType.OK) {
            return;
        }

        connect = database.connect();

        try {
            String deleteData = "DELETE FROM course_db WHERE course_id = ?";
            prepare = connect.prepareStatement(deleteData);
            prepare.setString(1, selectedCourse.getCid());

            int rowsDeleted = prepare.executeUpdate();

            if (rowsDeleted > 0) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Course deleted successfully!");
                alert.showAndWait();

                // Refresh the table view to remove the deleted data
                courseShowData();
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Failed to delete course. Please try again.");
                alert.showAndWait();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Error: " + e.getMessage());
            alert.showAndWait();
        } finally {
            if (prepare != null) {
                prepare.close();
            }
            if (connect != null) {
                connect.close();
            }
        }
    }

    @FXML
    public void course_clearbtn(ActionEvent event) {
        // Clear all text fields
        course_id.clear();
        course_name.clear();

        // Clear the combo box
        course_cr.getSelectionModel().clearSelection();
        
    }

    @FXML
    public void courseSelectedData() {
        CourseData cData = course_tableview.getSelectionModel().getSelectedItem();
        int num = course_tableview.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }

        course_id.setText(String.valueOf(cData.getCid()));
        course_name.setText(String.valueOf(cData.getCname()));
        //course_cr.setValue(cData.getCr());
        course_cr.setValue(String.valueOf(cData.getCr()));

    }

    public ObservableList<CourseData> courseListData() throws SQLException {

        ObservableList<CourseData> listData = FXCollections.observableArrayList();

        String selecteData = "SELECT * FROM course_db";

        connect = database.connect();

        try {

            prepare = connect.prepareStatement(selecteData);
            result = prepare.executeQuery();

            CourseData cData;

            while (result.next()) {
                cData = new CourseData(result.getString("course_id"),
                        result.getString("course_name"),
                        result.getString("creditHour"));

                listData.add(cData);

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

    private ObservableList<CourseData> CourseData;

    public void courseShowData() throws SQLException {
        CourseData = courseListData();

        course_col_id.setCellValueFactory(new PropertyValueFactory<>("Cid"));
        course_col_name.setCellValueFactory(new PropertyValueFactory<>("Cname"));
        course_col_cr.setCellValueFactory(new PropertyValueFactory<>("Cr"));

        course_tableview.setItems(CourseData);
    }

    private final String[] crList = {"1", "2", "3", "4", "5"};

    @FXML
    public void credithourList() {
        List<String> CList = new ArrayList<>();
        CList.addAll(Arrays.asList(crList));

        ObservableList<String> listData = FXCollections.observableArrayList(CList);
        course_cr.setItems(listData);
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
                sData = new ScheduleData(result.getString("day"),
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
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userGenderList();
        userPositionList();
        credithourList();
        try {
            userShowData();
            courseShowData();
            scheduleShowData();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void userSelectData(MouseEvent event) {
        userSelectedData();
    }

}
