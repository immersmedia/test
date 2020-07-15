package co.tau.manthan.baseclasses;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import co.tau.manthan.R;

import static android.view.View.VISIBLE;
import static co.tau.manthan.baseclasses.Constants.TAG_CLINCHER;
import static co.tau.manthan.baseclasses.Constants.TAG_FALLACY;
import static co.tau.manthan.baseclasses.Constants.TAG_IAGREE;
import static co.tau.manthan.baseclasses.Constants.TURN_AG;
import static co.tau.manthan.baseclasses.Constants.TURN_FOR;


public class Utils {
    public static final String getakey(){
        String uid = FirebaseFirestore.getInstance().collection("random").document().getId();
        return uid;
    }
    public static final String getoppturn(String turn){
        if (turn.equals(TURN_FOR)){
            return TURN_AG;
        }else{
            return TURN_FOR;
        }
    }
    public void disableEnableControls(boolean enable, ViewGroup vg){
        vg.setEnabled(enable); // the point that I was missing
        for (int i = 0; i < vg.getChildCount(); i++){
            View child = vg.getChildAt(i);
            child.setEnabled(enable);
            child.setClickable(enable);
            if (child instanceof ViewGroup){
                disableEnableControls(enable, (ViewGroup)child);
            }
        }
    }

    public static String getEmojiByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }

    public static int dpToPx(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static void scoreforkeyarg(LinearLayout forlo, LinearLayout aglo, Arg_Key keyarg, Context context){
        int pixwid = Utils.dpToPx(20,context);
        forlo.removeAllViews();
        aglo.removeAllViews();

        for (String tag: keyarg.getTagson_for()){
            if (tag.equals(TAG_FALLACY)){
                ImageView imgView = new ImageView(context);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(pixwid, pixwid);
                imgView.setLayoutParams(lp);
                imgView.setColorFilter(context.getResources().getColor(R.color.accent_light));
                Glide.with(context)
                        .load(R.drawable.ic_error_black_24dp)
                        .override(pixwid,pixwid)
                        .into(imgView);
                forlo.addView(imgView);
            }else if(tag.equals(TAG_CLINCHER)){
                ImageView imgView = new ImageView(context);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(pixwid, pixwid);
                imgView.setLayoutParams(lp);
                imgView.setColorFilter(context.getResources().getColor(R.color.accent));
                Glide.with(context)
                        .load(R.drawable.ic_error_black_24dp)
                        .override(pixwid,pixwid)
                        .into(imgView);
                forlo.addView(imgView);
            }else if(tag.equals(TAG_IAGREE)){
                ImageView imgView = new ImageView(context);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(pixwid, pixwid);
                imgView.setLayoutParams(lp);
                imgView.setColorFilter(context.getResources().getColor(R.color.myback));
                Glide.with(context)
                        .load(R.drawable.ic_lens_black_24dp)
                        .override(pixwid,pixwid)
                        .into(imgView);
                forlo.addView(imgView);
            }else{

            }
        }

        for (String tag: keyarg.getTagson_ag()){
            if (tag.equals(TAG_FALLACY)){
                ImageView imgView = new ImageView(context);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(pixwid, pixwid);
                imgView.setLayoutParams(lp);
                imgView.setColorFilter(context.getResources().getColor(R.color.accent_light));
                Glide.with(context)
                        .load(R.drawable.ic_error_black_24dp)
                        .override(pixwid,pixwid)
                        .into(imgView);
                aglo.addView(imgView);
            }else if(tag.equals(TAG_CLINCHER)){
                ImageView imgView = new ImageView(context);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(pixwid, pixwid);
                imgView.setLayoutParams(lp);
                imgView.setColorFilter(context.getResources().getColor(R.color.accent));
                Glide.with(context)
                        .load(R.drawable.ic_error_black_24dp)
                        .override(pixwid,pixwid)
                        .into(imgView);
                aglo.addView(imgView);
            }else if(tag.equals(TAG_IAGREE)){
                ImageView imgView = new ImageView(context);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(pixwid, pixwid);
                imgView.setLayoutParams(lp);
                imgView.setColorFilter(context.getResources().getColor(R.color.myback));
                Glide.with(context)
                        .load(R.drawable.ic_lens_black_24dp)
                        .override(pixwid,pixwid)
                        .into(imgView);
                aglo.addView(imgView);
            }else{
            }
        }
    }

    public static void displayalltags(ChipGroup alllo, ArrayList<String> tags, Context context){
    int pixwid = Utils.dpToPx(20,context);
        alllo.removeAllViews();
        for (String tag: tags) {
            ViewGroup.MarginLayoutParams lparams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            TextView tv=new TextView(alllo.getContext());
            tv.setBackground(context.getResources().getDrawable(R.drawable.deblabel));

            tv.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.accent_light));

            tv.setPadding(dpToPx(5,alllo.getContext()),
                    dpToPx(5,alllo.getContext()),
                    dpToPx(5,alllo.getContext()),
                    dpToPx(5,alllo.getContext()));
            lparams.setMargins(15,
                    dpToPx(5,alllo.getContext()),
                    15,
                    dpToPx(5,alllo.getContext()));

            tv.setTextColor(context.getResources().getColor(R.color.primary_text));

            tv.setLayoutParams(lparams);
            tv.setText(tag);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            alllo.addView(tv);
            alllo.setVisibility(VISIBLE);
        }
    }
}

