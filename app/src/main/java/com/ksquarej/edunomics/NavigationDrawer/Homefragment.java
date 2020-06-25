package com.ksquarej.edunomics.NavigationDrawer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ksquarej.edunomics.R;

public class Homefragment extends Fragment {
    View v;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_home, container, false);
        // Get a reference to the AutoCompleteTextView in the layout
        final AutoCompleteTextView searchCareer = v.findViewById(R.id.search_career);
        // Get the string array
        String[] careers = getResources().getStringArray(R.array.career_array);
        // Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, careers);
        searchCareer.setAdapter(adapter);

        // Get a reference to the AutoCompleteTextView in the layout
        final AutoCompleteTextView searchSkills = v.findViewById(R.id.search_skills);
        // Get the string array
        String[] skills = getResources().getStringArray(R.array.skills_array);
        // Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapterSkills =
                new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, skills);
        searchSkills.setAdapter(adapterSkills);

        final Button careerbtn=v.findViewById(R.id.career_btn);
        final Button skillbtn=v.findViewById(R.id.skills_btn);
        careerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchCareer.setText("");
                searchSkills.setText("");
                searchCareer.setVisibility(View.VISIBLE);
                searchSkills.setVisibility(View.GONE);
                careerbtn.setBackgroundResource(R.drawable.active_btn_background);
                skillbtn.setBackgroundResource(R.drawable.unactive_btn_background);
            }
        });

        skillbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchCareer.setText("");
                searchSkills.setText("");
                searchCareer.setVisibility(View.GONE);
                searchSkills.setVisibility(View.VISIBLE);
                careerbtn.setBackgroundResource(R.drawable.unactive_btn_background);
                skillbtn.setBackgroundResource(R.drawable.active_btn_background);
            }
        });
        return v;
    }
}
