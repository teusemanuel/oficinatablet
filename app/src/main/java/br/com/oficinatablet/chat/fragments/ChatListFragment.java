package br.com.oficinatablet.chat.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.oficinatablet.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class ChatListFragment extends Fragment {

    public ChatListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_chat, container, false);
    }
}
