package co.tau.manthan.baseclasses;

public class viewerfb {

    public static final String ICONCUR = "ICONCUR";
    public static final String ICONCURNOT = "ICONCURNOT";
    public static final String NOSAY = "NOSAY";

    private String agreement = "NOSAY";
    private String uid = null;
    private String name = null;
    private String pic = null;
    private String sid = null;

    public viewerfb(String iconcur) {
    }

    public viewerfb(String uid, String name, String pic, String sid) {
        this.uid = uid;
        this.name = name;
        this.pic = pic;
        this.sid = sid;
    }

    public String getAgreement() {
        return agreement;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public void setAgreement(String agreement) {
        this.agreement = agreement;
    }
}
