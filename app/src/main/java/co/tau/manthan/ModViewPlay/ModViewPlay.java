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
package co.tau.manthan.ModViewPlay;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
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
import static co.tau.manthan.baseclasses.Arg_Key.KEYARGAACTIV_FINISHED;
import static co.tau.manthan.baseclasses.Constants.*;
import static co.tau.manthan.baseclasses.Debate.PARTISTATUS_ACCEPTED;
import static co.tau.manthan.baseclasses.InviteMod.INVITESTATE_ALIVE;
import static co.tau.manthan.baseclasses.InviteMod.INVITESTATE_EXPIRED;
import static co.tau.manthan.baseclasses.InviteMod.INVTYPE_ASKGURU;
import static co.tau.manthan.baseclasses.InviteMod.INVTYPE_MOD;
import static co.tau.manthan.baseclasses.Utils.getakey;
import static co.tau.manthan.baseclasses.Utils.getoppturn;

public class ModViewPlay extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener {

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        ImageView messageImageView;
//        AppCompatTextView mseltagView;
        LinearLayout tagview;
        ChipGroup allscorelo;
        ConstraintLayout hightlightlo;
        ConstraintLayout msg_outerlo;
        LinearLayout viewerpanel;
        TextView iconcur;
        TextView iconcurnot;



        public MessageViewHolder(View v) {
            super(v);
            messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
            messageImageView = (ImageView) itemView.findViewById(R.id.messageImageView);
//            messengerImageView = (CircleImageView) itemView.findViewById(R.id.messengerImageView);
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

    public static class InviteViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout item_invite_outerlo;
        AppCompatButton item_invite_acceptinvite;
        TextView item_invite_txtlbl1;

        EditText item_invite_msgedit;
        ImageButton item_invite_sendbtn;

        public InviteViewHolder(View v) {
            super(v);
            item_invite_outerlo = (ConstraintLayout) itemView.findViewById(R.id.item_invite_outerlo);
            item_invite_acceptinvite = (AppCompatButton) itemView.findViewById(R.id.item_invite_acceptinvite);
            item_invite_txtlbl1 = (TextView)itemView.findViewById(R.id.item_invite_txtlbl1);
            item_invite_msgedit = (EditText)itemView.findViewById(R.id.item_invite_msgedit);
            item_invite_sendbtn = (ImageButton)itemView.findViewById(R.id.item_invite_sendbtn);
        }
    }

    private static final String TAG = "ModViewPlay";


    private String mUsername;
    private String mPhotoUrl;
    private SharedPreferences mSharedPreferences;
    private GoogleApiClient mGoogleApiClient;

    private ImageButton mSendButton;
    private RecyclerView mMessageRecyclerView;
    private RecyclerView mFireStoreInviteRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private EditText mMessageEditText;
//    private TextView mMessageReasonText;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private Debate mydebateintent = new Debate();
    private Debate mydebate = new Debate();
    private Debater myselfintent = new Debater();
    private Arg_Key mykeyArg = new Arg_Key();
    private ArrayList<Arg_Key> keyargs = new ArrayList<>();
    private Map keyargmap = new HashMap<String, Arg_Key>();
    List<Siditem> sidlist = new ArrayList<>();


//    ListenerRegistration kerargreg;
    private ModViewViewModel modviewmodel;
    String myactivekeyarg = null;

    CircleImageView forImageView;
    CircleImageView agImageView;
    CircleImageView modImageView;


    private FirebaseFirestore mFireStoreRef;
    private FirestoreRecyclerAdapter mFirestoreAdapter;
//    private FirestoreRecyclerAdapter mFireStoreInviteAdapter;

    private RecyclerView.Adapter inviteadapter;
    ArrayList<InviteMod> invitelist = new ArrayList<>();

    private Activity activitycontext;
    private EditText meditop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modview_play);

        activitycontext = this;
        mMessageEditText = (EditText) findViewById(R.id.mvmessageEditText);
//        mMessageReasonText = (TextView) findViewById(R.id.mvreasonTextView);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Gson gson = new Gson();
        mydebateintent = gson.fromJson(getIntent().getStringExtra("mydebate"), Debate.class);
        myselfintent = gson.fromJson(getIntent().getStringExtra("myself"), Debater.class);
        myactivekeyarg = mydebateintent.getActivekeyargguid();


        forImageView = (CircleImageView)findViewById(R.id.mv_leftImageView);
        modImageView = (CircleImageView)findViewById(R.id.mv_centerImageView);
        agImageView = (CircleImageView)findViewById(R.id.mv_rightImageView);

        Glide.with(getApplicationContext())
                .load(mydebateintent.getDebater_for_pic())
                .into(forImageView);
        ((TextView)findViewById(R.id.mv_leftname)).setText(mydebateintent.getDebater_for_name());

        Glide.with(getApplicationContext())
                .load(mydebateintent.getDebater_ag_pic())
                .into(agImageView);
        ((TextView)findViewById(R.id.mv_rightname)).setText(mydebateintent.getDebater_ag_name());

        meditop = findViewById(R.id.mvmessageEditTextop);
        Autocomplete<Hashtag> htagacomplete = Tags.setupHashtagAutocomplete(meditop,
                '#',activitycontext,Hashtag.HashtagModop);


        // Set default username is anonymous.
        mUsername = ANONYMOUS;
        modviewmodel = ViewModelProviders.of(this).get(ModViewViewModel.class);
        modviewmodel.getSendmode().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals(ARGOP_MESSAGE)){
                    mMessageEditText.setVisibility(View.VISIBLE);
//                    mMessageReasonText.setVisibility(View.INVISIBLE);
                }
            }
        });

        // Initialize Firebase Auth
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
        mMessageRecyclerView = (RecyclerView) findViewById(R.id.modviewplay_messageRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);

        mFireStoreInviteRecyclerView = (RecyclerView) findViewById(R.id.mv_inviterv);
        LinearLayoutManager mLinearLayoutManagerinvite = new LinearLayoutManager(this);
        mLinearLayoutManagerinvite.setStackFromEnd(true);
        mFireStoreInviteRecyclerView.setLayoutManager(mLinearLayoutManagerinvite);



        mFireStoreRef = FirebaseFirestore.getInstance();


