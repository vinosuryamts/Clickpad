package classcubby.com.clickpad.Receptionist.Dashboard.Appointments;

/**
 * Created by Vino on 3/14/2018.
 */

public class AppointmentsDoctorList {

    private String id,departmentid,departmentname;
    private String name;
    private String image;

    public AppointmentsDoctorList(String doctorid, String doctorname, String doctorimage, String departmentid,String departmentname) {
        this.id = doctorid;
        this.name = doctorname;
        this.departmentid = departmentid;
        this.departmentname = departmentname;
        this.image = doctorimage;
    }

    public String getid() {
        return this.id;
    }

    public String getname() {
        return this.name;
    }

    public String getdepartmentid() {
        return this.departmentid;
    }

    public String getdepartmentname() {
        return this.departmentname;
    }

    public String getimage() {
        return this.image;
    }

}
