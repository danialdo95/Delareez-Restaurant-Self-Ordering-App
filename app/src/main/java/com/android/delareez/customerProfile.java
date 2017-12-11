package com.android.delareez;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.DA.delareez.CustomerDA;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.model.delareez.Customer;

public class customerProfile extends AppCompatActivity {
    private Button btnChangeEmail, btnChangePassword,changeEmail, changePassword;

    private EditText newEmail, newPassword;
    private ProgressBar progressBar;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private TextView mail, pass;
    private DatabaseReference mDatabase;
    CustomerDA Helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String custID = user.getUid().toString();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Customer").child(custID);
        Helper = new CustomerDA(mDatabase);
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(customerProfile.this, LoginActivity.class));
                    finish();
                }
            }
        };


        btnChangeEmail = (Button) findViewById(R.id.change_email_button);
        btnChangePassword = (Button) findViewById(R.id.change_password_button);
        changeEmail = (Button) findViewById(R.id.changeEmail);
        changePassword = (Button) findViewById(R.id.changePass);
        mail = (TextView) findViewById(R.id.mail);
        pass = (TextView) findViewById(R.id.pass);


        newEmail = (EditText) findViewById(R.id.new_email);
        newPassword = (EditText) findViewById(R.id.newPassword);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Customer value = dataSnapshot.getValue(Customer.class);
                mail.setText(value.getCustEmail());
                pass.setText(value.getCustPassword());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        newEmail.setVisibility(View.GONE);
        newPassword.setVisibility(View.GONE);
        changeEmail.setVisibility(View.GONE);
        changePassword.setVisibility(View.GONE);


        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }

        btnChangeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newEmail.setVisibility(View.VISIBLE);
                newPassword.setVisibility(View.GONE);
                changeEmail.setVisibility(View.VISIBLE);
                changePassword.setVisibility(View.GONE);

            }
        });
//Change Email
        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (user != null && !newEmail.getText().toString().trim().equals("")) {
                    user.updateEmail(newEmail.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(customerProfile.this, "Email address is updated. Please sign in with new email id!", Toast.LENGTH_LONG).show();
                                        Customer customer = new Customer();
                                        customer.setCustEmail(newEmail.getText().toString());
                                        customer.setCustID(custID);
                                        customer.setCustPassword(pass.getText().toString());
                                        Helper.updateCustomer(customer);
                                        signOut();
                                        progressBar.setVisibility(View.GONE);
                                    } else {
                                        Toast.makeText(customerProfile.this, "Failed to update email!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                } else if (newEmail.getText().toString().trim().equals("")) {
                    newEmail.setError("Enter email");
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newEmail.setVisibility(View.GONE);
                newPassword.setVisibility(View.VISIBLE);
                changeEmail.setVisibility(View.GONE);
                changePassword.setVisibility(View.VISIBLE);

            }
        });
//Change Password
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (user != null && !newPassword.getText().toString().trim().equals("")) {
                    if (newPassword.getText().toString().trim().length() < 6) {
                        newPassword.setError("Password too short, enter minimum 6 characters");
                        progressBar.setVisibility(View.GONE);
                    } else {
                        user.updatePassword(newPassword.getText().toString().trim())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(customerProfile.this, "Password is updated, sign in with new password!", Toast.LENGTH_SHORT).show();
                                            Customer customer = new Customer();
                                            customer.setCustEmail(mail.getText().toString());
                                            customer.setCustID(custID);
                                            customer.setCustPassword(newPassword.getText().toString());
                                            Helper.updateCustomer(customer);
                                            signOut();
                                            progressBar.setVisibility(View.GONE);
                                        } else {
                                            Toast.makeText(customerProfile.this, "Failed to update password!", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                });
                    }
                } else if (newPassword.getText().toString().trim().equals("")) {
                    newPassword.setError("Enter password");
                    progressBar.setVisibility(View.GONE);
                }
            }
        });



    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    //sign out method
    public void signOut() {
        auth.signOut();
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }
}
