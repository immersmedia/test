package co.tau.manthan.login;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import co.tau.manthan.R;
import co.tau.manthan.baseclasses.BUser;
import co.tau.manthan.baseclasses.Constants;
import co.tau.manthan.baseclasses.Debate;
import co.tau.manthan.baseclasses.Debater;
import co.tau.manthan.baseclasses.Siditem;
import co.tau.manthan.dashboard.DashboardActivity;

import static android.provider.AlarmClock.EXTRA_MESSAGE;
import static co.tau.manthan.baseclasses.Constants.FB_RCOLL_SIDLIST;
import static co.tau.manthan.baseclasses.Constants.FB_RCOLL_USERSBYSID;
import static co.tau.manthan.baseclasses.Constants.FB_RCOLL_USERSBYUID;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "SIGNINTAG";
    //    private FusedLocationProviderClient fusedLocationClient;
    private int locationRequestCode = 1010;
    CountryCodePicker ccp;
//    private static Handler mHandler;


    final FirebaseFirestore mFireStoreref = FirebaseFirestore.getInstance();

    private String loginuid;
    private String loginname;
    private String loginpic;
    private String loginphone;
    private String loginemail;
    private String loginsid;

    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    SharedPreferences sharedPref;

    private Debate mydebate = new Debate();
    private Debater myself = new Debater();


    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPref = getSharedPreferences(Constants.sharedprefs, 0);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);
                String code = credential.getSmsCode();
//                if (code != null) {
//                    editTextCode.setText(code);
//                    verifyVerificationCode(code);
//                }

                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                } else if (e instanceof FirebaseTooManyRequestsException) {
                }
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };
//        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        findViewById(R.id.loginphonebuttonsubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phonetext = ((EditText) findViewById(R.id.loginphonenum)).getText().toString();
                ccp = (CountryCodePicker) findViewById(R.id.ccp);
                String cc = ccp.getDefaultCountryCodeWithPlus();
                if (phonetext == "") {

                } else {
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            cc + phonetext,
                            120,
                            TimeUnit.SECONDS,
//                            getParent(),
                            TaskExecutors.MAIN_THREAD,
                            mCallbacks
                    );
                }
            }
        });

        findViewById(R.id.google_signin_button).setOnClickListener((View.OnClickListener) this);
        mAuth = FirebaseAuth.getInstance();
        createNotificationChannel();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser account = mAuth.getCurrentUser();

//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.google_signin_button) {
            signIn();
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void verifyVerificationCode(String otp) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            updateUI(user);


                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {

                            }
                        }
                    }
                });
    }



    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }

                    }
                });
    }


    private void updateUI(FirebaseUser user){
        final SharedPreferences sharedPref = getSharedPreferences("sharedpreferences", 0);
//        hideProgressBar();
        if (user != null) {


            loginuid = user.getUid();
            loginname = user.getDisplayName();
            loginpic = String.valueOf(user.getPhotoUrl());
            loginphone = user.getPhoneNumber();
            loginemail = user.getEmail();

            mFireStoreref.collection(FB_RCOLL_USERSBYUID).document(loginuid).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    myself = document.toObject(Debater.class);

                                    Gson gson = new Gson();
                                    String myselfjson = gson.toJson(myself);
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putString("myself", myselfjson);
                                    editor.commit();
                                    launchDashboardActivity();
                                } else {
                                    Log.d(TAG, "No such document");
                                    Date currentTime = Calendar.getInstance().getTime();
                                    loginsid = makesid(loginname);

                                    BUser user = new BUser(loginuid, loginsid, loginname, loginpic,
                                            loginemail, loginphone, currentTime);

//                                    Map<String,String> sidname = new HashMap<>();
//                                    sidname.put(loginsid,loginname);
                                    Siditem siditem = new Siditem();
                                    siditem.setSid(loginsid);
                                    siditem.setName(loginname);

                                    mFireStoreref.collection(FB_RCOLL_SIDLIST).document(loginsid).set(siditem);
                                    mFireStoreref.collection(FB_RCOLL_USERSBYUID).document(loginuid).set(user);
                                    mFireStoreref.collection(FB_RCOLL_USERSBYSID).document(loginsid).set(user);


                                    Gson gson = new Gson();
                                    String myselfjson = gson.toJson(user);
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putString("myself", myselfjson);
                                    editor.commit();
                                    launchDashboardActivity();
                                }
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());

                            }
                        }
                    });

        } else {
//            mStatusTextView.setText(R.string.signed_out);
//            mDetailTextView.setText(null);

//            findViewById(R.id.google_signin_button).setVisibility(View.VISIBLE);
//            findViewById(R.id.signOutAndDisconnect).setVisibility(View.GONE);
        }
    }


    private String makesid(String loginname){
        String sid = "@";
        Random r = new Random();
        Integer i1 = r.nextInt(9999);
        String[] words = loginname.toLowerCase().split(" ");
        for (String w : words){
            if(w.length()>3){
                sid = sid +w.substring(0,3);
            }else{
                sid = sid +w;
            }
        }
        sid = sid +i1.toString();
        return sid;
    }


    public void launchDashboardActivity() {
        Intent intent = new Intent(this, DashboardActivity.class);
//        EditText editText = (EditText) findViewById(R.id.editText);
//        String message = editText.getText().toString();
        String message = "nomessage";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
