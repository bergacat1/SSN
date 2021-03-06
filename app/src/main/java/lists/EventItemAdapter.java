package lists;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ssn.eps.model.Result;
import com.ssn.eps.ssn.R;
import com.ssn.eps.ssn.activities.EventDetailActivity;
import com.ssn.eps.model.Event;
import com.ssn.eps.ssn.activities.EventDetailTabedActivity;
import com.ssn.eps.ssn.activities.EventsFragment;
import com.ssn.eps.ssn.activities.MainActivity;
import com.ssn.eps.ssn.wscaller.SoapWSCaller;
import com.ssn.eps.ssn.wscaller.WSCallbackInterface;

import java.util.List;

import General.Globals;

/**
 * Created by alber on 17/11/2015.
 */
public class EventItemAdapter extends BaseExpandableListAdapter {

    private final Context context;
    private List<Event> events;
    private int tab;
    private Button butJoin, butLeave, butView;
    final private SharedPreferences prefs;
    private final EventItemAdapter adapter = this;
    private EventsFragment fragment;

    public EventItemAdapter(Context context, List<Event> events, int tab, EventsFragment fragment){
        this.context = context;
        this.events = events;
        this.tab = tab;
        this.fragment = fragment;

        prefs = PreferenceManager.getDefaultSharedPreferences(context);
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
        rowView.setBackgroundColor(getLightColorByState(e.getState()));

        // Set data into the view.
        TextView tvCompressed = (TextView) rowView.findViewById(R.id.event_compressed_description);
        tvCompressed.setText(e.getSportName() + " " + Globals.sdfNoDay.format(e.getStartDate().getTime()) + " " + e.getCity());

        Event item = this.events.get(groupPosition);

        return rowView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final View rowView;

        if (convertView == null) {
            // Create a new view into the list.
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.event_details, parent, false);
        }
        else
            rowView = convertView;

        final Event e = this.events.get(groupPosition);

        LinearLayout layout = (LinearLayout)rowView.findViewById(R.id.list_item_details);
        int backgroundId = getDrawableBySport(e.getIdSport());
        if(backgroundId != -1)
            layout.setBackground(rowView.getResources().getDrawable(backgroundId));

        // Set data into the view.
        TextView tvState = (TextView) rowView.findViewById(R.id.event_state);
        tvState.setText(context.getString(context.getResources().getIdentifier(e.getState().toString(), "string", context.getPackageName())));
        tvState.setTextColor(getHeavyColorByState(e.getState()));
        TextView tvSport = (TextView) rowView.findViewById(R.id.event_expanded_sport);
        tvSport.setText(rowView.getResources().getText(R.string.sport) + " " + e.getSportName());
        TextView tvTime = (TextView) rowView.findViewById(R.id.event_expanded_time);
        tvTime.setText(rowView.getResources().getText(R.string.date_hour) + " " + Globals.sdf.format(e.getStartDate().getTime()));
        TextView tvLimitTime = (TextView) rowView.findViewById(R.id.event_expanded_time);
        tvLimitTime.setText(rowView.getResources().getText(R.string.date_hour) + " " + Globals.sdf.format(e.getStartDate().getTime()));
        TextView tvMinMaxPlayers = (TextView) rowView.findViewById(R.id.event_expanded_minmaxplayers);
        tvMinMaxPlayers.setText(String.format(rowView.getResources().getText(R.string.players).toString(), e.getActualPlayers(), e.getMinPlayers(), e.getMaxPlayers()));
        TextView tvMaxPrice = (TextView) rowView.findViewById(R.id.event_expanded_maxprice);
        tvMaxPrice.setText(rowView.getResources().getText(R.string.max_price_player) + " " + e.getMaxPrice());
        TextView tvZone = (TextView) rowView.findViewById(R.id.event_expanded_zone);
        tvZone.setText(rowView.getResources().getText(R.string.place) + " " + e.getCity());

