package co.tau.manthan.dashboard.ui.myconvs;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;

import co.tau.manthan.DebViewPlay.DebViewPlay;
import co.tau.manthan.ModViewPlay.ModViewPlay;
import co.tau.manthan.Profile.ProfileActivity;
import co.tau.manthan.R;
import co.tau.manthan.baseclasses.Debate;
import co.tau.manthan.baseclasses.Debater;
import co.tau.manthan.dashboard.DashboardActivity;
import co.tau.manthan.infoscreen.InfoScreen;
import co.tau.manthan.startdeb.Startdeb;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.provider.AlarmClock.EXTRA_MESSAGE;
import static co.tau.manthan.baseclasses.Constants.ARG_ROLE_DEB;
import static co.tau.manthan.baseclasses.Constants.ARG_ROLE_MOD;
import static co.tau.manthan.baseclasses.Constants.ARG_ROLE_NONE;
import static co.tau.manthan.baseclasses.Constants.FB_RCOLL_DEBATES;

import static co.tau.manthan.baseclasses.Constants.MYDEB_MEASDEB;
import static co.tau.manthan.baseclasses.Constants.MYDEB_MEASMOD;
import static co.tau.manthan.baseclasses.Constants.MYDEB_NOTMEASDEB;
import static co.tau.manthan.baseclasses.Constants.MYDEB_NOTMEASMOD;
import static co.tau.manthan.baseclasses.Constants.NOTIFICATION_CHANNEL_ID;
import static co.tau.manthan.baseclasses.Constants.TILT_AG;
import static co.tau.manthan.baseclasses.Constants.TILT_FOR;
import static co.tau.manthan.baseclasses.Constants.TILT_MOD;
import static co.tau.manthan.baseclasses.Constants.TILT_NONE;
import static co.tau.manthan.baseclasses.Constants.TURN_AG;
import static co.tau.manthan.baseclasses.Constants.TURN_FOR;
import static co.tau.manthan.baseclasses.Constants.TURN_MOD;


public class MyConvsFragment extends Fragment {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    FirestoreRecyclerAdapter mFirestoreAdapter;
    private FirebaseFirestore mFireStoreRef;
    private String myuid = null;
    private String myname = null;
    private String mypic = null;
    private CircleImageView mypicview;
    private TextView mynameview;
    private TextView mysidview;

    ArrayList<Debate> debatelist = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;

    SharedPreferences sharedPref;

    RecyclerView.Adapter adapter;
    Debater myself = new Debater();


    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView debforname;
        TextView debagname;
        TextView modname;
        CircleImageView debforpic;
        CircleImageView debagpic;
        CircleImageView modpic;
        ConstraintLayout deb;
        TextView debtopic;
        ConstraintLayout rightlo;
        TextView myturnind;
        TextView debstatus;
        TextView scoreplate;
        TextView time;
        ConstraintLayout modlayout;



        public MessageViewHolder(View v) {
            super(v);
            deb = (ConstraintLayout)itemView.findViewById(R.id.im_toplo);
            debtopic = (TextView) itemView.findViewById(R.id.im_debatetopic);
            debforname = (TextView) itemView.findViewById(R.id.im_deb1_name);
            debagname = (TextView) itemView.findViewById(R.id.im_deb2_name);
            modname = (TextView) itemView.findViewById(R.id.im_mod_name);
            debforpic = (CircleImageView) itemView.findViewById(R.id.im_deb1_img);
            debagpic = (CircleImageView) itemView.findViewById(R.id.im_deb2_img);
            modpic = (CircleImageView) itemView.findViewById(R.id.im_mod_img);
            rightlo = (ConstraintLayout)itemView.findViewById(R.id.im_rightlo);
            myturnind = (TextView) itemView.findViewById(R.id.im_myturnind);
            debstatus = (TextView) itemView.findViewById(R.id.im_debstatus);
            scoreplate = (TextView) itemView.findViewById(R.id.scoreplate);
            time = (TextView) itemView.findViewById(R.id.im_time);
            modlayout = (ConstraintLayout)itemView.findViewById(R.id.modlayout);
        }
    }


    private MyConvsViewModel dashboardViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(MyConvsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_myconvos, container, false);
