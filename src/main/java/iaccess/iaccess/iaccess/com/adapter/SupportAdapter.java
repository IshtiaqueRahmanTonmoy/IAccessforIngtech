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
import iaccess.iaccess.com.entity.Support;
import iaccess.iaccess.com.iaccess.R;

/**
 * Created by TONMOYPC on 2/13/2018.
 */

public class SupportAdapter extends RecyclerView.Adapter<SupportAdapter.MyViewHolder> {

    private List<Support> suppList;
    private Context context;

    public SupportAdapter(List<Support> suppList){
        this.suppList = suppList;
        Log.d("emplist", String.valueOf(suppList.size()));
        //Toast.makeText(context, ""+empList.size(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public SupportAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_support_history,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SupportAdapter.MyViewHolder holder, int position) {
        Support support = suppList.get(position);
        holder.ids.setText(support.getId());
        holder.name.setText(support.getName());
        holder.fromtodate.setText(support.getFromto());
        holder.location.setText(support.getAddress());
        holder.monthtext.setText(support.getMonth());
        holder.datetext.setText(support.getDate());
    }

    @Override
    public int getItemCount() {
        return suppList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView ids,name,fromtodate,location,monthtext,datetext;

        public MyViewHolder(View view) {
            super(view);
            ids = (TextView) view.findViewById(R.id.id);
            ids.setVisibility(View.INVISIBLE);
            name = (TextView) view.findViewById(R.id.name);
            fromtodate = (TextView) view.findViewById(R.id.fromtotime);
            location = (TextView) view.findViewById(R.id.location);
            monthtext = (TextView) view.findViewById(R.id.month);
            datetext = (TextView) view.findViewById(R.id.date);

        }
    }
}
