package com2027.software.group1.groupproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class GroupsListAdapter extends ArrayAdapter<GroupItem> {
    private ArrayList<GroupItem> groups = null;
    public GroupsListAdapter(Context context, ArrayList<GroupItem> groups)
    {
        super(context, R.layout.groups_list_item_layout ,groups);
        this.groups = groups;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.groups_list_item_layout, parent, false);

        TextView textView = (TextView) row.findViewById(R.id.groups_name);
        textView.setText(groups.get(position).getName());

        textView = (TextView) row.findViewById(R.id.groups_activity_name);
        textView.setText(groups.get(position).getActivity_name());


        return row;
    }
}