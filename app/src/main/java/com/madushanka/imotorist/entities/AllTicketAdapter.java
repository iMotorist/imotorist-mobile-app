package com.madushanka.imotorist.entities;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.madushanka.imotorist.R;
import com.madushanka.imotorist.TicketFragment;

import java.util.List;


public class AllTicketAdapter extends RecyclerView.Adapter<AllTicketAdapter.RecyclerViewHolder> {

    private List<Ticket> arrayList;
    private Context context;
    private Activity a;
    private SparseBooleanArray mSelectedItemsIds;

    public AllTicketAdapter(Context context, List<Ticket> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(final AllTicketAdapter.RecyclerViewHolder holder, final int i) {
        holder.ticket_no.setText(arrayList.get(i).getTicket_no());

        if (arrayList.get(i).isPaid()) {
            holder.ticket_status.setText("PAID");
            holder.ticket_status.setTextColor(context.getResources().getColor(R.color.bg));
        } else {
            holder.ticket_status.setText("UNPAID");
            holder.ticket_status.setTextColor(context.getResources().getColor(R.color.fbutton_color_alizarin));
        }

        holder.date_time.setText(arrayList.get(i).getOffence_datetime());

        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TicketFragment fragment = new TicketFragment();
                fragment.setT(arrayList.get(i));
                FragmentTransaction ft = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                ft.add(R.id.main_view, fragment, "ticket_view");
                ft.addToBackStack("ticket_view");
                ft.commit();
            }
        });
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_ticket_row_layout, viewGroup, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    /**
     * Remove all checkbox Selection
     **/
    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    /**
     * Check the Checkbox if not checked
     **/
    public void checkCheckBox(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, true);
        else
            mSelectedItemsIds.delete(position);

        notifyDataSetChanged();
    }

    /**
     * Return the selected Checkbox IDs
     **/
    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView ticket_no;
        private TextView ticket_status;
        private TextView date_time;
        private RelativeLayout rl;


        RecyclerViewHolder(View view) {
            super(view);
            ticket_no = (TextView) view.findViewById(R.id.ticket_no);
            ticket_status = (TextView) view.findViewById(R.id.ticket_status);
            date_time = (TextView) view.findViewById(R.id.date_time);
            rl = (RelativeLayout) view.findViewById(R.id.ticket_list_item);

        }

    }


}