//        final TextView textView = root.findViewById(R.id.text_dashboard);
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        sharedPref = getActivity().getSharedPreferences("sharedpreferences", 0);

        Gson gson = new Gson();
        String myselfjson = sharedPref.getString("myself","");
        myself = gson.fromJson(myselfjson,Debater.class);

        mFireStoreRef = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
//            startActivity(new Intent(this.getContext(), SignInActivity.class));
//            getActivity().finish();

        } else {
            myuid = mFirebaseUser.getUid();
            myname = mFirebaseUser.getDisplayName();
            mypic = mFirebaseUser.getPhotoUrl().toString();
        }
        mypicview = root.findViewById(R.id.dashviewer_my_pic);
        mynameview = (TextView)root.findViewById(R.id.dashviewer_myname);
        mysidview = (TextView)root.findViewById(R.id.dashviewer_mysid);

        Glide.with(getContext())
                .load(mypic)
                .into(mypicview);
        mypicview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchProfileActivity();
            }
        });
        mynameview.setText(myself.getName());
        mysidview.setText(myself.getSid());
        ((FloatingActionButton)root.findViewById(R.id.dashviewer_newdeb)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchnewdeb();
            }
        });
        ((FloatingActionButton)root.findViewById(R.id.dashviewer_info)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchinfoscreen();
            }
        });




        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.dashviewer_modreqlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new RecyclerView.Adapter() {
            @NonNull
            @Override
            public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                View v=null;
                v = inflater.inflate(R.layout.item_mydebs_newdesign1,parent,false);
                return new MessageViewHolder(v);

//                if (viewType == MYDEB_MEASMOD){
//                    v = inflater.inflate(R.layout.item_mydebs_measmod,parent,false);
//                }else if (viewType == MYDEB_MEASDEB){
//                    v = inflater.inflate(R.layout.item_mydebs_measdeb,parent,false);
//                }else if (viewType == MYDEB_NOTMEASMOD){
//                    v = inflater.inflate(R.layout.item_mydebs_notmeasmod,parent,false);
//                }else if (viewType == MYDEB_NOTMEASDEB){
//                    v = inflater.inflate(R.layout.item_mydebs_notmeasdeb,parent,false);
//                }else{
//
//                }
//                return new MessageViewHolder(v);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, final int position) {
                final int pos = position;
                Debate debate = debatelist.get(position);
                MessageViewHolder viewHolder = (MessageViewHolder)vh;

                String conscore = debate.getDebstattotalpoints() +"/"+ debate.getActivekeyargindex();
                viewHolder.scoreplate.setText(String.valueOf(conscore));
//                viewHolder.time.setText(String.valueOf(debate.getActualstartts().toString()));
                viewHolder.debstatus.setText(debate.getFlagactivestate());

                if (debate.getModeratoruid() == null){
                    viewHolder.modlayout.setVisibility(View.GONE);
                    viewHolder.modname.setVisibility(View.GONE);
                    viewHolder.modpic.setVisibility(View.GONE);
                }

                if (debate.getTurn().equals(TURN_FOR) && debate.getDebater_for_uid().equals(myself.getUid())){
                    viewHolder.myturnind.setVisibility(View.VISIBLE);
                }
                if (debate.getTurn().equals(TURN_AG) && debate.getDebater_ag_uid().equals(myself.getUid())){
                    viewHolder.myturnind.setVisibility(View.VISIBLE);
                }
                if (debate.getTurn().equals(TURN_MOD) && debate.getModeratoruid().equals(myself.getUid())){
                    viewHolder.myturnind.setVisibility(View.VISIBLE);
                }

                if (debate.getTopic()!= null) {
                    viewHolder.debtopic.setText('"'+debate.getTopic()+'"');
                }

                if (debate.getDebater_for_name()!= null) {
                    viewHolder.debforname.setText(debate.getDebater_for_name());
                }
                if (debate.getDebater_for_pic() != null) {
                    Glide.with(viewHolder.debforpic.getContext())
                            .load(debate.getDebater_for_pic())
                            .into(viewHolder.debforpic);
                }

                if (debate.getDebater_ag_name()!= null) {
                    viewHolder.debagname.setText(debate.getDebater_ag_name());
                }
                if (debate.getDebater_ag_pic() != null) {
                    Glide.with(getContext())
                            .load(debate.getDebater_ag_pic())
                            .into(viewHolder.debagpic);
                }

                if (debate.getModerator_name()!= null) {
                    viewHolder.modname.setText(debate.getModerator_name());
                }
                if (debate.getModerator_pic() != null) {
                    Glide.with(getContext())
                            .load(debate.getModerator_pic())
                            .into(viewHolder.modpic);
                }

                viewHolder.myturnind.setVisibility(View.GONE);
                if (debate.getTurn().equals(TURN_FOR)){
                    if (debate.getDebater_for_uid().equals(myself.getUid())){
                        viewHolder.myturnind.setVisibility(View.VISIBLE);
                    }
                }else if(debate.getTurn().equals(TURN_AG)){
                    if (debate.getDebater_ag_uid().equals(myself.getUid())){
                        viewHolder.myturnind.setVisibility(View.VISIBLE);
                    }
                }


                viewHolder.deb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Debate deb = debatelist.get(position);
                        lauchdebate(deb);
                    }
                });
            }

            @Override
            public int getItemCount() {
                return debatelist.size();
            }

            @Override
            public int getItemViewType(int position) {
                if (myself.getUid().equals(debatelist.get(position).getInviteruid())){
                    if (debatelist.get(position).getModeratoruid() != null){
                        if (debatelist.get(position).getModeratoruid().equals(myself.getUid())){
                            return MYDEB_MEASMOD;
                        }else {
                            return MYDEB_MEASDEB;
                        }
                    }
                    return MYDEB_MEASDEB;
                }else{
                    if (debatelist.get(position).getModeratoruid() != null){
                        if (debatelist.get(position).getModeratoruid().equals(myself.getUid())){
                            return MYDEB_NOTMEASMOD;
                        }else {
                            return MYDEB_NOTMEASDEB;
                        }
                    }
                    return MYDEB_NOTMEASDEB;
                }
            }
        };

        recyclerView.setAdapter(adapter);
        swipeRefreshLayout = root.findViewById(R.id.dashviewer_mod_swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshdata();
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });



        refreshdata();
