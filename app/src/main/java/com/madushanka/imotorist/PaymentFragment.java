package com.madushanka.imotorist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import movile.com.creditcardguide.CreditCardFragment;
import movile.com.creditcardguide.model.IssuerCode;
import movile.com.creditcardguide.model.PaymentMethod;
import movile.com.creditcardguide.model.PurchaseOption;


/**
 * Created by madushanka on 4/15/17.
 */

public class PaymentFragment extends android.support.v4.app.Fragment {

    Double amount;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.payment_fragment, container, false);

        CreditCardFragment inputCardFragment = (CreditCardFragment) getActivity().getFragmentManager().findFragmentById(R.id.frgCreditCard);
        inputCardFragment.setVisibleSaveCard(false);
        inputCardFragment.setVisibleInstallments(false);

        inputCardFragment.setPagesOrder(CreditCardFragment.Step.FLAG, CreditCardFragment.Step.NUMBER,
                CreditCardFragment.Step.EXPIRE_DATE, CreditCardFragment.Step.CVV, CreditCardFragment.Step.NAME);

        inputCardFragment.setListPurchaseOptions(getStubList(), amount);
        return v;

    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    private List<PurchaseOption> getStubList() {
        List<PurchaseOption> list = new ArrayList<>();
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.MASTERCARD, 6));
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.VISACREDITO, 6));
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.AMEX, 6));


        return list;
    }
}
