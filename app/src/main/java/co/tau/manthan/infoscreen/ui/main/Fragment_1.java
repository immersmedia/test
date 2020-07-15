package co.tau.manthan.infoscreen.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import co.tau.manthan.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class Fragment_1 extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private int index = 1;

    private PageViewModel pageViewModel;

    public static Fragment_1 newInstance(int index) {
        Fragment_1 fragment = new Fragment_1();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_infosc_1, container, false);
        final ImageView imgview = root.findViewById(R.id.infoview1_img1);
        switch(index){
            case 1: imgview.setImageDrawable(getContext().getDrawable(R.drawable.text1));
                break;
            case 2: imgview.setImageDrawable(getContext().getDrawable(R.drawable.text2));
                break;
            case 3: imgview.setImageDrawable(getContext().getDrawable(R.drawable.text3));
                break;
            case 4: imgview.setImageDrawable(getContext().getDrawable(R.drawable.text4));
                break;
            case 5: imgview.setImageDrawable(getContext().getDrawable(R.drawable.text5));
                break;
            case 6: imgview.setImageDrawable(getContext().getDrawable(R.drawable.text6));
                break;
            default:
                imgview.setImageDrawable(getContext().getDrawable(R.drawable.text1));
        }

//        String txt = "<font color=#3d5afe>There is fire on the hill..</font> <font color=#64b5f6> For there is smoke,</font>  <font color=#81d4fa>as in the kitchen.</font> ";
////        Spanned sp = Html.fromHtml("<b>There is fire on the hill. For there is smoke, as in the kitchen.</b>");
//        textView.setText(Html.fromHtml(txt));

//        pageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }
}