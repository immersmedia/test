package co.tau.manthan.baseclasses;
import androidx.annotation.NonNull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.tau.manthan.R;

import static co.tau.manthan.baseclasses.Constants.TAG_CLINCHER;
import static co.tau.manthan.baseclasses.Constants.TAG_FALLACY;
import static co.tau.manthan.baseclasses.Constants.TAG_OP;
import static co.tau.manthan.baseclasses.Constants.TAG_POSITIVE;
import static co.tau.manthan.baseclasses.Constants.TAG_SYS;
import static co.tau.manthan.baseclasses.Constants.TAG_VIEWER;
import static co.tau.manthan.baseclasses.Constants.TAG_VIEWEROP;

@SuppressWarnings("WeakerAccess")
public class Hashtag {

    private String hashtaginfull;
    private String hashtagstring;
    private int index;
    private String hashtagtype;

    public Hashtag(@NonNull String hashtaginfull, @NonNull String hashtagstring) {
        this.hashtaginfull = hashtaginfull;
        this.hashtagstring = hashtagstring;
    }

    public Hashtag(@NonNull int index, @NonNull String hashtaginfull, @NonNull String hashtagstring, @NonNull String hashtagtype) {
        this.hashtaginfull = hashtaginfull;
        this.hashtagstring = hashtagstring;
        this.index = index;
        this.hashtagtype = hashtagtype;
    }

    public void setHashtaginfull(String hashtaginfull) {
        this.hashtaginfull = hashtaginfull;
    }

