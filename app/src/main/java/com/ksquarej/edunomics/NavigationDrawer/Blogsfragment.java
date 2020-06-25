package com.ksquarej.edunomics.NavigationDrawer;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ksquarej.edunomics.NavigationDrawer.Adapters.BlogAdapter;
import com.ksquarej.edunomics.R;

import java.util.ArrayList;
import java.util.Calendar;

public class Blogsfragment extends Fragment {
    View v;
    Context mContext;
    FirebaseAuth mAuth;
    DatabaseReference mref;
    RecyclerView mRecyclerView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.blog_fragment, container, false);
        mContext = getContext();
        mAuth=FirebaseAuth.getInstance();
        mref= FirebaseDatabase.getInstance().getReference();

        mRecyclerView = v.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final ProgressBar progressBar=v.findViewById(R.id.progress_circle);
        mref.child("Blogs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<BlogUploadClass> list1=new ArrayList<>();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    BlogUploadClass blg=new BlogUploadClass(ds.child("email").getValue().toString(),ds.child("blogdata").getValue().toString());
                    list1.add(blg);
                }
                progressBar.setVisibility(View.GONE);
                BlogAdapter blgAdap=new BlogAdapter(getContext(),list1);
                mRecyclerView.setAdapter(blgAdap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FloatingActionButton fab = v.findViewById(R.id.add_blogs);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog customView = new Dialog(mContext);
                customView.setContentView(R.layout.writeblogs);
                customView.setTitle("Write Blogs");
                customView.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                customView.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_bottom;
//            customView.getWindow().setGravity(Gravity.BOTTOM);

                // Get a reference for the custom view close button
                ImageButton closeButton = customView.findViewById(R.id.ib_close);
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customView.dismiss();
                    }
                });
                TextView personname = customView.findViewById(R.id.personname);
                Button btn = customView.findViewById(R.id.post);

                if (mAuth != null) {
                    final String personmail = mAuth.getCurrentUser().getEmail();
                    personname.setText(personmail);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EditText blogdata = customView.findViewById(R.id.blog_data);
                            String blogs = blogdata.getText().toString();
                            if (!blogs.equals("")) {
                                BlogUploadClass objblog = new BlogUploadClass(personmail, blogs);
                                mref.child("Blogs").push().setValue(objblog);
                                Toast.makeText(mContext, "Successfully posted", Toast.LENGTH_SHORT).show();
                                customView.dismiss();
                            } else {
                                Toast.makeText(mContext, "Please write something", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                customView.show();

            }
        });
        return v;
    }
}