        butView = (Button) rowView.findViewById(R.id.button_event_details);
        butView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, EventDetailTabedActivity.class);
                i.putExtra("idevent", e.getIdEvent());
                context.startActivity(i);
            }
        });

        butJoin = (Button) rowView.findViewById(R.id.button_join);
        butJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle(rowView.getResources().getText(R.string.join))
                        .setMessage(rowView.getResources().getText(R.string.join_confirmation))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                int userId = prefs.getInt(Globals.PROPERTY_USER_ID, 0);
                                if (userId > 0)
                                    SoapWSCaller.getInstance().joinEventCall((Activity)context, userId, e.getIdEvent(), new WSCallbackInterface() {
                                        @Override
                                        public void onProcesFinished(Result res) {
                                            if (res.isValid()){
                                                fragment.refreshTab(EventsFragment.TABMYEVENTS);
                                                Toast.makeText(context, context.getText(R.string.joinOk).toString(), Toast.LENGTH_LONG).show();
                                            }else{
                                                new AlertDialog.Builder(context)
                                                        .setTitle(R.string.atencion)
                                                        .setMessage(context.getText(R.string.serverError).toString() + res.getError())
                                                        .setPositiveButton(context.getText(R.string.ok).toString(), null)
                                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                                        .show();
                                            }

                                        }
                                        @Override
                                        public void onProcessError() {
                                            showToast(context.getText(R.string.server_error));
                                        }
                                    });
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .show();
            }
        });
        butLeave = (Button) rowView.findViewById(R.id.button_leave);
        butLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle(rowView.getResources().getText(R.string.leave))
                        .setMessage(rowView.getResources().getText(R.string.leave_confirmation))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                int userId = prefs.getInt(Globals.PROPERTY_USER_ID, 0);
                                if (userId > 0)
                                    SoapWSCaller.getInstance().leaveEventCall((Activity) context, userId, e.getIdEvent(), new WSCallbackInterface() {
                                        @Override
                                        public void onProcesFinished(Result res) {
                                            if (res.isValid()) {
                                                if(tab == EventsFragment.TABMYEVENTS) {
                                                    events.remove(e);
                                                    adapter.notifyDataSetChanged();
                                                    fragment.collapseAllList();
                                                }
                                                if(tab != EventsFragment.TABEVENTS) fragment.refreshTab(EventsFragment.TABEVENTS);
                                                Toast.makeText(context, context.getText(R.string.leaveOk).toString(), Toast.LENGTH_LONG).show();
                                            } else {
                                                new AlertDialog.Builder(context)
                                                        .setTitle(R.string.atencion)
                                                        .setMessage(context.getText(R.string.serverError).toString() + res.getError())
                                                        .setPositiveButton(context.getText(R.string.ok).toString(), null)
                                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                                        .show();
                                            }

                                        }
                                        @Override
                                        public void onProcessError() {
                                            showToast(context.getText(R.string.server_error));
                                        }
                                    });
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .show();
            }
        });
        switch(this.tab)
        {
            case EventsFragment.TABEVENTS:
                if(e.isJoined()) {
                    butJoin.setVisibility(View.GONE);
                    butLeave.setVisibility(View.VISIBLE);
                }else {
                    butLeave.setVisibility(View.GONE);
                    butJoin.setVisibility(View.VISIBLE);
                }
                break;
            case EventsFragment.TABMYEVENTS:
                butJoin.setVisibility(View.GONE);
                break;
            case EventsFragment.TABHISTORYEVENTS:
                butJoin.setVisibility(View.GONE);
                butLeave.setVisibility(View.GONE);
                break;
        }

        return rowView;
    }

    private int getDrawableBySport(int idSport) {
        switch(idSport)
        {
            case 1:
                return R.drawable.football;
            case 2:
                return R.drawable.basket;
            case 3:
                return R.drawable.padel;
            case 4:
                return R.drawable.tennis;
            case 5:
                return R.drawable.volley;
            case 7:
                return R.drawable.futsal;
        }
        return -1;
    }

    private int getLightColorByState(Event.States state){

        switch(state){
            case OPEN:
                return context.getResources().getColor(R.color.light_green);
            case RESERVED:
                return context.getResources().getColor(R.color.light_orange);
            case FINISHED:
                return context.getResources().getColor(R.color.light_blue);
            case CANCELED:
                return context.getResources().getColor(R.color.light_red);
        }
        return 0;
    }

    private int getHeavyColorByState(Event.States state){

        switch(state){
            case OPEN:
                return context.getResources().getColor(R.color.heavy_green);
            case RESERVED:
                return context.getResources().getColor(R.color.heavy_orange);
            case FINISHED:
                return context.getResources().getColor(R.color.heavy_blue);
            case CANCELED:
                return context.getResources().getColor(R.color.heavy_red);
        }
        return 0;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void setEvents(List<Event> events){ this.events = events;}

    private void showToast(CharSequence text){
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}
