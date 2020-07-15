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
package co.tau.manthan.ViewerPlay;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import androidx.core.content.ContextCompat;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import co.tau.manthan.ModViewPlay.ModViewViewModel;
import co.tau.manthan.R;
import co.tau.manthan.baseclasses.Arg_Conv;
import co.tau.manthan.baseclasses.Arg_Key;
import co.tau.manthan.baseclasses.Debate;
import co.tau.manthan.baseclasses.Debater;
import co.tau.manthan.baseclasses.TextUtils;
import co.tau.manthan.baseclasses.Utils;
import co.tau.manthan.baseclasses.viewerfb;
import co.tau.manthan.login.SignInActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static co.tau.manthan.baseclasses.Constants.ANONYMOUS;
import static co.tau.manthan.baseclasses.Constants.DEBACTIVSTATE_FINISHED;
import static co.tau.manthan.baseclasses.Constants.DEBACTIVSTATE_ONGOING;
import static co.tau.manthan.baseclasses.Constants.DEBACTIVSTATE_WAITINGJOIN;
import static co.tau.manthan.baseclasses.Constants.FB_COLL_CONVS;
import static co.tau.manthan.baseclasses.Constants.FB_COLL_KEYARGS;
import static co.tau.manthan.baseclasses.Constants.FB_COLL_VIEWFBCONVS;
import static co.tau.manthan.baseclasses.Constants.FB_CONV_VIEWERFB;
import static co.tau.manthan.baseclasses.Constants.FB_RCOLL_DEBATES;
import static co.tau.manthan.baseclasses.Constants.MSGTYPE_DEB;
import static co.tau.manthan.baseclasses.Constants.MSGTYPE_MOD;
import static co.tau.manthan.baseclasses.Constants.MSGTYPE_SYS;
import static co.tau.manthan.baseclasses.Constants.MSGTYPE_SYS_DEBEND;
import static co.tau.manthan.baseclasses.Constants.TILT_FOR;
import static co.tau.manthan.baseclasses.Constants.TURN_AG;
import static co.tau.manthan.baseclasses.Constants.TURN_FOR;
import static co.tau.manthan.baseclasses.Constants.TURN_MOD;
import static co.tau.manthan.baseclasses.Constants.VIEW_LEFTMSG;
import static co.tau.manthan.baseclasses.Constants.VIEW_MODERATORMSG;
import static co.tau.manthan.baseclasses.Constants.VIEW_NOMSG;
import static co.tau.manthan.baseclasses.Constants.VIEW_RIGHTMSG;
import static co.tau.manthan.baseclasses.Constants.VIEW_SYSMSG;
import static co.tau.manthan.baseclasses.Constants.VIEW_SYSMSG_DEBEND;
import static co.tau.manthan.baseclasses.Utils.dpToPx;

public class ViewerPlay extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener {

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        ImageView messageImageView;
//        CircleImageView messengerImageView;
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
            tagview = (LinearLayout) itemView.findViewById(R.id.tagview);
            allscorelo = (ChipGroup)itemView.findViewById(R.id.allscore);
            hightlightlo = (ConstraintLayout)itemView.findViewById(R.id.hightlightlo);
            msg_outerlo = (ConstraintLayout)itemView.findViewById(R.id.msg_outerlo);
            viewerpanel = (LinearLayout) itemView.findViewById(R.id.viewerpanel);
            iconcur = (TextView) itemView.findViewById(R.id.tagconcur);
            iconcurnot = (TextView) itemView.findViewById(R.id.tagnoconcur);

        }
    }

    private static final String TAG = "ViewerPlay";


    private String mUsername;
    private String mPhotoUrl;
    private SharedPreferences mSharedPreferences;
    private GoogleApiClient mGoogleApiClient;

    private RecyclerView mMessageRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private Debater myselfintent = new Debater();
    private Debate mydebateintent = new Debate();
    private Debate mydebate = new Debate();
    private Arg_Key mykeyArg = new Arg_Key();
    private ArrayList<Arg_Key> keyargs = new ArrayList<>();
    private Map keyargmap = new HashMap<String, Arg_Key>();
    private viewerfb myfb;
    private Arg_Conv contextconv;
    private ArrayList<viewerfb> fblist = new ArrayList<>();
    ListenerRegistration fbhandle = null;


    //    ListenerRegistration kerargreg;
    private ModViewViewModel modviewmodel;
