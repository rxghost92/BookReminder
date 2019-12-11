package com.example.bookreminder.login;

import android.content.ContentResolver;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import android.webkit.MimeTypeMap;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



import com.example.bookreminder.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class LoginFragment extends Fragment {

    private static final int CAMERA_REQUEST_CODE = 1;
    String uid;
    TextView total, reading, future, done;
    ImageView editText;
    EditText editUsername;
    Toolbar mToolbar;
    CircleImageView logo;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef2, mDatabaseRef, mDatabaseRef2;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private int countBook = 0;
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;

    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    String mEditTextFileName = "test";

    private Uri mImageUri;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        auth = FirebaseAuth.getInstance();

        mStorageRef = FirebaseStorage.getInstance().getReference("Members Logo").child(uid);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Members Logo");
        mDatabaseRef2 = FirebaseDatabase.getInstance().getReference("Members Username");

        mToolbar = rootView.findViewById(R.id.cvToolbar);

        total = rootView.findViewById(R.id.total_book_added);
        reading = rootView.findViewById(R.id.total_book_reading);
        future = rootView.findViewById(R.id.total_future);
        done = rootView.findViewById(R.id.total_completati);

        editText = rootView.findViewById(R.id.write_Username);

        editUsername = rootView.findViewById(R.id.editUsername);
        editUsername.setEnabled(false);

        logo = rootView.findViewById(R.id.logoCircle);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef2 = mFirebaseDatabase.getReference("Member Preference").child(uid);

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }
            }
        };

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUsername.setEnabled(true);


            }
        });

editUsername.setOnEditorActionListener(new TextView.OnEditorActionListener() {
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        boolean handled = false;
        if (actionId == EditorInfo.IME_ACTION_SEND) {
            editUsername.setEnabled(false);

            String text = editUsername.getText().toString();

            mDatabaseRef2.child(uid).child("username").setValue(text);
            handled = true;
        }
        return handled;
    }
});


        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.option:

                        Intent intent = new Intent(getContext(), Option.class);
                        startActivity(intent);

                        return true;

                    case R.id.logout:
                        signOut();
                        Toast.makeText(getContext(), "Logged Out", Toast.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            }
        });

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getContext(), v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.logo_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.add_logo:
                                    Intent intent = new Intent();
                                    intent.setType("image/*");
                                    intent.setAction(Intent.ACTION_GET_CONTENT);
                                    startActivityForResult(intent, CAMERA_REQUEST_CODE);

                                return true;
                            case R.id.do_photo:

                                Toast.makeText(getContext(), "Aggiunto alla lista Da Iniziare 1", Toast.LENGTH_SHORT).show();

                                return true;
                            default:
                                return false;
                        }
                    }

                });

                popup.show();
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK  && data != null && data.getData() != null) {

            mImageUri = data.getData();

            uploadFile();

        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (mImageUri != null) {
            {
                mStorageRef.putFile(mImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return mStorageRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            Log.e(TAG, "then: " + downloadUri.toString());


                            Upload upload = new Upload(mEditTextFileName.getBytes().toString().trim(),
                                    downloadUri.toString());

                            mDatabaseRef.child(uid).setValue(upload);
                        } else {
                            Toast.makeText(getContext(), "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle saveIstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(saveIstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        auth.addAuthStateListener(authListener);


        mDatabaseRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    String name = (String) map.get("imageUrl");


                    Picasso.get().load(name).into(logo);


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabaseRef2.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    String username = (String) map.get("username");

                    editUsername.setText(username);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mRef2.orderByChild(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    countBook = (int) dataSnapshot.getChildrenCount();
                    total.setText(Integer.toString(countBook));
                } else {
                    total.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mRef2.orderByChild("status").equalTo("future").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    countBook = (int) dataSnapshot.getChildrenCount();
                    future.setText(Integer.toString(countBook));
                } else {
                    future.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mRef2.orderByChild("status").equalTo("reading").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    countBook = (int) dataSnapshot.getChildrenCount();
                    reading.setText(Integer.toString(countBook));
                } else {
                    reading.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mRef2.orderByChild("status").equalTo("done").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    countBook = (int) dataSnapshot.getChildrenCount();
                    done.setText(Integer.toString(countBook));
                } else {
                    done.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //sign out method
    public void signOut() {
        auth.signOut();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

}