//        mFireStoreRef.collection(FB_COLL_MYINVITES)
//                .whereEqualTo("debateuid",mydebateintent.getUid()).get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//            }
//        });

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

                LinearLayout leftlo = (LinearLayout) findViewById(R.id.mv_leftdebpt);
                LinearLayout rightlo = (LinearLayout) findViewById(R.id.mv_rightpt);
                Utils.scoreforkeyarg(leftlo,rightlo,mykeyArg,getApplicationContext());
            }
        });

        findViewById(R.id.mvsendButtonop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = ((EditText)(findViewById(R.id.mvmessageEditTextop))).getText().toString();
                ArrayList<String> hashtags = TextUtils.getHashTags(msg);
                if (hashtags.size() > 0) {
                    String tag = hashtags.get(0);
                    if (tag.equals(TAGOP_MODIACCEPTASMOD)) {
                        iacceptasmod(msg);
//                        invitesetstate();

                    } else if (tag.equals(TAGOP_MODIREJECTASMOD)) {
                        irejectasmod(msg);
//                        invitesetstate();

                    } else {
                        newsysmessage(myselfintent.getSid() + ", You need to Accept or Reject the fallacy");
                    }
                } else {
                    newsysmessage(myselfintent.getSid() + ", You need to Accept or Reject the fallacy");
                }
                ((EditText)(findViewById(R.id.mvmessageEditTextop))).setText("");
            }
        });

        mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
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

                if (mydebate.getModeratoruid() != null){
                    Glide.with(getApplicationContext())
                            .load(mydebate.getModerator_pic())
                            .into(modImageView);
                    ((TextView)findViewById(R.id.mv_centername)).setText(mydebate.getModerator_name());
                    ((TextView)findViewById(R.id.mv_centername)).setVisibility(View.VISIBLE);
                    modImageView.setVisibility(View.VISIBLE);
                }else{
                    ((TextView)findViewById(R.id.mv_centername)).setVisibility(GONE);
                    modImageView.setVisibility(GONE);
                }

                ((TextView)findViewById(R.id.mv_debtitle)).setText(mydebate.getTopic());

                if (mydebate.getFlagactivestate().equals(DEBACTIVSTATE_WAITINGJOIN)){

                }else if (mydebate.getFlagactivestate().equals(DEBACTIVSTATE_ONGOING)) {
                    findViewById(R.id.mv_toplo3).setVisibility(GONE);
                    if (myselfintent.getUid().equals(mydebate.getModeratoruid())){
                        if (mydebate.getTurn().equals(TURN_MOD)){
                            findViewById(R.id.mv_sendmessagelayoutop).setVisibility(View.VISIBLE);
                            findViewById(R.id.mv_sendmessagelayout).setVisibility(View.GONE);
                        }else{
                            findViewById(R.id.mv_sendmessagelayoutop).setVisibility(GONE);
                            findViewById(R.id.mv_sendmessagelayout).setVisibility(VISIBLE);
                        }
                    }else{
                        findViewById(R.id.mv_sendmessagelayoutop).setVisibility(GONE);
                        findViewById(R.id.mv_sendmessagelayout).setVisibility(GONE);
                    }

//                    if (mydebate.getTurn().equals(TURN_MOD)){
//
//                        mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
//                                .collection(FB_COLL_CONVS).document(mydebate.getConvcontextarguid())
//                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                Arg_Conv conv = task.getResult().toObject(Arg_Conv.class);
//                                ((TextView)findViewById(R.id.modaction_messageTextView)).setText(conv.getArgtext());
//                                ((TextView)findViewById(R.id.modact_convmsg_seltag)).setText(conv.getSeltag());
//                                ((TextView)findViewById(R.id.modact_mvreasonTextView)).setText(conv.getTagreason());
//                            }
//                        });
//
//                        findViewById(R.id.mv_sendmessagelayout).setVisibility(View.GONE);
//                    }else{
//                        findViewById(R.id.mv_sendmessagelayout).setVisibility(View.VISIBLE);
//
//                        ((TextView)findViewById(R.id.modaction_messageTextView)).setText("");
//                        ((TextView)findViewById(R.id.modact_convmsg_seltag)).setText("");
//                        ((TextView)findViewById(R.id.modact_mvreasonTextView)).setText("");
//                    }

                    String source = snapshot != null && snapshot.getMetadata().hasPendingWrites()
                            ? "Local" : "Server";

                    if (snapshot != null && snapshot.exists()) {
                        Log.d(TAG, source + " data: " + snapshot.getData());
                    } else {
                        Log.d(TAG, source + " data: null");
                    }
                }else if (mydebate.getFlagactivestate().equals(DEBACTIVSTATE_FINISHED)) {
                    findViewById(R.id.mv_toplo2).setVisibility(GONE);
                    findViewById(R.id.mv_toplo3).setVisibility(View.VISIBLE);
                    findViewById(R.id.mv_sendmessagelayout).setVisibility(View.GONE);
                    findViewById(R.id.mv_sendmessagelayoutop).setVisibility(GONE);

//                    ((TextView)findViewById(R.id.mv_finishconvscore)).setText(String.valueOf(mydebate.getDebstattotalpoints()));
                }
            }
        });