//    String myactivekeyarg = null;

    CircleImageView forImageView;
    CircleImageView agImageView;
    CircleImageView modImageView;

    int contextconvindex=0;

    private EditText mMessageEditText;
    private EditText messageEditTextop;


//    private DatabaseReference mFirebaseDatabaseReference;
//    private FirebaseRecyclerAdapter<FriendlyMessage, MessageViewHolder>
//            mFirebaseAdapter;

    private FirebaseFirestore mFireStoreRef;
    private FirestoreRecyclerAdapter mFirestoreAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewer_play);

//        mMessageReasonText = (TextView) findViewById(R.id.mvreasonTextView);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Gson gson = new Gson();
        mydebateintent = gson.fromJson(getIntent().getStringExtra("mydebate"), Debate.class);
//        myactivekeyarg = mydebateintent.getActivekeyargguid();
        myselfintent = gson.fromJson(getIntent().getStringExtra("myself"), Debater.class);

        forImageView = (CircleImageView)findViewById(R.id.vv_leftImageView);
        modImageView = (CircleImageView)findViewById(R.id.vv_centerImageView);
        agImageView = (CircleImageView)findViewById(R.id.vv_rightImageView);



        mUsername = ANONYMOUS;

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
//        ============================

//        mMessageEditText = (EditText) findViewById(R.id.dv_messageEditText);
//        messageEditTextop = (EditText) findViewById(R.id.dv_messageEditTextop);
//
//        Autocomplete<Hashtag> htagacomplete = Tags.setupHashtagAutocomplete(mMessageEditText,
//                '#',this,Hashtag.HashtagViewersS);
//        Autocomplete<Hashtag> htagacompleteop = Tags.setupHashtagAutocomplete(messageEditTextop,
//                '#',this,Hashtag.HashtagSop);
//        Autocomplete<Hashtag> htagacompleteop = Tags.setupatlistAutocomplete(mMessageEditText,
//                '@',this,list);


        Glide.with(ViewerPlay.this)
                .load(mydebateintent.getDebater_for_pic())
                .into(forImageView);
        ((TextView)findViewById(R.id.vv_leftname)).setText(mydebateintent.getDebater_for_name());

        Glide.with(ViewerPlay.this)
                .load(mydebateintent.getModerator_pic())
                .into(modImageView);
        ((TextView)findViewById(R.id.vv_centername)).setText(mydebateintent.getModerator_name());

        Glide.with(ViewerPlay.this)
                .load(mydebateintent.getDebater_ag_pic())
                .into(agImageView);
        ((TextView)findViewById(R.id.vv_rightname)).setText(mydebateintent.getDebater_ag_name());


        ((TextView)findViewById(R.id.vv_debtitle)).setText(mydebateintent.getTopic());

        // Initialize ProgressBar and RecyclerView.
        mMessageRecyclerView = (RecyclerView) findViewById(R.id.modviewplay_messageRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);

        mFireStoreRef = FirebaseFirestore.getInstance();

