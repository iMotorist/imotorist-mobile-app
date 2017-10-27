package com.madushanka.imotorist;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.madushanka.imotorist.controllers.TokenManager;
import com.madushanka.imotorist.entities.AllTicketAdapter;
import com.madushanka.imotorist.entities.ApiError;
import com.madushanka.imotorist.entities.Ticket;
import com.madushanka.imotorist.network.ApiService;
import com.madushanka.imotorist.network.RetrofitBuilder;

import net.bohush.geometricprogressview.GeometricProgressView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class TicketListFragment extends Fragment {

    private static final String TAG = "Ticket_All_fragment";
    List<Ticket> list_ticket;
    TokenManager tokenManager;
    Call<List<Ticket>> ticket_call;
    ApiService authService;
    GeometricProgressView progressView;
    SparseBooleanArray mSelectedItemsIds;
    TextView title;
    private Context context;
    private AllTicketAdapter adapter;
    private List<Ticket> arrayList;


    public TicketListFragment() {


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.issue_history_fragment, container, false);
        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        authService = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
        progressView = (GeometricProgressView) v.findViewById(R.id.progressView);
        title = (TextView) v.findViewById(R.id.ticket_list_title);
        getAllTickets(v);

        return v;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onClickEvent(view);
    }

    private void populateRecyclerView(View view, List<Ticket> list) {

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        arrayList = list;
        list_ticket = new ArrayList<Ticket>();
        adapter = new AllTicketAdapter(context, list);
        recyclerView.setAdapter(adapter);


    }

    private void onClickEvent(View view) {


    }


    void getAllTickets(final View v) {


        ticket_call = authService.getAllTickets();

        ticket_call.enqueue(new Callback<List<Ticket>>() {
            @Override
            public void onResponse(Call<List<Ticket>> ticket_call, Response<List<Ticket>> response) {

                Log.w(TAG, "onResponse: " + response);

                if (response.isSuccessful()) {

                    if (response.code() == 200) {

                        // Toast.makeText(getActivity(), response.body().get(0).getDescription(), Toast.LENGTH_LONG).show();
                        populateRecyclerView(v, response.body());
                        progressView.setVisibility(View.GONE);

                        if (response.body().size() == 0) {
                            title.setVisibility(View.VISIBLE);
                        }


                    }

                } else {
                    if (response.code() == 422) {


                        Toast.makeText(getActivity(), "Error ", Toast.LENGTH_LONG).show();

                    }
                    if (response.code() == 401) {
                        ApiError apiError = Utils.convertErrors(response.errorBody());

                        Toast.makeText(getActivity(), apiError.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<List<Ticket>> ticket_call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());

            }
        });


    }


}
