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
import iaccess.iaccess.com.entity.EmployeeInfo;
import iaccess.iaccess.com.iaccess.R;

/**
 * Created by TONMOYPC on 2/13/2018.
 */

public class EmployeeAttendanceAdapter extends RecyclerView.Adapter<EmployeeAttendanceAdapter.MyViewHolder> implements View.OnClickListener  {

    private List<EmployeeInfo> empList;
    private Context context;
    RecyclerViewItemClickInterface listener;

    public EmployeeAttendanceAdapter(List<EmployeeInfo> empList){
        this.empList = empList;
        Log.d("emplist", String.valueOf(empList.size()));
        //Toast.makeText(context, ""+empList.size(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public EmployeeAttendanceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_employee_history,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EmployeeAttendanceAdapter.MyViewHolder holder, final int position) {
        EmployeeInfo employee = empList.get(position);

        holder.id.setText(employee.getId());
        holder.name.setText(employee.getName());
        holder.designation.setText(employee.getDesignation());
        holder.role.setText(employee.getRole());

        holder.id.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final int currentPosition = position;
                final EmployeeInfo infoData = empList.get(position);
                removeItem(infoData);
                return false;
            }
        });

    }


        private void removeItem(EmployeeInfo infoData) {
            int currPosition = empList.indexOf(infoData);
            empList.remove(currPosition);
            notifyItemRemoved(currPosition);

    }

    @Override
    public int getItemCount() {
        return empList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView id,name,designation,role;

        public MyViewHolder(View view) {
            super(view);
            id = (TextView) view.findViewById(R.id.id);
            id.setVisibility(View.INVISIBLE);
            name = (TextView) view.findViewById(R.id.name);
            designation = (TextView) view.findViewById(R.id.designation);
            role = (TextView) view.findViewById(R.id.role);

            /*
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int p=getLayoutPosition();
                    EmployeeInfo notes = empList.get(p);
                    Toast.makeText(context, "Recycle Click" + p +"  ", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
            */
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (listener != null) {
            EmployeeInfo model = (EmployeeInfo) v.getTag();
            listener.onItemclick(v,model);}
    }

    public void setOnItemClickListener(RecyclerViewItemClickInterface listener){

        this.listener=listener;
    }

    public void remove(EmployeeInfo item) {
        int position = empList.indexOf(item);
        empList.remove(position);
        notifyItemRemoved(position);
    }

}
