package br.com.oficinatablet.message.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import br.com.oficinatablet.R;
import br.com.oficinatablet.model.User;
import br.com.oficinatablet.service.MessageService;

/**
 * A placeholder fragment containing a simple view.
 */
public class MessageInputFragment extends Fragment {

    public static final String ARG_CHAT_KEY = "arg_chat_key";
    public static final String ARG_LOGGED_USER = "arg_logged_user";
    private String chatKey;
    private User loggedUser;

    private MessageService messageService;

    private EditText messageEditText;
    private FloatingActionButton actionSendMessage;

    public MessageInputFragment() {
    }


    public static MessageInputFragment newInstance(String chatKey, User loggedUser) {
        MessageInputFragment fragment = new MessageInputFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CHAT_KEY, chatKey);
        args.putSerializable(ARG_LOGGED_USER, loggedUser);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_input, container, false);

        this.messageService = new MessageService();
        this.chatKey = getArguments().getString(ARG_CHAT_KEY);
        this.loggedUser = (User) getArguments().getSerializable(ARG_LOGGED_USER);

        this.messageEditText = (EditText) view.findViewById(R.id.input_message);
        this.actionSendMessage = (FloatingActionButton) view.findViewById(R.id.action_send_message);
        actionSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        return view;
    }


    private void sendMessage() {
        String message = messageEditText.getText().toString();
        if(TextUtils.isEmpty(message)){
            return;
        }

        actionSendMessage.setEnabled(false);
        messageService.send(chatKey, message, loggedUser, new DatabaseReference.CompletionListener() {

            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(databaseError != null) {
                } else {
                    messageEditText.setText("");
                }

                actionSendMessage.setEnabled(true);
            }
        });
    }
}
