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
import iaccess.iaccess.com.entity.EmployeeInfo;
import iaccess.iaccess.com.iaccess.R;

/**
 * Created by TONMOYPC on 2/13/2018.
 */

public class EmployeeAttendanceAdapter extends RecyclerView.Adapter<EmployeeAttendanceAdapter.MyViewHolder> {

    private List<EmployeeInfo> empList;
    private Context context;

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
    public void onBindViewHolder(EmployeeAttendanceAdapter.MyViewHolder holder, int position) {
        EmployeeInfo employee = empList.get(position);

        holder.id.setText(employee.getId());
        holder.name.setText(employee.getName());
        holder.designation.setText(employee.getDesignation());
        holder.role.setText(employee.getRole());

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
        }
    }
}
