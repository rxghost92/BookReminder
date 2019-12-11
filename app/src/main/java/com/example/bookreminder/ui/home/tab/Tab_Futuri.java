package com.example.bookreminder.ui.home.tab;

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
import com.example.bookreminder.ui.search.Model;
import com.example.bookreminder.ui.search.PreviewFragment;
import com.example.bookreminder.ui.search.ViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class Tab_Futuri extends Fragment {

    SharedPreferences sharedpreference;
    RecyclerView mRecyclerView;

    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef2, mRef3, reff;

    String uid;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_futuri, container, false);

        mRecyclerView = rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        sharedpreference = PreferenceManager.getDefaultSharedPreferences(this.getContext());

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef2 = mFirebaseDatabase.getReference("Books");

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        mRef3 = mFirebaseDatabase.getReference("Member Preference");

        reff = FirebaseDatabase.getInstance().getReference().child("Member Preference").child(uid);


        Query searchQuery = mRef3.child(uid).orderByChild("status").equalTo("future");

        FirebaseRecyclerAdapter<Model, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Model, ViewHolder>(
                        Model.class,
                        R.layout.preview_without_menu,
                        ViewHolder.class,
                        searchQuery
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
                                String mImage = getItem(position).getImage();
                                String mAutore = getItem(position).getAutore();


                                Intent intent = new Intent(view.getContext(), PreviewFragment.class);
                                intent.putExtra("title", mTitle);
                                intent.putExtra("description", mDesc);
                                intent.putExtra("image", mImage);
                                intent.putExtra("autore", mAutore);
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
                                inflater.inflate(R.menu.home_menu, popup.getMenu());

                                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    public boolean onMenuItemClick(MenuItem item) {
                                        switch (item.getItemId()) {
                                            case R.id.move_to_complete:

                                                reff.child(mTitle).child("status").setValue("done");

                                                Toast.makeText(getContext(), "Spostato nella lista Completati", Toast.LENGTH_SHORT).show();
                                                return true;
                                            case R.id.move_to_reading:

                                                reff.child(mTitle).child("status").setValue("reading");
                                                Toast.makeText(getContext(), "Spostato nella lista In Corso", Toast.LENGTH_SHORT).show();
                                                return true;
                                            case R.id.move_to_future:
                                                reff.child(mTitle).child("status").setValue("future");
                                                Toast.makeText(getContext(), "Spostato nella Da Iniziare", Toast.LENGTH_SHORT).show();
                                                return true;
                                            case R.id.remove:
                                                reff.child(mTitle).removeValue();
                                                Toast.makeText(getContext(), "Rimosso dai preferiti", Toast.LENGTH_SHORT).show();
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