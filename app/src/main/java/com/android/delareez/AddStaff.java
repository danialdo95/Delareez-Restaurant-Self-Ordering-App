package com.android.delareez;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.DA.delareez.StaffDA;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.model.delareez.Customer;
import com.model.delareez.Staff;

public class AddStaff extends AppCompatActivity {

    private Button register, clear;
    private EditText email, password ;
    private Spinner type;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    StaffDA Helper;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_staff);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Staff");
        Helper = new StaffDA(mDatabase);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        register = (Button) findViewById(R.id.button4);
        clear = (Button) findViewById(R.id.button5);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        type = (Spinner) findViewById(R.id.spinner4);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Staff staff = dataSnapshot.getValue(Staff.class);
                        if (staff.getStaffType().equals("Manager")) {

                            String mail = email.getText().toString().trim();
                            String pass = password.getText().toString().trim();
                            String Type = type.getSelectedItem().toString();


                            if (TextUtils.isEmpty(mail)) {
                                Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            if (TextUtils.isEmpty(pass)) {
                                Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            if (pass.length() < 6) {
                                Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            progressBar.setVisibility(View.VISIBLE);
                            auth.createUserWithEmailAndPassword(mail, pass)
                                    .addOnCompleteListener(AddStaff.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            Toast.makeText(AddStaff.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                            // If sign in fails, display a message to the user. If sign in succeeds
                                            // the auth state listener will be notified and logic to handle the
                                            // signed in user can be handled in the listener.
                                            if (!task.isSuccessful()) {
                                                Toast.makeText(AddStaff.this, "Authentication failed." + task.getException(),
                                                        Toast.LENGTH_SHORT).show();
                                            } else {

                                                String user_id = auth.getCurrentUser().getUid();

                                                Staff staff = new Staff(user_id,mail,pass, Type, "BqS8rwyLbRewmJ2MHzYfZV2uoIH2");
                                                Helper.CreateStaff(staff, user_id);

                                                startActivity(new Intent(AddStaff.this, MainActivity.class));
                                                finish();
                                            }
                                        }
                                    });
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Only manager can add new staff", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });


        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email.setText("");
                password.setText("");
            }
        });




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home)
            finish();
        return  super.onOptionsItemSelected(item);
    }
}
