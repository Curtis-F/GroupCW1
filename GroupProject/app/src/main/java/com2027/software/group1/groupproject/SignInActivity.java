package com2027.software.group1.groupproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {

    Button mButtonSignIn = null;
    GoogleSignInClient mGoogleSignInClient = null;
    public static final int RC_SIGN_IN = 1001;
    private FirebaseAuth mAuth;
    Intent mIntent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        mButtonSignIn = (Button) findViewById(R.id.button_sign_in);

        mAuth = FirebaseAuth.getInstance();
        mIntent = getIntent();
        if(!mIntent.hasExtra("logout")) {
            //GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                SignIn(currentUser);
            }
        }
        else
        {
            mGoogleSignInClient.signOut();
        }

        mButtonSignIn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                Intent signInIntent  = mGoogleSignInClient.getSignInIntent();

                startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        });

    }
    private void SignIn (FirebaseUser user)
    {
        if(user!=null) {
            if(!mIntent.hasExtra("logout")) {
                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            }
            final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            final User newUser = new User(user.getDisplayName(), user.getEmail());
            DatabaseReference ref = mDatabase.child("/users").child(user.getUid());
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.exists())
                    {
                        String key = mDatabase.child("userEmails").push().getKey();
                        mDatabase.child("userEmails").child(key).setValue(newUser.getEmail());
                        mDatabase.child("users").child(dataSnapshot.getKey()).setValue(newUser);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Error signing in, please try again." , Toast.LENGTH_SHORT).show();
        }
    }

    private void FirebaseAuthWithGoogle(GoogleSignInAccount account)
    {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            SignIn(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("error", "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication Failed." , Toast.LENGTH_SHORT).show();
                            SignIn(null);
                        }

                        // ...
                    }
                });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            FirebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("error", "signInResult:failed code=" + e.getStatusCode());
            SignIn(null);
        }
    }
}
