package co.tau.manthan.baseclasses;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.EditText;

import com.otaliastudios.autocomplete.Autocomplete;
import com.otaliastudios.autocomplete.AutocompleteCallback;
import com.otaliastudios.autocomplete.AutocompletePolicy;
import com.otaliastudios.autocomplete.AutocompletePresenter;
import com.otaliastudios.autocomplete.CharPolicy;

import java.util.List;

import static android.graphics.Typeface.BOLD;
import static co.tau.manthan.baseclasses.Hashtag.gethashtagtocolor;

public class Tags {

    public Tags() {
    }

    public static Autocomplete setupHashtagAutocomplete(EditText edittext, char c, final Context context, List<Hashtag> list) {
        float elevation = 6f;
        Drawable backgroundDrawable = new ColorDrawable(Color.WHITE);
        AutocompletePolicy policy = new CharPolicy(c);
        AutocompletePresenter<Hashtag> presenter = new HashtagPresenter(context,list);
        AutocompleteCallback<Hashtag> callback = new AutocompleteCallback<Hashtag>() {

            @Override
            public boolean onPopupItemClicked(Editable editable, Hashtag item) {
//                editable.clear();
                editable.append(item.getHashtagstring());

                int[] range = CharPolicy.getQueryRange(editable);
                if (range == null) return false;
                int start = range[0];
                int end = range[1];
                String replacement = item.getHashtagstring();
                editable.replace(start, end, replacement);
                // This is better done with regexes and a TextWatcher, due to what happens when
                // the user clears some parts of the text. Up to you.
                editable.setSpan(new StyleSpan(BOLD), start-1, start+replacement.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                int color = gethashtagtocolor(item.getHashtagstring());
                editable.setSpan(new ForegroundColorSpan(context.getResources().getColor(color)), start-1, start+replacement.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                return true;
            }

            public void onPopupVisibilityChanged(boolean shown) {}
        };

        Autocomplete hashtagAutocomplete = Autocomplete.<Hashtag>on(edittext)
                .with(elevation)
                .with(backgroundDrawable)
                .with(policy)
                .with(presenter)
                .with(callback)
                .build();
        return hashtagAutocomplete;
    }

    public static Autocomplete setupatlistAutocomplete(EditText edittext, char c, final Context context, List<Siditem> list) {
        float elevation = 6f;
        Drawable backgroundDrawable = new ColorDrawable(Color.WHITE);
        AutocompletePolicy atpolicy = new CharPolicy('@');
        AutocompletePresenter<Siditem> atpresenter = new SiditemsPresenter(context,list);
        AutocompleteCallback<Siditem> atcallback = new AutocompleteCallback<Siditem>() {

            @Override
            public boolean onPopupItemClicked(Editable editable, Siditem item) {
//                editable.clear();
                editable.append(item.getSid());

                int[] range = CharPolicy.getQueryRange(editable);
                if (range == null) return false;
                int start = range[0];
                int end = range[1];
                String replacement = item.getSid();
                editable.replace(start, end, replacement);
                // This is better done with regexes and a TextWatcher, due to what happens when
                // the user clears some parts of the text. Up to you.
                editable.setSpan(new StyleSpan(BOLD), start-1, start+replacement.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                editable.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.accent_light)), start-1, start+replacement.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                return true;
            }

            public void onPopupVisibilityChanged(boolean shown) {}
        };

        Autocomplete siditemAutocomplete = Autocomplete.<Siditem>on(edittext)
                .with(elevation)
                .with(backgroundDrawable)
                .with(atpolicy)
                .with(atpresenter)
                .with(atcallback)
                .build();
        return siditemAutocomplete;

    }
}
