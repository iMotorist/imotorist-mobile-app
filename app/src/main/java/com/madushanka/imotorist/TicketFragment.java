package com.madushanka.imotorist;


import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import com.madushanka.imotorist.controllers.TokenManager;
import com.madushanka.imotorist.entities.Ticket;
import com.madushanka.imotorist.network.ApiService;
import com.madushanka.imotorist.network.RetrofitBuilder;

import net.bohush.geometricprogressview.GeometricProgressView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import retrofit2.Call;

import static android.content.Context.MODE_PRIVATE;


public class TicketFragment extends Fragment {

    private static final String TAG = "_Ticket";
    Button pay_ticket;
    GeometricProgressView progressView;
    Call<Ticket> ticket_call;
    ApiService authService;
    TokenManager tokenManager;
    WebView wv;
    TicketCreater tc;
    Ticket t;

    public static String StreamToString(InputStream in) throws IOException {
        if (in == null) {
            return "";
        }
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
        }
        return writer.toString();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        tc = new TicketCreater();
        //  t = DashBoardActivity.m.getTicket();

        View v = inflater.inflate(R.layout.fragment_ticket, container, false);

        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        authService = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);

        progressView = (GeometricProgressView) v.findViewById(R.id.progressView);

        wv = (WebView) v.findViewById(R.id.webView);

        WebSettings webSetting = wv.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setDisplayZoomControls(true);
        webSetting.setDomStorageEnabled(true);

        AssetManager mgr = getActivity().getBaseContext().getAssets();

        wv.loadDataWithBaseURL(null, tc.getTicketData(t), "text/html", "utf-8", null);

        pay_ticket = (Button) v.findViewById(R.id.ticket_print);

        if (t.isPaid()) {
            pay_ticket.setVisibility(View.GONE);
        }


        pay_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        // DashBoardActivity.m = new Motorist();
        return v;

    }

    public Ticket getT() {
        return t;
    }

    public void setT(Ticket t) {
        this.t = t;
    }

}