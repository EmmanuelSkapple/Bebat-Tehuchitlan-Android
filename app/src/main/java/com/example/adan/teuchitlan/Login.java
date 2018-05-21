package com.example.adan.teuchitlan;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.google.firebase.auth.AuthCredential;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class Login extends AppCompatActivity  implements View.OnClickListener{
    private static final String EMAIL = "email";
    FirebaseAuth mAuth;
    EditText correo;
    EditText password;
    TextView registro;
    CallbackManager callbackManager;
    private DatabaseReference mDatabase,referenciaUsuario,referenciaUsuario2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //getActionBar().hide();
        callbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();
        //botones
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);//facebook
        loginButton.setReadPermissions("email", "public_profile");
        findViewById(R.id.btnLogin).setOnClickListener(this);

        //editText
        correo = (EditText) findViewById(R.id.txtCorreo);
        password = (EditText) findViewById(R.id.txtPass);

        //Txt
        findViewById(R.id.registrarse).setOnClickListener(this);


        //callbacK login facebook

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("XXX", "facebook:onCancel");

            }

            @Override
            public void onError(FacebookException exception) {
                Log.d("XXX", "facebook: onError", exception);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }



    public void Ingresar(String email,String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("xxx", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent=new Intent(Login.this,SplashScreen.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("xx", "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }




    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

        if(user!=null) {
            String name=user.getDisplayName();
            Toast.makeText(Login.this, "ingreso exitoso:"+name, Toast.LENGTH_SHORT).show();

            Intent intent=new Intent(this,SplashScreen.class);
            startActivity(intent);
            // Toast.makeText(Login.this, "ingreso exitoso:"+email, Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(Login.this, "registrate", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onBackPressed() {

    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("xxx", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(Login.this,"ingreso exitoso",Toast.LENGTH_SHORT).show();
                            //updateUI(user);

                            subirDatos(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("xxx", "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onClick(View v){
        int i=v.getId();
        if(i==R.id.btnLogin){//login


            String Correo = correo.getText().toString();
            String pass = password.getText().toString();
            if(!Correo.isEmpty()&&!pass.isEmpty()){
                Toast.makeText(Login.this, "Ingreso correcto", Toast.LENGTH_SHORT).show();
                Ingresar(Correo, pass);
            }

            else{
                Toast.makeText(Login.this, "Llena los campos correctamente", Toast.LENGTH_SHORT).show();

            }

        }

        else if(i==R.id.registrarse){
            Intent intent=new Intent(this,Registro.class);
            startActivity(intent);
        }
    }

    public void subirDatos(FirebaseUser user){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        referenciaUsuario=mDatabase.child("Teuchitlan/Users/"+user.getUid()+"/DatosPersonales");
        referenciaUsuario.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Log.d("ingreso","el usuario ya existe");
                }
                else {
                    Log.d("login class", " existe el child en bd");
                    usuario u = new usuario(user.getUid(), user.getDisplayName(), "0", user.getEmail(), user.getPhoneNumber());
                    Map<String, Object> person = u.toMap();
                    referenciaUsuario.updateChildren(person);
                }

                Intent intent=new Intent(Login.this,SplashScreen.class);
                startActivity(intent);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