//        fbhandle = setupfbwatcher();

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

                if (mydebate.getConvcontextarguid()!= null){
                    contextconv = null;
                    for(int i= 0;i < mFirestoreAdapter.getItemCount();i++){
                        Arg_Conv conv = (Arg_Conv) mFirestoreAdapter.getItem(i);
                        if (conv.getUid().equals(mydebate.getConvcontextarguid())){
                            contextconv = conv;
                            contextconvindex = i;
                        }
                    }
                }




                if (mydebate.getTurn().equals(TURN_MOD)){
                    findViewById(R.id.vv_turnviewmod).setVisibility(VISIBLE);
                    findViewById(R.id.vv_turnviewleft).setVisibility(View.INVISIBLE);
                    findViewById(R.id.vv_turnviewright).setVisibility(View.INVISIBLE);
                }else if (mydebate.getTurn().equals(TURN_AG)){
                    findViewById(R.id.vv_turnviewmod).setVisibility(View.INVISIBLE);
                    findViewById(R.id.vv_turnviewleft).setVisibility(View.INVISIBLE);
                    findViewById(R.id.vv_turnviewright).setVisibility(VISIBLE);
                }else if (mydebate.getTurn().equals(TURN_FOR)){
                    findViewById(R.id.vv_turnviewmod).setVisibility(View.INVISIBLE);
                    findViewById(R.id.vv_turnviewleft).setVisibility(VISIBLE);
                    findViewById(R.id.vv_turnviewright).setVisibility(View.INVISIBLE);
                }else{

                }

                if (mSharedPreferences.contains(mydebate.getConvcontextarguid()+"iconcur")){
                    ((AppCompatButton)findViewById(R.id.iconcur)).setEnabled(false);
                }else{
                    ((AppCompatButton)findViewById(R.id.iconcur)).setEnabled(true);
                }


                if (mydebate.getFlagactivestate().equals(DEBACTIVSTATE_WAITINGJOIN)){
                    findViewById(R.id.vv_toplo3).setVisibility(GONE);
//                    findViewById(R.id.dv_sendmessagelayout).setVisibility(GONE);

                }else if (mydebate.getFlagactivestate().equals(DEBACTIVSTATE_ONGOING)) {
                    findViewById(R.id.vv_toplo3).setVisibility(GONE);
                    findViewById(R.id.vv_reviewmessagepanel).setVisibility(View.VISIBLE);

//                    if (myactivekeyarg.equals(mydebate.getActivekeyargguid().toString())){
//
//                    }else{
//                        myactivekeyarg = mydebate.getActivekeyargguid().toString();
//                    }


                    if(mydebate.getConvcontextarguid() != null){
                        findViewById(R.id.vv_reviewmessagepanel).setVisibility(View.VISIBLE);
                    }else{
                        findViewById(R.id.vv_reviewmessagepanel).setVisibility(GONE);
                    }

                    String source = snapshot != null && snapshot.getMetadata().hasPendingWrites()
                            ? "Local" : "Server";

                    if (snapshot != null && snapshot.exists()) {
                        Log.d(TAG, source + " data: " + snapshot.getData());
                    } else {
                        Log.d(TAG, source + " data: null");
                    }
                }else if (mydebate.getFlagactivestate().equals(DEBACTIVSTATE_FINISHED)) {
                    findViewById(R.id.vv_toplo3).setVisibility(VISIBLE);
//                    findViewById(R.id.dv_sendmessagelayout).setVisibility(GONE);
//                    ((TextView)findViewById(R.id.vv_finishconvscore)).setText(String.valueOf(mydebate.getDebstattotalpoints()));
                    ChipGroup alltagsv = (ChipGroup)findViewById(R.id.vv_finishtxtlabel1);
                    Utils.displayalltags(alltagsv, mydebate.getDeballtags(),getApplicationContext());
                }
            }
        });

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
                mykeyArg = keyargs.get(keyargs.size()-1);
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

        ((AppCompatButton)findViewById(R.id.iconcur)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                long count = mSharedPreferences.getLong(mydebate.getConvcontextarguid()+"iconcur",0);
                if((count == 0) && (contextconv != null)) {
//                    long iconcur = contextconv.getStaticoncur();
                    long iconcur = ((Arg_Conv)mFirestoreAdapter.getItem(contextconvindex)).getStaticoncur();
                    iconcur = iconcur+1;
                    mFireStoreRef.collection(FB_RCOLL_DEBATES)
                            .document(mydebateintent.getUid())
                            .collection(FB_COLL_CONVS)
                            .document(mydebate.getConvcontextarguid())
                            .update("staticoncur",iconcur);

                    viewerfb vfb = new viewerfb(myselfintent.getUid(),myselfintent.getName(),myselfintent.getPiclink(),myselfintent.getSid());
                    vfb.setAgreement(viewerfb.ICONCUR);

                    mFireStoreRef.collection(FB_RCOLL_DEBATES)
                            .document(mydebateintent.getUid())
                            .collection(FB_COLL_VIEWFBCONVS)
                            .document(mydebate.getConvcontextarguid())
                            .collection(FB_CONV_VIEWERFB)
                            .document(myselfintent.getUid())
                            .set(vfb);

                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putLong(mydebate.getConvcontextarguid()+"iconcur", iconcur);
                    editor.commit();
                    Snackbar.make(view,"You concur.", Snackbar.LENGTH_SHORT).show();
                }else{
                    Snackbar.make(view,"You already said it.", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        ((AppCompatButton)findViewById(R.id.iconcurnot)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long count = mSharedPreferences.getLong(mydebate.getConvcontextarguid()+"iconcur",0);
                if((count == 0) && (contextconv != null)) {

//                    long iconcurnot = contextconv.getStaticoncurnot();
                    long iconcurnot = ((Arg_Conv)mFirestoreAdapter.getItem(contextconvindex)).getStaticoncurnot();

                    iconcurnot = iconcurnot+1;
                    mFireStoreRef.collection(FB_RCOLL_DEBATES)
                            .document(mydebateintent.getUid())
                            .collection(FB_COLL_CONVS)
                            .document(mydebate.getConvcontextarguid())
                            .update("staticoncurnot",iconcurnot);

                    viewerfb vfb = new viewerfb(myselfintent.getUid(),myselfintent.getName(),myselfintent.getPiclink(),myselfintent.getSid());
                    vfb.setAgreement(viewerfb.ICONCURNOT);

                    mFireStoreRef.collection(FB_RCOLL_DEBATES)
                            .document(mydebateintent.getUid())
                            .collection(FB_COLL_VIEWFBCONVS)
                            .document(mydebate.getConvcontextarguid())
                            .collection(FB_CONV_VIEWERFB)
                            .document(myselfintent.getUid())
                            .set(vfb);

                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putLong(mydebate.getConvcontextarguid()+"iconcur", iconcurnot);
                    editor.commit();
                    Snackbar.make(view,"You do not concur.", Snackbar.LENGTH_SHORT).show();
                }else{
                    Snackbar.make(view,"You already said it.", Snackbar.LENGTH_SHORT).show();
                }
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
//                if (Arg_Conv.getUid().equals(mydebate.getConvcontextarguid())){
//                    contextconvindex = position;
//                }

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


                if (Arg_Conv.getArgtext() != null) {
                    String msg = Arg_Conv.getArgtext();
                    SpannableString htag = TextUtils.decoratemessage(msg);
                    viewHolder.messageTextView.setText(htag);
                    viewHolder.messageTextView.setVisibility(VISIBLE);
                }

                if(Arg_Conv.getUid().equals(mydebate.getConvcontextarguid())){
                    viewHolder.hightlightlo.setBackgroundResource(R.drawable.cardshape);
                }else{
                    viewHolder.hightlightlo.setBackgroundResource(0);
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

            @Override
            public void onChanged() {

                super.onChanged();
            }
        });

        mMessageRecyclerView.setAdapter(mFirestoreAdapter);
    }


    ListenerRegistration setupfbwatcher(){
        ListenerRegistration lfbhandle = null;
        if (mydebateintent.getConvcontextarguid() != null){
            myfb = null;
            lfbhandle = mFireStoreRef.collection(FB_RCOLL_DEBATES).document(mydebateintent.getUid())
                    .collection(FB_COLL_VIEWFBCONVS).document(mydebateintent.getConvcontextarguid())
                    .collection(FB_CONV_VIEWERFB).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    fblist.clear();
                    for (DocumentSnapshot dc : queryDocumentSnapshots.getDocuments()){
                        viewerfb fb = dc.toObject(viewerfb.class);
                        fblist.add(fb);
                        if (fb.getUid().equals(myselfintent.getUid())){
                            myfb = fb;
                        }
                    }
                }
            });
        }else{
            myfb = null;
        }
        return lfbhandle;
    }

    @Override
    public void onStart() {
        super.onStart();
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
}
