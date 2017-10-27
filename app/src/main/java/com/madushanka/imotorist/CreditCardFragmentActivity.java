package com.madushanka.imotorist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import movile.com.creditcardguide.ActionOnPayListener;
import movile.com.creditcardguide.CreditCardFragment;
import movile.com.creditcardguide.model.CreditCardPaymentMethod;
import movile.com.creditcardguide.model.IssuerCode;
import movile.com.creditcardguide.model.PaymentMethod;
import movile.com.creditcardguide.model.PurchaseOption;

public class CreditCardFragmentActivity extends AppCompatActivity implements ActionOnPayListener {

    String amount;
    private CreditCardFragment inputCardFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card_fragment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("iMotorist");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inputCardFragment = (CreditCardFragment) getFragmentManager().findFragmentById(R.id.frg_pay);

        inputCardFragment.setVisibleInstallments(false);
        inputCardFragment.setVisibleSaveCard(false);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            amount = getIntent().getExtras().getString("amount");
        }

        inputCardFragment.setPagesOrder(CreditCardFragment.Step.FLAG, CreditCardFragment.Step.NUMBER,
                CreditCardFragment.Step.EXPIRE_DATE, CreditCardFragment.Step.CVV, CreditCardFragment.Step.NAME);

        inputCardFragment.setListPurchaseOptions(getList(), Double.parseDouble(amount));
    }

    private List<PurchaseOption> getList() {
        List<PurchaseOption> list = new ArrayList<>();
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.MASTERCARD, 6));
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.VISACREDITO, 6));
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.AMEX, 6));

        return list;
    }

    @Override
    public void onChangedPage(CreditCardFragment.Step page) {

    }

    @Override
    public void onComplete(CreditCardPaymentMethod purchaseOption, boolean saveCard) {
        Toast.makeText(this, purchaseOption.toString(), Toast.LENGTH_LONG).show();

        if (saveCard) {
            // TODO: Persist the card
            // WARNING: It's not recommended persist the security code CVV, please remove it before persist, calling:
            purchaseOption.setSecurityCode(null);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        inputCardFragment.backPressed();
    }
}
