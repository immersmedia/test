package co.tau.manthan.dashboard.ui.invites;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;


import java.util.ArrayList;

import co.tau.manthan.ModViewPlay.ModViewPlay;
import co.tau.manthan.R;
import co.tau.manthan.baseclasses.Debate;
import co.tau.manthan.baseclasses.Debater;
import co.tau.manthan.baseclasses.InviteMod;
import co.tau.manthan.dashboard.DashboardActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import static co.tau.manthan.baseclasses.Constants.FB_COLL_MYINVITES;
import static co.tau.manthan.baseclasses.Constants.FB_RCOLL_DEBATES;
import static co.tau.manthan.baseclasses.Constants.FB_RCOLL_MYPARTICIPATION;
import static co.tau.manthan.baseclasses.Constants.NOTIFICATION_CHANNEL_ID;
import static co.tau.manthan.baseclasses.Constants.TAGOP_IASKGURU;
import static co.tau.manthan.baseclasses.InviteMod.INVITESTATE_ALIVE;

public class InvitesFragment extends Fragment {

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

    ArrayList<InviteMod> invitelist = new ArrayList<>();
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
        CircleImageView myturnind;
        TextView debstatus;



        public MessageViewHolder(View v) {
            super(v);
            deb = (ConstraintLayout)itemView.findViewById(R.id.viewer_toplo);
            debtopic = (TextView) itemView.findViewById(R.id.viewer_debatetopic);
            debforname = (TextView) itemView.findViewById(R.id.viewer_deb1_name);
            debagname = (TextView) itemView.findViewById(R.id.viewer_deb2_name);
            modname = (TextView) itemView.findViewById(R.id.viewer_mod_name);
            debforpic = (CircleImageView) itemView.findViewById(R.id.viewer_deb1_img);
            debagpic = (CircleImageView) itemView.findViewById(R.id.viewer_deb2_img);
            modpic = (CircleImageView) itemView.findViewById(R.id.viewer_mod_img);
            rightlo = (ConstraintLayout)itemView.findViewById(R.id.viewer_rightlo);
            myturnind = (CircleImageView) itemView.findViewById(R.id.viewer_myturnind);
            debstatus = (TextView) itemView.findViewById(R.id.viewer_debstatus);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

//        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        final View root = inflater.inflate(R.layout.fragment_invites, container, false);

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
        mypicview = root.findViewById(R.id.home_my_pic);
        mynameview = (TextView)root.findViewById(R.id.home_myname);
        mysidview = (TextView)root.findViewById(R.id.home_mysid);

        Glide.with(getContext())
                .load(mypic)
                .into(mypicview);
        mynameview.setText(myself.getName());
        mysidview.setText(myself.getSid());

        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.home_alldeblist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new RecyclerView.Adapter() {
            @NonNull
            @Override
            public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                View v = inflater.inflate(R.layout.item_viewer,parent,false);
                return new MessageViewHolder(v);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, final int position) {
                final int pos = position;
                InviteMod invite = invitelist.get(position);
                MessageViewHolder viewHolder = (MessageViewHolder)vh;

                if (invite.getDebtopic()!= null) {
                    viewHolder.debtopic.setText('"'+invite.getDebtopic()+'"');
                }

                if (invite.getInvitername()!= null) {
                    viewHolder.debforname.setText(invite.getInvitername());
                }
                if (invite.getForpic() != null) {
                    Glide.with(viewHolder.debforpic.getContext())
                            .load(invite.getForpic())
                            .into(viewHolder.debforpic);
                }

                if (invite.getAgname()!= null) {
                    viewHolder.debagname.setText(invite.getAgname());
                }
                if (invite.getAgpic() != null) {
                    Glide.with(getContext())
                            .load(invite.getAgpic())
                            .into(viewHolder.debagpic);
                }

                viewHolder.deb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String deb = invitelist.get(position).getDebateuid();
                        lauchdebate(deb);
                    }
                });
            }

            @Override
            public int getItemCount() {
                return invitelist.size();
            }

            @Override
            public int getItemViewType(int position) {
//                if (invitelist.get(position).getInviteruid().equals(myself.getUid())){
//                    if (invitelist.get(position).getModeratoruid().equals(myself.getUid())){
//                        return MYDEB_MEASMOD;
//                    }else {
//                        return MYDEB_MEASDEB;
//                    }
//                }else{
//                    if (invitelist.get(position).getModeratoruid().equals(myself.getUid())){
//                        return MYDEB_NOTMEASMOD;
//                    }else {
//                        return MYDEB_NOTMEASDEB;
//                    }
//                }
                return super.getItemViewType(position);
            }
        };

        recyclerView.setAdapter(adapter);

        mFireStoreRef.collection(FB_RCOLL_MYPARTICIPATION).document(myuid)
                .collection(FB_COLL_MYINVITES)
//                .whereEqualTo("state", INVITESTATE_ALIVE)
                .orderBy("ts")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        if (queryDocumentSnapshots != null){
                            invitelist.clear();
                            for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {

                                if (dc.getType() == DocumentChange.Type.ADDED) {
//                                    String title = dc.getDocument().getData().get("notificationTitle").toString();
//                                    String body = dc.getDocument().getData().get("notificationBody").toString();

                                    //Method to set Notifications
                                    InviteMod ds = dc.getDocument().toObject(InviteMod.class);
                                    sendnofificationinvite(ds);
                                }
                            }

                            for (DocumentSnapshot dc : queryDocumentSnapshots.getDocuments()){

                                InviteMod ds = dc.toObject(InviteMod.class);
                                if (ds.getState().equals(INVITESTATE_ALIVE)){
                                    invitelist.add(ds);
                                }
//                                invitelist.add(ds);
                            }
                            adapter.notifyDataSetChanged();
                        }

                    }
                });
        return root;
    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();
//        mFirestoreAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
//        adapter.notifyDataSetChanged();
//        mFirestoreAdapter.stopListening();
    }


    private void sendnofificationinvite(InviteMod invite){

        Intent resultIntent = new Intent(getActivity(), DashboardActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0 /* Request code */, resultIntent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.getActivity(), NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_logosvgsolidoutline)
                .setContentTitle("You are needed!")
                .setContentText("Someone is looking for your assistance on their Manthan. Click to get to the list.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                                .setSound(alarmSound);
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this.getActivity());
        notificationManager.notify(2198, builder.build());
    }

    private void lauchdebate(String debuid){

        mFireStoreRef.collection(FB_RCOLL_DEBATES).document(debuid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Debate deb = task.getResult().toObject(Debate.class);
                Gson gson = new Gson();
                String myDebateJson = gson.toJson(deb);
                String myselfJson = gson.toJson(myself);

                Intent intent = new Intent(getContext(), ModViewPlay.class);
                intent.putExtra("mydebate", myDebateJson);
                intent.putExtra("participation", TAGOP_IASKGURU);
                intent.putExtra("myself", myselfJson);
                startActivity(intent);
            }
        });
    }
}
