package co.tau.manthan.urltodeb;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import co.tau.manthan.DebViewPlay.DebViewPlay;
import co.tau.manthan.ModViewPlay.ModViewPlay;
import co.tau.manthan.R;
import co.tau.manthan.ViewerPlay.ViewerPlay;
import co.tau.manthan.baseclasses.Debate;
import co.tau.manthan.baseclasses.Debater;
import co.tau.manthan.error.ErrorActivity;
import co.tau.manthan.login.SignInActivity;

import static co.tau.manthan.baseclasses.Constants.FB_RCOLL_DEBATES;
import static co.tau.manthan.baseclasses.Constants.FB_RCOLL_USERSBYUID;
import static co.tau.manthan.baseclasses.Constants.TILT_NONE;

public class Urltodeb extends AppCompatActivity {


    private String myuid;
    private String mUsername;
    private String mPhotoUrl;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseFirestore mFireStoreRef;

    private Debater myself;

    private String debateuid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urltodeb);


        mFireStoreRef = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        }

        mUsername = mFirebaseUser.getDisplayName();
        myuid = mFirebaseUser.getUid();
        if (mFirebaseUser.getPhotoUrl() != null) {
            mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
        }
        handleIntent(getIntent());
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }


    private void handleIntent(Intent intent) {
        String appLinkAction = intent.getAction();
        Uri appLinkData = intent.getData();
        if (Intent.ACTION_VIEW.equals(appLinkAction) && appLinkData != null){
            debateuid = appLinkData.getLastPathSegment();
            processrequest();
        }
    }

    private void processrequest(){
        mFireStoreRef.collection(FB_RCOLL_USERSBYUID).document(myuid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                myself = task.getResult().toObject(Debater.class);

                mFireStoreRef.collection(FB_RCOLL_DEBATES).document(debateuid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        Debate debate = task.getResult().toObject(Debate.class);
                        if (debate == null){
                            launcherror();

                        }else{

                            lauchdebate(debate, TILT_NONE);

//                            if (debate.getParticipants().contains(myself.getUid())){
//                                //already present.
//                                //go to the debate
//                                if ((debate.getParticipants().size()>1) && debate.getFlagactivestate().equals(DEBACTIVSTATE_WAITINGJOIN)){
//                                    mFireStoreRef.collection(FB_RCOLL_DEBATES).document(debate.getUid())
//                                            .update("flagactivestate",DEBACTIVSTATE_ONGOING);
//                                }
//                                if (myself.getUid().equals(debate.getDebater_for_uid())){
//                                    lauchdebate(debate,TILT_FOR);
//                                }else if(myself.getUid().equals(debate.getDebater_ag_uid())){
//                                    lauchdebate(debate,TILT_AG);
//                                }else if(myself.getUid().equals(debate.getModeratoruid())) {
//                                    launchMod(debate);
//                                }else{
//                                    lauchdViewer(debate);
//                                }
//                            }else{
//                                if (debate.getParticipants().size() == 0){
//
//                                    debate.adddebfor(myself.getUid(),myself.getSid(),myself.getName(),myself.getPiclink());
//                                    debate.setDebater_for_status(PARTISTATUS_ACCEPTED);
//
//                                    String keyarguid = getakey();
//                                    Date ts = new Date();
//                                    String turn = TILT_FOR;
//                                    Arg_Key keyarg = new
//                                            Arg_Key(keyarguid,
//                                            myself.getUid(),myself.getPiclink(),
//                                            myself.getName(),myself.getSid(),debate.getUid(),
//                                            TILT_FOR,ARGTEXT_DEFAULT,null,ARG_ROLE_DEB,ts);
//                                    keyarg.setkeyparams(KEYARGAACTIV_ACTIVE,RESULTSTATE_WAITING);
//                                    debate.setActivekeyargguid(keyarguid);
//                                    debate.setActivekeyargindex(1);
//
//                                    mFireStoreRef.collection(FB_RCOLL_DEBATES).document(debateuid).set(debate);//we can use update also
//                                    mFireStoreRef.collection(FB_RCOLL_DEBATES).document(debateuid).collection(FB_COLL_KEYARGS).document(keyarguid).set(keyarg);
//                                    mFireStoreRef.collection(FB_RCOLL_MYPARTICIPATION).document(myself.getUid()).collection(FB_COLL_MYDEBATES).document(debateuid).set(debate);
//
//                                    lauchdebate(debate, TILT_FOR);
//                                }else{
//                                    debate.adddebag(myself.getUid(),myself.getSid(),myself.getName(),myself.getPiclink());
//                                    debate.setDebater_ag_status(PARTISTATUS_ACCEPTED);
//
//                                    mFireStoreRef.collection(FB_RCOLL_DEBATES).document(debateuid).set(debate);//we can use update also
//                                    mFireStoreRef.collection(FB_RCOLL_MYPARTICIPATION).document(myself.getUid()).collection(FB_COLL_MYDEBATES).document(debateuid).set(debate);
//                                    lauchdebate(debate, TILT_AG);
//
//                                }
//                            }
                        }
                    }
                });
            }
        });

    }



    private void lauchdebate(Debate deb, String tilt){

        if (myself.getUid().equals(deb.getModeratoruid())) {
            Gson gson = new Gson();
            String myDebateJson = gson.toJson(deb);
            String myselfJson = gson.toJson(myself);

            finishAffinity();
            Intent intent = new Intent(getApplicationContext(), ModViewPlay.class);
            intent.putExtra("mydebate", myDebateJson);
            intent.putExtra("myself", myselfJson);
            startActivity(intent);
        }else {
            Gson gson = new Gson();
            myself.setTilt(tilt);
            String myDebateJson = gson.toJson(deb);
            String myselfJson = gson.toJson(myself);
            finishAffinity();
            Intent intent = new Intent(getApplicationContext(), DebViewPlay.class);
            intent.putExtra("mydebate", myDebateJson);
            intent.putExtra("myself", myselfJson);
            startActivity(intent);
        }
    }

    private void launchMod(Debate deb){
        Gson gson = new Gson();
        String myDebateJson = gson.toJson(deb);
        String myselfJson = gson.toJson(myself);

        Intent intent = new Intent(getApplicationContext(), ModViewPlay.class);
        intent.putExtra("mydebate", myDebateJson);
        intent.putExtra("myself", myselfJson);
        startActivity(intent);
    }

    private void lauchdViewer(Debate deb){

        Gson gson = new Gson();
        String myDebateJson = gson.toJson(deb);
        String myselfJson = gson.toJson(myself);

        Intent intent = new Intent(getApplicationContext(), ViewerPlay.class);
        intent.putExtra("mydebate", myDebateJson);
        intent.putExtra("myself", myselfJson);
        startActivity(intent);
    }

    private void launcherror(){
        Intent intent = new Intent(getApplicationContext(), ErrorActivity.class);
        startActivity(intent);
    }
}
