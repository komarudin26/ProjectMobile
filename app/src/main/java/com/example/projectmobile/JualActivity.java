package com.example.projectmobile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class JualActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    Button mButtonChooseImage, mButtonUpload;
    TextView mTextViewShowUploads;
    EditText mEditTextFileName ,  mEditTextDeskripsi, mEditTextHarga, mEditTextLokasi, mEditTextTanggal, mEditTextTelp;
    ImageView mImageView, backimage;
    ProgressBar mProgressBar;


    int y,m,d;
    String DOB;

    Uri mImageUri;

    StorageReference mStorageRef;
    DatabaseReference mDatabaseRef;

    StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jual);

        mButtonChooseImage = findViewById(R.id.button_choose_image);
        mButtonUpload = findViewById(R.id.upload_button);
        mEditTextFileName = findViewById(R.id.nama);
        mEditTextDeskripsi = findViewById(R.id.deskripsi);
        mEditTextHarga = findViewById(R.id.harga);
        mEditTextLokasi = findViewById(R.id.lokasi);
        mEditTextTanggal = findViewById(R.id.tanggal);
        mEditTextTelp = findViewById(R.id.telp);
        mImageView = findViewById(R.id.image_view);
        mProgressBar = findViewById(R.id.progressBar);
        backimage = findViewById(R.id.back);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        final Calendar calendar= Calendar.getInstance();

        backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(JualActivity.this, HomeActivity.class);
                startActivity(Intent);
            }
        });

        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            openFileChoose();
            }
        });

        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()){
                    Toast.makeText(JualActivity.this, "Upload In Progress", Toast.LENGTH_LONG).show();
                }else {
                    uploadFile();
                }
            }
        });


        mEditTextTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                y = calendar.get(Calendar.YEAR);
                m = calendar.get(Calendar.MONTH);
                d = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(JualActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        DOB = i2+"/"+(i1+1)+"/"+i;
                        mEditTextTanggal.setText(DOB);
                    }
                },y,m,d);
                datePickerDialog.show();
            }
        });
    }

    private void openFileChoose(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
        && data != null && data.getData() !=null){
            mImageUri = data.getData();

            Picasso.with(this).load(mImageUri).into(mImageView);
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(){
        if (mImageUri != null){
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
            + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 5000);
                            Toast.makeText(JualActivity.this, "Upload Successfel", Toast.LENGTH_LONG).show();
                            Upload upload = new Upload(mEditTextFileName.getText().toString().trim(), mEditTextDeskripsi.getText().toString().trim(),
                                    taskSnapshot.getUploadSessionUri().toString(), mEditTextHarga.getText().toString().trim(),
                                    mEditTextLokasi.getText().toString().trim(), mEditTextTanggal.getText().toString().trim(), mEditTextTelp.getText().toString().trim());
                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(uploadId).setValue(upload);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(JualActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });
        }else{
            Toast.makeText(this, "No File Selected", Toast.LENGTH_SHORT).show();
        }
    }
}
