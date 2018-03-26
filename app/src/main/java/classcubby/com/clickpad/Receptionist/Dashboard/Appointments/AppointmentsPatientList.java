package classcubby.com.clickpad.Receptionist.Dashboard.Appointments;

/**
 * Created by Vino on 3/14/2018.
 */

public class AppointmentsPatientList {

    private String id;
    private String name;
    private String mrnumber;
    private String mobilenumber;
    private String image;

    public AppointmentsPatientList(String patientid, String patientname, String patientmrnumber, String patientmobilenumber, String imagelist) {
        this.id = patientid;
        this.name = patientname;
        this.mrnumber = patientmrnumber;
        this.mobilenumber = patientmobilenumber;
        this.image = imagelist;
    }

    public String getid() {
        return this.id;
    }

    public String getname() {
        return this.name;
    }

    public String getmrnumber() {
        return this.mrnumber;
    }

    public String getMobilenumber() {
        return this.mobilenumber;
    }

    public String getimage() {
        return this.image;
    }

}
