/**
 * Copyright Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package co.tau.manthan.DebViewPlay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.otaliastudios.autocomplete.Autocomplete;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.tau.manthan.R;
import co.tau.manthan.baseclasses.Arg_Conv;
import co.tau.manthan.baseclasses.Arg_Key;
import co.tau.manthan.baseclasses.BUser;
import co.tau.manthan.baseclasses.Debate;
import co.tau.manthan.baseclasses.Debater;
import co.tau.manthan.baseclasses.Hashtag;
import co.tau.manthan.baseclasses.InviteMod;
import co.tau.manthan.baseclasses.Siditem;
import co.tau.manthan.baseclasses.Tags;
import co.tau.manthan.baseclasses.TextUtils;
import co.tau.manthan.baseclasses.Utils;
import co.tau.manthan.login.SignInActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static co.tau.manthan.baseclasses.Arg_Key.KEYARGAACTIV_ACTIVE;
import static co.tau.manthan.baseclasses.Arg_Key.KEYARGAACTIV_FINISHED;
import static co.tau.manthan.baseclasses.Arg_Key.RESULTSTATE_WAITING;
import static co.tau.manthan.baseclasses.BArg.ARGTEXT_DEFAULT;
import static co.tau.manthan.baseclasses.Constants.*;
import static co.tau.manthan.baseclasses.Debate.PARTISTATUS_ACCEPTED;
import static co.tau.manthan.baseclasses.InviteMod.INVTYPE_ASKGURU;
import static co.tau.manthan.baseclasses.InviteMod.INVTYPE_MOD;
import static co.tau.manthan.baseclasses.Utils.dpToPx;
import static co.tau.manthan.baseclasses.Utils.getakey;
import static co.tau.manthan.baseclasses.Utils.getoppturn;


public class DebViewPlay extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener {

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        ImageView messageImageView;
//        AppCompatTextView mseltagView;
        LinearLayout tagview;
        ChipGroup allscorelo;
        ConstraintLayout msg_outerlo;
        ConstraintLayout hightlightlo;
        LinearLayout viewerpanel;
        TextView iconcur;
        TextView iconcurnot;


        public MessageViewHolder(View v) {
            super(v);
            messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
            messageImageView = (ImageView) itemView.findViewById(R.id.messageImageView);
//            mseltagView = (AppCompatTextView)itemView.findViewById(R.id.convmsg_seltag);
            tagview = (LinearLayout)itemView.findViewById(R.id.tagview);
            allscorelo = (ChipGroup)itemView.findViewById(R.id.allscore);
            msg_outerlo = (ConstraintLayout)itemView.findViewById(R.id.msg_outerlo);
            hightlightlo = (ConstraintLayout)itemView.findViewById(R.id.hightlightlo);
            viewerpanel = (LinearLayout) itemView.findViewById(R.id.viewerpanel);
            iconcur = (TextView) itemView.findViewById(R.id.tagconcur);
            iconcurnot = (TextView) itemView.findViewById(R.id.tagnoconcur);


        }
    }

    private static final String TAG = "MainActivity";


    private String mUsername;
    private String mPhotoUrl;
    private SharedPreferences mSharedPreferences;
    private GoogleApiClient mGoogleApiClient;

    private ImageButton mSendButton;
    private ImageButton mSendButtonop;
    private RecyclerView mMessageRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private EditText mMessageEditText;
    private EditText messageEditTextop;

    private ImageView mAddMessageImageView;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private Debate mydebateintent = new Debate();
    private Debate mydebate = new Debate();
    private Debater myselfintent = new Debater();
//    private Arg_Key mykeyArg = new Arg_Key();
    private Arg_Key mykeyArg = null;

    private ArrayList<Arg_Key> keyargs = new ArrayList<>();
    private Map keyargmap = new HashMap<String, Arg_Key>();

    List<Siditem> sidlist = new ArrayList<>();

//    ListenerRegistration kerargreg;

    private DebViewViewModel debviewmodel;

    String myactivekeyarg = null;
    CircleImageView deboppImageView;
    CircleImageView modImageView;
    CircleImageView myselfImageView;
    private Autocomplete hashtagAutocomplete;
    private Autocomplete siditemAutocomplete;
    private  ConstraintLayout mlo_normal;
    private  ConstraintLayout mlo_op;

    String opponenttilt = TILT_FOR;


//    private DatabaseReference mFirebaseDatabaseReference;
//    private FirebaseRecyclerAdapter<FriendlyMessage, MessageViewHolder>
//            mFirebaseAdapter;

    private FirebaseFirestore mFireStoreRef;
    private FirestoreRecyclerAdapter mFirestoreAdapter;
    Activity applicationcontext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        applicationcontext  = this;

        mMessageEditText = (EditText) findViewById(R.id.dv_messageEditText);
        messageEditTextop = (EditText) findViewById(R.id.dv_messageEditTextop);

        mlo_normal = (ConstraintLayout)findViewById(R.id.dv_sendmessagelayout);
        mlo_op = (ConstraintLayout)findViewById(R.id.dv_sendmessagelayoutop);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Gson gson = new Gson();
        mydebateintent = gson.fromJson(getIntent().getStringExtra("mydebate"), Debate.class);
        myselfintent = gson.fromJson(getIntent().getStringExtra("myself"), Debater.class);
        myactivekeyarg = mydebateintent.getActivekeyargguid();
        deboppImageView = (CircleImageView)findViewById(R.id.dv_leftImageView);
        modImageView = (CircleImageView)findViewById(R.id.dv_centerImageView);
        myselfImageView = (CircleImageView)findViewById(R.id.dv_rightImageView);


        // Set default username is anonymous.
        mUsername = ANONYMOUS;
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        // Initialize ProgressBar and RecyclerView.


        mMessageRecyclerView = (RecyclerView) findViewById(R.id.dv_messageRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);

        mFireStoreRef = FirebaseFirestore.getInstance();

//        =======================================================




        Autocomplete<Hashtag> htagacomplete = Tags.setupHashtagAutocomplete(mMessageEditText,
                '#',this,Hashtag.HashtagS);

        Autocomplete<Hashtag> htagacompleteop = Tags.setupHashtagAutocomplete(messageEditTextop,
                '#',this,Hashtag.HashtagSop);

        mFireStoreRef.collection(FB_RCOLL_SIDLIST).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                QuerySnapshot qs = task.getResult();
                for (DocumentSnapshot dc : qs.getDocuments()){
                    Siditem ds = dc.toObject(Siditem.class);
                    ds.setSid(ds.getSid().substring(1,ds.getSid().length()));
                    sidlist.add(ds);
                }
                Autocomplete<Siditem> sidtagacomplete = Tags.setupatlistAutocomplete(mMessageEditText,
                        '@',applicationcontext,sidlist);

                Autocomplete<Siditem> sidtagacompleteop = Tags.setupatlistAutocomplete(messageEditTextop,
                        '@',applicationcontext,sidlist);
            }
        });


//        if (myselfintent.getTilt().equals(TILT_FOR)){



        debviewmodel = ViewModelProviders.of(this).get(DebViewViewModel.class);
//        debviewmodel.getSendmode().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(String s) {
//                if (s.equals(ARGOP_MESSAGE)){
////                    mMessageEditText.setVisibility(VISIBLE);
//                    mlo_normal.setVisibility(VISIBLE);
//                    mlo_op.setVisibility(GONE);
//                }else if(s.equals(ARGOP_FALLACY)){
//                    mlo_normal.setVisibility(GONE);
//                    mlo_op.setVisibility(VISIBLE);
////                    mMessageEditText.setVisibility(View.INVISIBLE);
//                }else if(s.equals(ARGOP_CLINCHER)){
//                    mlo_normal.setVisibility(GONE);
//                    mlo_op.setVisibility(VISIBLE);
////                    mMessageEditText.setVisibility(View.INVISIBLE);
//                }else if (s.equals(ARGOP_IAGREE)){
//                    mlo_normal.setVisibility(GONE);
//                    mlo_op.setVisibility(VISIBLE);
////                    mMessageEditText.setVisibility(View.INVISIBLE);
//                }
//            }
//        });

        // Initialize Firebase Auth





        ((TextView)findViewById(R.id.dv_debtitle)).setText(mydebateintent.getTopic());

        mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                .collection(FB_COLL_KEYARGS).orderBy("ts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                keyargs.clear();
                keyargmap.clear();

                for (DocumentSnapshot dc : queryDocumentSnapshots.getDocuments()){
                    Arg_Key keyarg = dc.toObject(Arg_Key.class);
                    keyargs.add(keyarg);
                    keyargmap.put(keyarg.getUid(),keyarg);
                }
                if (keyargs.size() >0){
                    mykeyArg = keyargs.get(keyargs.size()-1);
                }
            }
        });

        mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                mydebate=snapshot.toObject(Debate.class);
                String conscore = mydebate.getDebstattotalpoints() +"/"+ mydebate.getActivekeyargindex();
                ((ExtendedFloatingActionButton)findViewById(R.id.conscore)).setText(conscore);
                if (getmytilt().equals(TILT_FOR)){
                    Glide.with(getApplicationContext())
                            .load(mydebate.getDebater_for_pic())
                            .into(myselfImageView);
                    ((TextView)findViewById(R.id.dv_rightname)).setText(mydebate.getDebater_for_name());

                    Glide.with(getApplicationContext())
                            .load(mydebate.getDebater_ag_pic())
                            .into(deboppImageView);
                    ((TextView)findViewById(R.id.dv_leftname)).setText(mydebate.getDebater_ag_name());
                }else if (getmytilt().equals(TILT_AG)){
                    Glide.with(getApplicationContext())
                            .load(mydebate.getDebater_ag_pic())
                            .into(myselfImageView);
                    ((TextView)findViewById(R.id.dv_rightname)).setText(mydebate.getDebater_ag_name());

                    Glide.with(getApplicationContext())
                            .load(mydebate.getDebater_for_pic())
                            .into(deboppImageView);
                    ((TextView)findViewById(R.id.dv_leftname)).setText(mydebate.getDebater_for_name());

                }else{
                    if (mydebate.getDebater_for_uid()!= null){
                        Glide.with(getApplicationContext())
                                .load(mydebate.getDebater_for_pic())
                                .into(deboppImageView);
                        ((TextView)findViewById(R.id.dv_leftname)).setText(mydebate.getDebater_for_name());
                    }
                    if (mydebate.getDebater_ag_uid()!= null){
                        Glide.with(getApplicationContext())
                                .load(mydebate.getDebater_ag_pic())
                                .into(deboppImageView);
                        ((TextView)findViewById(R.id.dv_leftname)).setText(mydebate.getDebater_ag_name());
                    }
                }

                if (mydebate.getModeratoruid() != null){
                    modImageView.setVisibility(VISIBLE);
                    ((TextView)findViewById(R.id.dv_centername)).setVisibility(VISIBLE);

                    Glide.with(getApplicationContext())
                            .load(mydebate.getModerator_pic())
                            .into(modImageView);
                    ((TextView)findViewById(R.id.dv_centername)).setText(mydebate.getModerator_name());
                }else{
                    ((TextView)findViewById(R.id.dv_centername)).setVisibility(GONE);
                    modImageView.setVisibility(GONE);
                }


                if (mydebate.getFlagactivestate().equals(DEBACTIVSTATE_WAITINGJOIN)){
                    findViewById(R.id.dv_toplo2).setVisibility(VISIBLE);
                    findViewById(R.id.dv_bottompanel).setVisibility(GONE);
                    findViewById(R.id.dv_toplo3).setVisibility(GONE);
                    findViewById(R.id.dv_credag_buttonlo).setVisibility(GONE);
                    findViewById(R.id.dv_credfor_buttonlo).setVisibility(GONE);

                    if (mydebate.getDebater_for_uid()!=null){
                        if (myselfintent.getUid().equals(mydebate.getDebater_for_uid())) {
                            if(mydebate.getDebater_for_status().equals(PARTISTATUS_ACCEPTED)){
                                ((TextView)findViewById(R.id.dv_credfor_txt)).setText("You have accepted");
                                findViewById(R.id.dv_credfor_buttonlo).setVisibility(GONE);
                            }else{
                                ((TextView)findViewById(R.id.dv_credfor_txt)).setText("Waiting for you");
                                findViewById(R.id.dv_credfor_buttonlo).setVisibility(VISIBLE);
                            }
                        }else{
                            if(mydebate.getDebater_for_status().equals(PARTISTATUS_ACCEPTED)){
                                ((TextView)findViewById(R.id.dv_credfor_txt)).setText((mydebate.getDebater_for_name()+"has accepted"));
                                findViewById(R.id.dv_credfor_buttonlo).setVisibility(GONE);
                            }else{
                                ((TextView)findViewById(R.id.dv_credfor_txt)).setText("Waiting for "+mydebate.getDebater_for_name());
                                findViewById(R.id.dv_credfor_buttonlo).setVisibility(GONE);

                            }
                        }


                        if (mydebate.getDebater_ag_uid() != null) {
                            if (myselfintent.getUid().equals(mydebate.getDebater_ag_uid())){
                                if(mydebate.getDebater_ag_status().equals(PARTISTATUS_ACCEPTED)) {
                                    ((TextView)findViewById(R.id.dv_credag_txt)).setText("You have accepted");
                                    findViewById(R.id.dv_credag_buttonlo).setVisibility(GONE);
                                }else{
                                    ((TextView)findViewById(R.id.dv_credag_txt)).setText("Waiting for you");
                                    findViewById(R.id.dv_credag_buttonlo).setVisibility(VISIBLE);
                                }
                            }else{
                                if(mydebate.getDebater_ag_status().equals(PARTISTATUS_ACCEPTED)) {
                                    ((TextView)findViewById(R.id.dv_credag_txt)).setText(mydebate.getDebater_ag_name()+"has accepted");
                                    findViewById(R.id.dv_credag_buttonlo).setVisibility(GONE);
                                }else{
                                    ((TextView)findViewById(R.id.dv_credag_txt)).setText("Waiting for "+mydebate.getDebater_ag_name());
                                    findViewById(R.id.dv_credag_buttonlo).setVisibility(GONE);
                                }
                            }
                        }else {
                            if(myselfintent.getUid().equals(mydebate.getDebater_for_uid())){
                                ((TextView)findViewById(R.id.dv_credag_txt)).setText("Waiting for your co-conversationalist");
                                findViewById(R.id.dv_credag_buttonlo).setVisibility(GONE);
                            }else{
                                ((TextView)findViewById(R.id.dv_credag_txt)).setText("Waiting for you");
                                findViewById(R.id.dv_credag_buttonlo).setVisibility(VISIBLE);
                            }

                        }
                    }else {
                        ((TextView)findViewById(R.id.dv_credfor_txt)).setText("Waiting for you");
                        findViewById(R.id.dv_credfor_buttonlo).setVisibility(VISIBLE);

                        ((TextView)findViewById(R.id.dv_credag_txt)).setText("Waiting for a co-conversationalist");
                        findViewById(R.id.dv_credag_buttonlo).setVisibility(GONE);
                    }

                    if(mydebate.getDebater_for_uid()!= null && (mydebate.getDebater_ag_uid()!= null)){
                        mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                                .update("flagactivestate",DEBACTIVSTATE_ONGOING);

//                        newsysmessage("Hello folks, wish you a fruitful discussion. \n"
//                                +mydebate.getDebater_for_name()+", you can start with your main line of thought on the subject",MSGTYPE_SYS);

                    }

                }else if (mydebate.getFlagactivestate().equals(DEBACTIVSTATE_ONGOING)){
                    findViewById(R.id.dv_toplo2).setVisibility(GONE);
                    findViewById(R.id.dv_toplo3).setVisibility(GONE);

                    if (mydebate.getTurn().equals(TURN_MOD)){
                        findViewById(R.id.dv_turnviewmod).setVisibility(VISIBLE);
                        findViewById(R.id.dv_turnviewleft).setVisibility(View.INVISIBLE);
                        findViewById(R.id.dv_turnviewright).setVisibility(View.INVISIBLE);
                    }else{
                        if (getmytilt().equals(mydebate.getTurn())){
                            //MY TURN
                            findViewById(R.id.dv_turnviewmod).setVisibility(View.INVISIBLE);
                            findViewById(R.id.dv_turnviewleft).setVisibility(View.INVISIBLE);
                            findViewById(R.id.dv_turnviewright).setVisibility(VISIBLE);
                        }else{
                            findViewById(R.id.dv_turnviewmod).setVisibility(View.INVISIBLE);
                            findViewById(R.id.dv_turnviewleft).setVisibility(VISIBLE);
                            findViewById(R.id.dv_turnviewright).setVisibility(View.INVISIBLE);
                        }
                    }

                    if (mydebate.getTurn().equals(getmytilt())){
                        //MY TURN
                        if ((mydebate.getModeratoruid() == null) && (mydebate.getConvcontexttags().size() >0) && (mydebate.getConvcontexttagstate().equals(TAG_PENDING))) {
                            String contxetargtext =mydebate.getConvcontextargtext();
                            mlo_normal.setVisibility(GONE);
                            mlo_op.setVisibility(VISIBLE);
                            findViewById(R.id.dv_bottompanel).setVisibility(VISIBLE);

                            messageEditTextop.requestFocus();
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(messageEditTextop, InputMethodManager.SHOW_IMPLICIT);
                        }else {
                            mlo_normal.setVisibility(VISIBLE);
                            mlo_op.setVisibility(GONE);
                            findViewById(R.id.dv_bottompanel).setVisibility(VISIBLE);

                            mMessageEditText.requestFocus();
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(mMessageEditText, InputMethodManager.SHOW_IMPLICIT);
                        }
                    }else{
                        findViewById(R.id.dv_bottompanel).setVisibility(GONE);

                        View view = getCurrentFocus();
                        if (view!= null){
                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                    }

                    String source = snapshot != null && snapshot.getMetadata().hasPendingWrites()
                            ? "Local" : "Server";

                    if (snapshot != null && snapshot.exists()) {
                        Log.d(TAG, source + " data: " + snapshot.getData());
                    } else {
                        Log.d(TAG, source + " data: null");
                    }
                }else if(mydebate.getFlagactivestate().equals(DEBACTIVSTATE_FINISHED)){

                    findViewById(R.id.dv_toplo3).setVisibility(VISIBLE);
                    findViewById(R.id.dv_toplo2).setVisibility(View.GONE);
                    findViewById(R.id.dv_bottompanel).setVisibility(GONE);
//                    ((TextView)findViewById(R.id.dv_finishconvscore)).setText(String.valueOf(mydebate.getDebstattotalpoints()));
                    ChipGroup alltagsv = (ChipGroup)findViewById(R.id.dv_finishtxtlabel1);
                    Utils.displayalltags(alltagsv, mydebate.getDeballtags(),getApplicationContext());

                }else{

                }
            }
        });

        ((AppCompatButton)findViewById(R.id.dv_credfor_acceptbutton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mydebate.getParticipants().add(myselfintent.getUid());
                mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                        .update(
                                "debater_for_uid",myselfintent.getUid(),
                                "debater_for_sid",myselfintent.getSid(),
                                "debater_for_name",myselfintent.getName(),
                                "debater_for_pic",myselfintent.getPiclink(),
                                "participants", mydebate.getParticipants(),
                                "debater_for_status",PARTISTATUS_ACCEPTED
                        );
                myselfintent.setTilt(TILT_FOR);




//                if (getmytilt().equals(TILT_FOR)){
//                    if (mydebate.getDebater_ag_status().equals(PARTISTATUS_ACCEPTED) ){
//
//                        mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
//                                .update("debater_for_status",PARTISTATUS_ACCEPTED,"flagactivestate",DEBACTIVSTATE_ONGOING);
//                        newsysmessage("Hello folks, wish you a fruitful discussion. \n"
//                                +mydebate.getDebater_for_name()+", you can start with your main line of thought on the subject",MSGTYPE_SYS);
//
//                    }else{
//                        mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
//                                .update("debater_for_status",PARTISTATUS_ACCEPTED);
//                        newsysmessage("Hello folks, wish you a fruitful discussion. \n"
//                                +mydebate.getDebater_for_name()+", you can start with your main line of thought on the subject",MSGTYPE_SYS);
//                    }
//                }else{
//                    if (mydebate.getDebater_for_status().equals(PARTISTATUS_ACCEPTED) ){
//
//                        mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
//                                .update("debater_ag_status",PARTISTATUS_ACCEPTED,"flagactivestate",DEBACTIVSTATE_ONGOING);
//                        newsysmessage("Hello folks, wish you a fruitful discussion. \n"
//                                +mydebate.getDebater_for_name()+", you can start with your main line of thought on the subject",MSGTYPE_SYS);
//
//                    }else{
//                        mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
//                                .update("debater_ag_status",PARTISTATUS_ACCEPTED);
//                        newsysmessage("Hello folks, wish you a fruitful discussion. \n"
//                                +mydebate.getDebater_for_name()+", you can start with your main line of thought on the subject",MSGTYPE_SYS);
//                    }
//                }
            }
        });

        ((AppCompatButton)findViewById(R.id.dv_credag_acceptbutton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mydebate.getParticipants().add(myselfintent.getUid());
                mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                        .update(
                                "debater_ag_uid",myselfintent.getUid(),
                                "debater_ag_sid",myselfintent.getSid(),
                                "debater_ag_name",myselfintent.getName(),
                                "debater_ag_pic",myselfintent.getPiclink(),
                                "participants", mydebate.getParticipants(),
                                "debater_ag_status",PARTISTATUS_ACCEPTED
                        );
                myselfintent.setTilt(TILT_AG);
//                if (mydebate.getParticipants().size()>1){
//                    mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
//                            .update("flagactivestate",DEBACTIVSTATE_ONGOING);
//                    newsysmessage("Hello folks, wish you a fruitful discussion. \n"
//                            +mydebate.getDebater_for_name()+", you can start with your main line of thought on the subject",MSGTYPE_SYS);
//                }

//                if (mydebate.getDebater_for_status().equals(PARTISTATUS_ACCEPTED)){
//                    mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
//                            .update("debater_ag_status",PARTISTATUS_ACCEPTED,"flagactivestate",DEBACTIVSTATE_ONGOING);
//                    newsysmessage("Hello folks, wish you a fruitful discussion. \n"
//                            +mydebate.getDebater_for_name()+", you can start with your main line of thought on the subject",MSGTYPE_SYS);
//                }else{
//                        mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
//                                .update("debater_ag_status",PARTISTATUS_ACCEPTED);
//                    newsysmessage("Hello folks, wish you a fruitful discussion. \n"
//                            +mydebate.getDebater_for_name()+", you can start with your main line of thought on the subject",MSGTYPE_SYS);
//                }
            }
        });


        Query query = mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                .collection(FB_COLL_CONVS).orderBy("ts");
        FirestoreRecyclerOptions<Arg_Conv> mstoreoptions =
                new FirestoreRecyclerOptions.Builder<Arg_Conv>()
                        .setQuery(query, Arg_Conv.class)
                        .build();
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setVisibility(VISIBLE);
                } else {
                    mSendButton.setVisibility(GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        messageEditTextop.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButtonop.setVisibility(VISIBLE);
                } else {
                    mSendButtonop.setVisibility(GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });



        mSendButton = (ImageButton) findViewById(R.id.dv_sendButton);
        mSendButtonop = (ImageButton) findViewById(R.id.dv_sendButtonop);
        mSendButton.setVisibility(GONE);
        mSendButtonop.setVisibility(GONE);

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date ts = new Date();

                String msg = mMessageEditText.getText().toString();
                ArrayList<String> hashtags = TextUtils.getHashTags(msg);
                if ((hashtags.size()>0) && (mydebate.getConvcontextarguid() == null)){
                    newsimpledebmessage(msg, ts);
                    newsysmessage(getactivedebname()+", The conversation expects a plain argument without tags.",MSGTYPE_SYS);
                    clearedittexts();
                    return;
                }

                if (hashtags.size() > 0){
                    if (hashtags.get(0).toLowerCase().equals(POSITIVE_IAGREE.toLowerCase())){
                        newsimpledebmessage(mMessageEditText.getText().toString(), ts);
                        finish_conv();
                        if (mydebate.getActivekeyargindex()>=MAX_KEYARGCOUNT){
                            newsysmessage("",MSGTYPE_SYS_DEBEND);
                            newsysmessage("The conversation ends here. That's a wrap folks.",MSGTYPE_SYS);
                            enddebate();
                        }else{
                            String nextturn = getoppturn(mykeyArg.getTilt());
                            newsysmessage("Thats fantastic! We have a meeting of minds here!",MSGTYPE_SYS_DEBEND);
                            addpointsto_deb(1000);
                            startnew_conv(nextturn);
                            setturn(nextturn);
                        }
                    }else if (hashtags.get(0).toLowerCase().equals(TAGOP_INVITEMOD.toLowerCase())){
                        ArrayList<String> attags = TextUtils.getatTags(msg);
                        newsimpledebmessage(mMessageEditText.getText().toString(), ts);
                        if (attags.size()>0) {
                            String modsid = attags.get(0);
                            if (modsid != null) {
                                iaskformod(modsid);
                            } else {
                                newsysmessage("The invite seems incorrect.", MSGTYPE_SYS);
                            }
                        }else{
                            newsysmessage("The invite seems incorrect. Probably missing whome to invite", MSGTYPE_SYS);
                        }
                    }
//                    TODO: check for only allowed #tags
//                    else if (){
//
//                    }
                    else{
                        newdebtag(hashtags,mMessageEditText.getText().toString());
                        newsimpledebmessage(mMessageEditText.getText().toString(), ts);
                        if (mydebate.getModeratoruid() ==(null)){
                            setturn(getoppturn(getmytilt()));
                        }else{
                            setturn(TURN_MOD);
                        }
                        debviewmodel.setSendmode(ARGOP_FALLACY);
                    }

                }else{
                    newdebmessage(mMessageEditText.getText().toString(), ts);
                    addpointsto_deb(100);

                }
                debviewmodel.setSendmode(ARGOP_MESSAGE);
                clearedittexts();
            }
        });

        mSendButtonop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date ts = new Date();
//                if ((mydebate.getModeratoruid() == null) && (mydebate.getConvcontexttags().size()>0) && (mydebate.getConvcontexttagstate().equals(TAG_PENDING))){

                String msg = messageEditTextop.getText().toString();
                ArrayList<String> hashtags = TextUtils.getHashTags(msg);

                if (hashtags.size() > 0){
                    String tag = hashtags.get(0);
                    if (tag.equals(TAGOP_IACCEPT)){
                        newsimpledebmessage(messageEditTextop.getText().toString(), ts);
                        accepttag();
                    }else if(tag.equals(TAGOP_IREJECT)){
                        newsimpledebmessage(messageEditTextop.getText().toString(), ts);
                        rejecttag();
                    }else if(tag.equals(TAGOP_IASKGURU)){
                        ArrayList<String> attags = TextUtils.getatTags(msg);
                        newsimpledebmessage(messageEditTextop.getText().toString(), ts);
                        if (attags.size()>0) {
                            String modsid = attags.get(0);
                            if (modsid != null) {
                                iaskguru(modsid);
                            } else {
                                newsysmessage("The invite seems incorrect.", MSGTYPE_SYS);
                            }
                        }else{
                            newsysmessage("The invite seems incorrect. Probably missing whome to invite", MSGTYPE_SYS);
                        }
                    }else if (tag.equals(TAGOP_INVITEMOD)){
                        ArrayList<String> attags = TextUtils.getatTags(msg);
                        newsimpledebmessage(messageEditTextop.getText().toString(), ts);
                        if (attags.size()>0) {
                            String modsid = attags.get(0);
                            if (modsid != null) {
                                iaskformod(modsid);
                            } else {
                                newsysmessage("The invite seems incorrect.", MSGTYPE_SYS);
                            }
                        }else{
                            newsysmessage("The invite seems incorrect. Probably missing whome to invite", MSGTYPE_SYS);
                        }
                    }else {
                        newsimpledebmessage(messageEditTextop.getText().toString(), ts);
                        newsysmessage(getactivedebname()+", +You need to Accept, Reject or ask for help.",MSGTYPE_SYS);
                        clearedittexts();
                    }

                }else{
                    newsimpledebmessage(messageEditTextop.getText().toString(), ts);
                    newsysmessage(getactivedebname()+", You need to #IAccept or #IReject",MSGTYPE_SYS);
                    clearedittexts();
//                        addpointsto_deb(100);
                }

                clearedittexts();
            }
        });




        mFirestoreAdapter = new FirestoreRecyclerAdapter<Arg_Conv, MessageViewHolder>(mstoreoptions) {

            @NonNull
            @Override
            public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                if (viewType == VIEW_RIGHTMSG) {
                    return new MessageViewHolder(inflater.inflate(R.layout.itemdebview_mymessage, parent, false));
                } else if (viewType == VIEW_LEFTMSG) {
                    return new MessageViewHolder(inflater.inflate(R.layout.itemdebview_oppomessage, parent, false));
                } else if (viewType == VIEW_MODERATORMSG) {
                    return new MessageViewHolder(inflater.inflate(R.layout.itemdebview_modmessage, parent, false));
                }else if (viewType == VIEW_SYSMSG) {
                    return new MessageViewHolder(inflater.inflate(R.layout.itemdebview_sysmessage, parent, false));
                }else if (viewType == VIEW_SYSMSG_DEBEND) {
                    return new MessageViewHolder(inflater.inflate(R.layout.itemdebview_sysmessage_debend, parent, false));
                }else{
                    return new MessageViewHolder(inflater.inflate(R.layout.itemdebview_oppomessage, parent, false));
                }
            }

            @Override
            public int getItemViewType(int position) {
//                if ()
                Arg_Conv fm = getItem(position);

                if (fm.getMsg_type().equals(MSGTYPE_DEB)) {
                    if (fm.getDebateruid().equals(myselfintent.getUid())) {
                        return VIEW_RIGHTMSG;
                    } else {
                        return VIEW_LEFTMSG;
                    }
                }else if(fm.getMsg_type().equals(MSGTYPE_MOD)){
                    return VIEW_MODERATORMSG;
                }else if(fm.getMsg_type().equals(MSGTYPE_SYS)){
                    return VIEW_SYSMSG;
                }else if(fm.getMsg_type().equals(MSGTYPE_SYS_DEBEND)){
                    return VIEW_SYSMSG_DEBEND;
                }
                else{
                    return VIEW_NOMSG;
                }
//                return super.getItemViewType(position);
            }

            @Override
            protected void onBindViewHolder(@NonNull final MessageViewHolder viewHolder, final int position, @NonNull final Arg_Conv Arg_Conv) {

                viewHolder.messageImageView.setVisibility(GONE);
                viewHolder.messageTextView.setVisibility(GONE);
                viewHolder.tagview.setVisibility(GONE);

                if (!Arg_Conv.isExpanded() && !Arg_Conv.isIskeyarg()){
                    viewHolder.msg_outerlo.setVisibility(GONE);
                    viewHolder.msg_outerlo.setLayoutParams(new LinearLayout.LayoutParams(0,0));
                    return;
                }else{
                    viewHolder.msg_outerlo.setVisibility(VISIBLE);
                    viewHolder.msg_outerlo.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                }

                if (Arg_Conv.isIskeyarg()){
                    viewHolder.msg_outerlo.setBackground(getDrawable(R.color.background_dark));
                    viewHolder.msg_outerlo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String keyarguid = Arg_Conv.getKeyarguid();
                            for (int i=0;i< mFirestoreAdapter.getItemCount(); i++){
                                if (keyarguid.equals(((Arg_Conv)mFirestoreAdapter.getItem(i)).getKeyarguid())) {
                                    boolean expanded = ((Arg_Conv)mFirestoreAdapter.getItem(i)).isExpanded();
                                    ((Arg_Conv)mFirestoreAdapter.getItem(i)).setExpanded(!expanded);
                                }
                            }
                            mFirestoreAdapter.notifyDataSetChanged();
                        }
                    });
                }else{
                    viewHolder.msg_outerlo.setBackgroundResource(0);
                    viewHolder.msg_outerlo.setOnClickListener(null);
                }

                if(Arg_Conv.getUid().equals(mydebate.getConvcontextarguid())){
                    viewHolder.hightlightlo.setBackgroundResource(R.drawable.cardshape);
                }else{
                    viewHolder.hightlightlo.setBackgroundResource(0);
                }

                if (Arg_Conv.getArgtext() != null) {
                    String msg = Arg_Conv.getArgtext();
                    SpannableString htag = TextUtils.decoratemessage(msg);
                    viewHolder.messageTextView.setText(htag);
                    viewHolder.messageTextView.setVisibility(VISIBLE);
                }
                if (Arg_Conv.getAttachimg() != null) {
                    String imageUrl = Arg_Conv.getAttachimg();
                    if (imageUrl.startsWith("gs://")) {
                        StorageReference storageReference = FirebaseStorage.getInstance()
                                .getReferenceFromUrl(imageUrl);
                        storageReference.getDownloadUrl().addOnCompleteListener(
                                new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()) {
                                            String downloadUrl = task.getResult().toString();
                                            Glide.with(viewHolder.messageImageView.getContext())
                                                    .load(downloadUrl)
                                                    .into(viewHolder.messageImageView);
                                        } else {
                                            Log.w(TAG, "Getting download url was not successful.",
                                                    task.getException());
                                        }
                                    }
                                });
                    } else {
                        Glide.with(viewHolder.messageImageView.getContext())
                                .load(Arg_Conv.getAttachimg())
                                .into(viewHolder.messageImageView);
                    }
                    viewHolder.messageImageView.setVisibility(VISIBLE);
                }
                if (Arg_Conv.getTags().size()>0){
                    viewHolder.tagview.removeAllViews();

                    for ( String tag: Arg_Conv.getTags()){
//                        ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(
//                                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        ViewGroup.MarginLayoutParams lparams = (ViewGroup.MarginLayoutParams)viewHolder.tagview.getLayoutParams();
                        TextView tv=new TextView(viewHolder.tagview.getContext());
                        tv.setBackground(getResources().getDrawable(R.drawable.deblabel));
                        tv.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(),R.color.accent_light));
                        tv.setPadding(dpToPx(5,viewHolder.tagview.getContext()),
                                dpToPx(5,viewHolder.tagview.getContext()),
                                dpToPx(5,viewHolder.tagview.getContext()),
                                dpToPx(5,viewHolder.tagview.getContext()));
                        lparams.setMargins(15,
                                dpToPx(5,viewHolder.tagview.getContext()),
                                15,
                                dpToPx(5,viewHolder.tagview.getContext()));

                        tv.setTextColor(getResources().getColor(R.color.primary_text));

                        tv.setLayoutParams(lparams);
                        tv.setText(tag);
                        viewHolder.tagview.addView(tv);
                        viewHolder.tagview.setVisibility(VISIBLE);
                    }
                }

                if (Arg_Conv.getMsg_type().equals(MSGTYPE_SYS_DEBEND)){
                    Arg_Key keyarg = (Arg_Key) keyargmap.get(Arg_Conv.getKeyarguid());
                    Utils.displayalltags(viewHolder.allscorelo, keyarg.getKeyargalltags(),getApplicationContext());
                }

                viewHolder.viewerpanel.setVisibility(GONE);
                if (Arg_Conv.getStaticoncur() > 0){
                    viewHolder.viewerpanel.setVisibility(VISIBLE);
                    viewHolder.iconcur.setText(String.valueOf(Arg_Conv.getStaticoncur()));
                }
                if (Arg_Conv.getStaticoncurnot() > 0){
                    viewHolder.viewerpanel.setVisibility(VISIBLE);
                    viewHolder.iconcurnot.setText(String.valueOf(Arg_Conv.getStaticoncurnot()));
                }
            }
        };

        mFirestoreAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mFirestoreAdapter.getItemCount();
                int lastVisiblePosition =
                        mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    mMessageRecyclerView.scrollToPosition(positionStart);
                }
            }
        });

        mMessageRecyclerView.setAdapter(mFirestoreAdapter);

        mAddMessageImageView = (ImageView) findViewById(R.id.dv_addMessageImageView);
        mAddMessageImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mFirestoreAdapter.startListening();
        // Check if user is signed in.
        // TODO: Add code to check if user is signed in.
    }

    @Override
    public void onPause() {
        mFirestoreAdapter.stopListening();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mFirestoreAdapter.startListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    private void clearedittexts(){
        mMessageEditText.setText("");
        messageEditTextop.setText("");
        mSendButton.setVisibility(GONE);
        mSendButtonop.setVisibility(GONE);
    }

    private String getNamefortilt(String tilt){
        if (tilt.equals(TILT_FOR)) {
            return mydebate.getDebater_for_name();
        }else if (tilt.equals(TILT_AG)){
            return mydebate.getDebater_ag_name();
        }else if (tilt.equals(TILT_MOD)){
            return mydebate.getModerator_name();
        }
        return null;
    }

    String getactivedebname(){
        if (mydebate.getTurn().equals(TURN_FOR)){
            return mydebate.getDebater_for_name();
        }else{
            return mydebate.getDebater_ag_name();
        }
    }

    private void iaskguru(final String moderatorsid){
        mFireStoreRef.collection(FB_RCOLL_USERSBYSID).document(moderatorsid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                BUser mod = task.getResult().toObject(BUser.class);
                String uid = getakey();
                InviteMod invitee = new InviteMod(uid,mydebate.getUid(),mydebate.getTopic(),mydebate.getConvcontextarguid(), mydebate.getConvcontextargtext(),INVTYPE_ASKGURU,
                        myselfintent.getUid(),myselfintent.getSid(),myselfintent.getName(),myselfintent.getPiclink());

                mFireStoreRef.collection(FB_RCOLL_MYPARTICIPATION).document(mod.getUid()).collection(FB_COLL_MYINVITES).document(uid).set(invitee);
            }
        });
    }

    private void iaskformod(final String moderatorsid){
        mFireStoreRef.collection(FB_RCOLL_USERSBYSID).document(moderatorsid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                BUser mod = task.getResult().toObject(BUser.class);
                String uid = getakey();
                InviteMod invitee = new InviteMod(uid,mydebate.getUid(),mydebate.getTopic(),mydebate.getConvcontextarguid(), mydebate.getConvcontextargtext(),INVTYPE_MOD,
                        myselfintent.getUid(),myselfintent.getSid(),myselfintent.getName(),myselfintent.getPiclink());

                mFireStoreRef.collection(FB_RCOLL_MYPARTICIPATION).document(mod.getUid()).collection(FB_COLL_MYINVITES).document(uid).set(invitee);
            }
        });
    }

    private void deboptagoperate(String operation){
        mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                .collection(FB_COLL_CONVS).document(mydebate.getConvcontextarguid())
                .update("tagacceptstate",operation);

        mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                .update("convcontexttagstate",operation);
    }

    private void clearconv(){
        ArrayList tags = mydebate.getConvcontexttags();
        tags.clear();
        mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                .update("convcontextarguid",null
                        ,"convcontexttags",tags);

    }

    private void accepttag(){
        if (getmytilt().equals(TILT_FOR)){
            deboptagoperate(TAG_ACCEPTED);
            give_tagon_for(mydebate.getConvcontexttags());
            keyarg_addalltags(mydebate.getConvcontexttags());
            clearconv();
            newsysmessage("Tags Accepted.", MSGTYPE_SYS);
            addpointsto_for(1);
            if (mykeyArg.getKeyargstattot_for_points()>=MAX_FALLACIES){
                finish_conv();
                if (mydebate.getActivekeyargindex()>=MAX_KEYARGCOUNT){
                    newsysmessage("",MSGTYPE_SYS_DEBEND);
                    newsysmessage("The conversation ends here. That's a wrap folks.",MSGTYPE_SYS);
                    enddebate();
                }else{
                    String nextturn = getoppturn(mykeyArg.getTilt());
                    newsysmessage("",MSGTYPE_SYS_DEBEND);
                    newsysmessage(getNamefortilt(nextturn)+ ",  Take 2: Keep it factual, rational and on point.",MSGTYPE_SYS);
                    startnew_conv(nextturn);
                    setturn(nextturn);
                }
            }else{
                newsysmessage(getNamefortilt(TURN_FOR)+",  Please try a better version of the argument.",MSGTYPE_SYS);
                setturn(TURN_FOR);
            }
        }else{
            deboptagoperate(TAG_ACCEPTED);
            give_tagon_ag(mydebate.getConvcontexttags());
            keyarg_addalltags(mydebate.getConvcontexttags());
            clearconv();
            newsysmessage("Tags Accepted.",MSGTYPE_SYS);
            addpointsto_ag(1);
            if (mykeyArg.getKeyargstattot_ag_points()>=MAX_FALLACIES){
                finish_conv();
                if (mydebate.getActivekeyargindex()>=MAX_KEYARGCOUNT){
                    newsysmessage("",MSGTYPE_SYS_DEBEND);
                    newsysmessage("The conversation ends here. That's a wrap folks.",MSGTYPE_SYS);
                    enddebate();
                }else{
                    String nextturn = getoppturn(mykeyArg.getTilt());
                    newsysmessage("",MSGTYPE_SYS_DEBEND);
                    newsysmessage(getNamefortilt(nextturn)+ ",  Take 2: Keep it factual, rational and on point.",MSGTYPE_SYS);

                    startnew_conv(nextturn);
                    setturn(nextturn);
                }
            }else{
                newsysmessage(getNamefortilt(TURN_AG)+", Please try a better version of the argument.",MSGTYPE_SYS);
                setturn(TURN_AG);
            }
        }
    }

    private void rejecttag(){
        deboptagoperate(TAG_REJECTED);
        keyarg_addalltags(SYSTAG_DEADLOCK);
        clearconv();
        newsysmessage("Tags Rejected. We have a deadlock.",MSGTYPE_SYS);
        finish_conv();
        if (mydebate.getActivekeyargindex()>=MAX_KEYARGCOUNT){
            newsysmessage("",MSGTYPE_SYS_DEBEND);
            newsysmessage("The conversation ends here. That's a wrap folks.",MSGTYPE_SYS);
            enddebate();
        }else{
            String nextturn = getoppturn(mykeyArg.getTilt());
            newsysmessage("",MSGTYPE_SYS_DEBEND);
            newsysmessage(getNamefortilt(nextturn)+ ",  Take 2: Keep it factual, rational and on point.",MSGTYPE_SYS);
            startnew_conv(nextturn);
            setturn(nextturn);
        }
    }

    private void newsysmessage(String message, String msgtype){
        String uid = getakey();
        Date ts = new Date();
        boolean iskeyarg = false;

        Arg_Conv Arg_Conv = new
                Arg_Conv(uid,  myselfintent.getUid(),  mPhotoUrl,
                mUsername,  myselfintent.getSid(),  mydebateintent.getUid(),
                TILT_NONE,  message,  null, msgtype, ts);
        Arg_Conv.setconv(
                ARGTYPE_ASSRT,mydebate.getActivekeyargguid(),null,TAG_NONE,
                null,TAG_NONE,null,false);

        mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                .collection(FB_COLL_CONVS).document(uid).set(Arg_Conv);
    }

    private void newdebmessage(String message, Date ts){
        String uid = getakey();
        boolean iskeyarg = false;

        if (mydebate.getActivekeyargguid() == null){
            //manju
            String keyarguid = getakey();
            Date kts = new Date();
            String turn = mydebate.getTurn();
            String duid = null;
            String dpic = null;
            String dname = null;
            String dsid = null;

            if (turn.equals(TILT_FOR)){
                duid = mydebate.getDebater_for_uid();
                dpic = mydebate.getDebater_for_pic();
                dsid = mydebate.getDebater_for_sid();
                dname = mydebate.getDebater_for_name();
            }else{
                duid = mydebate.getDebater_ag_uid();
                dpic = mydebate.getDebater_ag_pic();
                dname = mydebate.getDebater_ag_name();
                dsid = mydebate.getDebater_ag_sid();
            }

            Arg_Key keyarg = new
                    Arg_Key(keyarguid,
                    duid,dpic,dname,dsid,mydebate.getUid(),
                    turn,ARGTEXT_DEFAULT,null,ARG_ROLE_DEB,kts);
            keyarg.setkeyparams(KEYARGAACTIV_ACTIVE,RESULTSTATE_WAITING);
            keyarg.setArgtext(message.toString());
            mydebate.setActivekeyargguid(keyarguid);
            mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebate.getUid()).collection(FB_COLL_KEYARGS).document(keyarguid).set(keyarg);
            mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebate.getUid())
                    .update("activekeyargguid",keyarguid
                            ,"activekeyargindex",mydebate.getActivekeyargindex()+1
                    ,"convcontextarguid",null
                    ,"convcontexttagstate",TAG_NONE);

            iskeyarg = true;
        }


//        if (mykeyArg.getArgtext().equals(ARGTEXT_DEFAULT)){
//            mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
//                    .collection(FB_COLL_KEYARGS).document(mydebate.getActivekeyargguid())
//                    .update("argtext", message);
//            iskeyarg = true;
//        }

        Arg_Conv Arg_Conv = new
                Arg_Conv(uid,  myselfintent.getUid(),  mPhotoUrl,
                mUsername,  myselfintent.getSid(),  mydebateintent.getUid(),
                getmytilt(),  message,  null, MSGTYPE_DEB, ts);
        Arg_Conv.setconv(
                ARGTYPE_ASSRT,mydebate.getActivekeyargguid(),null,TAG_NONE,
                null,TAG_NONE,null,iskeyarg);

        mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                .collection(FB_COLL_CONVS).document(uid).set(Arg_Conv);

        /*********/
        mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                .update("convcontextarguid", uid
                        , "turn", getoppturn(mydebate.getTurn())
                        , "convcontextargop", ARGOP_MESSAGE
                        , "convcontextargtilt", getmytilt()
                        ,"convcontextargtext", message
                        , "debstattotalargs", mydebate.getDebstattotalargs() + 1);

    }

    private void newsimpledebmessage(String message, Date ts){
        String uid = getakey();
        boolean iskeyarg = false;

        if (mykeyArg.getArgtext().equals(ARGTEXT_DEFAULT)){
            mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                    .collection(FB_COLL_KEYARGS).document(mydebate.getActivekeyargguid())
                    .update("argtext", message);
            iskeyarg = true;
        }

        Arg_Conv Arg_Conv = new
                Arg_Conv(uid,  myselfintent.getUid(),  mPhotoUrl,
                mUsername,  myselfintent.getSid(),  mydebateintent.getUid(),
                getmytilt(),  message,  null, MSGTYPE_DEB, ts);
        Arg_Conv.setconv(
                ARGTYPE_ASSRT,mydebate.getActivekeyargguid(),null,TAG_NONE,
                null,TAG_NONE,null,iskeyarg);

        mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                .collection(FB_COLL_CONVS).document(uid).set(Arg_Conv);

        //IF first arg
        if (mykeyArg.getArgtext().equals(ARGTEXT_DEFAULT)){
            mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                    .collection(FB_COLL_KEYARGS).document(mydebate.getActivekeyargguid())
                    .update("argtext", message);
        }
    }

    private void newdebtag(ArrayList<String> tags,String message){
        mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                .collection(FB_COLL_CONVS).document(mydebate.getConvcontextarguid())
                .update("tags", tags);


        mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                .update("convcontextargop",ARGOP_FALLACY
                        ,"convcontexttags",tags,
                        "convcontextargtext",message
                        ,"convcontexttagstate",TAG_PENDING);
    }



    private void enddebate(){
        mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                .update("flagactivestate",DEBACTIVSTATE_FINISHED);
    }

    private void keyarg_addalltags(ArrayList<String> intags){
        ArrayList<String> tags = mykeyArg.getKeyargalltags();
        tags.addAll(intags);
        mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                .collection(FB_COLL_KEYARGS).document(mydebate.getActivekeyargguid())
                .update("keyargalltags",tags);

        ArrayList<String> debtags = mydebate.getDeballtags();
        if (debtags == null){
            debtags = new ArrayList<>();
        }
        debtags.addAll(intags);
        mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                .update("deballtags",debtags);
    }

    private void keyarg_addalltags(String intag){
        ArrayList<String> tags = mykeyArg.getKeyargalltags();
        tags.add(intag);
        mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                .collection(FB_COLL_KEYARGS).document(mydebate.getActivekeyargguid())
                .update("keyargalltags",tags);
    }


    private void give_tagon_for(ArrayList<String> intags){
        ArrayList<String> tags = mykeyArg.getTagson_for();
        tags.addAll(intags);
        mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                .collection(FB_COLL_KEYARGS).document(mydebate.getActivekeyargguid())
                .update("tagson_for",tags);
    }

    private void give_tagon_ag(ArrayList<String> intags){
        ArrayList<String> tags = mykeyArg.getTagson_ag();
        tags.addAll(intags);
        mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                .collection(FB_COLL_KEYARGS).document(mydebate.getActivekeyargguid())
                .update("tagson_ag",tags);
    }

    private void addpointsto_for(int points){
        mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                .collection(FB_COLL_KEYARGS).document(mydebate.getActivekeyargguid())
                .update("keyargstattot_for_points",mykeyArg.getKeyargstattot_for_points()+points);
    }

    private void addpointsto_ag(int points){
        mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                .collection(FB_COLL_KEYARGS).document(mydebate.getActivekeyargguid())
                .update("keyargstattot_ag_points",mykeyArg.getKeyargstattot_ag_points()+points);
    }

    private void addpointsto_deb(int points){
        mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                .update("debstattotalpoints",mydebate.getDebstattotalpoints()+points);
    }

    private void finish_conv(){
        mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                .collection(FB_COLL_KEYARGS).document(mydebate.getActivekeyargguid())
                .update("keyargflagactivestate", KEYARGAACTIV_FINISHED);
    }

    private void startnew_conv(String nextturn){
        mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                .update("activekeyargguid",null
                        ,"convcontextarguid",null
                        ,"convcontexttagstate",TAG_NONE);
    }

    private void setturn(String turn){
        mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                .update("turn",turn);
    }

    private String getmytilt(){
        if (myselfintent.getUid().equals(mydebate.getDebater_for_uid())){
            return TILT_FOR;
        }else if (myselfintent.getUid().equals(mydebate.getDebater_ag_uid())){
            return TILT_AG;
        }else{
            return TILT_NONE;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    final Uri uri = data.getData();
                    Log.d(TAG, "Uri: " + uri.toString());

                    Date ts = new Date();
                    Arg_Conv Arg_Conv = new
                            Arg_Conv(getakey(),  myselfintent.getUid(),  mPhotoUrl,
                            mUsername,  myselfintent.getSid(),  mydebateintent.getUid(),
                            TILT_FOR,  mMessageEditText.getText().toString(),  LOADING_IMAGE_URL, ARG_ROLE_DEB, ts);

                    mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                            .collection(FB_COLL_CONVS).add(Arg_Conv).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
//                            String key = databaseReference.getKey();
                            String key = task.getResult().getId();
                            StorageReference storageReference =
                                    FirebaseStorage.getInstance()
                                            .getReference(mFirebaseUser.getUid())
                                            .child(key)
                                            .child(uri.getLastPathSegment());

                            putImageInStorage(storageReference, uri, key);

                        }
                    });
                }
            }
        }
    }

    private void putImageInStorage(StorageReference storageReference, Uri uri, final String key) {
        storageReference.putFile(uri).addOnCompleteListener(DebViewPlay.this,
                new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            task.getResult().getMetadata().getReference().getDownloadUrl()
                                    .addOnCompleteListener(DebViewPlay.this,
                                            new OnCompleteListener<Uri>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Uri> task) {
                                                    if (task.isSuccessful()) {
                                                        Date ts = new Date();

                                                        Arg_Conv Arg_Conv = new
                                                                Arg_Conv(getakey(),  myselfintent.getUid(),  mPhotoUrl,
                                                                mUsername,  myselfintent.getSid(),  mydebateintent.getUid(),
                                                                TILT_FOR,  mMessageEditText.getText().toString(),
                                                                task.getResult().toString(),ARG_ROLE_DEB, ts);

                                                        mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                                                                .collection(FB_COLL_CONVS).document(key).set(Arg_Conv);
                                                    }
                                                }
                                            });
                        } else {
                            Log.w(TAG, "Image upload task was not successful.",
                                    task.getException());
                        }
                    }
                });
    }

}
