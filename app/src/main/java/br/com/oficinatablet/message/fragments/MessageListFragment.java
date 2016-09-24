package br.com.oficinatablet.message.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Query;

import br.com.oficinatablet.R;
import br.com.oficinatablet.model.Chat;
import br.com.oficinatablet.service.MessageService;

/**
 * A placeholder fragment containing a simple view.
 */
public class MessageListFragment extends Fragment {

    private MessageService messageService;

    private RecyclerView chatMessageRecyclerView;
    private LinearLayoutManager chatMessageLayoutManager;
    private FirebaseRecyclerAdapter chatMessageAdapter;

    public MessageListFragment() {
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
        View view  = inflater.inflate(R.layout.fragment_list_chat, container, false);

        this.messageService = new MessageService();

        chatMessageRecyclerView = (RecyclerView) view.findViewById(R.id.messages_recycler_view);
        chatMessageRecyclerView.setHasFixedSize(true);

        this.loadAdapter();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        // altera o título da activity
        if (firebaseUser != null) {
            String title = firebaseUser.getDisplayName();
            getActivity().setTitle(title);
        }

        chatMessageLayoutManager = new LinearLayoutManager(getActivity());
        chatMessageRecyclerView.setLayoutManager(chatMessageLayoutManager);

        return view;
    }

    private void loadAdapter() {
        this.chatMessageAdapter = this.getFirebaseMessageAdapter();

        chatMessageRecyclerView.setAdapter(this.chatMessageAdapter);
    }

    private FirebaseRecyclerAdapter<Chat, ChatMessagesViewHolder> getFirebaseMessageAdapter() {
        final Query query = this.messageService.getMessagesForChat("");

        return new FirebaseRecyclerAdapter<Chat, ChatMessagesViewHolder>(
                Chat.class, R.layout.layout_chat_section_row,
                ChatMessagesViewHolder.class, query) {
            @Override
            protected void populateViewHolder(ChatMessagesViewHolder viewHolder, Chat chatModel, int position) {
                viewHolder.messageUserName.setText(chatModel.getChatName());
                viewHolder.itemView.setClickable(true);
            }
        };
    }
}
