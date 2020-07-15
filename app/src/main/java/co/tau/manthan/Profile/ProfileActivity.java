package co.tau.manthan.Profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import co.tau.manthan.R;
import co.tau.manthan.login.SignInActivity;

import static co.tau.manthan.baseclasses.Constants.ANONYMOUS;

public class ProfileActivity extends AppCompatActivity {
    SharedPreferences sharedPref;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private GoogleSignInClient mGoogleSignInClient;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private String mUsername;
    private String mPhotoUrl;
    private String mUserphone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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
            mUserphone = mFirebaseUser.getPhoneNumber();
            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }
        }

//         sharedPref =  getSharedPreferences("sharedpreferences", 0);
//        String loginname = sharedPref.getString("loginname", "");
//        String loginuid = sharedPref.getString("loginuid", "");
//        String loginpic = sharedPref.getString("loginpic", "");
//        String loginphone = sharedPref.getString("loginphone", "");






        ((TextView)findViewById(R.id.profile_name)).setText(mUsername);
        ((TextView)findViewById(R.id.profile_phone)).setText(mUserphone);
        Picasso.get().load(mPhotoUrl).into((ImageView)findViewById(R.id.profile_image_view));

        findViewById(R.id.profileclose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.pleditnamesave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = ((AppCompatEditText)findViewById(R.id.profileeditname)).getText().toString();
                if (!(name.equals(""))){
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("forcedloginname", name);
                    editor.putString("loginname", name);
                    editor.commit();
                    ((TextView)findViewById(R.id.profile_name)).setText(name);
                }
            }
        });
        findViewById(R.id.profileshareappurllo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareTextUrl();
            }
        });
        findViewById(R.id.profilelogoutandexit).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                logoutandexit();
                return false;
            }
        });
    }

    private void shareTextUrl() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_SUBJECT, "Let's Converse. Let's Engage. Let's Manthan.");
        share.putExtra(Intent.EXTRA_TEXT, "http://app.manthanapp.com");
        startActivity(Intent.createChooser(share, "Share link!"));
    }

    private void logoutandexit() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sure to EXIT?");
//        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Are you sure you want to logout and Exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        firebaseAuth.signOut();
                        mGoogleSignInClient.signOut();

//                        SharedPreferences.Editor editor = sharedPref.edit();
//                        editor.remove("forcedloginname");
//                        editor.remove("forcedpostalcode");
//                        editor.remove("qshopname");
//                        editor.commit();

                        finishAffinity();                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
