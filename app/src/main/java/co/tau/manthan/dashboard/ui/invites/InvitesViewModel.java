package co.tau.manthan.dashboard.ui.invites;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InvitesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public InvitesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}