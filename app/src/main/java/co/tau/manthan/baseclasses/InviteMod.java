package co.tau.manthan.baseclasses;

import java.util.Date;

public class InviteMod {

    public static String INVTYPE_MOD="INVTYPE_MOD";
    public static String INVTYPE_DEB="INVTYPE_DEB";
    public static String INVTYPE_VIEW="INVTYPE_VIEW";
    public static String INVTYPE_ASKGURU="INVTYPE_ASKGURU";

    public static String INVITESTATE_NONE="INVITESTATE_NONE";
    public static String INVITESTATE_ALIVE="INVITESTATE_ALIVE";
    public static String INVITESTATE_EXPIRED="INVITESTATE_EXPIRED";

    String uid;
    String debateuid;
    String debtopic;
    String convtxt;
    String convuid;
    String invitetype;

    String inviteruid;
    String invitersid;
    String invitername;
    String inviterpic;

    String foruid;
    String forsid;
    String forname;
    String forpic;

    String aguid;
    String agsid;
    String agname;
    String agpic;
    String state= INVITESTATE_ALIVE;
    Date ts = new Date();

    public InviteMod() {
    }

    public InviteMod(String uid, String debateuid, String debtopic,
                     String convuid, String convtxt, String invitetype, String inviteruid,
                     String invitersid, String invitername, String inviterpic) {
        this.uid = uid;
        this.debateuid = debateuid;
        this.debtopic = debtopic;
        this.convuid = convuid;
        this.convtxt = convtxt;
        this.invitetype = invitetype;
        this.inviteruid = inviteruid;
        this.invitersid = invitersid;
        this.invitername = invitername;
        this.inviterpic = inviterpic;
    }



    public String getConvuid() {
        return convuid;
    }

    public void setConvuid(String convuid) {
        this.convuid = convuid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDebateuid() {
        return debateuid;
    }

    public void setDebateuid(String debateuid) {
        this.debateuid = debateuid;
    }

    public String getDebtopic() {
        return debtopic;
    }

    public void setDebtopic(String debtopic) {
        this.debtopic = debtopic;
    }

    public String getConvtxt() {
        return convtxt;
    }

    public void setConvtxt(String convtxt) {
        this.convtxt = convtxt;
    }

    public String getInvitetype() {
        return invitetype;
    }

    public void setInvitetype(String invitetype) {
        this.invitetype = invitetype;
    }

    public String getInviteruid() {
        return inviteruid;
    }

    public void setInviteruid(String inviteruid) {
        this.inviteruid = inviteruid;
    }

    public String getInvitersid() {
        return invitersid;
    }

    public void setInvitersid(String invitersid) {
        this.invitersid = invitersid;
    }

    public String getInvitername() {
        return invitername;
    }

    public void setInvitername(String invitername) {
        this.invitername = invitername;
    }

    public String getInviterpic() {
        return inviterpic;
    }

    public void setInviterpic(String inviterpic) {
        this.inviterpic = inviterpic;
    }

    public String getForuid() {
        return foruid;
    }

    public void setForuid(String foruid) {
        this.foruid = foruid;
    }

    public String getForsid() {
        return forsid;
    }

    public void setForsid(String forsid) {
        this.forsid = forsid;
    }

    public String getForname() {
        return forname;
    }

    public void setForname(String forname) {
        this.forname = forname;
    }

    public String getForpic() {
        return forpic;
    }

    public void setForpic(String forpic) {
        this.forpic = forpic;
    }

    public String getAguid() {
        return aguid;
    }

    public void setAguid(String aguid) {
        this.aguid = aguid;
    }

    public String getAgsid() {
        return agsid;
    }

    public void setAgsid(String agsid) {
        this.agsid = agsid;
    }

    public String getAgname() {
        return agname;
    }

    public void setAgname(String agname) {
        this.agname = agname;
    }

    public String getAgpic() {
        return agpic;
    }

    public void setAgpic(String agpic) {
        this.agpic = agpic;
    }

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }
}
