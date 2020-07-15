package co.tau.manthan.baseclasses;

import java.util.ArrayList;
import java.util.Date;

public class Arg_Key extends BArg {

    public static final String RESULTSTATE_WON = "CONVWIN";
    public static final String RESULTSTATE_LOST = "CONVLOST";
    public static final String RESULTSTATE_DRAW = "CONVDRAW";
    public static final String RESULTSTATE_WAITING = "CONVWAITING";

    public static final String KEYARGAACTIV_ACTIVE = "Active";
    public static final String KEYARGAACTIV_FINISHED = "Finished";

//    ================================================

    private String keyargflagactivestate;//ACTIVE, FINISHED
    private String keyargflagresultstate;//WON,LOST,DRAW

    private int keyargstattot_for_points =0;
    private int keyargstattot_ag_points =0;

    private ArrayList<String> tagson_for = new ArrayList<>();
    private ArrayList<String> tagson_ag = new ArrayList<>();
    private ArrayList<String> keyargalltags = new ArrayList<>();


    public Arg_Key() {
        super();
    }

    public Arg_Key(String uid, String debateruid, String debaterpic, String debatername, String debatersid,
                   String debateuid, String tilt, String argtext, String attachimg, String role, Date ts) {
        super(uid, debateruid, debaterpic, debatername, debatersid, debateuid, tilt, argtext, attachimg, role, ts);
    }

    public void setkeyparams( String flagactivestate, String flagresultstate) {
        this.keyargflagactivestate = flagactivestate;
        this.keyargflagresultstate = flagresultstate;
    }


    public ArrayList<String> getKeyargalltags() {
        return keyargalltags;
    }

    public void setKeyargalltags(ArrayList<String> keyargalltags) {
        this.keyargalltags = keyargalltags;
    }

    public int getKeyargstattot_for_points() {
        return keyargstattot_for_points;
    }

    public void setKeyargstattot_for_points(int keyargstattot_for_points) {
        this.keyargstattot_for_points = keyargstattot_for_points;
    }

    public int getKeyargstattot_ag_points() {
        return keyargstattot_ag_points;
    }

    public void setKeyargstattot_ag_points(int keyargstattot_ag_points) {
        this.keyargstattot_ag_points = keyargstattot_ag_points;
    }

    public ArrayList<String> getTagson_for() {
        return tagson_for;
    }

    public void setTagson_for(ArrayList<String> tagson_for) {
        this.tagson_for = tagson_for;
    }

    public ArrayList<String> getTagson_ag() {
        return tagson_ag;
    }

    public void setTagson_ag(ArrayList<String> tagson_ag) {
        this.tagson_ag = tagson_ag;
    }

    public String getKeyargflagactivestate() {
        return keyargflagactivestate;
    }

    public void setKeyargflagactivestate(String keyargflagactivestate) {
        this.keyargflagactivestate = keyargflagactivestate;
    }

    public String getKeyargflagresultstate() {
        return keyargflagresultstate;
    }

    public void setKeyargflagresultstate(String keyargflagresultstate) {
        this.keyargflagresultstate = keyargflagresultstate;
    }

}
