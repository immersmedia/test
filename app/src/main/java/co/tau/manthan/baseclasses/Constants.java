package co.tau.manthan.baseclasses;

public class Constants {

    public static final String TAG_FALLACY  = "TAGFALLACY";
    public static final String TAG_CLINCHER = "TAGCLINCHER";
    public static final String TAG_IAGREE = "TAGIAGREE";
    public static final String TAG_POSITIVE = "TAG_POSITIVE";
    public static final String TAG_OP = "TAG_OP";
    public static final String TAG_VIEWER = "TAG_VIEWER";
    public static final String TAG_VIEWEROP = "TAG_VIEWEROP";
    public static final String TAG_SYS = "TAG_SYS";



    public static final String sharedprefs = "sharedpreferences";
    public static final String NOTIFICATION_CHANNEL_ID = "vitandachannelid";

    //DEBATER, MODERATOR CONSTANTS
    public static final String CLINCHER_NONE = "";
    public static final String CLINCHER_SHIFT_PRO = "Shifting the Proposition";
    public static final String CLINCHER_OPP_PRO =   "Opposing the Proposition";
    public static final String CLINCHER_SHIFT_TOP = "Shifting Topic";
    public static final String CLINCHER_INCOHERENT ="Incoherent";
    public static final String CLINCHER_SAYING_LIT ="Saying too Little";
    public static final String CLINCHER_REPEAT =    "Repetition";
    public static final String CLINCHER_CONTRA_ST = "Contradiction to Stand";
    public static final String CLINCHER_OBSTINACY = "Obstinacy";

    public static final  String[] CLINCHERS = {
            CLINCHER_NONE,
            CLINCHER_SHIFT_PRO,
            CLINCHER_OPP_PRO,
            CLINCHER_SHIFT_TOP,
            CLINCHER_INCOHERENT,
            CLINCHER_SAYING_LIT,
            CLINCHER_REPEAT,
            CLINCHER_CONTRA_ST,
            CLINCHER_OBSTINACY,
    };


    public static final String FALLACY_NONE = "";
    public static final String FALLACY_ERRATIC = "Erratic Reasoning";
    public static final String FALLACY_SHIFT_REA = "Shifting the Reason";
    public static final String FALLACY_UNINTELLI = "Unintelligible";
    public static final String FALLACY_SHAKY_FOU = "Shaky Foundation";
    public static final String FALLACY_FAR_FETCH = "Far fetched Analogy";
    public static final String FALLACY_QUIBBLE_T = "Quibbling on the Term";
    public static final String FALLACY_QUIBBLE_M = "Qubbling on the Metaphor";
    public static final String FALLACY_ATTACK_WS = "Attack witout substance";

    public static final String POSITIVE_NONE = "POSITIVE_NONE";
    public static final String POSITIVE_IAGREE = "#IAgree";

    public static final String TAGOP_NONE = "TAGOP_NONE";
    public static final String TAGOP_IACCEPT = "#IAccept";
    public static final String TAGOP_IREJECT = "#IReject";

    public static final String SYSTAG_DEADLOCK = "#Deadlock";
    public static final String TAGOP_INVITEMOD = "#InviteaModerator";
    public static final String TAGOP_IASKGURU = "#IAskGuru";

    public static final String TAGOP_MODIACCEPTASGURU = "#IAcceptasGuru";
    public static final String TAGOP_MODIREJECTASGURU = "#IRejectasGuru";

    public static final String TAGOP_MODIACCEPTASMOD = "#IAcceptasMod";
    public static final String TAGOP_MODIREJECTASMOD = "#IRejectasMod";


    public static final String[] FALLACIES = {
            FALLACY_NONE,
            FALLACY_ERRATIC,
            FALLACY_SHIFT_REA,
            FALLACY_UNINTELLI,
            FALLACY_SHAKY_FOU,
            FALLACY_FAR_FETCH,
            FALLACY_QUIBBLE_T,
            FALLACY_QUIBBLE_M,
            FALLACY_ATTACK_WS
    };

    public static final  String[] POSITIVES = {
            POSITIVE_NONE,
            POSITIVE_IAGREE
    };

    public static final  String[] TAGOPS = {
            TAGOP_NONE,
            TAGOP_IACCEPT,
            TAGOP_IREJECT,
            TAGOP_INVITEMOD,
            TAGOP_IASKGURU
    };

    public static final  String[] SYS_TAGS = {
            SYSTAG_DEADLOCK
    };



    public static final String PA_NONE = "";
    public static final String PA_AGREEMENT = "We're in Agreement";

    public static final String[] POSITIVEACTIONS = {
            PA_NONE,
            PA_AGREEMENT
    };


    public static final String ACTION_CLARI = "Clarification";
    public static final String ACTION_REQ_IS = "Request Information Source";
    public static final String ACTION_LIST_CL = "List Claim";
    public static final String ACTION_SEEK_IN = "Seek Intervention";

