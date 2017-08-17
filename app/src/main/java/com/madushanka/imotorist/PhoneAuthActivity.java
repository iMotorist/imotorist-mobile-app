package com.madushanka.imotorist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import net.bohush.geometricprogressview.GeometricProgressView;

import java.util.concurrent.TimeUnit;

public class PhoneAuthActivity extends AppCompatActivity implements
        View.OnClickListener {

    private static final String TAG = "PhoneAuthActivity";

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;

    GeometricProgressView progressView ;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private TextView mStatusText;
    private TextView mRegistergo;

    private EditText mPhoneNumberField;
    private EditText mVerificationField;

    private Button mStartButton;
    private Button mVerifyButton;
    private Button mResendButton;
    private Button mSignOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);
        getSupportActionBar().hide();

        // Restore instance state
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        progressView = (GeometricProgressView) findViewById(R.id.progressView);




        mStatusText = (TextView) findViewById(R.id.result);
        mRegistergo = (TextView) findViewById(R.id.register_go);

      mPhoneNumberField = (EditText) findViewById(R.id.field_phone_number);
     mVerificationField = (EditText) findViewById(R.id.field_verification_code);

        mStartButton = (Button) findViewById(R.id.button_start_verification);
        mVerifyButton = (Button) findViewById(R.id.button_verify_phone);
        mResendButton = (Button) findViewById(R.id.button_resend);
    //    mSignOutButton = (Button) findViewById(R.id.sign_out_button);

        // Assign click listeners
       mStartButton.setOnClickListener(this);
      mVerifyButton.setOnClickListener(this);
      mResendButton.setOnClickListener(this);
      //  mSignOutButton.setOnClickListener(this);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // Initialize phone auth callbacks
        // [START phone_auth_callbacks]
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

                Log.d(TAG, "onVerificationCompleted:" + credential);

                mVerificationInProgress = false;


                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Log.w(TAG, "onVerificationFailed", e);

                mVerificationInProgress = false;


                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    mPhoneNumberField.setError("Invalid phone number.");
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                    // [END_EXCLUDE]
                }

                // Show a message and update the UI
                // [START_EXCLUDE]

                // [END_EXCLUDE]
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {

                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;


                mPhoneNumberField.setVisibility(View.GONE);
                mStartButton.setVisibility(View.GONE);
                mRegistergo.setVisibility(View.GONE);
                progressView.setVisibility(View.GONE);

                mVerificationField.setVisibility(View.VISIBLE);
                mResendButton.setVisibility(View.VISIBLE);
                mVerifyButton.setVisibility(View.VISIBLE);
                mStatusText.setVisibility(View.VISIBLE);


            }
        };

    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();


        if (mVerificationInProgress && validatePhoneNumber()) {
            startPhoneNumberVerification(mPhoneNumberField.getText().toString());
        }

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }


    private void startPhoneNumberVerification(String phoneNumber) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks


        mVerificationInProgress = true;

    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        signInWithPhoneAuthCredential(credential);
    }

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks

        progressView.setVisibility(View.GONE);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();


                            progressView.setVisibility(View.GONE);
                            goRegister();

                        } else {

                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {

                                mVerificationField.setError("Invalid code.");
                                progressView.setVisibility(View.GONE);

                            }

                        }
                    }
                });
    }


    private void signOut() {
        mAuth.signOut();

    }




    private boolean validatePhoneNumber() {
        String phoneNumber = mPhoneNumberField.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            mPhoneNumberField.setError("Invalid phone number.");
            return false;

        }

        return true;
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_start_verification:
                if (!validatePhoneNumber()) {
                    progressView.setVisibility(View.GONE);
                    return;

                }

                startPhoneNumberVerification(mPhoneNumberField.getText().toString());
                progressView.setVisibility(View.VISIBLE);
                break;
           case R.id.button_verify_phone:
                String code = mVerificationField.getText().toString();
               mStatusText.setVisibility(View.GONE);
                if (TextUtils.isEmpty(code)) {
                    mVerificationField.setError("Cannot be empty.");
                    return;
                }

                verifyPhoneNumberWithCode(mVerificationId, code);
                progressView.setVisibility(View.VISIBLE);
                break;
            case R.id.button_resend:
                resendVerificationCode(mPhoneNumberField.getText().toString(), mResendToken);
                progressView.setVisibility(View.VISIBLE);
                mStatusText.setVisibility(View.GONE);
                break;
           /* case R.id.sign_out_button:
                signOut();
                break;*/
        }
    }

    public void goRegister(){
        Intent i  = new Intent(this,RegisterActivity.class);
        startActivity(i);
        finish();
    }
    public void goLoginA(View v){
        Intent i  = new Intent(this,MainActivity.class);
        startActivity(i);
        finishAffinity();

    }
}