//        SnapshotParser<Arg_Conv> parser = new SnapshotParser<Arg_Conv>() {
//            @Override
//            public Arg_Conv parseSnapshot(DataSnapshot dataSnapshot) {
//                Arg_Conv Arg_Conv = dataSnapshot.getValue(Arg_Conv.class);
//                if (Arg_Conv != null) {
//                    Arg_Conv.setUid(dataSnapshot.getKey());
//                }
//                return Arg_Conv;
//            }
//        };


        Query query = mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                .collection(FB_COLL_CONVS).orderBy("ts");
        FirestoreRecyclerOptions<Arg_Conv> mstoreoptions =
                new FirestoreRecyclerOptions.Builder<Arg_Conv>()
                        .setQuery(query, Arg_Conv.class)
                        .build();


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
                }
                else if (viewType == VIEW_SYSMSG_DEBEND) {
                    return new MessageViewHolder(inflater.inflate(R.layout.itemdebview_sysmessage_debend, parent, false));
                }
                else{
                    return new MessageViewHolder(inflater.inflate(R.layout.itemdebview_oppomessage, parent, false));
                }
            }

            @Override
            public int getItemViewType(int position) {
//                if ()
                Arg_Conv fm = getItem(position);

                if (fm.getMsg_type().equals(MSGTYPE_DEB)) {
                    if (fm.getTilt().equals(TILT_FOR)) {
                        return VIEW_LEFTMSG;
                    } else {
                        return VIEW_RIGHTMSG;
                    }
                }else if(fm.getMsg_type().equals(MSGTYPE_MOD)){
                    return VIEW_MODERATORMSG;
                }else if(fm.getMsg_type().equals(MSGTYPE_SYS)){
                    return VIEW_SYSMSG;
                }else if(fm.getMsg_type().equals(MSGTYPE_SYS_DEBEND)){
                    return VIEW_SYSMSG_DEBEND;
                }else{
                    return VIEW_NOMSG;
                }
//                return super.getItemViewType(position);
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected void onBindViewHolder(@NonNull final MessageViewHolder viewHolder, int position, @NonNull final Arg_Conv Arg_Conv) {

                viewHolder.messageImageView.setVisibility(ImageView.GONE);
                viewHolder.messageTextView.setVisibility(TextView.GONE);
                viewHolder.tagview.setVisibility(AppCompatTextView.GONE);

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
                    viewHolder.messageTextView.setText(Arg_Conv.getArgtext());
                    viewHolder.messageTextView.setVisibility(TextView.VISIBLE);
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
                    viewHolder.messageImageView.setVisibility(ImageView.VISIBLE);
                }
//                if (Arg_Conv.getSeltag()!=null){
//                    viewHolder.mseltagView.setText(Arg_Conv.getSeltag());
//                    viewHolder.reasonTextView.setText(Arg_Conv.getTagreason());
//                    viewHolder.mseltagView.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(),R.color.accent_light));
//
//                    if (Arg_Conv.getSeltag_acceptstate().equals(TAG_REJECTED)){
//                        //STRIKETHROUGH
//                        viewHolder.mseltagView.setPaintFlags(viewHolder.mseltagView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                    }else if (Arg_Conv.getSeltag_acceptstate().equals(TAG_PENDING)){
//                    }
//                    viewHolder.tagview.setVisibility(AppCompatTextView.VISIBLE);
//                }
//                if (Arg_Conv.getClinchtag()!=null){
//                    viewHolder.mseltagView.setText(Arg_Conv.getClinchtag());
//                    viewHolder.reasonTextView.setText(Arg_Conv.getTagreason());
//                    viewHolder.mseltagView.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(),R.color.accent));
//                    if (Arg_Conv.getClinchtag_accepstate().equals(TAG_REJECTED)){
//                        //STRIKETHROUGH
//                        viewHolder.mseltagView.setPaintFlags(viewHolder.mseltagView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                    }else if (Arg_Conv.getSeltag_acceptstate().equals(TAG_PENDING)){
//                    }
//                    viewHolder.tagview.setVisibility(AppCompatTextView.VISIBLE);
//                }

//                if (Arg_Conv.getDebaterpic() == null) {
//                    viewHolder.messengerImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
//                            R.drawable.ic_account_circle_black_36dp));
//                } else {
//                    Glide.with(getApplicationContext())
//                            .load(Arg_Conv.getDebaterpic())
//                            .into(viewHolder.messengerImageView);
//                }
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


        mFireStoreRef.collection(FB_RCOLL_MYPARTICIPATION).document(myselfintent.getUid())
            .collection(FB_COLL_MYINVITES)
            .addSnapshotListener(
                    new EventListener<QuerySnapshot>() {
                         @Override
                         public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                             if (queryDocumentSnapshots != null){
                                 invitelist.clear();
                                 for (DocumentSnapshot dc : queryDocumentSnapshots.getDocuments()){
                                     InviteMod ds = dc.toObject(InviteMod.class);
                                     if (ds.getState().equals(INVITESTATE_ALIVE) && (ds.getDebateuid().equals(mydebate.getUid()))){
                                         invitelist.add(ds);
                                     }
                                 }
                                 inviteadapter.notifyDataSetChanged();
                             }
                         }
                    });



        Query invitequerynew = mFireStoreRef.collection(FB_RCOLL_MYPARTICIPATION).document(myselfintent.getUid())
                .collection(FB_COLL_MYINVITES)
                .whereEqualTo("debateuid", mydebateintent.getUid())
                .whereEqualTo("state", INVITESTATE_ALIVE)
                .orderBy("ts");


        FirestoreRecyclerOptions<InviteMod> invitesoptions =
                new FirestoreRecyclerOptions.Builder<InviteMod>()
                        .setQuery(invitequerynew, InviteMod.class)
                        .build();

        inviteadapter = new RecyclerView.Adapter(){

            @NonNull
            @Override
            public InviteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                if (viewType == 1) {
                    return new InviteViewHolder(inflater.inflate(R.layout.item_invitemodasguru, parent, false));
                } else if (viewType == 2) {
                    return new InviteViewHolder(inflater.inflate(R.layout.item_invitemodaccept, parent, false));
                }
                else{
                    return null;
                }
            }


            @Override
            public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder vh, final int position) {
                final InviteMod invitemod = invitelist.get(position);
                final InviteViewHolder viewHolder = (InviteViewHolder)vh;
                if (invitemod.getInvitetype().equals(INVTYPE_ASKGURU)){
                    if (invitemod.getState().equals(INVITESTATE_EXPIRED)){
                        viewHolder.item_invite_sendbtn.setVisibility(GONE);
                        viewHolder.item_invite_msgedit.setVisibility(GONE);
                        viewHolder.item_invite_txtlbl1.setText("This invitation expired");
//                        viewHolder.item_invite_outerlo.setVisibility(GONE);

                    }else{
                        Autocomplete<Hashtag> htagacomplete = Tags.setupHashtagAutocomplete(viewHolder.item_invite_msgedit,
                                '#',activitycontext,Hashtag.HashtagGuruop);
                        Autocomplete<Siditem> sidtagacomplete = Tags.setupatlistAutocomplete(viewHolder.item_invite_msgedit,
                                '@', activitycontext,sidlist);

                        viewHolder.item_invite_sendbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String msg = viewHolder.item_invite_msgedit.getText().toString();
                                ArrayList<String> hashtags = TextUtils.getHashTags(msg);
                                if (hashtags.size() > 0) {
                                    String tag = hashtags.get(0);
                                    if (tag.equals(TAGOP_MODIACCEPTASGURU)) {
                                        iacceptasguru(msg);
                                        invitesetstate();

                                    } else if (tag.equals(TAGOP_MODIREJECTASGURU)) {
                                        irejectasguru(msg);
                                        invitesetstate();

                                    } else {
                                        newsysmessage(myselfintent.getSid() + ", You need to Accept or Reject the fallacy");
                                    }
                                } else {
                                    newsysmessage(myselfintent.getSid() + ", You need to Accept or Reject the fallacy");
                                }
                                viewHolder.item_invite_msgedit.setText("");
                            }
                        });
                    }
                }else if (invitemod.getInvitetype().equals(INVTYPE_MOD)){
                    viewHolder.item_invite_acceptinvite.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            addmyselfasmod();
                        }
                    });

                }else{

                }

            }

            @Override
            public int getItemCount() {
//                return 0;
                return invitelist.size();
            }

            @Override
            public int getItemViewType(int position) {
//                return super.getItemViewType(position);
                InviteMod fm = invitelist.get(position);

                if (fm.getInvitetype().equals(INVTYPE_ASKGURU)) {
                    return 1;
                }else if(fm.getInvitetype().equals(INVTYPE_MOD)){
                    return 2;
                }
                return 0;
            }
        };



