package iaccess.iaccess.iaccess.com.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import iaccess.iaccess.com.entity.Employee;
import iaccess.iaccess.com.iaccess.R;
/**
 * Created by TONMOYPC on 2/13/2018.
 */

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.MyViewHolder> {

    private List<Employee> empList;
    private Context context;

    public EmployeeAdapter(List<Employee> empList){
        this.empList = empList;
        Log.d("emplist", String.valueOf(empList.size()));
        //Toast.makeText(context, ""+empList.size(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public EmployeeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_history,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EmployeeAdapter.MyViewHolder holder, int position) {
        Employee employee = empList.get(position);
        holder.ids.setText(employee.getId());
        holder.name.setText(employee.getName());
        holder.fromtodate.setText(employee.getFromtotime());
        holder.location.setText(employee.getLocation());
        holder.abovetext.setText(employee.getMonth());
        holder.belowtext.setText(employee.getDay());
    }

    @Override
    public int getItemCount() {
        return empList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView ids,name,fromtodate,location,abovetext,belowtext;

        public MyViewHolder(View view) {
            super(view);
            ids = (TextView) view.findViewById(R.id.belowTextsf);
            ids.setVisibility(View.INVISIBLE);
            name = (TextView) view.findViewById(R.id.name);
            fromtodate = (TextView) view.findViewById(R.id.fromtotime);
            location = (TextView) view.findViewById(R.id.location);
            abovetext = (TextView) view.findViewById(R.id.aboveText);
            belowtext = (TextView) view.findViewById(R.id.belowText);

        }
    }
}
