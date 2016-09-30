package br.com.oficinatablet.message.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

import br.com.oficinatablet.R;
import br.com.oficinatablet.model.Message;
import br.com.oficinatablet.service.MessageService;

/**
 * A placeholder fragment containing a simple view.
 */
public class MessageListFragment extends Fragment {

    public static final String ARG_CHAT_KEY = "arg_chat_key";

    private MessageService messageService;

    private RecyclerView chatMessageRecyclerView;
    private LinearLayoutManager chatMessageLayoutManager;
    private FirebaseRecyclerAdapter chatMessageAdapter;

    public MessageListFragment() {
    }


    public static MessageListFragment newInstance(String chatKey) {
        MessageListFragment fragment = new MessageListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CHAT_KEY, chatKey);
        fragment.setArguments(args);
        return fragment;
    }


    public static class ChatMessagesViewHolder extends RecyclerView.ViewHolder {

        public TextView messageUserName;
        public ImageView messageOptions;
        public TextView messageUser;

        public ChatMessagesViewHolder(View itemView) {
            super(itemView);

            messageUserName = (TextView) itemView.findViewById(R.id.message_user_name);
            messageOptions = (ImageView) itemView.findViewById(R.id.message_options);
            messageUser = (TextView) itemView.findViewById(R.id.message_user);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_message_list, container, false);

        this.messageService = new MessageService();

        chatMessageRecyclerView = (RecyclerView) view.findViewById(R.id.messages_recycler_view);
        chatMessageRecyclerView.setHasFixedSize(true);

        String chatKey = getArguments().getString(ARG_CHAT_KEY);

        if (!TextUtils.isEmpty(chatKey)) {
            this.loadAdapterByChatKey(chatKey);
        }

        chatMessageLayoutManager = new LinearLayoutManager(getActivity());
        chatMessageRecyclerView.setLayoutManager(chatMessageLayoutManager);

        return view;
    }

    public void loadAdapterByChatKey(String chatKey) {
        this.chatMessageAdapter = this.getFirebaseMessageAdapter(chatKey);

        chatMessageRecyclerView.setAdapter(this.chatMessageAdapter);
    }

    private FirebaseRecyclerAdapter<Message, ChatMessagesViewHolder> getFirebaseMessageAdapter(String chatKey) {

        final Query query = this.messageService.getMessagesForChat(chatKey);

        return new FirebaseRecyclerAdapter<Message, ChatMessagesViewHolder>(
                Message.class, R.layout.layout_chat_section_row,
                ChatMessagesViewHolder.class, query) {
            @Override
            protected void populateViewHolder(ChatMessagesViewHolder viewHolder, Message messageModel, int position) {
                viewHolder.messageUserName.setText(messageModel.getOwner().getName());
                viewHolder.messageUserName.setText(messageModel.getMessage());
                viewHolder.itemView.setClickable(true);
            }
        };
    }
}
