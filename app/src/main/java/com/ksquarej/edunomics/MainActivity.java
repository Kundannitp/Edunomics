package com.ksquarej.edunomics;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;


import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.ksquarej.edunomics.NavigationDrawer.Blogsfragment;
import com.ksquarej.edunomics.NavigationDrawer.Botfragment;
import com.ksquarej.edunomics.NavigationDrawer.Homefragment;
import com.ksquarej.edunomics.NavigationDrawer.ImpactFragment;
import com.ksquarej.edunomics.NavigationDrawer.Initiativesfragment;
import com.ksquarej.edunomics.NavigationDrawer.Opportunitesfragment;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    NavigationView navigationView;
    DrawerLayout drawer ;
    FirebaseAuth mAuth;

    private ActionBarDrawerToggle mToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        mToggle=new ActionBarDrawerToggle(this,drawer,R.string.open,R.string.close);

        Intent intent = getIntent();
        drawer.addDrawerListener(mToggle);
        mToggle.syncState();
        Toolbar mytool=findViewById(R.id.home_toolbar);
        setSupportActionBar(mytool);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(getDrawable(R.drawable.logo));
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_navigation_drawer);

        setUpDrawerContent(navigationView);
        navigationView.getMenu().getItem(0).setChecked(true);

        mAuth=FirebaseAuth.getInstance();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                displaySelectedScreen(menuItem.getItemId());

                return true;
            }
        });
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.nav_host_fragment, new Homefragment());
        tx.commit();
    }


    private void setUpDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                displaySelectedScreen(menuItem.getItemId());
                return true;
            }
        });
    }

    protected void onStart() {

        super.onStart();
        if(mAuth.getCurrentUser()!=null){
            navigationView.getMenu().getItem(4).getSubMenu().getItem(0).setTitle("Logout");
        }else{
            navigationView.getMenu().getItem(4).getSubMenu().getItem(0).setTitle("Login");

        }

    }

    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;
        int[] b=new int[]{1,2,3};

        //initializing the fragment object which is selected
        switch (itemId) {

            case R.id.nav_home:
                b[0]=1;
                b[1]=2;
                b[2]=3;
                navColor(0,b);
                fragment = new Homefragment();
                break;
            case R.id.nav_impact:
                b[0]=0;
                b[1]=2;
                b[2]=3;
                navColor(1,b);
                fragment=new ImpactFragment();
                break;
            case R.id.nav_initiative:
                b[0]=1;
                b[1]=2;
                b[2]=0;
                navColor(3,b);
                fragment=new Initiativesfragment();
                break;
            case R.id.nav_opportunites:
                b[0]=1;
                b[1]=0;
                b[2]=3;
                navColor(2,b);
                fragment=new Opportunitesfragment();
                break;
            case R.id.nav_blogs:
                if(mAuth.getCurrentUser()!=null) {
                    discolor();
//                    navigationView.getMenu().getItem(4).getSubMenu().getItem(1).setChecked(true);
                    fragment = new Blogsfragment();
                }else{
                    openLoginDialog();
                }
                break;
            case R.id.nav_login:
                if(mAuth.getCurrentUser()!=null){
                    showLogoutDialog();
                }else{
                    openLoginDialog();
                }
                break;
            case R.id.nav_bot:
                if(mAuth.getCurrentUser()!=null) {
//                    navigationView.getMenu().getItem(4).getSubMenu().getItem(2).setChecked(true);
                    discolor();
                    fragment = new Botfragment();
                }else{
                    openLoginDialog();
                }
                break;
        }


        //replacing the fragment
        if (fragment != null) {
            navColor(0,b);
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.nav_host_fragment, fragment)
                    .commit();
        }
        drawer.closeDrawer(GravityCompat.START);
    }

    void discolor(){
        for(int i=0;i<4;i++){
            navigationView.getMenu().getItem(i).setChecked(false);
        }
    }

    void discolorchild(){
        for(int i=0;i<3;i++){
            navigationView.getMenu().getItem(4).getSubMenu().getItem(i).setChecked(false);

        }
    }

    void openLoginDialog(){
        final Dialog customView = new Dialog(MainActivity.this);
        customView.setContentView(R.layout.login_dialog);
        customView.setTitle("Login");
        customView.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customView.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_bottom;
        final EditText loginemail=customView.findViewById(R.id.login_email);
        final EditText loginpsw=customView.findViewById(R.id.login_psw);
        Button loginbtn=customView.findViewById(R.id.login_btn);
        TextView signupreq=customView.findViewById(R.id.signup_now);
        signupreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customView.dismiss();
                openSignupDialog();
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=loginemail.getText().toString();
                String psw=loginpsw.getText().toString();
                try {
                    mAuth.signInWithEmailAndPassword(email, psw)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Toast.makeText(MainActivity.this, "Successfully Loged In", Toast.LENGTH_SHORT).show();
                                        if(mAuth.getCurrentUser()!=null){
                                            navigationView.getMenu().getItem(4).getSubMenu().getItem(0).setTitle("Logout");
                                        }else{
                                            navigationView.getMenu().getItem(4).getSubMenu().getItem(0).setTitle("Login");

                                        }
                                        customView.dismiss();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                    }

                                    // ...
                                }
                            });
                }catch (Exception e){}
                customView.dismiss();
            }
        });

        customView.show();
    }

    void openSignupDialog(){
        final Dialog customView = new Dialog(MainActivity.this);
        customView.setContentView(R.layout.signup_dialog);
        customView.setTitle("Login");
        customView.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customView.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_bottom;
        final EditText loginemail=customView.findViewById(R.id.signup_email);
        final EditText loginpsw=customView.findViewById(R.id.signup_psw);
        Button loginbtn=customView.findViewById(R.id.signup_btn);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=loginemail.getText().toString();
                String psw=loginpsw.getText().toString();

                mAuth.createUserWithEmailAndPassword(email, psw)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(MainActivity.this, "Successfully signed in", Toast.LENGTH_SHORT).show();
                                    if(mAuth.getCurrentUser()!=null){
                                        navigationView.getMenu().getItem(4).getSubMenu().getItem(0).setTitle("Logout");
                                    }else{
                                        navigationView.getMenu().getItem(4).getSubMenu().getItem(0).setTitle("Login");

                                    }
                                    customView.dismiss();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(MainActivity.this, "Sign up Failed", Toast.LENGTH_SHORT).show();
                                }

                                // ...
                            }
                        });
            }
        });

        customView.show();
    }

    void navColor(int a,int b[]){
        for(int i=0;i<b.length;i++){
            navigationView.getMenu().getItem(i).setChecked(false);
        }
        navigationView.getMenu().getItem(a).setChecked(true);
    }

    private void showLogoutDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Sure to logout?");
        builder.setCancelable(false);

        builder.setPositiveButton("LOGOUT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mAuth.signOut();
                if(mAuth.getCurrentUser()!=null){
                    navigationView.getMenu().getItem(4).getSubMenu().getItem(0).setTitle("Logout");
                }else{
                    navigationView.getMenu().getItem(4).getSubMenu().getItem(0).setTitle("Login");

                }
                dialog.cancel();
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });

        builder.show();

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
