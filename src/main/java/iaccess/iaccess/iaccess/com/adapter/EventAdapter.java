package iaccess.iaccess.iaccess.com.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import iaccess.iaccess.com.entity.Employee;
import iaccess.iaccess.com.entity.Event;
import iaccess.iaccess.com.iaccess.R;

/**
 * Created by TONMOYPC on 2/13/2018.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private List<Event> eventList;
    private Context context;

    public EventAdapter(List<Event> eventList){
        this.eventList = eventList;
        Log.d("emplist", String.valueOf(eventList.size()));
        //Toast.makeText(context, ""+empList.size(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public EventAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_eventlist,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EventAdapter.MyViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.ids.setText(event.getId());
        holder.names.setText(event.getName());
        holder.titles.setText(event.getTitle());
        holder.dates.setText(event.getTime());
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView ids,names,titles,dates;

        public MyViewHolder(View view) {
            super(view);
            ids = (TextView) view.findViewById(R.id.id);
            ids.setVisibility(View.INVISIBLE);
            names = (TextView) view.findViewById(R.id.name);
            titles = (TextView) view.findViewById(R.id.title);
            dates = (TextView) view.findViewById(R.id.date);
        }
    }
}
