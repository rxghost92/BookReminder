package com.example.bookreminder.ui.search;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bookreminder.MainActivity;
import com.example.bookreminder.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class PreviewFragment extends AppCompatActivity {


    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef, mCheck, mReff;
    String uid;

    TextView mTitleTv, mDetailTv, mAutore, mType, mPages;
    ImageView mImageIv, mBackBtn;
    ImageView addTot, addIng, addFuture, mPrefer;
    Bitmap bitmap;
    RecyclerView mRec;
    private int countBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().child("Member Preference").child(uid);
        mReff = FirebaseDatabase.getInstance().getReference().child("Books");


        //initialize views
        mTitleTv = findViewById(R.id.titleTv);
        mDetailTv = findViewById(R.id.descriptionTv);
        mAutore = findViewById(R.id.autore);
        mImageIv = findViewById(R.id.imageView);
        mType = findViewById(R.id.nType);
        mPages = findViewById(R.id.nPage);


        addTot = findViewById(R.id.add_to_total);
        addIng = findViewById(R.id.add_reading);
        addFuture = findViewById(R.id.add_future);
        mPrefer = findViewById(R.id.add_prefer1);

        mBackBtn = findViewById(R.id.backBtn);

        mRec = findViewById(R.id.recyclerViewPreview);


        //get data from intent
        final String image = getIntent().getStringExtra("image");
        final String autore = getIntent().getStringExtra("autore");
        final String title = getIntent().getStringExtra("title");
        final String desc = getIntent().getStringExtra("description");
        final String tipo = getIntent().getStringExtra("type");
        final String desc_cv = getIntent().getStringExtra("desc_cv");
        final String number_pages = getIntent().getStringExtra("number_pages");


        //set data to views
        mTitleTv.setText(title);
        mDetailTv.setText(desc);
        mAutore.setText(autore);
        mType.setText("Genere:" + " " + tipo);
        mPages.setText("N° pagine:" + " " + number_pages);
        Glide.with(this).load(image).into(mImageIv);


        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        mCheck = FirebaseDatabase.getInstance().getReference().child("Member Preference").child(uid).child(title);

        mPrefer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mRef.child(title).child("numberpages").setValue(number_pages);
                mRef.child(title).child("status").setValue("none");
                mRef.child(title).child("title").setValue(title);
                mRef.child(title).child("image").setValue(image);
                mRef.child(title).child("autore").setValue(autore);
                mRef.child(title).child("type").setValue(tipo);
                mRef.child(title).child("description").setValue(desc);
                mRef.child(title).child("description_cardview").setValue(desc_cv);


            }
        });

        mCheck.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                mPrefer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mRef.child(title).removeValue();
                        addTot.setVisibility(View.GONE);
                        addFuture.setVisibility(View.GONE);
                        addIng.setVisibility(View.GONE);

                    }
                });

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                mPrefer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mRef.child(title).child("numberpages").setValue(number_pages);
                        mRef.child(title).child("status").setValue("none");
                        mRef.child(title).child("title").setValue(title);
                        mRef.child(title).child("image").setValue(image);
                        mRef.child(title).child("autore").setValue(autore);
                        mRef.child(title).child("type").setValue(tipo);
                        mRef.child(title).child("description").setValue(desc);
                        mRef.child(title).child("description_cardview").setValue(desc_cv);

                        addTot.setVisibility(View.VISIBLE);
                        addFuture.setVisibility(View.VISIBLE);
                        addIng.setVisibility(View.VISIBLE);

                    }
                });

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                addTot.setVisibility(View.GONE);
                addFuture.setVisibility(View.GONE);
                addIng.setVisibility(View.GONE);
            }
        });


        addTot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mRef.child(title).child("title").setValue(title);
                mRef.child(title).child("image").setValue(image);
                mRef.child(title).child("description").setValue(desc);
                mRef.child(title).child("autore").setValue(autore);
                mRef.child(title).child("type").setValue(tipo);
                mRef.child(title).child("description_cardview").setValue(desc_cv);

                mRef.child(title).child("status").setValue("done");
                Toast.makeText(PreviewFragment.this, "Aggiunto alla lista completati", Toast.LENGTH_SHORT).show();


            }
        });

        addIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRef.child(title).child("title").setValue(title);
                mRef.child(title).child("image").setValue(image);
                mRef.child(title).child("description").setValue(desc);
                mRef.child(title).child("autore").setValue(autore);
                mRef.child(title).child("type").setValue(tipo);
                mRef.child(title).child("description_cardview").setValue(desc_cv);

                mRef.child(title).child("status").setValue("reading");
                Toast.makeText(PreviewFragment.this, "Aggiunto alla lista in corso", Toast.LENGTH_SHORT).show();

            }
        });

        addFuture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRef.child(title).child("title").setValue(title);
                mRef.child(title).child("image").setValue(image);
                mRef.child(title).child("description").setValue(desc);
                mRef.child(title).child("autore").setValue(autore);
                mRef.child(title).child("type").setValue(tipo);
                mRef.child(title).child("description_cardview").setValue(desc_cv);

                mRef.child(title).child("status").setValue("future");
                Toast.makeText(PreviewFragment.this, "Aggiunto alla lista da iniziare", Toast.LENGTH_SHORT).show();

            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        String title = getIntent().getStringExtra("title");


        mRef.child(title).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    String username = (String) map.get("status");
                    String check = (String) map.get("title");


                    if (username != null && username.equals("done")) {
                        addTot.setImageResource(R.drawable.ic_library_add_yellow_24dp);
                    } else {
                        addTot.setImageResource(R.drawable.ic_library_add_black_24dp);

                    }

                    if (username != null && username.equals("reading")) {
                        addIng.setImageResource(R.drawable.ic_library_add_yellow_24dp);
                    } else {
                        addIng.setImageResource(R.drawable.ic_library_add_black_24dp);
                    }

                    if (username != null && username.equals("future")) {
                        addFuture.setImageResource(R.drawable.ic_library_add_yellow_24dp);
                    } else {
                        addFuture.setImageResource(R.drawable.ic_library_add_black_24dp);
                    }


                    if (username != null && username.equals("none")) {
                        addFuture.setImageResource(R.drawable.ic_library_add_black_24dp);
                        addIng.setImageResource(R.drawable.ic_library_add_black_24dp);
                        addTot.setImageResource(R.drawable.ic_library_add_black_24dp);

                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mRef.child(title).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mPrefer.setImageResource(R.drawable.added_heart);
                } else {
                    mPrefer.setImageResource(R.drawable.added_not_heart);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mPrefer.setImageResource(R.drawable.added_not_heart);
            }
        });

        final String autore = getIntent().getStringExtra("autore");

        Query typeQuery = mReff.orderByChild("autore").equalTo(autore);

        FirebaseRecyclerAdapter<Model, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Model, ViewHolder>(
                        Model.class,
                        R.layout.preview_type,
                        ViewHolder.class,
                        typeQuery
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, Model model, int position) {
                        viewHolder.setDetails(PreviewFragment.this, model.getTitle(), model.getDescription(), model.getDescription_cardview(), model.getImage(), model.getAutore(), model.getType(), model.getNumberpages());
                    }

                    @Override
                    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                        ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);


                        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                String mTitle = getItem(position).getTitle();
                                String mDesc = getItem(position).getDescription();
                                String mAutore = getItem(position).getAutore();
                                String mType = getItem(position).getType();
                                String mImage = getItem(position).getImage();
                                String mDescCV = getItem(position).getDescription_cardview();
                                String mPages = getItem(position).getNumberpages();


                                Intent intent = new Intent(view.getContext(), PreviewFragment.class);
                                intent.putExtra("title", mTitle);
                                intent.putExtra("description", mDesc);
                                intent.putExtra("image", mImage);
                                intent.putExtra("autore", mAutore);
                                intent.putExtra("type", mType);
                                intent.putExtra("desc_cv", mDescCV);
                                intent.putExtra("number_pages", mPages);
                                startActivity(intent);

                            }


                            @Override
                            public void onItemLongClick(View view, final int position) {


                            }
                        });

                        return viewHolder;
                    }
                };

        mRec.setAdapter(firebaseRecyclerAdapter);


    }

}
