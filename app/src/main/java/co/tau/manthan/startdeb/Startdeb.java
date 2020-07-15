package co.tau.manthan.startdeb;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

import co.tau.manthan.Invitation.Invitation;
import co.tau.manthan.R;
import co.tau.manthan.baseclasses.Debate;
import co.tau.manthan.baseclasses.Debater;
import co.tau.manthan.baseclasses.Utils;
import co.tau.manthan.login.SignInActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import static co.tau.manthan.baseclasses.Constants.ARG_ROLE_DEB;
import static co.tau.manthan.baseclasses.Constants.DEBACTIVSTATE_WAITINGJOIN;
import static co.tau.manthan.baseclasses.Constants.FB_RCOLL_DEBATES;
import static co.tau.manthan.baseclasses.Constants.FB_COLL_MYDEBATES;
import static co.tau.manthan.baseclasses.Constants.FB_RCOLL_MYPARTICIPATION;
import static co.tau.manthan.baseclasses.Constants.FB_RCOLL_USERSBYUID;
import static co.tau.manthan.baseclasses.Constants.TILT_AG;
import static co.tau.manthan.baseclasses.Constants.TILT_FOR;

public class Startdeb extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseFirestore mFireStoreRef;

    private Debater myself;
    private Debater dopp = null;
    private Debater dmod = null;
    private Debater dfor = null;
    private String sharelink = null;

    Intent incintent;
    String incaction;
    String inctype;
//    MutableLiveData<Boolean> enablebutton = new MutableLiveData<>(false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startdeb);

        mFireStoreRef = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        } else {
            String myuid = mFirebaseUser.getUid();
            mFireStoreRef.collection(FB_RCOLL_USERSBYUID).document(myuid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    myself = task.getResult().toObject(Debater.class);
//                    dfor = myself;
//
//                    Glide.with(getApplicationContext())
//                            .load(myself.getPiclink())
//                            .into((CircleImageView)findViewById(R.id.playerforpic));
//                    ((TextView)findViewById(R.id.playerforname)).setText(myself.getName());
//                    ((TextView)findViewById(R.id.playerforsid)).setText(myself.getSid());
                }
            });
        }

        incintent = getIntent();
        incaction = incintent.getAction();
        inctype = incintent.getType();

        if (Intent.ACTION_SEND.equals(incaction) && inctype != null) {
            if ("text/plain".equals(inctype)) {
                handleSendText(incintent); // Handle text being sent
            }
        } else {
            // Handle other intents, such as being started from the home screen
        }

