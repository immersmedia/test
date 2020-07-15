package co.tau.manthan.dashboard.ui.myconvs;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyConvsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MyConvsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}