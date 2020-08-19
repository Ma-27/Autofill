package com.example.autofill.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.autofill.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryRecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryRecordFragment extends Fragment {

    public HistoryRecordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history_record, container, false);
    }
}