    public void setHashtagstring(String hashtagstring) {
        this.hashtagstring = hashtagstring;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getHashtagtype() {
        return hashtagtype;
    }

    public void setHashtagtype(String hashtagtype) {
        this.hashtagtype = hashtagtype;
    }

    @NonNull
    public String getHashtaginfull() {
        return hashtaginfull;
    }

    @NonNull
    public String getHashtagstring() {
        return hashtagstring;
    }

    // Thanks uinames.com
    public static List<Hashtag> HashtagS = Arrays.asList(
            new Hashtag(1,"Erratic Reasoning", "ErraticReasoning", TAG_FALLACY),
            new Hashtag(2,"Shifting the Reason", "ShiftingtheReason", TAG_FALLACY),
            new Hashtag(3,"Unintelligible", "Unintelligible",TAG_FALLACY),
            new Hashtag(4, "Shaky Foundation", "ShakyFoundation",TAG_FALLACY),
            new Hashtag(5, "Far fetched Analogy", "FarfetchedAnalogy", TAG_FALLACY),
            new Hashtag(6, "Quibbling on the Term", "QuibblingontheTerm",TAG_FALLACY),
            new Hashtag(7,"Quibbling on the Metaphor", "QuibblingontheMetaphor", TAG_FALLACY),
            new Hashtag(8,"Attack without substance", "Attackwithoutsubstance",TAG_FALLACY),

            new Hashtag(11,"Shifting the Proposition", "ShiftingtheProposition", TAG_CLINCHER),
            new Hashtag(12,"OpposingtheProposition", "OpposingtheProposition", TAG_CLINCHER),
            new Hashtag(13,"Shifting Topic", "ShiftingTopic",TAG_CLINCHER),
            new Hashtag(14, "Incoherent", "Incoherent",TAG_CLINCHER),
            new Hashtag(15, "Saying too Little", "SayingtooLittle", TAG_CLINCHER),
            new Hashtag(16, "Repetition", "Repetition",TAG_CLINCHER),
            new Hashtag(17,"Contradiction to Stand", "ContradictiontoStand", TAG_CLINCHER),
            new Hashtag(18,"Obstinacy", "Obstinacy",TAG_CLINCHER),

            new Hashtag(21,"I Agree", "IAgree", TAG_POSITIVE),
            new Hashtag(54,"I will invite a Moderator", "InviteaModerator", TAG_OP)
    );

    public static List<Hashtag> HashtagSop = Arrays.asList(
            new Hashtag(51,"I Accept the tags", "IAccept", TAG_OP),
            new Hashtag(52,"I Reject the tags", "IReject", TAG_OP),
            new Hashtag(53,"I will ask a Guru", "IAskGuru", TAG_OP),
            new Hashtag(54,"I will invite a Moderator", "InviteaModerator", TAG_OP)
    );

    public static List<Hashtag> HashtagGuruop = Arrays.asList(
            new Hashtag(61,"I Accept the tags", "IAcceptasGuru", TAG_OP),
            new Hashtag(62,"I Reject the tags", "IRejectasGuru", TAG_OP)
    );

    public static List<Hashtag> HashtagModop = Arrays.asList(
            new Hashtag(61,"I Accept the tags", "IAcceptasMod", TAG_OP),
            new Hashtag(62,"I Reject the tags", "IRejectasMod", TAG_OP)
    );

    public static List<Hashtag> HashtagViewersS = Arrays.asList(
            new Hashtag(61,"I Concur", "IConcur", TAG_VIEWER),
            new Hashtag(62,"I Don't Concur", "IDontConcur", TAG_VIEWER)
    );

    public static List<Hashtag> HashtagViewersSOp = Arrays.asList(
            new Hashtag(61,"I Accept the tags", "IAcceptasViewer", TAG_VIEWEROP),
            new Hashtag(62,"I Reject the tags", "IRejectasViewer", TAG_VIEWEROP)
    );


//    -------------------------------------------------

    private static Map<String, String> hashtagtotype;
    static {
        hashtagtotype = new HashMap<>();
        hashtagtotype.put("ErraticReasoning",TAG_FALLACY);
        hashtagtotype.put("ShiftingtheReason",TAG_FALLACY);
        hashtagtotype.put("Unintelligible",TAG_FALLACY);
        hashtagtotype.put("ShakyFoundation",TAG_FALLACY);
        hashtagtotype.put("FarfetchedAnalogy",TAG_FALLACY);
        hashtagtotype.put("QuibblingontheTerm",TAG_FALLACY);
        hashtagtotype.put("QuibblingontheMetaphor",TAG_FALLACY);
        hashtagtotype.put("Attackwithoutsubstance",TAG_FALLACY);

        hashtagtotype.put("ShiftingtheProposition",TAG_CLINCHER);
        hashtagtotype.put("OpposingtheProposition",TAG_CLINCHER);
        hashtagtotype.put("ShiftingTopic",TAG_CLINCHER);
        hashtagtotype.put("Incoherent",TAG_CLINCHER);
        hashtagtotype.put("SayingtooLittle",TAG_CLINCHER);
        hashtagtotype.put("Repetition",TAG_CLINCHER);
        hashtagtotype.put("ContradictiontoStand",TAG_CLINCHER);
        hashtagtotype.put("Obstinacy",TAG_CLINCHER);

        hashtagtotype.put("IAgree",TAG_POSITIVE);

        hashtagtotype.put("IAccept",TAG_OP);
        hashtagtotype.put("IReject",TAG_OP);
        hashtagtotype.put("IAskGuru",TAG_OP);
        hashtagtotype.put("InviteaModerator",TAG_OP);

        hashtagtotype.put("IAcceptasGuru",TAG_OP);
        hashtagtotype.put("IRejectasGuru",TAG_OP);

        hashtagtotype.put("IAcceptasMod",TAG_OP);
        hashtagtotype.put("IRejectasMod",TAG_OP);

    }

    private static Map<String, Integer> hashtagtypetocolor;
    static{
        hashtagtypetocolor = new HashMap<>();
        hashtagtypetocolor.put(TAG_FALLACY, R.color.accent_light);
        hashtagtypetocolor.put(TAG_CLINCHER,R.color.accent);
        hashtagtypetocolor.put(TAG_POSITIVE,R.color.secondary_text);
        hashtagtypetocolor.put(TAG_OP,R.color.primary);
        hashtagtypetocolor.put(TAG_SYS,R.color.primary_dark);
        
    }

    public static String gethashtagtotype(String ht){
        return hashtagtotype.get(ht);
    }

    public static int gethashtagtocolor(String ht){
        String aa = gethashtagtotype(ht);
        Integer bb =  hashtagtypetocolor.get(aa);
        return bb.intValue();
    }
}