//        mFireStoreInviteAdapter = new FirestoreRecyclerAdapter<InviteMod, InviteViewHolder>(invitesoptions) {
//
//            @NonNull
//            @Override
//            public InviteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//                if (viewType == 1) {
//                    return new InviteViewHolder(inflater.inflate(R.layout.item_invitemodasguru, parent, false));
//                } else if (viewType == 2) {
//                    return new InviteViewHolder(inflater.inflate(R.layout.item_invitemodaccept, parent, false));
//                }
//                else{
//                    return null;
//                }
//            }
//
//            @Override
//            public int getItemCount() {
//                int i = super.getItemCount();
//                return i;
//            }
//
//            @Override
//            public int getItemViewType(int position) {
////                if ()
//                InviteMod fm = getItem(position);
//
//                if (fm.getInvitetype().equals(INVTYPE_ASKGURU)) {
//                        return 1;
//                }else if(fm.getInvitetype().equals(INVTYPE_MOD)){
//                    return 2;
//                }
//                return 0;
////                return super.getItemViewType(position);
//            }
//
//            @RequiresApi(api = Build.VERSION_CODES.M)
//            @Override
//            protected void onBindViewHolder(@NonNull final InviteViewHolder viewHolder, final int position, @NonNull InviteMod invitemod) {
//
//                if (invitemod.getInvitetype().equals(INVTYPE_ASKGURU)){
//                    if (invitemod.getState().equals(INVITESTATE_EXPIRED)){
//                        viewHolder.item_invite_sendbtn.setVisibility(GONE);
//                        viewHolder.item_invite_msgedit.setVisibility(GONE);
//                        viewHolder.item_invite_txtlbl1.setText("This invitation expired");
////                        viewHolder.item_invite_outerlo.setVisibility(GONE);
//
//                    }else{
//                        Autocomplete<Hashtag> htagacomplete = Tags.setupHashtagAutocomplete(viewHolder.item_invite_msgedit,
//                                '#',activitycontext,Hashtag.HashtagGuruop);
//                        Autocomplete<Siditem> sidtagacomplete = Tags.setupatlistAutocomplete(viewHolder.item_invite_msgedit,
//                                '@', activitycontext,sidlist);
//
//                        viewHolder.item_invite_sendbtn.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                String msg = viewHolder.item_invite_msgedit.getText().toString();
//                                ArrayList<String> hashtags = TextUtils.getHashTags(msg);
//                                if (hashtags.size() > 0) {
//                                    String tag = hashtags.get(0);
//                                    if (tag.equals(TAGOP_MODIACCEPTASGURU)) {
//                                        iacceptasguru(msg);
//                                        invitesetstate((InviteMod)getItem(position));
//                                        mFireStoreInviteAdapter.notifyDataSetChanged();
//
//                                    } else if (tag.equals(TAGOP_MODIREJECTASGURU)) {
//                                        irejectasguru(msg);
//                                        invitesetstate((InviteMod)getItem(position));
//                                        mFireStoreInviteAdapter.notifyDataSetChanged();
//
//                                    } else {
//                                        newsysmessage(myselfintent.getSid() + ", You need to Accept or Reject the fallacy");
//                                    }
//                                } else {
//                                    newsysmessage(myselfintent.getSid() + ", You need to Accept or Reject the fallacy");
//                                }
//                            }
//                        });
//                    }
//                }else if (invitemod.getInvitetype().equals(INVTYPE_MOD)){
//                    viewHolder.item_invite_acceptinvite.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            addmyselfasmod();
//                        }
//                    });
//
//                }else{
//
//                }
//            }
//        };
        mFireStoreRef.collection(FB_RCOLL_SIDLIST).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                QuerySnapshot qs = task.getResult();
                for (DocumentSnapshot dc : qs.getDocuments()){
                    Siditem ds = dc.toObject(Siditem.class);
                    ds.setSid(ds.getSid().substring(1,ds.getSid().length()));
                    sidlist.add(ds);
                }
            }
        });


