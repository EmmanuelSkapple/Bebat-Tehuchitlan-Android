package com.example.adan.teuchitlan;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.net.MalformedURLException;

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

    /**
     * Free for anyone to use, just say thanks and share :-)
     * @author Blundell
     *
     */
    public static class LoaderImageView extends LinearLayout {

        private static final int COMPLETE = 0;
        private static final int FAILED = 1;

        private Context mContext;
        private Drawable mDrawable;
        private ProgressBar mSpinner;
        private ImageView mImage;

        /**
         * This is used when creating the view in XML
         * To have an image load in XML use the tag 'image="http://developer.android.com/images/dialog_buttons.png"'
         * Replacing the url with your desired image
         * Once you have instantiated the XML view you can call
         * setImageDrawable(url) to change the image
         * @param context
         * @param attrSet
         */
        public LoaderImageView(final Context context, final AttributeSet attrSet) {
            super(context, attrSet);
            final String url = attrSet.getAttributeValue(null, "image");
            if(url != null){
                instantiate(context, url);
            } else {
                instantiate(context, null);
            }
        }

        /**
         * This is used when creating the view programatically
         * Once you have instantiated the view you can call
         * setImageDrawable(url) to change the image
         * @param context the Activity context
         * @param imageUrl the Image URL you wish to load
         */
        public LoaderImageView(final Context context, final String imageUrl) {
            super(context);
            instantiate(context, imageUrl);
        }

        /**
         *  First time loading of the com.example.adan.teuchitlan.Registro.LoaderImageView
         *  Sets up the LayoutParams of the view, you can change these to
         *  get the required effects you want
         */
        private void instantiate(final Context context, final String imageUrl) {
            mContext = context;

            mImage = new ImageView(mContext);
            mImage.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

            mSpinner = new ProgressBar(mContext);
            mSpinner.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

            mSpinner.setIndeterminate(true);

            addView(mSpinner);
            addView(mImage);

            if(imageUrl != null){
                setImageDrawable(imageUrl);
            }
        }

        /**
         * Set's the view's drawable, this uses the internet to retrieve the image
         * don't forget to add the correct permissions to your manifest
         * @param imageUrl the url of the image you wish to load
         */
        public void setImageDrawable(final String imageUrl) {
            mDrawable = null;
            mSpinner.setVisibility(View.VISIBLE);
            mImage.setVisibility(View.GONE);
            new Thread(){
                public void run() {
                    try {
                        mDrawable = getDrawableFromUrl(imageUrl);
                        imageLoadedHandler.sendEmptyMessage(COMPLETE);
                    } catch (MalformedURLException e) {
                        imageLoadedHandler.sendEmptyMessage(FAILED);
                    } catch (IOException e) {
                        imageLoadedHandler.sendEmptyMessage(FAILED);
                    }
                };
            }.start();
        }

        /**
         * Callback that is received once the image has been downloaded
         */
        private final Handler imageLoadedHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case COMPLETE:
                        mImage.setImageDrawable(mDrawable);
                        mImage.setVisibility(View.VISIBLE);
                        mSpinner.setVisibility(View.GONE);
                        break;
                    case FAILED:
                    default:
                        // Could change image here to a 'failed' image
                        // otherwise will just keep on spinning
                        break;
                }
                return true;
            }
        });

        /**
         * Pass in an image url to get a drawable object
         * @return a drawable object
         * @throws IOException
         * @throws MalformedURLException
         */
        private static Drawable getDrawableFromUrl(final String url) throws IOException, MalformedURLException {
            return Drawable.createFromStream(((java.io.InputStream)new java.net.URL(url).getContent()), "name");
        }

    }
}
