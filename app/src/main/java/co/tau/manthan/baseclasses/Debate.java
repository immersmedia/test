package co.tau.manthan.baseclasses;

import java.util.ArrayList;

import static co.tau.manthan.baseclasses.Constants.TAG_NONE;
import static co.tau.manthan.baseclasses.Constants.TURN_FOR;

public class Debate  {

    public static final String RESULT_WIN_FOR = "FORWINS";
    public static final String RESULT_WIN_AG = "AGWINS";

    public static final String PARTISTATUS_WAITING = "WAITING";
    public static final String PARTISTATUS_ACCEPTED = "ACCEPTED";
    public static final String PARTISTATUS_REJECTED = "REJECTED";
    public static final String PARTISTATUS_NORESPONSE = "NORESPONSE";

    private String uid;
    private String inviteruid;
    private String topic;
    private String actualstartts;
    private String actualendts;
    private String scheduledstartts;

    private String flagactivestate;//NOTSTARTED, WAITINGTOJOIN, ONGOING, WAITINGASSESSMENT, FINISHED

    private String debater_for_uid; //same as the challenger
    private String debater_ag_uid;  //same as the acceptor
    private String moderatoruid;

    private String debater_for_sid; //same as the challenger
    private String debater_ag_sid;  //same as the acceptor
    private String moderatorsid;
    //Doubled
    private String debater_for_name; //same as the challenger
    private String debater_ag_name;  //same as the acceptor
    private String moderator_name;
    //Doubled
    private String debater_for_pic; //same as the challenger
    private String debater_ag_pic;  //same as the acceptor
    private String moderator_pic;
    private ArrayList<String> participants = new ArrayList<>();

    ////////////STATUS//////////////////////////


    private String debater_for_status = PARTISTATUS_WAITING;
    private String debater_ag_status = PARTISTATUS_WAITING;
    private String moderatorstatus = PARTISTATUS_WAITING; //NOSTATUS, ACCEPTED, REJECTED,

    ////////////STATISTICS//////////////////////////
    private int debstatcurrentviewership =0;
    private int debstatpeakviewership =0;

    private int debstattotalargs =0;
    private int debstattotalpoints =0;
    private int debstattot_fallacies =0;
    private int debstattot_clinchers =0;
    private int debstattot_iagree =0;
    private String overallresult;
    private ArrayList<String> deballtags = new ArrayList<>();


    ////////////RUNTIME//////////////////////////
    private String turn = TURN_FOR;
    private String convcontextargtilt;//FOR,AGAINST
    private String convcontextarguid;
    private String convcontextargop; //ARGOP_FALLACY,ARGOP_CLINCHER,..
    private String convcontextargtext;
    private ArrayList<String> convcontexttags = new ArrayList<>();
    private String convcontexttagstate = TAG_NONE;

    private String activekeyargguid;
    private int activekeyargindex = 0;


    public Debate() {
    }

    public Debate(String uid,String inviteruid, String topic,
                  String debater_for_uid, String debater_ag_uid, String moderatoruid,
                  String debater_for_sid, String debater_ag_sid, String moderatorsid,
                  String debater_for_name, String debater_ag_name,String moderator_name,
                  String debater_for_pic, String debater_ag_pic, String moderator_pic) {
        this.uid = uid;
        this.inviteruid = inviteruid;
        this.topic = topic;
        this.debater_for_uid = debater_for_uid;
        this.debater_ag_uid = debater_ag_uid;
        this.moderatoruid = moderatoruid;
        this.debater_for_sid = debater_for_sid;
        this.debater_ag_sid = debater_ag_sid;
        this.moderatorsid = moderatorsid;
        this.debater_for_name = debater_for_name;
        this.debater_ag_name = debater_ag_name;
        this.moderator_name = moderator_name;
        this.debater_for_pic = debater_for_pic;
        this.debater_ag_pic = debater_ag_pic;
        this.moderator_pic = moderator_pic;
    }

