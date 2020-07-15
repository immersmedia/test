package co.tau.manthan.DebViewPlay;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import static co.tau.manthan.baseclasses.Constants.ARGOP_MESSAGE;

public class DebViewViewModel extends AndroidViewModel {
    private String TAG = "AndroidViewModel";
    private MutableLiveData<String> sendmode = new MutableLiveData<>(ARGOP_MESSAGE);

    public DebViewViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<String> getSendmode() {
        return sendmode;
    }

    public String getSM() {
        return sendmode.getValue().toString();
    }

    public void setSendmode(String sm) {
        sendmode.postValue(sm);
    }
}