package org.altbeacon.beaconreference;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;


public class Payment_received_fragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_payment_received, container, false);
        TextView textView=(TextView)view.findViewById(R.id.payment);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Details",MODE_PRIVATE);
        String phone=sharedPreferences.getString("phone","NULL");
        String plate=sharedPreferences.getString("numplate","NULL");

        String result="Payment for \nCar number "+plate+" received from \nPhone <b>"+phone+"</b>";
        textView.setText(result);


        return view;
    }

}