//        mFireStoreInviteRecyclerView.setAdapter(mFireStoreInviteAdapter);
        mFireStoreInviteRecyclerView.setAdapter(inviteadapter);
//        mFireStoreInviteAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
//            @Override
//            public void onItemRangeInserted(int positionStart, int itemCount) {
//                super.onItemRangeInserted(positionStart, itemCount);
//                int friendlyMessageCount = mFireStoreInviteAdapter.getItemCount();
//            }
//
//        });





        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mSendButton = (ImageButton) findViewById(R.id.mvsendButton);

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date ts = new Date();

                if (modviewmodel.getSM() == ARGOP_MESSAGE){
                    String message = mMessageEditText.getText().toString();
                    sendmodmessage(message);
                }
                mMessageEditText.setText("");
//                mMessageReasonText.setText("");
                modviewmodel.setSendmode(ARGOP_MESSAGE);
            }
        });
        ((RecyclerView)findViewById(R.id.modviewplay_messageRecyclerView)).addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                View v = recyclerView.getChildAt(0);
                int positionOfChild = recyclerView.getChildAdapterPosition(v);
                Arg_Key argkey;
                if (positionOfChild >= 0){
                    Arg_Conv argconv = (Arg_Conv) mFirestoreAdapter.getItem(positionOfChild);
                    argkey = (Arg_Key) keyargmap.get(argconv.getKeyarguid());
                    if (argkey != null){
                        ((TextView)findViewById(R.id.mv_keyargtopic)).setText(argkey.getArgtext());
                    }

                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
//        mFireStoreInviteAdapter.startListening();
    }

    private  void addmyselfasmod(){

        for (int i=0;i< inviteadapter.getItemCount();i++){
            InviteMod inviteMod = invitelist.get(i);
            if (inviteMod.getInvitetype().equals(INVTYPE_MOD)){
                mFireStoreRef.collection(FB_RCOLL_MYPARTICIPATION).document(myselfintent.getUid())
                        .collection(FB_COLL_MYINVITES).document(inviteMod.getUid()).update("state",INVITESTATE_EXPIRED);
            }
        }

        ArrayList<String> participants = mydebate.getParticipants();
        participants.add(myselfintent.getUid());

        mydebate.setParticipants(participants);
        mydebate.setModeratoruid(myselfintent.getUid());
        mydebate.setModerator_name(myselfintent.getName());
        mydebate.setModeratorsid(myselfintent.getSid());
        mydebate.setModerator_pic(myselfintent.getPiclink());
        mydebate.setModeratorstatus(PARTISTATUS_ACCEPTED);
        mydebate.setTurn(TURN_MOD);//manju

        mFireStoreRef.collection(FB_RCOLL_MYPARTICIPATION).document(mydebateintent.getUid())
                .collection(FB_COLL_MYDEBATES).document(mydebate.getUid())
                .set(mydebate);
        if (mydebate.getConvcontexttagstate().equals(TAG_PENDING)){
            //Change in TURN
            mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                    .update("participants",participants,
                            "moderatoruid",myselfintent.getUid(),
                            "moderator_name",myselfintent.getName(),
                            "moderatorsid",myselfintent.getSid(),
                            "moderator_pic",myselfintent.getPiclink(),
                            "moderatorstatus",PARTISTATUS_ACCEPTED,
                            "turn", TURN_MOD);
        }else{
            //No change in TURN
            mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                    .update("participants",participants,
                            "moderatoruid",myselfintent.getUid(),
                            "moderator_name",myselfintent.getName(),
                            "moderatorsid",myselfintent.getSid(),
                            "moderator_pic",myselfintent.getPiclink(),
                            "moderatorstatus",PARTISTATUS_ACCEPTED);
        }
    }


    private void invitesetstate(){
//        for (int i=0;i< mFireStoreInviteAdapter.getItemCount();i++){
//            InviteMod inviteMod = (InviteMod)mFireStoreInviteAdapter.getItem(i);
//            if (inviteMod.getInvitetype().equals(INVTYPE_ASKGURU)){
//                mFireStoreRef.collection(FB_RCOLL_MYPARTICIPATION).document(myselfintent.getUid())
//                        .collection(FB_COLL_MYINVITES).document(inviteMod.getUid()).update("state",INVITESTATE_EXPIRED);
//            }
//        }

        for (int i=0;i< inviteadapter.getItemCount();i++){
            InviteMod inviteMod = invitelist.get(i);
            if (inviteMod.getInvitetype().equals(INVTYPE_ASKGURU)){
                mFireStoreRef.collection(FB_RCOLL_MYPARTICIPATION).document(myselfintent.getUid())
                        .collection(FB_COLL_MYINVITES).document(inviteMod.getUid()).update("state",INVITESTATE_EXPIRED);
            }
        }

    }
    private void iacceptasguru(String msg){
        if (mydebate.getConvcontextarguid() != null){
            if (mydebate.getConvcontextargtilt().equals(TILT_FOR)) {
//                give_tagon_for(TAG_FALLACY);
                modoptagoperate(TAG_ACCEPTED);
                keyarg_addalltags(mydebate.getConvcontexttags());
                sendmodmessage("Guru " + myselfintent.getSid()+ "has accepted the fallacy");
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
                    newsysmessage(getNamefortilt(TURN_FOR)+",  That didn't add to the quality of the conversation. Please rephrase.",MSGTYPE_SYS);
                    setturn(TURN_FOR);
                }
            }else{
                modoptagoperate(TAG_ACCEPTED);
                keyarg_addalltags(mydebate.getConvcontexttags());
                sendmodmessage("Guru " + myselfintent.getSid()+ "has accepted the fallacy");
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
                    newsysmessage(getNamefortilt(TURN_AG)+",  That didn't add to the quality of the conversation. Please rephrase.",MSGTYPE_SYS);
                    setturn(TURN_AG);
                }
            }
        }else{
            invitesetstate();
        }
    }

    private void irejectasguru(String msg){
        if (mydebate.getConvcontextarguid() != null){
            if (mydebate.getConvcontextargtilt().equals(TILT_FOR)) {
//                give_tagon_for(TAG_FALLACY);
                modoptagoperate(TAG_REJECTED);
                keyarg_addalltags(mydebate.getConvcontexttags());
                sendmodmessage("Guru " + myselfintent.getSid()+ "has rejected the fallacy");
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
                    newsysmessage(getNamefortilt(TURN_AG)+",  That didn't add to the quality of the conversation. Please rephrase.",MSGTYPE_SYS);
                    setturn(TURN_AG);
                }
            }else{
                modoptagoperate(TAG_REJECTED);
                keyarg_addalltags(mydebate.getConvcontexttags());
                sendmodmessage("Guru " + myselfintent.getSid()+ "has accepted the fallacy");
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
                    newsysmessage(getNamefortilt(TURN_FOR)+",  That didn't add to the quality of the conversation. Please rephrase.",MSGTYPE_SYS);
                    setturn(TURN_FOR);
                }

            }
        }else {
            invitesetstate();
        }
    }

    private void iacceptasmod(String msg){
        if (mydebate.getConvcontextarguid() != null){
            if (mydebate.getConvcontextargtilt().equals(TILT_FOR)) {
//                give_tagon_for(TAG_FALLACY);
                modoptagoperate(TAG_ACCEPTED);
                keyarg_addalltags(mydebate.getConvcontexttags());
                sendmodmessage("Moderator " + myselfintent.getSid()+ "has accepted the fallacy");
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
                    newsysmessage(getNamefortilt(TURN_FOR)+",  That didn't add to the quality of the conversation. Please rephrase.",MSGTYPE_SYS);
                    setturn(TURN_FOR);
                }
            }else{
                modoptagoperate(TAG_ACCEPTED);
                keyarg_addalltags(mydebate.getConvcontexttags());
                sendmodmessage("Moderator " + myselfintent.getSid()+ "has accepted the fallacy");
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
                    newsysmessage(getNamefortilt(TURN_AG)+",  That didn't add to the quality of the conversation. Please rephrase.",MSGTYPE_SYS);
                    setturn(TURN_AG);
                }
            }
        }else{
            invitesetstate();
        }
    }

    private void irejectasmod(String msg){
        if (mydebate.getConvcontextarguid() != null){
            if (mydebate.getConvcontextargtilt().equals(TILT_FOR)) {
//                give_tagon_for(TAG_FALLACY);
                modoptagoperate(TAG_REJECTED);
                keyarg_addalltags(mydebate.getConvcontexttags());
                sendmodmessage("Moderator " + myselfintent.getSid()+ "has rejected the fallacy");
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
                    newsysmessage(getNamefortilt(TURN_AG)+",  That didn't add to the quality of the conversation. Please rephrase.",MSGTYPE_SYS);
                    setturn(TURN_AG);
                }
            }else{
                modoptagoperate(TAG_REJECTED);
                keyarg_addalltags(mydebate.getConvcontexttags());
                sendmodmessage("Moderator " + myselfintent.getSid()+ "has accepted the fallacy");
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
                    newsysmessage(getNamefortilt(TURN_FOR)+",  That didn't add to the quality of the conversation. Please rephrase.",MSGTYPE_SYS);
                    setturn(TURN_FOR);
                }
            }
        }else {
            invitesetstate();
        }
    }






    private void keyarg_addalltags(String intag){
        ArrayList<String> tags = mykeyArg.getKeyargalltags();
        tags.add(intag);
        mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                .collection(FB_COLL_KEYARGS).document(mydebate.getActivekeyargguid())
                .update("keyargalltags",tags);
    }

    private void keyarg_addalltags(ArrayList<String> intags){
        ArrayList<String> tags = mykeyArg.getKeyargalltags();
        tags.addAll(intags);
        mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                .collection(FB_COLL_KEYARGS).document(mydebate.getActivekeyargguid())
                .update("keyargalltags",tags);

        ArrayList<String> debtags = mydebate.getDeballtags();
        debtags.addAll(intags);
        mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                .update("deballtags",debtags);
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


    private void modoptagoperate(String operation){
        mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                .collection(FB_COLL_CONVS).document(mydebate.getConvcontextarguid())
                .update("tagacceptstate",operation);
        mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                .update("convcontexttagstate",operation);
    }

    private void sendmodmessage (String message){
        String uid = getakey();
        Date ts = new Date();

        /*********/
        Arg_Conv Arg_Conv = new
                Arg_Conv(uid,  myselfintent.getUid(),  mPhotoUrl,
                mUsername,  myselfintent.getSid(),  mydebateintent.getUid(),
                TILT_NONE, message ,  null, MSGTYPE_MOD, ts);
        Arg_Conv.setconv(
                ARGTYPE_ASSRT,mydebate.getActivekeyargguid(),null,TAG_NONE,
                null,TAG_NONE,null,false);


        mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())

                .collection(FB_COLL_CONVS).document(uid).set(Arg_Conv);
    }

    private void modoptag(String tagtype, String operation){
        if (tagtype.equals(ARGOP_FALLACY)){
            if (operation.equals(TAG_ACCEPTED)){
                mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                        .collection(FB_COLL_CONVS).document(mydebate.getConvcontextarguid())
                        .update("seltag_acceptstate",TAG_ACCEPTED);
            }else {
                mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                        .collection(FB_COLL_CONVS).document(mydebate.getConvcontextarguid())
                        .update("seltag_acceptstate",TAG_REJECTED);
            }
        }else if (tagtype.equals(ARGOP_CLINCHER)){
            if (operation.equals(TAG_ACCEPTED)){
                mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                        .collection(FB_COLL_CONVS).document(mydebate.getConvcontextarguid())
                        .update("clinchtag_acceptstate",TAG_ACCEPTED);
            }else {
                mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                        .collection(FB_COLL_CONVS).document(mydebate.getConvcontextarguid())
                        .update("clinchtag_acceptstate",TAG_REJECTED);
            }
        }else{

        }
    }

    private void enddebate(){
        newsysmessage("The Debate Ended");
        mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                .update("flagactivestate",DEBACTIVSTATE_FINISHED);
    }

    private void give_tagon_for(String tagtype){
        ArrayList<String> tags = mykeyArg.getTagson_for();
        tags.add(tagtype);
        mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                .collection(FB_COLL_KEYARGS).document(mydebate.getActivekeyargguid())
                .update("tagson_for",tags);
    }

    private void give_tagon_ag(String tagtype){
        ArrayList<String> tags = mykeyArg.getTagson_for();
        tags.add(tagtype);
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

    private void newsysmessage(String message){
        String uid = getakey();
        Date ts = new Date();
        Arg_Conv Arg_Conv = new
                Arg_Conv(uid,  myselfintent.getUid(),  mPhotoUrl,
                mUsername,  myselfintent.getSid(),  mydebateintent.getUid(),
                TILT_NONE,  message,  null, MSGTYPE_SYS, ts);
        Arg_Conv.setconv(
                ARGTYPE_ASSRT,mydebate.getActivekeyargguid(),null,TAG_NONE,
                null,TAG_NONE,null,false);

        mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                .collection(FB_COLL_CONVS).document(uid).set(Arg_Conv);
    }

    private void newsysmessagedebend(String message){
        String uid = getakey();
        Date ts = new Date();
        Arg_Conv Arg_Conv = new
                Arg_Conv(uid,  myselfintent.getUid(),  mPhotoUrl,
                mUsername,  myselfintent.getSid(),  mydebateintent.getUid(),
                TILT_NONE,  message,  null, MSGTYPE_SYS_DEBEND, ts);
        Arg_Conv.setconv(
                ARGTYPE_ASSRT,mydebate.getActivekeyargguid(),null,TAG_NONE,
                null,TAG_NONE,null,false);

        mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                .collection(FB_COLL_CONVS).document(uid).set(Arg_Conv);
    }



//    private void tiekeyarlistener(String activkerarguid){
//        kerargreg = mFireStoreRef.collection(FB_RCOLL_DEBATES).document(myselfintent.getActivedebuid())
//                .collection(FB_COLL_KEYARGS).document(activkerarguid)
//                .addSnapshotListener(keyArglistener);
//    }

//    private EventListener<DocumentSnapshot> keyArglistener = new EventListener<DocumentSnapshot>() {
//        @RequiresApi(api = Build.VERSION_CODES.M)
//        @Override
//        public void onEvent(@Nullable DocumentSnapshot snapshot,
//                            @Nullable FirebaseFirestoreException e) {
//            if (e != null) {
//                Log.w(TAG, "Listen failed.", e);
//                return;
//            }
//            mykeyArg = snapshot.toObject(Arg_Key.class);
//
//            ((ScaleRatingBar)findViewById(R.id.mv_leftdebpt)).setRating(mykeyArg.getKeyargstattot_for_points());
//            ((ScaleRatingBar)findViewById(R.id.mv_rightpt)).setRating(mykeyArg.getKeyargstattot_ag_points());
//
//            String source = snapshot != null && snapshot.getMetadata().hasPendingWrites()
//                    ? "Local" : "Server";
//
//            if (snapshot != null && snapshot.exists()) {
//                Log.d(TAG, source + " data: " + snapshot.getData());
//            } else {
//                Log.d(TAG, source + " data: null");
//            }
//        }
//    };

    @Override
    public void onStart() {
        super.onStart();
        mFirestoreAdapter.startListening();
//        mFireStoreInviteAdapter.startListening();
        // Check if user is signed in.
        // TODO: Add code to check if user is signed in.
    }

    @Override
    public void onPause() {
        mFirestoreAdapter.stopListening();
//        mFireStoreInviteAdapter.stopListening();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mFirestoreAdapter.startListening();
//        mFireStoreInviteAdapter.startListening();
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
                            TILT_FOR,  mMessageEditText.getText().toString(),  LOADING_IMAGE_URL, ARG_ROLE_MOD, ts);

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
        storageReference.putFile(uri).addOnCompleteListener(ModViewPlay.this,
                new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            task.getResult().getMetadata().getReference().getDownloadUrl()
                                    .addOnCompleteListener(ModViewPlay.this,
                                            new OnCompleteListener<Uri>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Uri> task) {
                                                    if (task.isSuccessful()) {
                                                        Date ts = new Date();

                                                        Arg_Conv Arg_Conv = new
                                                                Arg_Conv(getakey(),  myselfintent.getUid(),  mPhotoUrl,
                                                                mUsername,  myselfintent.getSid(),  mydebateintent.getUid(),
                                                                TILT_FOR,  mMessageEditText.getText().toString(),
                                                                task.getResult().toString(),ARG_ROLE_MOD, ts);

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

//    private void clearedittexts(){
//        mMessageEditText.setText("");
//    }

}
