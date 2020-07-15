package co.tau.manthan.infoscreen.ui.main;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

//    @StringRes
//    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).

        switch (position){
//            case 0: return Fragment_1.newInstance(position + 1);
            case 1: return Fragment_2.newInstance(position + 1);
            case 2: return Fragment_3.newInstance(position + 1);
            case 3: return Fragment_4.newInstance(position + 1);
            case 4: return Fragment_5.newInstance(position + 1);
            case 5: return Fragment_6.newInstance(position + 1);
            case 6: return Fragment_7.newInstance(position + 1);

            default: return Fragment_1.newInstance(position + 1);
        }
    }

//    @Nullable
//    @Override
//    public CharSequence getPageTitle(int position) {
//        return mContext.getResources().getString(TAB_TITLES[position]);
//    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 7;
    }
}