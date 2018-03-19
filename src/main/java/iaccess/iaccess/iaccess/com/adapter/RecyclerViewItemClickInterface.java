package iaccess.iaccess.iaccess.com.adapter;

import android.view.View;

import iaccess.iaccess.com.entity.EmployeeInfo;

/**
 * Created by TONMOYPC on 3/19/2018.
 */

public interface RecyclerViewItemClickInterface {

    void onItemclick(View v, EmployeeInfo viewModel);

}