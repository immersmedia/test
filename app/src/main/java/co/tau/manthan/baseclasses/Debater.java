package co.tau.manthan.baseclasses;


import java.util.Date;

public class Debater extends BUser {
    private String debaterrole; //Debater,moderator,viewer, none
    private String tilt; //FOR, AGAINST, NONE

    private Boolean active; //true (True for Debating, moderating or Viewing
//    private String activedebuid;//uid of the deb if active

    public Debater() {
    }

    public Debater(String uid, String sid, String name,
                   String piclink, String email,
                   String phone, Date ts) {
        super(uid, sid, name, piclink, email, phone, ts);
    }

    public void addDebaterdetails(String debaterrole, String tilt, Boolean active) {
        this.debaterrole = debaterrole;
        this.tilt = tilt;
        this.active = active;
    }

    public String getDebaterrole() {
        return debaterrole;
    }

    public void setDebaterrole(String debaterrole) {
        this.debaterrole = debaterrole;
    }

    public String getTilt() {
        return tilt;
    }

    public void setTilt(String tilt) {
        this.tilt = tilt;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
