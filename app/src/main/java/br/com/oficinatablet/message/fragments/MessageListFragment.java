package br.com.oficinatablet.message.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

import br.com.oficinatablet.R;
import br.com.oficinatablet.model.Message;
import br.com.oficinatablet.model.User;
import br.com.oficinatablet.service.MessageService;

/**
 * A placeholder fragment containing a simple view.
 */
public class MessageListFragment extends Fragment {

    public static final String ARG_CHAT_KEY = "arg_chat_key";
    public static final String ARG_LOGGED_USER = "arg_logged_user";

    private MessageService messageService;
    private User loggedUser;

    private RecyclerView chatMessageRecyclerView;
    private LinearLayoutManager chatMessageLayoutManager;
    private FirebaseRecyclerAdapter chatMessageAdapter;

    public MessageListFragment() {
    }


    public static MessageListFragment newInstance(String chatKey, User loggedUser) {
        MessageListFragment fragment = new MessageListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CHAT_KEY, chatKey);
        args.putSerializable(ARG_LOGGED_USER, loggedUser);
        fragment.setArguments(args);
        return fragment;
    }


    public static class ChatMessagesViewHolder extends RecyclerView.ViewHolder {

        public TextView messageUserName;
        public TextView messageUser;
        public ImageView messageUserIcon;

        public ChatMessagesViewHolder(View itemView) {
            super(itemView);

            messageUserName = (TextView) itemView.findViewById(R.id.message_user_name);
            messageUser = (TextView) itemView.findViewById(R.id.message_user);
            messageUserIcon = (ImageView) itemView.findViewById(R.id.message_user_icon);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_message_list, container, false);

        this.messageService = new MessageService();

        chatMessageRecyclerView = (RecyclerView) view.findViewById(R.id.messages_recycler_view);
        chatMessageRecyclerView.setHasFixedSize(true);

        String chatKey = getArguments().getString(ARG_CHAT_KEY);
        this.loggedUser = (User) getArguments().getSerializable(ARG_LOGGED_USER);

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
                Message.class, R.layout.layout_message_right_row,
                ChatMessagesViewHolder.class, query) {
            @Override
            protected void populateViewHolder(final ChatMessagesViewHolder viewHolder, Message messageModel, int position) {
                viewHolder.messageUserName.setText(messageModel.getOwner().getName());
                viewHolder.messageUser.setText(messageModel.getMessage());


                //carrega a imagem
                Glide.with(getActivity()).load(messageModel.getOwner().getIconUrl())

                    // aplica bordas arredondadas
                    .asBitmap().centerCrop().into(new BitmapImageViewTarget(viewHolder.messageUserIcon) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        viewHolder.messageUserIcon.setImageDrawable(circularBitmapDrawable);
                    }
                });
            }

            @Override
            public int getItemViewType(int position) {
                Message message = getItem(position);
                boolean friendMessage = true;
                if (loggedUser != null && message != null) {
                    friendMessage = !message.getOwner().getId().equals(loggedUser.getId());
                }

                return friendMessage ? R.layout.layout_message_left_row : R.layout.layout_message_right_row;
            }
        };
    }
}
