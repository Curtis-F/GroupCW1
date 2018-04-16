package com2027.software.group1.groupproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class LogsListAdapter extends ArrayAdapter<LogItem> {
    private ArrayList<LogItem> logs = null;

    public LogsListAdapter(Context context, ArrayList<LogItem> logs)
    {
        super(context, R.layout.logs_list_item_layout ,logs);
        this.logs = logs;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.logs_list_item_layout, parent, false);

        TextView textView = (TextView) row.findViewById(R.id.logs_value);
        String units = "";
        if(getContext().getClass() == GroupsActivity.class)
        {
            units = UnitType.getUnitType(( (GroupsActivity)getContext()).getGroup().getUnit()) != UnitType.NUMBER ? ((GroupsActivity) getContext()).getGroup().getUnit().toString() : "";
        }
        else
        {
            units = UnitType.getUnitType(( (TargetActivity)getContext()).getTarget().getUnit()) != UnitType.NUMBER ? ((TargetActivity) getContext()).getTarget().getUnit().toString() : "";
        }


        textView.setText(Integer.toString(logs.get(position).getValue()) + " "+ units );

        textView = (TextView) row.findViewById(R.id.logs_time);

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(logs.get(position).getTime().getTimestamp());
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        textView.setText(dateFormat.format(c.getTime()));

        return row;
    }
}