//        mFireStoreRef.collection(FB_RCOLL_MYPARTICIPATION)
//                .document(myuid).collection(FB_COLL_MYDEBATES)
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    private Debate ds;
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                        debatelist.clear();
//                        for (DocumentSnapshot dc : queryDocumentSnapshots.getDocuments()){
//                            ds = dc.toObject(Debate.class);
//                            debatelist.add(ds);
//                        }
//                        adapter.notifyDataSetChanged();
//                    }
//        });

        mFireStoreRef.collection(FB_RCOLL_DEBATES).whereArrayContains("participants",myself.getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    private Debate ds;
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        debatelist.clear();
                        for (DocumentSnapshot dc : queryDocumentSnapshots.getDocuments()){
                            ds = dc.toObject(Debate.class);
                            debatelist.add(ds);


                            if (getmytilt(ds).equals(ds.getTurn())){
                                sendnofificationmyturn(ds);
                            }

                        }
                        adapter.notifyDataSetChanged();
                    }
                });

        //        mFireStoreRef.collection(FB_RCOLL_MYPARTICIPATION)
//                .document(myuid).collection(FB_COLL_MYDEBATES)
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    private Debate ds;
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
////                        debatelist.clear();
//                        for (DocumentSnapshot dc : queryDocumentSnapshots.getDocuments()){
//                            ds = dc.toObject(Debate.class);
//                            if (getmytilt(ds).equals(ds.getTurn())){
//                                sendnofificationmyturn(ds);
//                            }
//                        }
//                    }
//                });


