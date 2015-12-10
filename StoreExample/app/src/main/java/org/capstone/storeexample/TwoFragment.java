package org.capstone.storeexample;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class TwoFragment extends Fragment implements OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.twofragment, container, false);

        Button button = (Button) v.findViewById(R.id.bt_ok);
        button.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.bt_ok:
                Toast.makeText(getActivity(), "TwoFragment", Toast.LENGTH_SHORT)
                        .show();
                break;

        }

    }

}
