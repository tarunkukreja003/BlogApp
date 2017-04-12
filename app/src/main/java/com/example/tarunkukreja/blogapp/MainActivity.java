package com.example.tarunkukreja.blogapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName() ;
 //   FloatingActionButton fab ;
    RecyclerView recyclerView ;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth ;
    FirebaseAuth.AuthStateListener mAuthListener ;
    LinearLayoutManager linearLayoutManager ;
   // ChildEventListener mChildEventListener ;
  //  BlogAdapter mBlogAdapter ;
    FirebaseRecyclerAdapter<Blog, BlogAdapter> firebaseRecyclerAdapter ;
    ArrayList<Blog> blogArrayList ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate() called");
        setContentView(R.layout.activity_main);

//        fab = (FloatingActionButton)findViewById(R.id.floatingActionButton) ;
        recyclerView = (RecyclerView) findViewById(R.id.blog_recyclerView);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this) ;
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        mAuth = FirebaseAuth.getInstance() ;

        databaseReference = FirebaseDatabase.getInstance().getReference("Blog");
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(mAuth.getCurrentUser() == null){
                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class) ;
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
                    startActivity(loginIntent);
                }
            }
        };
       // mBlogAdapter = new BlogAdapter() ;

//       // fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this, PostActivity.class));
//                finish();
//            }
//        });
//

    }

        @Override
        protected void onStart () {
            super.onStart();
            Log.d(LOG_TAG, "onStart() called");
            mAuth.addAuthStateListener(mAuthListener);
            Log.d(LOG_TAG, "Auth listener attached");
            addChildListen();
            Log.d(LOG_TAG, "Children Added") ;

            firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogAdapter>(Blog.class,
                    R.layout.row,
                    BlogAdapter.class,
                    databaseReference) {
                @Override
                protected void populateViewHolder(BlogAdapter viewHolder, Blog model, int position) {
                      viewHolder.bindBlog(model);

                }
            };

            recyclerView.setAdapter(firebaseRecyclerAdapter);

            }




    private void addChildListen(){
        blogArrayList = new ArrayList<>() ;
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    blogArrayList.add(snapshot.getValue(Blog.class));

                }}

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.addpost){
            Intent intent = new Intent(MainActivity.this, PostActivity.class) ;
            startActivity(intent);
        }

        if(item.getItemId() == R.id.sign_out){
            mAuth.signOut();
        }
        return true;
    }
}
