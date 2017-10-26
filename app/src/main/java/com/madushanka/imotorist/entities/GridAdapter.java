package com.madushanka.imotorist.entities;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.madushanka.imotorist.HomeActivity;
import com.madushanka.imotorist.R;
import com.madushanka.imotorist.TicketListFragment;

import co.ceryle.fitgridview.FitGridAdapter;

public class GridAdapter extends FitGridAdapter {

    private int[] drawables = {
            R.mipmap.ic_ticket,
            R.mipmap.ic_search_ticket,
            R.mipmap.ic_pay,
            R.mipmap.ic_account,
            R.mipmap.ic_info,
            R.mipmap.ic_comp};


    private String[] lable = {
            "My Tickets",
            "Search",
            "Pay Fine",
            "Account",
            "Information",
            "Complain"};

    private Context context;

    public GridAdapter(Context context) {
        super(context, R.layout.grid_item);
        this.context = context;
    }

    @Override
    public void onBindView(final int position, View itemView) {
        ImageView iv = (ImageView) itemView.findViewById(R.id.grid_item_iv);
        TextView tv = (TextView) itemView.findViewById(R.id.grid_view_text);
        iv.setImageResource(drawables[position]);
        tv.setText(lable[position]);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (position == 0) {

                    Fragment fragment = new TicketListFragment();
                    FragmentTransaction ft = ((HomeActivity) context).getSupportFragmentManager().beginTransaction();
                    ((HomeActivity) context).getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    ft.replace(R.id.main_view, fragment, "all_ticket");
                    ft.addToBackStack("all_ticket");
                    ft.commit();
                }
            }
        });
    }
}