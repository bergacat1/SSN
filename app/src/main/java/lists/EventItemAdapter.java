package lists;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.ssn.eps.ssn.R;

import java.util.List;

import model.Event;

/**
 * Created by alber on 17/11/2015.
 */
public class EventItemAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<Event> events;

    public EventItemAdapter(Context context, List<Event> events){
        this.context = context;
        this.events = events;
    }

    @Override
    public int getGroupCount() {
        return this.events.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.events.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.events.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent){
        View rowView = convertView;

        if (convertView == null) {
            // Create a new view into the list.
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.event_list_item, parent, false);
        }

        Event e = this.events.get(groupPosition);


        // Set data into the view.
        TextView tvCompressed = (TextView) rowView.findViewById(R.id.event_compressed_description);
        tvCompressed.setText(e.getSport().getDescription() + " " + e.getEventDate().toString() + " " + e.getZone());

        Event item = this.events.get(groupPosition);

        return rowView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View rowView = convertView;

        if (convertView == null) {
            // Create a new view into the list.
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.event_details, parent, false);
        }

        // Set data into the view.
        TextView tvSport = (TextView) rowView.findViewById(R.id.event_expanded_sport);
        tvSport.setText("EEE" + groupPosition);
        TextView tvTime = (TextView) rowView.findViewById(R.id.event_expanded_time);
        tvTime.setText("EEE" + groupPosition);
        TextView tvMinMaxPlayers = (TextView) rowView.findViewById(R.id.event_expanded_minmaxplayers);
        tvMinMaxPlayers.setText("EEE" + groupPosition);
        TextView tvMaxPrice = (TextView) rowView.findViewById(R.id.event_expanded_maxprice);
        tvMaxPrice.setText("EEE" + groupPosition);
        TextView tvZone = (TextView) rowView.findViewById(R.id.event_expanded_zone);
        tvZone.setText("EEE" + groupPosition);

        return rowView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