    public Debate(String uid,String inviteruid, String topic) {
        this.uid = uid;
        this.inviteruid = inviteruid;
        this.topic = topic;
    }

    public void addmoderator(String uid, String sid, String name, String piclink){
        this.moderatoruid = uid;
        this.moderatorsid = sid;
        this.moderator_name = name;
        this.moderator_pic = piclink;
        this.participants.add(uid);

    }

    public void adddebag(String uid, String sid, String name, String piclink){
        this.debater_ag_uid = uid;
        this.debater_ag_sid = sid;
        this.debater_ag_name = name;
        this.debater_ag_pic = piclink;
        this.participants.add(uid);
    }

    public void adddebfor(String uid, String sid, String name, String piclink){
        this.debater_for_uid = uid;
        this.debater_for_sid = sid;
        this.debater_for_name = name;
        this.debater_for_pic = piclink;
        this.participants.add(uid);
    }

    public ArrayList<String> getDeballtags() {
        return deballtags;
    }

    public void setDeballtags(ArrayList<String> deballtags) {
        this.deballtags = deballtags;
    }

    public ArrayList<String> getParticipants() {
        return participants;
    }

    public String getConvcontexttagstate() {
        return convcontexttagstate;
    }

    public void setConvcontexttagstate(String convcontexttagstate) {
        this.convcontexttagstate = convcontexttagstate;
    }

    public int getDebstattotalpoints() {
        return debstattotalpoints;
    }

    public void setDebstattotalpoints(int debstattotalpoints) {
        this.debstattotalpoints = debstattotalpoints;
    }

    public ArrayList<String> getConvcontexttags() {
        return convcontexttags;
    }

    public String getConvcontextargtext() {
        return convcontextargtext;
    }

    public void setConvcontextargtext(String convcontextargtext) {
        this.convcontextargtext = convcontextargtext;
    }

    public void setConvcontexttags(ArrayList<String> convcontexttags) {
        this.convcontexttags = convcontexttags;
    }

    public int getDebstattot_iagree() {
        return debstattot_iagree;
    }

    public void setDebstattot_iagree(int debstattot_iagree) {
        this.debstattot_iagree = debstattot_iagree;
    }

    public void setParticipants(ArrayList<String> participants) {
        this.participants = participants;
    }

    public String getInviteruid() {
        return inviteruid;
    }

