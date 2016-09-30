package br.com.oficinatablet.message.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.oficinatablet.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MessageInputFragment extends Fragment {

    public MessageInputFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message_input, container, false);
    }
}
