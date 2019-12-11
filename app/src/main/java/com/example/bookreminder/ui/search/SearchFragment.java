package com.example.bookreminder.ui.search;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bookreminder.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";

    RecyclerView mRecyclerView;
    SearchView msearchView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef, mRef3, reff;


    String uid;

    long maxid = 0;

    SharedPreferences sharedpreference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        setHasOptionsMenu(true);


        sharedpreference = PreferenceManager.getDefaultSharedPreferences(this.getContext());

        msearchView = rootView.findViewById(R.id.searchView);
        mRecyclerView = rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("Books");


        mRef3 = FirebaseDatabase.getInstance().getReference();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        msearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                firebaseSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                firebaseSearch(newText);
                return false;
            }
        });


        return rootView;
    }

    private void firebaseSearch(String searchText) {
        Query firebaseSearchQuery = mRef.orderByChild("title_search").startAt(searchText).endAt(searchText + "\uf8ff");

        FirebaseRecyclerAdapter<Model, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Model, ViewHolder>(
                        Model.class,
                        R.layout.preview,
                        ViewHolder.class,
                        firebaseSearchQuery
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, Model model, int position) {
                        viewHolder.setDetails(getActivity(), model.getTitle(), model.getDescription(), model.getDescription_cardview(), model.getImage(), model.getAutore(), model.getType(), model.getNumberpages());
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



                                Intent intent = new Intent(view.getContext(), PreviewFragment.class);
                                intent.putExtra("title", mTitle);
                                intent.putExtra("description", mDesc);
                                intent.putExtra("image", mImage);
                                intent.putExtra("autore", mAutore);
                                intent.putExtra("type", mType);
                                intent.putExtra("desc_cv", mDescCV);

                                startActivity(intent);

                            }


                            @Override
                            public void onItemLongClick(View view, int position) {

                                final String mTitle = getItem(position).getTitle();
                                final String mDesc = getItem(position).getDescription();
                                final String mDescCV = getItem(position).getDescription_cardview();
                                final String mAutore = getItem(position).getAutore();
                                final String mImage = getItem(position).getImage();
                                final String mType = getItem(position).getType();

                                PopupMenu popup = new PopupMenu(getContext(), view);
                                MenuInflater inflater = popup.getMenuInflater();
                                inflater.inflate(R.menu.cardview_menu, popup.getMenu());

                                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    public boolean onMenuItemClick(MenuItem item) {
                                        switch (item.getItemId()) {
                                            case R.id.add_to_complete:

                                                reff.child(mTitle).child("title").setValue(mTitle);
                                                reff.child(mTitle).child("description").setValue(mDesc);
                                                reff.child(mTitle).child("image").setValue(mImage);
                                                reff.child(mTitle).child("autore").setValue(mAutore);
                                                reff.child(mTitle).child("description_cardview").setValue(mDescCV);
                                                reff.child(mTitle).child("type").setValue(mType);


                                                reff.child(mTitle).child("status").setValue("done");
                                                Toast.makeText(getContext(), "Aggiunto alla lista Completati", Toast.LENGTH_SHORT).show();
                                                return true;
                                            case R.id.add_to_prefer:

                                                reff.child(mTitle).child("title").setValue(mTitle);
                                                reff.child(mTitle).child("description").setValue(mDesc);
                                                reff.child(mTitle).child("image").setValue(mImage);
                                                reff.child(mTitle).child("autore").setValue(mAutore);
                                                reff.child(mTitle).child("description_cardview").setValue(mDescCV);
                                                reff.child(mTitle).child("type").setValue(mType);


                                                reff.child(mTitle).child("status").setValue("reading");
                                                Toast.makeText(getContext(), "Aggiunto alla lista In Corso", Toast.LENGTH_SHORT).show();
                                                return true;
                                            case R.id.future:

                                                reff.child(mTitle).child("title").setValue(mTitle);
                                                reff.child(mTitle).child("description").setValue(mDesc);
                                                reff.child(mTitle).child("image").setValue(mImage);
                                                reff.child(mTitle).child("autore").setValue(mAutore);
                                                reff.child(mTitle).child("description_cardview").setValue(mDescCV);
                                                reff.child(mTitle).child("type").setValue(mType);


                                                reff.child(mTitle).child("status").setValue("future");
                                                Toast.makeText(getContext(), "Aggiunto alla lista Da Iniziare", Toast.LENGTH_SHORT).show();
                                                return true;
                                            default:
                                                return false;
                                        }
                                    }

                                });

                                popup.show();


                            }
                        });

                        return viewHolder;
                    }
                };

        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onStart() {

        reff = FirebaseDatabase.getInstance().getReference().child("Member Preference").child(uid);
        Query test = mRef.orderByKey().limitToLast(4);


        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    maxid = (dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        super.onStart();
        FirebaseRecyclerAdapter<Model, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Model, ViewHolder>(
                        Model.class,
                        R.layout.preview,
                        ViewHolder.class,
                        test
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, Model model, int position) {
                        viewHolder.setDetails(getActivity(), model.getTitle(), model.getDescription(), model.getDescription_cardview(), model.getImage(), model.getAutore(), model.getType(), model.getNumberpages());
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

                                final String mTitle = getItem(position).getTitle();
                                final String mDesc = getItem(position).getDescription();
                                final String mDescCV = getItem(position).getDescription_cardview();
                                final String mAutore = getItem(position).getAutore();
                                final String mImage = getItem(position).getImage();
                                final String mType = getItem(position).getType();

                                PopupMenu popup = new PopupMenu(getContext(), view);
                                MenuInflater inflater = popup.getMenuInflater();
                                inflater.inflate(R.menu.cardview_menu, popup.getMenu());

                                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    public boolean onMenuItemClick(MenuItem item) {
                                        switch (item.getItemId()) {
                                            case R.id.add_to_complete:

                                                reff.child(mTitle).child("title").setValue(mTitle);
                                                reff.child(mTitle).child("description").setValue(mDesc);
                                                reff.child(mTitle).child("image").setValue(mImage);
                                                reff.child(mTitle).child("autore").setValue(mAutore);
                                                reff.child(mTitle).child("description_cardview").setValue(mDescCV);
                                                reff.child(mTitle).child("type").setValue(mType);



                                                reff.child(mTitle).child("status").setValue("done");
                                                Toast.makeText(getContext(), "Aggiunto alla lista Completati", Toast.LENGTH_SHORT).show();
                                                return true;
                                            case R.id.add_to_prefer:

                                                reff.child(mTitle).child("title").setValue(mTitle);
                                                reff.child(mTitle).child("description").setValue(mDesc);
                                                reff.child(mTitle).child("image").setValue(mImage);
                                                reff.child(mTitle).child("autore").setValue(mAutore);
                                                reff.child(mTitle).child("description_cardview").setValue(mDescCV);
                                                reff.child(mTitle).child("type").setValue(mType);


                                                reff.child(mTitle).child("status").setValue("reading");
                                                Toast.makeText(getContext(), "Aggiunto alla lista In Corso", Toast.LENGTH_SHORT).show();
                                                return true;
                                            case R.id.future:

                                                reff.child(mTitle).child("title").setValue(mTitle);
                                                reff.child(mTitle).child("description").setValue(mDesc);
                                                reff.child(mTitle).child("image").setValue(mImage);
                                                reff.child(mTitle).child("autore").setValue(mAutore);
                                                reff.child(mTitle).child("description_cardview").setValue(mDescCV);
                                                reff.child(mTitle).child("type").setValue(mType);


                                                reff.child(mTitle).child("status").setValue("future");
                                                Toast.makeText(getContext(), "Aggiunto alla lista Da Iniziare", Toast.LENGTH_SHORT).show();
                                                return true;
                                            default:
                                                return false;
                                        }
                                    }

                                });

                                popup.show();

                            }
                        });

                        return viewHolder;
                    }
                };

        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }


}