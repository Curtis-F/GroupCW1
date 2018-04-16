package com2027.software.group1.groupproject;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MembersListAdapter extends ArrayAdapter<MemberItem> {
    private ArrayList<MemberItem> users = null;

    public MembersListAdapter(Context context, ArrayList<MemberItem> users)
    {
        super(context, R.layout.members_list_item_layout ,users);
        this.users = users;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.members_list_item_layout, parent, false);

        TextView textView = (TextView) row.findViewById(R.id.member_name);
        textView.setText(users.get(position).getUsername());

        textView = (TextView) row.findViewById(R.id.member_name);
        textView.setText(users.get(position).getEmail());



        return row;
    }

}