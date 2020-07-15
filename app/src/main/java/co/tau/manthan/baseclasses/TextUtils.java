package co.tau.manthan.baseclasses;

import android.text.SpannableString;
import android.text.style.StyleSpan;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.graphics.Typeface.BOLD;

public class TextUtils {
    public static ArrayList<String> getHashTags(String str) {
        Pattern MY_PATTERN = Pattern.compile("(#[a-zA-Z0-9ğüşöçıİĞÜŞÖÇ]{2,50}\\b)"); // or "#(\\S+)"
        Matcher mat = MY_PATTERN.matcher(str);
        ArrayList<String> strs = new ArrayList<>();
        while (mat.find()) {
            strs.add(mat.group(1));
        }
        return strs;
    }

    public static ArrayList<String> getatTags(String str) {
        Pattern MY_PATTERN = Pattern.compile("(@[a-zA-Z0-9ğüşöçıİĞÜŞÖÇ]{2,50}\\b)"); // or "#(\\S+)"
        Matcher mat = MY_PATTERN.matcher(str);
        ArrayList<String> strs = new ArrayList<>();
        while (mat.find()) {
            strs.add(mat.group(1));
        }
        return strs;
    }

    public static SpannableString decoratemessage(String message){
        SpannableString hashtagintitle = new SpannableString(message);
        Matcher matcher = Pattern.compile("#([A-Za-z0-9_-]+)").matcher(hashtagintitle);
        while (matcher.find())
        {
//            hashtagintitle.setSpan(new ForegroundColorSpan(Color.WHITE), matcher.start(), matcher.end(), 0);
            hashtagintitle.setSpan(new StyleSpan(BOLD), matcher.start(), matcher.end(), 0);
        }
        return hashtagintitle;
    }

}
