/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author KALEB
 */
public class HeadController implements Initializable {

    @FXML
    private Button add_schedulebtn;
    @FXML
    private Button clear_schdulebtn;
    @FXML
    private Button update_schdulebtn;
    @FXML
    private TableView<UserData> user_tableview;
    @FXML
    private TextField username_update;
    @FXML
    private TextField password_update;
    @FXML
    private Button username_updatebtn1;
    @FXML
    private Button username_clearbtn1;
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
    private ComboBox<String> sch_day;
    @FXML
    private ComboBox<String> sch_time;
    @FXML
    private ComboBox<String> sch_instructor_combo;
    @FXML
    private ComboBox<String> sch_course;
    @FXML
    private Button delete_schdulebtn;
    @FXML
    private Button backToLoginBtn;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private Statement statement;

    private ObservableList<UserData> userDataList = FXCollections.observableArrayList();

    private Alert alert;

    public void loadInstructorNames() throws SQLException {
        ObservableList<String> instructorList = FXCollections.observableArrayList();
        String selectInstructors = "SELECT full_name FROM user_db WHERE position = 'teacher'";

        connect = database.connect();

        try {
            prepare = connect.prepareStatement(selectInstructors);
            result = prepare.executeQuery();

            while (result.next()) {
                instructorList.add(result.getString("full_name"));
            }

            sch_instructor_combo.setItems(instructorList);

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
    }

    public void loadCourseNames() throws SQLException {
        ObservableList<String> courseList = FXCollections.observableArrayList();
        String selectCourses = "SELECT course_name FROM course_db ";

        connect = database.connect();

        try {
            prepare = connect.prepareStatement(selectCourses);
            result = prepare.executeQuery();

            while (result.next()) {
                courseList.add(result.getString("course_name"));
            }

            sch_course.setItems(courseList);

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
    }

    private int calculateCreditHours(String timeRange) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
        String[] times = timeRange.split("-");
        LocalTime startTime = LocalTime.parse(times[0].trim(), formatter);
        LocalTime endTime = LocalTime.parse(times[1].trim(), formatter);
        Duration duration = Duration.between(startTime, endTime);
        long minutes = duration.toMinutes();
        return (int) (minutes / 60); // Assuming 1 credit hour per full hour
    }

    @FXML
    private void handleBackToLogin(ActionEvent event) {
        Stage stage = (Stage) backToLoginBtn.getScene().getWindow();
        NavigationUtils.goToLoginPage(stage);
    }

    @FXML
    public void schedule_addbtn(ActionEvent event) throws SQLException {
        connect = database.connect();

        try {
            if (sch_instructor_combo.getSelectionModel().getSelectedItem() == null
                    || sch_course.getSelectionModel().getSelectedItem() == null
                    || sch_time.getSelectionModel().getSelectedItem() == null
                    || sch_day.getSelectionModel().getSelectedItem() == null) {

                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();

            } else {
                String instructor = sch_instructor_combo.getSelectionModel().getSelectedItem();
                String checkData = "SELECT * FROM schedule_db WHERE day = ? AND time = ? AND instructor = ?";
                prepare = connect.prepareStatement(checkData);
                prepare.setString(1, sch_day.getSelectionModel().getSelectedItem());
                prepare.setString(2, sch_time.getSelectionModel().getSelectedItem());
                prepare.setString(3, instructor);
                result = prepare.executeQuery();

                if (result.next()) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Instructor " + instructor + " already has a schedule on the assigned period");
                    alert.showAndWait();

                } else {
                    // Calculate the new credit hours
                    int newCreditHours = calculateCreditHours(sch_time.getSelectionModel().getSelectedItem());

                    // Get current credit hours and salary for the instructor
                    String getCurrentData = "SELECT credithour, salary FROM user_db WHERE full_name = ?";
                    prepare = connect.prepareStatement(getCurrentData);
                    prepare.setString(1, instructor);
                    result = prepare.executeQuery();

                    int currentCreditHours = 0;
                    int currentSalary = 0;
                    if (result.next()) {
                        currentCreditHours = result.getInt("credithour");
                        currentSalary = result.getInt("salary");
                    }

                    int totalCreditHours = currentCreditHours + newCreditHours;

                    // Check if total credit hours exceed 9
                    if (totalCreditHours > 9) {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Instructor " + instructor + " cannot be assigned more than 9 credit hours.");
                        alert.showAndWait();
                        return;
                    }

                    // Update the schedule
                    String insertData = "INSERT INTO schedule_db (day, time, course, instructor) VALUES(?,?,?,?)";
                    prepare = connect.prepareStatement(insertData);
                    prepare.setString(1, sch_day.getSelectionModel().getSelectedItem());
                    prepare.setString(2, sch_time.getSelectionModel().getSelectedItem());
                    prepare.setString(3, sch_course.getSelectionModel().getSelectedItem());
                    prepare.setString(4, instructor);
                    prepare.executeUpdate();

                    // Update the credit hours for the instructor
                    String updateCredits = "UPDATE user_db SET credithour = ? WHERE full_name = ?";
                    prepare = connect.prepareStatement(updateCredits);
                    prepare.setInt(1, totalCreditHours);
                    prepare.setString(2, instructor);
                    prepare.executeUpdate();

                    // Adjust the salary based on the new credit hours
                    double increasePercentage = 0.0;
                    if (totalCreditHours > 5) {
                        if (totalCreditHours <= 6) {
                            increasePercentage = 0.10;
                        } else if (totalCreditHours <= 8) {
                            increasePercentage = 0.12;
                        } else if (totalCreditHours <= 9) {
                            increasePercentage = 0.14;
                        }

                        int newSalary = (int) (currentSalary * (1 + increasePercentage));
                        String updateSalary = "UPDATE user_db SET salary = ? WHERE full_name = ?";
                        prepare = connect.prepareStatement(updateSalary);
                        prepare.setInt(1, newSalary);
                        prepare.setString(2, instructor);
                        prepare.executeUpdate();
                    }

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Schedule Added Successfully!");
                    alert.showAndWait();

                    // Update table view
                    scheduleShowData();
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

    @FXML
    public void scheduleSelectedData() {
        ScheduleData sData = sch_tableview.getSelectionModel().getSelectedItem();
        int num = sch_tableview.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }

        //sch_instructor.setText(sData.getSinstructor());
        sch_course.setValue(sData.getScourse());
        sch_day.setValue(sData.getSday());
        sch_time.setValue(sData.getStime());
        sch_instructor_combo.setValue(sData.getSinstructor());

    }

    @FXML
    public void schedule_updatebtn(ActionEvent event) throws SQLException {
        ScheduleData selectedSchedule = sch_tableview.getSelectionModel().getSelectedItem();

        if (selectedSchedule == null) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please select a schedule to update");
            alert.showAndWait();
            return;
        }

        if (sch_instructor_combo.getSelectionModel().getSelectedItem() == null
                || sch_course.getSelectionModel().getSelectedItem() == null
                || sch_time.getSelectionModel().getSelectedItem() == null
                || sch_day.getSelectionModel().getSelectedItem() == null) {

            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
            return;
        }

        connect = database.connect();

        try {
            String updateData = "UPDATE schedule_db SET instructor = ?, course = ?, day = ?, time = ? WHERE id = ?";
            prepare = connect.prepareStatement(updateData);
            prepare.setString(1, sch_instructor_combo.getSelectionModel().getSelectedItem());
            prepare.setString(2, sch_course.getSelectionModel().getSelectedItem());
            prepare.setString(3, sch_day.getSelectionModel().getSelectedItem());
            prepare.setString(4, sch_time.getSelectionModel().getSelectedItem());
            prepare.setInt(5, selectedSchedule.getId());

            int rowsUpdated = prepare.executeUpdate();

            if (rowsUpdated > 0) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Schedule updated successfully!");
                alert.showAndWait();

                // Refresh the table view to show updated data
                scheduleShowData();
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Failed to update schedule. Please try again.");
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
    public void schedule_clearbtn(ActionEvent event) {

        // Clear the combo box
        sch_day.getSelectionModel().clearSelection();
        sch_time.getSelectionModel().clearSelection();
        sch_instructor_combo.getSelectionModel().clearSelection();
        sch_course.getSelectionModel().clearSelection();
    }

    @FXML
    public void schedule_deletebtn(ActionEvent event) throws SQLException {
        // Get the selected schedule from the table
        ScheduleData selectedSchedule = sch_tableview.getSelectionModel().getSelectedItem();

        if (selectedSchedule == null) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please select a schedule to delete");
            alert.showAndWait();
            return;
        }

        // Confirm deletion
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete schedule: " + selectedSchedule.getScourse() + "?");

        if (alert.showAndWait().get() != ButtonType.OK) {
            return;
        }

        connect = database.connect();

        try {
            // Get the instructor's current credit hours and salary
            String instructor = selectedSchedule.getSinstructor();
            String getCurrentData = "SELECT credithour, salary FROM user_db WHERE full_name = ?";
            prepare = connect.prepareStatement(getCurrentData);
            prepare.setString(1, instructor);
            result = prepare.executeQuery();

            int currentCreditHours = 0;
            int currentSalary = 0;
            if (result.next()) {
                currentCreditHours = result.getInt("credithour");
                currentSalary = result.getInt("salary");
            }

            // Calculate the credit hours for the deleted schedule
            int deletedCreditHours = calculateCreditHours(selectedSchedule.getStime());

            // Calculate the new credit hours
            int totalCreditHours = currentCreditHours - deletedCreditHours;

            // Calculate the new salary only if totalCreditHours are above 5
            int newSalary = currentSalary;
            if (totalCreditHours > 5) {
                double decreasePercentage = 0.0;
                if (totalCreditHours <= 6) {
                    decreasePercentage = 0.10;
                } else if (totalCreditHours <= 8) {
                    decreasePercentage = 0.12;
                } else if (totalCreditHours <= 9) {
                    decreasePercentage = 0.14;
                }

                newSalary = (int) (currentSalary / (1 + decreasePercentage));
            }

            // Update the credit hours and salary for the instructor
            String updateUser = "UPDATE user_db SET credithour = ?, salary = ? WHERE full_name = ?";
            prepare = connect.prepareStatement(updateUser);
            prepare.setInt(1, totalCreditHours);
            prepare.setInt(2, newSalary);
            prepare.setString(3, instructor);
            prepare.executeUpdate();

            // Delete the schedule
            String deleteData = "DELETE FROM schedule_db WHERE id = ?";
            prepare = connect.prepareStatement(deleteData);
            prepare.setInt(1, selectedSchedule.getId());

            int rowsDeleted = prepare.executeUpdate();

            if (rowsDeleted > 0) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Schedule deleted successfully!");
                alert.showAndWait();

                // Refresh the table view to remove the deleted data
                scheduleShowData();
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Failed to delete schedule. Please try again.");
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

    private String[] dayList = {"Monday", "Tuesday", "Wednesday", "Thuresday", "Friday", "Saturday", "Sunday"};

    public void scheduleDayList() {
        List<String> DList = new ArrayList<>();

        for (String data : dayList) {
            DList.add(data);
        }

        ObservableList<String> dayData = FXCollections.observableArrayList(DList);
        sch_day.setItems(dayData);
    }

    private String[] timeList = {"7:45-9:45", "9:45-11:45", "9:45-10:45", "14:30-17:30", "18:00-20:00", "15:00-18:00"};

    public void scheduleTimeList() {
        List<String> TList = new ArrayList<>();

        for (String data : timeList) {
            TList.add(data);
        }

        ObservableList<String> timeData = FXCollections.observableArrayList(TList);
        sch_time.setItems(timeData);
    }

//    ############################################################# 


//    ############################################################ 
    private void handleRowSelect() {
        UserData selectedUser = user_tableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            username_update.setText(selectedUser.getUsername());
            password_update.setText(selectedUser.getPassword());
        }
    }

    private void handleUpdate() {
        UserData selectedUser = user_tableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            String newUsername = username_update.getText();
            String newPassword = password_update.getText();

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

    private void clearFields() {
        username_update.clear();
        password_update.clear();
        user_tableView.getSelectionModel().clearSelection();
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    //    ############################################################
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String username = UserSession.getInstance().getUsername();
        scheduleDayList();
        scheduleTimeList();

        try {
            loadUserData(username);
            scheduleShowData();
            // userShowData();
            loadInstructorNames();
            loadCourseNames();
            user_tableView.setOnMouseClicked(event -> handleRowSelect());
            username_updatebtn1.setOnAction(event -> handleUpdate());
            username_clearbtn1.setOnAction(event -> clearFields());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void scheduelSelectData(MouseEvent event) {
        scheduleSelectedData();
    }

}
