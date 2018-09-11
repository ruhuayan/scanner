package com.richyan.android.textscanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LauncherActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUsername;
    private String mEmail;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.topbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_recommend:
                Intent send = new Intent(Intent.ACTION_SEND);
                send.setType("text/plain");
                send.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.sendMsg));
                startActivity(Intent.createChooser(send, getResources().getString(R.string.share)));
                return true;
            case R.id.action_about:

                return true;

            case R.id.action_setting:

                return true;
            case R.id.action_logout:
                mFirebaseAuth.signOut();
                startActivity(new Intent(LauncherActivity.this, EmailPasswordActivity.class));
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null){
            //Not signed in, launch the Sign In Activity
            startActivity(new Intent(this, EmailPasswordActivity.class));
            finish();
            return;
        }else {
            mUsername = mFirebaseUser.getDisplayName();
            mEmail = mFirebaseUser.getEmail();

            //mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            //journalCloudEndPoint =  mDatabase.child("/users/" +
              //      mFirebaseUser.getUid() + "journalentris");
            TextView userTextView = (TextView) findViewById(R.id.welcomeTv);
            userTextView.setText(mEmail);
        }
        findViewById(R.id.stockinBtn).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
                //intent.putExtra("numCust", numCust);
                startActivity(intent);
                finish();
            }
        });
        findViewById(R.id.stockoutBtn).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Not Ready", Toast.LENGTH_LONG).show();
            }
        });
    }
}
