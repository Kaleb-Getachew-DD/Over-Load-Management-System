package otapp;

public class ScheduleData {

    private String Sday;
    private String Stime;
    private String Sinstructor;
    private String Scourse;
    private int id;

    public ScheduleData(String Sday,
            String Stime,
            String Sinstructor,
            String Scourse, int id) {

        this.Sday = Sday;
        this.Stime = Stime;
        this.Sinstructor = Sinstructor;
        this.Scourse = Scourse;
        this.id = id;
    }
    
     public int getId(){
        return id;
    }

    public String getSday() {
        return Sday;
    }

    public String getStime() {
        return Stime;
    }

    public String getSinstructor() {
        return Sinstructor;
    }
    
    public String getScourse() {
        return Scourse;
    }
    

}
