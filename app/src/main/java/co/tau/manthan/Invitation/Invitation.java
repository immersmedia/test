package co.tau.manthan.Invitation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

import co.tau.manthan.R;
import co.tau.manthan.baseclasses.Debater;
import de.hdodenhof.circleimageview.CircleImageView;

import static co.tau.manthan.baseclasses.Constants.FB_RCOLL_USERSBYUID;

public class Invitation extends AppCompatActivity {
    String inviteeintent;
    FirestoreRecyclerAdapter mFirestoreAdapter;
    private FirebaseFirestore mFireStoreRef;
    RecyclerView rcview;
    private LinearLayoutManager mLinearLayoutManager;
    ArrayList<String> excludes = new ArrayList<>();


    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView inviteename;
        TextView inviteesid;
        CircleImageView inviteepic;
        ConstraintLayout invitee;


        public MessageViewHolder(View v) {
            super(v);
            invitee = (ConstraintLayout)itemView.findViewById(R.id.invitee);
            inviteename = (TextView) itemView.findViewById(R.id.inviteename);
            inviteesid = (TextView) itemView.findViewById(R.id.inviteesid);
            inviteepic = (CircleImageView) itemView.findViewById(R.id.inviteepic);
//            invitee.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int position = getAdapterPosition();
//                }
//            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);

        inviteeintent = getIntent().getAction();

        rcview = findViewById(R.id.invitionsrecview);
        mLinearLayoutManager = new LinearLayoutManager(this);
        rcview.setLayoutManager(mLinearLayoutManager);
        mFireStoreRef = FirebaseFirestore.getInstance();

        excludes = getIntent().getStringArrayListExtra("excludes");

        Query query = mFireStoreRef.collection(FB_RCOLL_USERSBYUID).orderBy("ts");
        FirestoreRecyclerOptions<Debater> mstoreoptions =
                new FirestoreRecyclerOptions.Builder<Debater>()
                        .setQuery(query, Debater.class)
                        .build();

        mFirestoreAdapter = new FirestoreRecyclerAdapter<Debater, Invitation.MessageViewHolder>(mstoreoptions) {

            @NonNull
            @Override
            public Invitation.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                return new Invitation.MessageViewHolder(inflater.inflate(R.layout.iteminvitation, parent, false));
            }
            

            @Override
            protected void onBindViewHolder(@NonNull final Invitation.MessageViewHolder viewHolder, int position, @NonNull Debater Debater) {

//                mProgressBar.setVisibility(ProgressBar.INVISIBLE);

                final int pos = position;
                if (Debater.getName() != null) {
                    viewHolder.inviteename.setText(Debater.getName());
                }
                if (Debater.getSid() != null) {
                    viewHolder.inviteesid.setText(Debater.getSid());
                }
                if (Debater.getPiclink() != null) {
                    Glide.with(viewHolder.inviteepic.getContext())
                            .load(Debater.getPiclink())
                            .into(viewHolder.inviteepic);
                }
                viewHolder.invitee.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String uid = getSnapshots().get(pos).getUid();
                        if (excludes.contains(uid)){
                            Snackbar.make(view,"Already selected", Snackbar.LENGTH_SHORT).show();
                        }else{
                            String sid = getSnapshots().get(pos).getSid();
                            String name = getSnapshots().get(pos).getName();
                            String pic = getSnapshots().get(pos).getPiclink();
                            returncreds(uid,name,sid,pic);
                        }

                    }
                });
            }
        };

        mFirestoreAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mFirestoreAdapter.getItemCount();
                int lastVisiblePosition =
                        mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    rcview.scrollToPosition(positionStart);
                }
            }
        });
        rcview.setAdapter(mFirestoreAdapter);
    }

    private void returncreds(String uid, String name, String sid, String piclick){
        Intent intent=new Intent();
        intent.putExtra("result","success");
        intent.putExtra("inviteeuid",uid);
        intent.putExtra("inviteesid",sid);
        intent.putExtra("inviteename",name);
        intent.putExtra("inviteepic",piclick);
        setResult(Integer.decode(inviteeintent),intent);
        finish();//finishing activity
    }

    @Override
    public void onStart() {
            super.onStart();
        mFirestoreAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mFirestoreAdapter.stopListening();
    }



}
