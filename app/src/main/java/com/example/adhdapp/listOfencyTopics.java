package com.example.adhdapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.ListFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A fragment representing a list of Items.
 */
public class listOfencyTopics extends ListFragment {

    private List<String> list = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private Listener listener;

    static interface Listener {
        void itemClicked(long id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(android.R.layout.list_content, container, false);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Headers");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String value = child.getValue(String.class);
                    if (value != null) {
                        list.add(value);
                    }
                }

                // Sort the list with headers without numbers first and then by numeric value
                Collections.sort(list, new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        // Check if the string has a number prefix
                        boolean hasNumber1 = extractNumber(o1) != null;
                        boolean hasNumber2 = extractNumber(o2) != null;

                        if (!hasNumber1 && hasNumber2) {
                            return -1; // o1 should come before o2
                        }
                        if (hasNumber1 && !hasNumber2) {
                            return 1; // o1 should come after o2
                        }
                        // Both have numbers or neither have numbers
                        Integer num1 = hasNumber1 ? extractNumber(o1) : Integer.MAX_VALUE;
                        Integer num2 = hasNumber2 ? extractNumber(o2) : Integer.MAX_VALUE;
                        return num1.compareTo(num2);
                    }

                    private Integer extractNumber(String s) {
                        Pattern pattern = Pattern.compile("^(\\d+)");
                        Matcher matcher = pattern.matcher(s);
                        return matcher.find() ? Integer.parseInt(matcher.group(1)) : null;
                    }
                });

                if (adapter == null) {
                    adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, list);
                    setListAdapter(adapter);
                } else {
                    adapter.clear();
                    adapter.addAll(list);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            listener = (Listener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement Listener");
        }
    }

    @Override
    public void onListItemClick(ListView lv, View view, int position, long id) {
        if (listener != null) {
            listener.itemClicked(id);
        }
    }
}
