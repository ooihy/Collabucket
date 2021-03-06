package com.example.ooikk.collabucket;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OwnProjectDetails extends AppCompatActivity {

    private TextView mTextViewTitle;
    private TextView mTextViewSummary;
    private TextView mTextViewResponsibilities;
    private TextView mTextViewQualifications;
    private TextView mTextViewPay;
    private TextView mTextViewDuration;
    private TextView mTextViewDateOfListing;
    private TextView mTextViewProjectStatus;

    private Button mBtnComplete;

    private DatabaseReference mDatabase;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_project_details);

        //Passed from the previous page -> Users_ProjectList
        final String project_title = getIntent().getStringExtra("Title");
        final String project_owner_id = getIntent().getStringExtra("Owner");

        mTextViewTitle = findViewById(R.id.titleHeader);
        mTextViewSummary = findViewById(R.id.textViewProjectSummary);
        mTextViewResponsibilities = findViewById(R.id.textViewResponsibilities);
        mTextViewQualifications = findViewById(R.id.textViewQualifications);
        mTextViewPay = findViewById(R.id.textViewPay);
        mTextViewDuration = findViewById(R.id.textViewDuration);
        mTextViewDateOfListing = findViewById(R.id.textViewDateOfListing);
        mTextViewProjectStatus = findViewById(R.id.textViewProjectStatus);
        mBtnComplete = findViewById(R.id.buttonComplete);


        //Retrieve firebase database for the project from the project owner's profile
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Current user data -> Reference to Firebase database root
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String user_id = mCurrentUser.getUid();


        // Below code is to retrieve the database information and display project's details

        mDatabase.child("Users")
                .child(project_owner_id)
                .child("Projects")
                .child(project_title).addValueEventListener(new ValueEventListener() {

               /*
        mDatabase.child("ProjectsListed")
                .child(project_owner_id)
                .child(project_title).addValueEventListener(new ValueEventListener() {
                */
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mTextViewTitle.setText(dataSnapshot.child("Title").getValue().toString());
                mTextViewSummary.setText(dataSnapshot.child("ProjectSummary").getValue().toString());
                mTextViewQualifications.setText(dataSnapshot.child("ProjectQualifications").getValue().toString());
                mTextViewResponsibilities.setText(dataSnapshot.child("ProjectResponsibilities").getValue().toString());
                mTextViewPay.setText(dataSnapshot.child("Pay").getValue().toString());
                mTextViewDuration.setText(dataSnapshot.child("Duration").getValue().toString());
                mTextViewDateOfListing.setText(dataSnapshot.child("DateOfListing").getValue().toString());
                mTextViewProjectStatus.setText(dataSnapshot.child("ProjectStatus").getValue().toString());
                final String status = dataSnapshot.child("ProjectStatus").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //when user view project owner's profile, bring user to the profile
        mBtnComplete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                mDatabase.child("Users")
                        .child(project_owner_id)
                        .child("Projects")
                        .child(project_title).addValueEventListener(new ValueEventListener() {


                /*++++++++++++++++++++++++++
                mDatabase.child("ProjectsListed")
                        .child(project_owner_id)
                        .child(project_title).addValueEventListener(new ValueEventListener() {
                        +++++++++++++++++++++++++++++++++++++++++++++*/
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshotCheck) {
                        if (dataSnapshotCheck.child("ProjectStatus").getValue().toString().equals("Completed")) {
                            Toast.makeText(OwnProjectDetails.this, "Project is already registered as completed.", Toast.LENGTH_LONG).show();
                        } else {
                            Intent intent = new Intent(OwnProjectDetails.this, FileProjectCompleted.class);
                            intent.putExtra("project_title", project_title);
                            startActivity(intent);
                            // End the activity
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

        });


    }
}
