package com.cap.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private final String TAG = "RegisterActivity";
    private UserEntity userEntity;
    private FirebaseAuth mAuth;
    private DatabaseReference  database;
    private Button buttonSave;
    private EditText editTextUser,editTextPass,editTextName,editTextLastName,editTextAge;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        buttonSave = findViewById(R.id.btn_save);
        editTextUser = findViewById(R.id.edt_user);
        editTextPass = findViewById(R.id.edt_pass);
        editTextName = findViewById(R.id.edt_name);
        editTextLastName = findViewById(R.id.edt_last_name);
        editTextAge = findViewById(R.id.edt_age);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        userEntity = new UserEntity();


        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()) {
                    createFirebase();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void createFirebase(){
        String email = editTextUser.getText().toString();
        String password = editTextPass.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        FirebaseUser user = mAuth.getCurrentUser();
                        userEntity.setNameUser(editTextName.getText().toString());
                        userEntity.setLastNameUser(editTextLastName.getText().toString());
                        userEntity.setAgeUser(Integer.valueOf(editTextAge.getText().toString()));
                        dataUser(user);
                    }
                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this,
                                "Fallo el registro",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    private boolean validateForm(){
        boolean valid = true;
        String email = editTextUser.getText().toString();
        if (TextUtils.isEmpty(email)) {
            editTextUser.setError("Required.");
            valid = false;
        } else {
            editTextUser.setError(null);
        }
        String password = editTextPass.getText().toString();
        if (TextUtils.isEmpty(password)) {
            editTextPass.setError("Required.");
            valid = false;
        } else {
            editTextPass.setError(null);
        }
        return valid;
    }

    private void dataUser(FirebaseUser user) {
        try {
            Log.i(TAG, user.getUid());
            //Log.i(TAG, user.getEmail());
            String uId = user.getUid();
            //database.child(uId).setValue(userEntity);
            database.child("cielo")
                    .child(uId)
                    .child("user")
                    .setValue(userEntity);
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
    }
}
