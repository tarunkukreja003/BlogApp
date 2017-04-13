package com.example.tarunkukreja.blogapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {

    private static final String LOG_TAG = SignInActivity.class.getSimpleName() ;

   private EditText mEmail ;
    private EditText mPass ;
    private Button mSigninButton ;
   private DatabaseReference mDatabaseReference ;
    private FirebaseAuth mFirebaseAuth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users") ;
        mFirebaseAuth  = FirebaseAuth.getInstance() ;
        mEmail = (EditText)findViewById(R.id.signin_email) ;
        mPass = (EditText)findViewById(R.id.signin_pass) ;
        mSigninButton = (Button)findViewById(R.id.signiIn_button) ;

        mSigninButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLogin() ;
            }
        });

    }

    private void checkLogin() {

        String email = mEmail.getText().toString();
        String pass = mPass.getText().toString();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)) {
            mFirebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d(LOG_TAG, "Email & Pass complete, now check user existence");
                        checkUserExist();
                    }
                }
            });
        }else{
            Toast.makeText(this, "All the fields are required", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkUserExist() {
       final String user_id = mFirebaseAuth.getCurrentUser().getUid() ;

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               if(dataSnapshot.hasChild(user_id)) {
                   Intent mainIntent = new Intent(SignInActivity.this, MainActivity.class) ;
                   mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                   startActivity(mainIntent);
               }else{
                   Toast.makeText(SignInActivity.this, "Create an account", Toast.LENGTH_SHORT).show();
               }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                 Log.d(LOG_TAG, "onCancelled called on Value Event Listener, Database error");
            }
        });
    }
}
