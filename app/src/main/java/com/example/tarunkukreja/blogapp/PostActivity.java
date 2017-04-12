package com.example.tarunkukreja.blogapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class PostActivity extends AppCompatActivity {

    private static final String LOG_TAG = PostActivity.class.getSimpleName() ;

    private ImageButton mImageButton ;
    private EditText mTitle ;
    private EditText mDesc ;
    private Button mPostButton ;
    private static final int GALLERY_REQ_CODE = 1 ;
    private Uri mImageUri = null;
    private Uri resultUri = null ;

    StorageReference mStorageReference ;
    DatabaseReference mDatabaseReference ;
    ProgressDialog mProgressDialog ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mImageButton = (ImageButton)findViewById(R.id.imageButton) ;
        mTitle = (EditText)findViewById(R.id.title) ;
        mDesc = (EditText)findViewById(R.id.desc) ;
        mPostButton = (Button)findViewById(R.id.postButton) ;
        mProgressDialog = new ProgressDialog(this) ;

        mStorageReference = FirebaseStorage.getInstance().getReference() ;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Blog") ;

        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT) ;
                galleryIntent.setType("image/*") ;
                startActivityForResult(galleryIntent, GALLERY_REQ_CODE);
            }
        });
        
        mPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitPost() ;
            }
        });
    }

    private void submitPost() {
        Log.d(LOG_TAG, "submit post Called");
        Blog blogObj = new Blog() ;
        mProgressDialog.setMessage("Posting");
        mProgressDialog.show();
        final String title_val = mTitle.getText().toString() ;
        final String desc_val = mDesc.getText().toString() ;

        if(!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val) && mImageUri !=null){

            blogObj.setTitle(title_val);
            Log.d(LOG_TAG, "Title in blog class set");
            blogObj.setDecription(desc_val);
            Log.d(LOG_TAG, "Desc in blog class set");

           StorageReference filePath = mStorageReference.child("blog_photos").child(resultUri.getLastPathSegment()) ;
           filePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
               @Override
               public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                   Uri downloadUrl = taskSnapshot.getDownloadUrl() ;
                   DatabaseReference newPost = mDatabaseReference.push() ; // pushing inside the "Blog" child under root and also getting an id
                   Blog blog = new Blog(title_val, desc_val, downloadUrl.toString());
                   newPost.setValue(blog);

                   mProgressDialog.dismiss();

                   startActivity(new Intent(PostActivity.this, MainActivity.class));


               }
           }) ;


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQ_CODE && resultCode == RESULT_OK){
            mImageUri = data.getData() ;
            CropImage.activity(mImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(4, 3)
                    .start(this);


        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                 resultUri = result.getUri();
                mImageButton.setImageURI(resultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
