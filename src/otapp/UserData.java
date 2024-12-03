
package otapp;


public class UserData {
    private String id;
    private String fullName;
    private String department;
    private String gender;
    private String position;
    private Integer salary;
    private String username;
    private String password;
    
    public UserData(String id,
            String fullName,
            String department,
            String gender,
            String position,
            Integer salary,
            String username,
            String password) {

        this.id = id;
        this.gender = gender;
        this.fullName = fullName;
        this.department = department;
        this.position = position;
        this.username = username;
        this.password = password;
        this.salary = salary;
    }
    
    public String getId(){
        return id;
    }
    public Integer getSalary (){
        return salary;
    }
    public String getFullName() {
        return fullName;
    }
    public String getGender(){
        return gender;
    }  
    public String getDepartment(){
        return department;
    }
    public String getPosition(){
        return position;
    }
    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }
     public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