    public void setInviteruid(String inviteruid) {
        this.inviteruid = inviteruid;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDebater_for_sid() {
        return debater_for_sid;
    }

    public void setDebater_for_sid(String debater_for_sid) {
        this.debater_for_sid = debater_for_sid;
    }

    public String getDebater_ag_sid() {
        return debater_ag_sid;
    }

    public void setDebater_ag_sid(String debater_ag_sid) {
        this.debater_ag_sid = debater_ag_sid;
    }

    public String getModeratorsid() {
        return moderatorsid;
    }

    public void setModeratorsid(String moderatorsid) {
        this.moderatorsid = moderatorsid;
    }

    public String getModerator_name() {
        return moderator_name;
    }

    public void setModerator_name(String moderator_name) {
        this.moderator_name = moderator_name;
    }

    public String getModerator_pic() {
        return moderator_pic;
    }

    public void setModerator_pic(String moderator_pic) {
        this.moderator_pic = moderator_pic;
    }

    public String getDebater_for_status() {
        return debater_for_status;
    }

    public void setDebater_for_status(String debater_for_status) {
        this.debater_for_status = debater_for_status;
    }

    public String getDebater_ag_status() {
        return debater_ag_status;
    }

    public void setDebater_ag_status(String debater_ag_status) {
        this.debater_ag_status = debater_ag_status;
    }

    public String getModeratorstatus() {
        return moderatorstatus;
    }

    public void setModeratorstatus(String moderatorstatus) {
        this.moderatorstatus = moderatorstatus;
    }

    public String getOverallresult() {
        return overallresult;
    }

    public void setOverallresult(String overallresult) {
        this.overallresult = overallresult;
    }

    public String getConvcontextargop() {
        return convcontextargop;
    }

    public void setConvcontextargop(String convcontextargop) {
        this.convcontextargop = convcontextargop;
    }

    public int getActivekeyargindex() {
        return activekeyargindex;
    }

    public void setActivekeyargindex(int activekeyargindex) {
        this.activekeyargindex = activekeyargindex;
    }

    public String getDebater_for_name() {
        return debater_for_name;
    }

    public String getActivekeyargguid() {
        return activekeyargguid;
    }

    public void setActivekeyargguid(String activekeyargguid) {
        this.activekeyargguid = activekeyargguid;
    }

    public void setDebater_for_name(String debater_for_name) {
        this.debater_for_name = debater_for_name;
    }

    public String getDebater_ag_name() {
        return debater_ag_name;
    }

    public void setDebater_ag_name(String debater_ag_name) {
        this.debater_ag_name = debater_ag_name;
    }

    public String getDebater_for_pic() {
        return debater_for_pic;
    }

    public void setDebater_for_pic(String debater_for_pic) {
        this.debater_for_pic = debater_for_pic;
    }

    public String getDebater_ag_pic() {
        return debater_ag_pic;
    }

    public void setDebater_ag_pic(String debater_ag_pic) {
        this.debater_ag_pic = debater_ag_pic;
    }

    public String getConvcontextarguid() {
        return convcontextarguid;
    }

    public void setConvcontextarguid(String convcontextarguid) {
        this.convcontextarguid = convcontextarguid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getActualstartts() {
        return actualstartts;
    }

    public void setActualstartts(String actualstartts) {
        this.actualstartts = actualstartts;
    }

    public String getActualendts() {
        return actualendts;
    }

    public void setActualendts(String actualendts) {
        this.actualendts = actualendts;
    }

    public String getScheduledstartts() {
        return scheduledstartts;
    }

    public void setScheduledstartts(String scheduledstartts) {
        this.scheduledstartts = scheduledstartts;
    }

    public String getFlagactivestate() {
        return flagactivestate;
    }

    public void setFlagactivestate(String flagactivestate) {
        this.flagactivestate = flagactivestate;
    }

    public String getDebater_for_uid() {
        return debater_for_uid;
    }

    public void setDebater_for_uid(String debater_for_uid) {
        this.debater_for_uid = debater_for_uid;
    }

    public String getDebater_ag_uid() {
        return debater_ag_uid;
    }

    public void setDebater_ag_uid(String debater_ag_uid) {
        this.debater_ag_uid = debater_ag_uid;
    }

    public String getModeratoruid() {
        return moderatoruid;
    }

    public void setModeratoruid(String moderatoruid) {
        this.moderatoruid = moderatoruid;
    }

    public int getDebstatcurrentviewership() {
        return debstatcurrentviewership;
    }

    public void setDebstatcurrentviewership(int debstatcurrentviewership) {
        this.debstatcurrentviewership = debstatcurrentviewership;
    }

    public int getDebstatpeakviewership() {
        return debstatpeakviewership;
    }

    public void setDebstatpeakviewership(int debstatpeakviewership) {
        this.debstatpeakviewership = debstatpeakviewership;
    }

    public int getDebstattotalargs() {
        return debstattotalargs;
    }

    public void setDebstattotalargs(int debstattotalargs) {
        this.debstattotalargs = debstattotalargs;
    }

    public int getDebstattot_fallacies() {
        return debstattot_fallacies;
    }

    public void setDebstattot_fallacies(int debstattot_fallacies) {
        this.debstattot_fallacies = debstattot_fallacies;
    }

    public int getDebstattot_clinchers() {
        return debstattot_clinchers;
    }

    public void setDebstattot_clinchers(int debstattot_clinchers) {
        this.debstattot_clinchers = debstattot_clinchers;
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public String getConvcontextargtilt() {
        return convcontextargtilt;
    }

    public void setConvcontextargtilt(String convcontextargtilt) {
        this.convcontextargtilt = convcontextargtilt;
    }
}
