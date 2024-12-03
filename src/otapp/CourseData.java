package otapp;


public class CourseData {
    private String Cid;
    private String Cname;
    private String Cr;
    
    public CourseData(String Cid,
            String Cname,
            String Cr) {

        this.Cid = Cid;
        this.Cname = Cname;
        this.Cr = Cr;
    }
    
    public String getCid(){
        return Cid;
    }
    public String getCname (){
        return Cname;
    }
    public String getCr() {
        return Cr;
    }
    
}