//
        return root;


    }

    private void refreshdata(){
//        mFireStoreRef.collection(FB_RCOLL_MYPARTICIPATION).document(myuid).collection(FB_COLL_MYDEBATES).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            private Debate ds;
//            @RequiresApi(api = Build.VERSION_CODES.N)
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                debatelist.clear();
//                for (DocumentSnapshot dc : queryDocumentSnapshots.getDocuments()){
//                    ds = dc.toObject(Debate.class);
//                    debatelist.add(ds);
//                }
//                adapter.notifyDataSetChanged();
//            }
//        });

    }

    private String getmytilt(Debate deb){
        if (myself.getUid().equals(deb.getDebater_for_uid())){
            return TILT_FOR;
        }else if (myself.getUid().equals(deb.getDebater_ag_uid())){
            return TILT_FOR;
        }else if(myself.getUid().equals(deb.getModeratoruid())){
            return TILT_MOD;
        } else {
            return TILT_NONE;
        }
    }

    private void sendnofificationmyturn(Debate deb){
        Intent resultIntent = new Intent(getActivity(), DashboardActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 1 /* Request code */, resultIntent,
                PendingIntent.FLAG_ONE_SHOT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.getActivity(), NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_logosvgsolidoutline)
                .setContentTitle("YOUR TURN!")
                .setContentText("It's your turn. Click to go to the list.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                                .setSound(alarmSound);
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this.getActivity());
        notificationManager.notify(2197, builder.build());
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    private void launchnewdeb(){
        Intent intent = new Intent(this.getContext(), Startdeb.class);
        startActivity(intent);
    }

    private void launchinfoscreen(){
        Intent intent = new Intent(this.getContext(), InfoScreen.class);
        startActivity(intent);
    }

    private void launchProfileActivity(){
        Intent intent = new Intent(this.getContext(), ProfileActivity.class);
        intent.putExtra(EXTRA_MESSAGE, "message");
        startActivity(intent);

    }

    private void lauchdebate(Debate deb){



        if (myuid.equals(deb.getModeratoruid())) {
            Gson gson = new Gson();
            String myDebateJson = gson.toJson(deb);
            String myselfJson = gson.toJson(myself);

            Intent intent = new Intent(this.getContext(), ModViewPlay.class);
            intent.putExtra("mydebate", myDebateJson);
            intent.putExtra("myself", myselfJson);
            startActivity(intent);
        }else if(myuid.equals(deb.getDebater_for_uid())){
            Gson gson = new Gson();
            myself.setTilt(TILT_FOR);
            String myDebateJson = gson.toJson(deb);
            String myselfJson = gson.toJson(myself);
            Intent intent = new Intent(this.getContext(), DebViewPlay.class);
            intent.putExtra("mydebate", myDebateJson);
            intent.putExtra("myself", myselfJson);
            startActivity(intent);

        }else if(myuid.equals(deb.getDebater_ag_uid())){
            Gson gson = new Gson();
            myself.setTilt(TILT_AG);
            String myDebateJson = gson.toJson(deb);
            String myselfJson = gson.toJson(myself);

            Intent intent = new Intent(this.getContext(), DebViewPlay.class);
            intent.putExtra("mydebate", myDebateJson);
            intent.putExtra("myself", myselfJson);
            startActivity(intent);
        }else{

        }
    }

    private Debater getdebater(Debate debate, Debater debater){
        Debater debr = debater;
        String tilt = TILT_NONE;
        String debrole = ARG_ROLE_NONE;

        if (debater.getUid().equals(debate.getDebater_for_uid())){
            tilt = TILT_FOR;
            debrole = ARG_ROLE_DEB;
        }else if (debater.getUid().equals(debate.getDebater_ag_uid())){
            tilt = TILT_AG;
            debrole = ARG_ROLE_DEB;
        }else if (debater.getUid().equals(debate.getModeratoruid())){
            tilt = TILT_NONE;
            debrole = ARG_ROLE_MOD;
        }else{
            return null;
        }
        debr.addDebaterdetails(debrole,tilt,true);
        return debr;
    }
}
