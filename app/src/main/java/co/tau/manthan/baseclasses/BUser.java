package co.tau.manthan.baseclasses;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static co.tau.manthan.baseclasses.Constants.TAG_OP;

public class BUser {
    private String uid;
    private String name;
    private String phone;
    private String email;
    private String piclink;

    private String sid;
    private Date ts;

    public BUser() {
    }

    public BUser(String uid, String sid, String name, String piclink,
                 String email, String phone, Date ts) {
        this.uid = uid;
        this.sid = sid;
        this.name = name;
        this.piclink = piclink;
        this.email = email;
        this.phone = phone;
        this.ts = ts;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }

    public String getPiclink() {
        return piclink;
    }

    public void setPiclink(String piclink) {
        this.piclink = piclink;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public static List<Hashtag> HashtagSop = Arrays.asList(
            new Hashtag(51,"I Accept the tags", "IAccept", TAG_OP),
            new Hashtag(22,"I Reject the tags", "IReject", TAG_OP),
            new Hashtag(22,"I will ask a Guru", "IAskGuru", TAG_OP),
            new Hashtag(22,"I will invite a Moderator", "InviteaModerator", TAG_OP)
    );


}
