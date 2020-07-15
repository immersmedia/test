package co.tau.manthan.dashboard.ui.people;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;

import java.util.ArrayList;

import co.tau.manthan.R;
import co.tau.manthan.baseclasses.Debater;
import de.hdodenhof.circleimageview.CircleImageView;

import static co.tau.manthan.baseclasses.Constants.FB_RCOLL_USERSBYUID;

public class peoplefragment extends Fragment {

    private PeopleViewModel peopleViewModel;

    String inviteeintent;
    FirestoreRecyclerAdapter mFirestoreAdapter;
    private FirebaseFirestore mFireStoreRef;
    RecyclerView rcview;
    private LinearLayoutManager mLinearLayoutManager;
    ArrayList<String> excludes = new ArrayList<>();


    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String myuid = null;
    private String myname = null;
    private String mypic = null;
    private CircleImageView mypicview;
    private TextView mynameview;
    private TextView mysidview;
    Debater myself = new Debater();



    SharedPreferences sharedPref;


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





    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        peopleViewModel =
                ViewModelProviders.of(this).get(PeopleViewModel.class);
        View root = inflater.inflate(R.layout.fragment_people, container, false);
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



        rcview = root.findViewById(R.id.home_alldeblist);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        rcview.setLayoutManager(mLinearLayoutManager);
        mFireStoreRef = FirebaseFirestore.getInstance();






//        excludes = getIntent().getStringArrayListExtra("excludes");

        Query query = mFireStoreRef.collection(FB_RCOLL_USERSBYUID).orderBy("ts");
        FirestoreRecyclerOptions<Debater> mstoreoptions =
                new FirestoreRecyclerOptions.Builder<Debater>()
                        .setQuery(query, Debater.class)
                        .build();

        mFirestoreAdapter = new FirestoreRecyclerAdapter<Debater, MessageViewHolder>(mstoreoptions) {

            @NonNull
            @Override
            public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                return new MessageViewHolder(inflater.inflate(R.layout.item_fragpeople, parent, false));
            }


            @Override
            protected void onBindViewHolder(@NonNull final MessageViewHolder viewHolder, int position, @NonNull Debater Debater) {

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
//                viewHolder.invitee.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        String uid = getSnapshots().get(pos).getUid();
//                        if (excludes.contains(uid)){
//                            Snackbar.make(view,"Already selected", Snackbar.LENGTH_SHORT).show();
//                        }else{
//                            String sid = getSnapshots().get(pos).getSid();
//                            String name = getSnapshots().get(pos).getName();
//                            String pic = getSnapshots().get(pos).getPiclink();
//                            returncreds(uid,name,sid,pic);
//                        }
//
//                    }
//                });
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
        mFirestoreAdapter.startListening();
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        mFirestoreAdapter.startListening();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        mFirestoreAdapter.stopListening();
    }
}
