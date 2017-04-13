package com.example.tarunkukreja.blogapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private EditText mName ;
    private EditText mEmail ;
    private EditText mPass ;
    private TextView mSignInText ;
    private Button mLoginButton ;
    private FirebaseAuth mFirebaseAuth ;
    private ProgressDialog mProgressDialog ;
    private DatabaseReference mDatabaseReference ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mName = (EditText)findViewById(R.id.name);
        mEmail= (EditText)findViewById(R.id.email);
        mPass = (EditText)findViewById(R.id.password);
        mLoginButton = (Button)findViewById(R.id.loginButton);
        mSignInText = (TextView)findViewById(R.id.sign_in) ;
        mSignInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(LoginActivity.this, SignInActivity.class) ;
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);
            }
        });
        mFirebaseAuth = FirebaseAuth.getInstance() ;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        mProgressDialog = new ProgressDialog(this) ;
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRegister() ;
            }
        });
    }

    private void startRegister() {

       final String name =  mName.getText().toString();
       final String email = mEmail.getText().toString() ;
       String pass = mPass.getText().toString() ;
        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)){
            mProgressDialog.setMessage("Signing Up");
            mProgressDialog.show();
          mFirebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
              @Override
              public void onComplete(@NonNull Task<AuthResult> task) {
                  if(task.isSuccessful()){
                      String user_id = mFirebaseAuth.getCurrentUser().getUid() ;
                     DatabaseReference current_userRef =  mDatabaseReference.child(user_id);
                     current_userRef.child("Username").setValue(name) ;
                     current_userRef.child("Profile Pic").setValue("default") ;

                      mProgressDialog.dismiss();
                      Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class) ;
                      mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                      startActivity(mainIntent);

                  }
              }
          }) ;
        }
        else{
            Toast.makeText(this, "All the fields are requied", Toast.LENGTH_SHORT).show();
           // Snackbar snack = Snackbar.make()

        }
    }
}
