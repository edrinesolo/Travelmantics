package com.example.travelmantics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
	private RecyclerView mEventList;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private DatabaseReference mDatabasePlace_users;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseRecyclerAdapter adapter;
    CoordinatorLayout mnl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                 if (mAuth.getCurrentUser() == null) {
                  Intent mainIntent = new Intent(MainActivity.this,LoginchoiceActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                }else {

                     // checkUserExists();
                }

            }
        };

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Hotels");
        mDatabasePlace_users = FirebaseDatabase.getInstance().getReference().child("Users");
        if (mDatabasePlace_users != null) {
            mDatabasePlace_users.keepSynced(true);
        }
        if (mDatabase != null) {
            mDatabase.keepSynced(true);
        }

        setContentView(R.layout.activity_main);

        mEventList = (RecyclerView) findViewById(R.id.event_list);
        mEventList.setHasFixedSize(true);
        mEventList.setLayoutManager(new LinearLayoutManager(this));
        fetch();
        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // setSupportActionBar(toolbar);
        

        // //handle the f`ab intent
        // FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        // fab.setOnClickListener(new View.OnClickListener() {
        //     @Override
        //     public void onClick(View view) {
        //         startActivity(new Intent(MainActivity.this, PostActivity.class));
        //     }
        // });
    }
    private void fetch(){
    	mAuth.addAuthStateListener(mAuthListener);
        Query q =FirebaseDatabase.getInstance()
                .getReference()
                .child("Hotels");

        FirebaseRecyclerOptions<Hotels> options=
                new FirebaseRecyclerOptions.Builder<Hotels>()
                .setQuery(q, new SnapshotParser<Hotels>() {
                    @NonNull
                    @Override
                    public Hotels parseSnapshot(@NonNull DataSnapshot snapshot) {
                        return new Hotels(snapshot.child("title").getValue().toString(),
                                snapshot.child("price").getValue().toString(),
                                snapshot.child("image").getValue().toString(),
                                snapshot.child("address").getValue().toString());
                    }
                }).build();
        adapter= new FirebaseRecyclerAdapter<Hotels, EventViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull EventViewHolder viewHolder, int i, @NonNull Hotels model) {
                final String event_key = getRef(i).getKey();

                viewHolder.setTitle(model.getTitle());
                viewHolder.setPrice(model.getPrice());
                viewHolder.setAddress(model.getAddress());
                viewHolder.setImage(getApplicationContext(), model.getImage());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(MainActivity.this, event_key, Toast.LENGTH_LONG).show();

                    }
                });

            }

            @NonNull
            @Override
            public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.holder_activity, parent, false);
                return new EventViewHolder(view);
            }
        };
        //pass the adapter to our event view appearance
        mEventList.setAdapter(adapter);

    }


    @Override
    protected void onStart() {
        super.onStart();
        //set up a`authst`atelistener
        
        // 
        adapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        //set up a`authst`atelistener
        
        // 
        adapter.stopListening();

    }

    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.add_event) {
            startActivity(new Intent(MainActivity.this, PostActivity.class));
        }
        if (id == R.id.action_settings) {
            startActivity(new    Intent(MainActivity.this,AppSettings.class));
        }
        if (id == R.id.logout) {
            //startActivity(new Intent(MainActivity.this,LoginActivity.class));
            logout();
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        mAuth.signOut();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public EventViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setAddress(String address) {

            TextView event_address = (TextView) mView.findViewById(R.id.holder_address);
            event_address.setText(address);

        }

        public void setPrice(String price) {

            TextView theprice = (TextView) mView.findViewById(R.id.costholder);
            theprice.setText(price);

        }

        public void setTitle(String title) {
            TextView event_title = (TextView) mView.findViewById(R.id.holder_title);
            event_title.setText(title);

        }

        





        public void setImage(final Context context, final String image) {
           final ImageView event_image = (ImageView) mView.findViewById(R.id.event_image);
           // Picasso.with(context).load(image).into(event_image);
            Picasso.with(context).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(event_image, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(context).load(image).into(event_image);
                }
            });
        }


    }
    private void loadAppPreferences() {
        SharedPreferences sharedprefences= PreferenceManager.getDefaultSharedPreferences(this);
        boolean isBackground_dark=sharedprefences.getBoolean("background_color",false);
        if(isBackground_dark) {
            @SuppressLint("WrongViewCast") CoordinatorLayout main_content = (CoordinatorLayout) findViewById(R.id.main);
            main_content.setBackgroundColor(Color.parseColor("#f4f4f4"));

        }
        boolean isAppBark_purple=sharedprefences.getBoolean("app_bar_background_color",false);
        if(isAppBark_purple){
//            Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
//            toolbar.setBackgroundColor(Color.parseColor("#71127e"));


        }


        String appName=sharedprefences.getString("title","travelmantics");
        setTitle(appName);


    }

    }

