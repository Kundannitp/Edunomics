package com.ksquarej.edunomics.NavigationDrawer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ksquarej.edunomics.R;

public class Initiativesfragment extends Fragment {
    View v;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.initiative_fragment, container, false);

        return v;
    }
}