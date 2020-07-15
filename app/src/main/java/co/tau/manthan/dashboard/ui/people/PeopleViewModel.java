package co.tau.manthan.dashboard.ui.people;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PeopleViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PeopleViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}