    //VIEWER CONSTANTS
    public static final String VIWERAC_PLUSONE = "plusone";
    public static final String VIWERAC_REPLY = "reply";
    public static final String VIWERAC_NEEDSOURCE = "needsource";
    public static final String VIWERAC_SUBJECTIVE = "subjective";
    public static final String VIWERAC_RELEVENCE = "relevance";
    public static final String VIWERAC_EXCELLENT = "excellence";
    public static final String VIWERAC_UPGRADE = "upgrade";
    public static final String VIWERAC_LACKREASON = "lacksreason";
    public static final String VIWERAC_LACKSEXAMP = "lacks example";

    public static final String TURN_FOR = "FOR";
    public static final String TURN_AG = "AGAINST";
    public static final String TURN_MOD = "MODERATOR";

    public static final String ARGTYPE_ASSRT = "ASSERT";

    public static final String MESSAGES_CHILD = "messages";

    public static final String MESSAGE_URL = "http://friendlychat.firebase.google.com/message/";
    public static final String LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif";
    public static final int REQUEST_INVITE = 1;
    public static final int REQUEST_IMAGE = 2;

    public static final int DEFAULT_MSG_LENGTH_LIMIT = 10;
    public static final String ANONYMOUS = "anonymous";
    public static final String MESSAGE_SENT_EVENT = "message_sent";

    public static final String FB_RCOLL_DEBATES = "debates";
    public static final String FB_RCOLL_USERSBYUID = "usersbyuid";
    public static final String FB_RCOLL_USERSBYSID = "usersbysid";
    public static final String FB_RCOLL_SIDLIST = "sidlist";
    public static final String FB_RCOLL_MYPARTICIPATION = "MYPARTICIPATION";

    public static final String FB_COLL_CONVS = "convs";
    public static final String FB_COLL_KEYARGS = "keyargs";
    public static final String FB_COLL_MYDEBATES = "MYDEBATES";
    public static final String FB_COLL_MYINVITES = "MYINVITES";
    public static final String FB_COLL_VIEWFBCONVS = "viewerconvs";
    public static final String FB_CONV_VIEWERFB = "viewerfb";




    public static final String ARGOP_FALLACY = "sendfallacy";
    public static final String ARGOP_CLINCHER = "sendclincher";
    public static final String ARGOP_MESSAGE = "sendmessage";
    public static final String ARGOP_IAGREE = "sendiagree";


//    public static final String DEBACTIVSTATE_NOTSTARTED = "Not Started";
    public static final String DEBACTIVSTATE_WAITINGJOIN = "Waiting to Start";
    public static final String DEBACTIVSTATE_ONGOING = "Ongoing";
    public static final String DEBACTIVSTATE_WAITINGASSESS = "Waiting Assessment";
    public static final String DEBACTIVSTATE_FINISHED = "Finished";


    public static final String TAG_ACCEPTED = "TAGACCEPTED";
    public static final String TAG_REJECTED = "TAG_REJECTED";
    public static final String TAG_PENDING = "TAG_PENDING";
    public static final String TAG_NONE = "TAG_NONE";

    public static final int VIEW_NOMSG = 0;
    public static final int VIEW_RIGHTMSG = 1;
    public static final int VIEW_LEFTMSG = 2;
    public static final int VIEW_MODERATORMSG = 3;
    public static final int VIEW_SYSMSG = 4;
    public static final int VIEW_SYSMSG_DEBEND = 5;

    public static final String MSGTYPE_DEB = "DEB";
    public static final String MSGTYPE_MOD = "MOD";
    public static final String MSGTYPE_SYS = "SYS";
    public static final String MSGTYPE_SYS_DEBEND = "SYSDEBEND";
    public static final String MSGTYPE_NONE = "NONE";

    public static final String TILT_NONE = "NONE";
    public static final String TILT_FOR = "FOR";
    public static final String TILT_AG = "AGAINST";
    public static final String TILT_MOD = "MODERATOR";
    public static final String TILT_SYS = "SYS";

    public static final String ARG_ROLE_DEB = "DEBATER";
    public static final String ARG_ROLE_MOD = "MODERATOR";
    public static final String ARG_ROLE_VIEW = "VIEWER";
    public static final String ARG_ROLE_NONE = "NONE";


    public static final int MAX_KEYARGCOUNT = 2;//NU
    public static final int MAX_FALLACIES = 3;//NU
    public static final int MAX_POINTSPERBOUT = 2;
    public static final int MAX_BOUTSPERCONV = 2;
    public static final int MAX_FALLCIESPERCLINCHER = 2;//NU
    public static final int ONEFALLACY = 1;

    //For debate listing where I am invited. Invited as -
    public static final int MYDEB_MEASMOD = 1;
    public static final int MYDEB_MEASDEB = 2;
    public static final int MYDEB_NOTMEASMOD = 3;
    public static final int MYDEB_NOTMEASDEB = 4;

    public static final String NOTIFICATION_MYTURN = "notificationmyturn";
    public static final int notificationmyturnid = 2197;

}
