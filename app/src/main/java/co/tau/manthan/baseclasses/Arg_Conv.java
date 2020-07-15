package co.tau.manthan.baseclasses;

import java.util.ArrayList;
import java.util.Date;

import static co.tau.manthan.baseclasses.Constants.TAG_NONE;

public class Arg_Conv extends BArg {

    private String argtype;//ASSERTION
    private String keyarguid;
    private boolean iskeyarg=false;
    private boolean expanded=true;

    private String seltag;
    private String seltag_acceptstate;//ACCPETED, REJECTED, PENDING
    private String clinchtag;
    private String clinchtag_accepstate;//ACCPETED, REJECTED, PENDING
    private String tagreason;
    private ArrayList<String> tags = new ArrayList<>();
    private String tagacceptstate = TAG_NONE;
    private long staticoncur = 0;
    private long staticoncurnot = 0;

    public Arg_Conv() {
    }

    public Arg_Conv(String uid, String debateruid, String debaterpic, String debatername,
                    String debatersid, String debateuid, String tilt, String argtext, String attachimg, String role, Date ts) {
        super(uid, debateruid, debaterpic, debatername, debatersid, debateuid, tilt, argtext, attachimg, role, ts);
    }

    public void setconv( String argtype, String keyarguid, String seltag,
                        String seltag_accepted, String clinchtag, String clinchtag_accepted, String tagreason,boolean iskeyarg) {
        this.argtype = argtype;
        this.keyarguid = keyarguid;
        this.seltag = seltag;
        this.seltag_acceptstate = seltag_accepted;
        this.clinchtag = clinchtag;
        this.clinchtag_accepstate = clinchtag_accepted;
        this.tagreason = tagreason;
        this.iskeyarg = iskeyarg;
    }

//    public void addconvdetails(String argtype, String keyarguid, String seltag,
//                               Boolean seltag_accepted, String clinchtag,
//                               Boolean clinchtag_accepted, String tagreason)
//    {
//        this.argtype = argtype;
//        this.keyarguid = keyarguid;
//        this.seltag = seltag;
//        this.seltag_accepted = seltag_accepted;
//        this.clinchtag = clinchtag;
//        this.clinchtag_accepted = clinchtag_accepted;
//        this.tagreason = tagreason;
//    }


    public long getStaticoncurnot() {
        return staticoncurnot;
    }

    public void setStaticoncurnot(long staticoncurnot) {
        this.staticoncurnot = staticoncurnot;
    }

    public long getStaticoncur() {
        return staticoncur;
    }

    public void setStaticoncur(long staticoncur) {
        this.staticoncur = staticoncur;
    }

    public boolean isIskeyarg() {
        return iskeyarg;
    }

    public void setIskeyarg(boolean iskeyarg) {
        this.iskeyarg = iskeyarg;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public String getTagacceptstate() {
        return tagacceptstate;
    }

    public void setTagacceptstate(String tagacceptstate) {
        this.tagacceptstate = tagacceptstate;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public String getArgtype() {
        return argtype;
    }

    public void setArgtype(String argtype) {
        this.argtype = argtype;
    }

    public String getKeyarguid() {
        return keyarguid;
    }

    public void setKeyarguid(String keyarguid) {
        this.keyarguid = keyarguid;
    }

    public String getSeltag() {
        return seltag;
    }

    public void setSeltag(String seltag) {
        this.seltag = seltag;
    }

    public String getSeltag_acceptstate() {
        return seltag_acceptstate;
    }

    public void setSeltag_acceptstate(String seltag_acceptstate) {
        this.seltag_acceptstate = seltag_acceptstate;
    }

    public String getClinchtag() {
        return clinchtag;
    }

    public void setClinchtag(String clinchtag) {
        this.clinchtag = clinchtag;
    }

    public String getClinchtag_accepstate() {
        return clinchtag_accepstate;
    }

    public void setClinchtag_accepstate(String clinchtag_accepstate) {
        this.clinchtag_accepstate = clinchtag_accepstate;
    }

    public String getTagreason() {
        return tagreason;
    }

    public void setTagreason(String tagreason) {
        this.tagreason = tagreason;
    }
}
