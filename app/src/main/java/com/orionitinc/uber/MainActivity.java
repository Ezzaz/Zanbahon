package com.orionitinc.uber;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orionitinc.uber.Model.User;
import com.rengwuxian.materialedittext.MaterialEditText;

import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout rootLayout;
    private Button btnSignIn;
    private Button btnRegister;

    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private DatabaseReference users;

    @Override
    protected void attachBaseContext ( Context newBase ) {
        super.attachBaseContext (CalligraphyContextWrapper.wrap (newBase));
    }

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate (savedInstanceState);
        CalligraphyConfig.initDefault (new CalligraphyConfig.Builder ()
                .setDefaultFontPath ("fonts/Arkhip_font.ttf")
                .setFontAttrId (R.attr.fontPath)
                .build ());
        setContentView (R.layout.activity_main);
        //ini firebase auth
        auth=FirebaseAuth.getInstance();
        db=FirebaseDatabase.getInstance ();
        users=db.getReference ("Users");

        btnRegister=(Button)findViewById (R.id.btnRegister);
        btnSignIn=(Button)findViewById (R.id.btnSignIn);
        rootLayout=(RelativeLayout)findViewById (R.id.rootLayout);

        //event
        btnRegister.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick ( View v ) {
                showRegisterDialogue();
            }
        });

        btnSignIn.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick ( View v ) {
                shoLoginDialogue();
            }
        });


    }

    private void shoLoginDialogue (){
        AlertDialog.Builder dialog=new AlertDialog.Builder (this);
        dialog.setTitle ("SIGN IN");
        dialog.setMessage ("Please use your email");

        LayoutInflater inflater=LayoutInflater.from (this);
        View login_layout=inflater.inflate (R.layout.layout_login,null);

        final MaterialEditText editEmail=login_layout.findViewById (R.id.editEmail);
        final MaterialEditText editPassword=login_layout.findViewById (R.id.editPassword);

        dialog.setView (login_layout);

        //set button
        dialog.setPositiveButton ("SIGN IN", new DialogInterface.OnClickListener ( ) {
            @Override
            public void onClick ( DialogInterface dialogInterface, int i ) {
                dialogInterface.dismiss ( );

                //set disable button Sign In if is processing
                btnSignIn.setEnabled (false);
                //set validation
                if (TextUtils.isEmpty (editEmail.getText ( ).toString ( ))) {
                    Snackbar.make (rootLayout, "Please enter email address", Snackbar.LENGTH_SHORT).show ( );
                    return;
                }

                if (TextUtils.isEmpty (editPassword.getText ( ).toString ( ))) {
                    Snackbar.make (rootLayout, "Please enter password", Snackbar.LENGTH_SHORT).show ( );
                    return;
                }

                if (editEmail.getText ( ).toString ( ).length ( ) < 6) {
                    Snackbar.make (rootLayout, "Password too short", Snackbar.LENGTH_SHORT).show ( );
                    return;
                }

                final SpotsDialog waitingDialog=new SpotsDialog (MainActivity.this);
                waitingDialog.show ();

                //start login user
                auth.signInWithEmailAndPassword (editEmail.getText ().toString (),editPassword.getText ().toString ())
                        .addOnSuccessListener (new OnSuccessListener<AuthResult> ( ) {
                            @Override
                            public void onSuccess ( AuthResult authResult ) {
                                waitingDialog.dismiss ();
                                startActivity (new Intent (MainActivity.this,Welcome.class));
                                finish ();
                            }
                        })
                        .addOnFailureListener (new OnFailureListener ( ) {
                            @Override
                            public void onFailure ( @NonNull Exception e ) {
                                waitingDialog.dismiss ();
                                Snackbar.make (rootLayout,"Failed"+ e.getMessage (),Snackbar.LENGTH_SHORT).show ();

                                //active button
                                btnSignIn.setEnabled (true);
                            }
                        });
            }
        });

        dialog.setNegativeButton ("CANCEL", new DialogInterface.OnClickListener ( ) {
            @Override
            public void onClick ( DialogInterface dialogInterface, int i ) {
                dialogInterface.dismiss ();
            }
        });

        dialog.show ();
    }


    private void showRegisterDialogue(){
        AlertDialog.Builder dialog=new AlertDialog.Builder (this);
        dialog.setTitle ("REGISTER");
        dialog.setMessage ("Please use the email address to register");

        LayoutInflater inflater=LayoutInflater.from (this);
        View register_layout=inflater.inflate (R.layout.layout_register,null);

        final MaterialEditText editEmail=register_layout.findViewById (R.id.editEmail);
        final MaterialEditText editPassword=register_layout.findViewById (R.id.editPassword);
        final MaterialEditText editName=register_layout.findViewById (R.id.editName);
        final MaterialEditText editPhone=register_layout.findViewById (R.id.editPhone);

        dialog.setView (register_layout);

        //set button
        dialog.setPositiveButton ("REGISTER", new DialogInterface.OnClickListener ( ) {
            @Override
            public void onClick ( DialogInterface dialogInterface, int i ) {
                dialogInterface.dismiss ();

                //set validation
                if (TextUtils.isEmpty (editEmail.getText ().toString ()))
                {
                    Snackbar.make (rootLayout,"Please enter email address",Snackbar.LENGTH_SHORT).show ();
                    return;
                }

                if (TextUtils.isEmpty (editPhone.getText ().toString ()))
                {
                    Snackbar.make (rootLayout,"Please enter phone number",Snackbar.LENGTH_SHORT).show ();
                    return;
                }

                if (TextUtils.isEmpty (editPassword.getText ().toString ()))
                {
                    Snackbar.make (rootLayout,"Please enter password",Snackbar.LENGTH_SHORT).show ();
                    return;
                }

                if (editEmail.getText ().toString ().length ()<6)
                {
                    Snackbar.make (rootLayout,"Password too short",Snackbar.LENGTH_SHORT).show ();
                    return;
                }

                //register new user
                auth.createUserWithEmailAndPassword (editEmail.getText ().toString (),editPassword.getText ().toString ())
                        .addOnSuccessListener (new OnSuccessListener<AuthResult> ( ) {
                            @Override
                            public void onSuccess ( AuthResult authResult ) {
                                //save user to db
                                User user=new User ();
                                user.setEmail (editEmail.getText ().toString ());
                                user.setName (editName.getText ().toString ());
                                user.setPhone (editPhone.getText ().toString ());
                                user.setPassword (editPassword.getText ().toString ());

                                //use email to key
                                users.child (FirebaseAuth.getInstance ().getCurrentUser ().getUid ())
                                        .setValue (user)
                                        .addOnSuccessListener (new OnSuccessListener<Void> ( ) {
                                            @Override
                                            public void onSuccess ( Void aVoid ) {
                                                Snackbar.make (rootLayout,"Register Successfully",Snackbar.LENGTH_SHORT).show ();
                                            }
                                        })
                                        .addOnFailureListener (new OnFailureListener ( ) {
                                            @Override
                                            public void onFailure ( @NonNull Exception e ) {
                                                Snackbar.make (rootLayout,"Failed" + e.getMessage (),Snackbar.LENGTH_SHORT).show ();
                                            }

                                        });
                            }
                        })
                        .addOnFailureListener (new OnFailureListener ( ) {
                            @Override
                            public void onFailure ( @NonNull Exception e ) {
                                Snackbar.make (rootLayout,"Failed" + e.getMessage (),Snackbar.LENGTH_SHORT).show ();
                            }

                        });
            }
        });
        dialog.setNegativeButton ("CANCEL", new DialogInterface.OnClickListener ( ) {
            @Override
            public void onClick ( DialogInterface dialogInterface, int i ) {
                dialogInterface.dismiss ();
            }
        });

        dialog.show ();
    }
}

