package com.auribises.gw2018firebase.viewcontroller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.auribises.gw2018firebase.R;
import com.auribises.gw2018firebase.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterUserActivity extends AppCompatActivity {
    @BindView(R.id.editTextEmail)
    EditText eTxtEmail;

    @BindView(R.id.editTextPassword)
    EditText eTxtPassword;

    User user;

    FirebaseAuth auth;
    FirebaseUser fbUser;

    FirebaseFirestore db;

    ProgressDialog progressDialog;

    ArrayList<User> users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        ButterKnife.bind(this);
        user = new User();

        users = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
    }

    public void clickHandler(View view){
        user.email = eTxtEmail.getText().toString();
        user.password = eTxtPassword.getText().toString();
        registerUser();
    }

    void registerUser(){
        progressDialog.show();
        auth.createUserWithEmailAndPassword(user.email,user.password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            fbUser = task.getResult().getUser();
                            String uid = fbUser.getUid();
                            Toast.makeText(RegisterUserActivity.this,"User Created !!"+uid,Toast.LENGTH_LONG).show();
                            addUserInFirestore();
                        }else{
                            Toast.makeText(RegisterUserActivity.this,"User Not Created !!",Toast.LENGTH_LONG).show();
                        }

                    }
                });

    }

    void addUserInFirestore(){

        //db.collection("users").document(fbUser.getUid()).set(user)
        db.collection("users")
        .add(user)
        .addOnSuccessListener(this, new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                //documentReference.getId() // ID of my newly created user document
                Intent intent = new Intent(RegisterUserActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
                progressDialog.dismiss();
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterUserActivity.this,"User Not Added in Firestore !!"+e,Toast.LENGTH_LONG).show();
                e.printStackTrace();
                Log.e("ERROR",e.toString());
                progressDialog.dismiss();
            }
        });

    }

    void deleteUserFromFirestore(){
        db.collection("users").document(fbUser.getUid()).delete();
        //db.collection("users").document(fbUser.getUid()).delete().addOnCompleteListener()
    }

    void updateUserInFirestore(){
        db.collection("users").document(fbUser.getUid()).set(user);
    }

    void retrieveFromFirestore(){

        // Retrieve all the documents in collection users
        db.collection("users")
                .get()
                .addOnCompleteListener(this, new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isComplete()){

                            for(QueryDocumentSnapshot snapshot : task.getResult()){
                                User u = snapshot.toObject(User.class);
                                Log.d("USER",u.toString());
                                users.add(u);
                            }

                            // Create a custom RecyclerView or ListView or GridView
                        }
                    }
                });

        // Retrieve Single Document
        //DocumentReference docRef = db.collection("users").document(fbUser.getUid());
        //User u1 = docRef.get().getResult().toObject(User.class);



    }


    void signInUser(){
        progressDialog.show();
        auth.signInWithEmailAndPassword(user.email,user.password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            fbUser = task.getResult().getUser();
                            String uid = fbUser.getUid();
                            Toast.makeText(RegisterUserActivity.this,"User Logged In !!"+uid,Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RegisterUserActivity.this,HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(RegisterUserActivity.this,"User Not Logged In !!",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }
}
