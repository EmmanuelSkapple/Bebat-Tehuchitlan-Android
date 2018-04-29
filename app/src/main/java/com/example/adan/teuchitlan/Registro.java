package com.example.adan.teuchitlan;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Registro extends AppCompatActivity implements View.OnClickListener {

    EditText email;
    EditText pass;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.btnregistro).setOnClickListener(this);
        email=(EditText)findViewById(R.id.emailTxt);
        pass=(EditText)findViewById(R.id.passTxt);

    }

    @Override
    public void onClick(View view) {
        int i=view.getId();
        if(i==R.id.btnregistro){//login
            if(!email.getText().toString().isEmpty()&&!pass.getText().toString().isEmpty()) {
                String Correo = email.getText().toString();
                String password = pass.getText().toString();
                Registrar(Correo, password);
            }
            else{
                Toast.makeText(Registro.this, "Llena los campos correctamente", Toast.LENGTH_SHORT).show();

            }
        }
    }

    public void Registrar(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email,password).

                addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete (@NonNull Task< AuthResult > task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("ccc", "createUserWithEmail:success");
                            Toast.makeText(Registro.this, "Registrado correctamente",Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("ccc", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Registro.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }
}