//        enablebutton.observe(this, new Observer<Boolean>() {
//            @Override
//            public void onChanged(Boolean aBoolean) {
//                    findViewById(R.id.createconv).setEnabled(aBoolean);
//            }
//        });


        findViewById(R.id.debopponentinvite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Startdeb.this, Invitation.class);
                intent.setAction(String.valueOf(24));

                ArrayList<String> excludes = new ArrayList<>();
                if (dfor!=null){
                    excludes.add(dfor.getUid());
                }
                if (dmod!=null){
                    excludes.add(dmod.getUid());
                }
                intent.putStringArrayListExtra("excludes",excludes);

                startActivityForResult(intent, 24);

            }
        });

        findViewById(R.id.debinviteme).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inviteeuid =myself.getUid();
                String inviteesid =myself.getSid();
                String inviteename =myself.getName();
                String inviteepic =myself.getPiclink();
                addplayer(inviteeuid,inviteesid,inviteename,inviteepic);
                ((AppCompatButton)findViewById(R.id.debinviteme)).setEnabled(false);
            }
        });



        findViewById(R.id.createconv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String topic = ((EditText)findViewById(R.id.startdeb_topic)).getText().toString();
                if (topic.equals("")){
                    Snackbar.make(findViewById(R.id.sdrootlo), "Need to have some topic!", Snackbar.LENGTH_LONG).show();
                    return;
                }
                String debuid = Utils.getakey();
                Debate newdebate = null;
                newdebate = new Debate(debuid, myself.getUid(), topic);

                if (dfor != null) {
                    newdebate.adddebfor(dfor.getUid(), dfor.getSid(), dfor.getName(), dfor.getPiclink());
                }
                if (dopp != null) {
                    newdebate.adddebag(dopp.getUid(),dopp.getSid(),dopp.getName(),dopp.getPiclink());
                }
                if (dmod != null) {
                    newdebate.addmoderator(dmod.getUid(), dmod.getSid(), dmod.getName(), dmod.getPiclink());
                }

                newdebate.setFlagactivestate(DEBACTIVSTATE_WAITINGJOIN);


                mFireStoreRef.collection(FB_RCOLL_DEBATES).document(debuid).set(newdebate);
                if (dfor !=null) {
                    mFireStoreRef.collection(FB_RCOLL_MYPARTICIPATION).document(dfor.getUid()).collection(FB_COLL_MYDEBATES).document(debuid).set(newdebate);
                }
                if (dopp!=null) {
                    mFireStoreRef.collection(FB_RCOLL_MYPARTICIPATION).document(dopp.getUid()).collection(FB_COLL_MYDEBATES).document(debuid).set(newdebate);
                }
                if (dmod!=null){
                    mFireStoreRef.collection(FB_RCOLL_MYPARTICIPATION).document(dmod.getUid()).collection(FB_COLL_MYDEBATES).document(debuid).set(newdebate);
                }


                findViewById(R.id.createconv).setVisibility(View.GONE);
                findViewById(R.id.lo_linkshare).setVisibility(View.VISIBLE);
                sharelink = String.format("http://app.manthanapp.com/join/%s",debuid);
                ((TextView)findViewById(R.id.deblink)).setText(sharelink);


//                if (Intent.ACTION_SEND.equals(incaction)){
//                    finish();
//                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
//                }else{
//                    finish();
//                }
            }
        });

        findViewById(R.id.deblinkshare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharelink != null){
                    Intent intent2 = new Intent();
                    intent2.setAction(Intent.ACTION_SEND);
                    intent2.setType("text/plain");
                    intent2.putExtra(Intent.EXTRA_TEXT, sharelink );
                    startActivity(Intent.createChooser(intent2, "Share via"));
                }
            }
        });

    }

    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            // Update UI to reflect text being shared
            ((EditText)findViewById(R.id.startdeb_topic)).setText(sharedText);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 24){

            String inviteeuid =data.getStringExtra("inviteeuid");
            String inviteesid =data.getStringExtra("inviteesid");
            String inviteename =data.getStringExtra("inviteename");
            String inviteepic =data.getStringExtra("inviteepic");
            addplayer(inviteeuid,inviteesid,inviteename,inviteepic);

//            updateopponentui(inviteeuid,inviteesid,inviteename,inviteepic);
//            dopp = new Debater(inviteeuid,inviteesid,inviteename,inviteepic,null,null,new Date());
//            dopp.addDebaterdetails(ARG_ROLE_DEB,TILT_AG,false);
//            enablebutton.postValue(true);
        }
    }

    private void addplayer(String inviteeuid,String inviteesid, String inviteename, String inviteepic){

        if (dfor == null){
            dfor = new Debater(inviteeuid,inviteesid,inviteename,inviteepic,null,null,new Date());
            dfor.addDebaterdetails(ARG_ROLE_DEB,TILT_FOR,false);
            updateforui(inviteeuid,inviteesid,inviteename,inviteepic);
        }else{
            dopp = new Debater(inviteeuid,inviteesid,inviteename,inviteepic,null,null,new Date());
            dopp.addDebaterdetails(ARG_ROLE_DEB,TILT_AG,false);
            updateopponentui(inviteeuid,inviteesid,inviteename,inviteepic);
        }
    }



    private void addmyself(){
        String inviteeuid =myself.getUid();
        String inviteesid =myself.getSid();
        String inviteename =myself.getName();
        String inviteepic =myself.getPiclink();

        updateforui(inviteeuid,inviteesid,inviteename,inviteepic);
        dfor = new Debater(inviteeuid,inviteesid,inviteename,inviteepic,null,null,new Date());
        dfor.addDebaterdetails(ARG_ROLE_DEB,TILT_FOR,false);
    }

    private void updateforui(String uid, String sid, String name, String pic){
        Glide.with(this)
                .load(pic)
                .into((CircleImageView)findViewById(R.id.playerforpic));
        ((TextView)findViewById(R.id.playerforname)).setText(name);
        ((TextView)findViewById(R.id.playerforsid)).setText(sid);

    }

    private void updateopponentui(String uid, String sid, String name, String pic){
        Glide.with(this)
                .load(pic)
                .into((CircleImageView)findViewById(R.id.playeragpic));
        ((TextView)findViewById(R.id.playeragname)).setText(name);
        ((TextView)findViewById(R.id.playeragsid)).setText(sid);

    }
}
