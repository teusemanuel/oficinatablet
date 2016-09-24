/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.com.oficinatablet.chats.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Query;

import br.com.oficinatablet.R;
import br.com.oficinatablet.message.MessageActivity;
import br.com.oficinatablet.chats.decoration.DividerItemDecoration;
import br.com.oficinatablet.model.Chat;
import br.com.oficinatablet.model.User;
import br.com.oficinatablet.service.ChatService;
import br.com.oficinatablet.service.UserService;

/**
 * Created by Mateus Emanuel Araújo on 9/11/16.
 * MA Solutions
 * teusemanuel@gmail.com
 */
public class ChatSectionFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private int rowSelectedPosition;
    private User userSelected;
    private Chat chatSelected;

    private UserService userService;
    private ChatService chatService;

    private RecyclerView chatSectionRecyclerView;
    private LinearLayoutManager chatSectionLayoutManager;
    private FirebaseRecyclerAdapter chatSectionAdapter;



    public ChatSectionFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ChatSectionFragment newInstance(int sectionNumber) {
        ChatSectionFragment fragment = new ChatSectionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }



    public static class ChatListViewHolder extends RecyclerView.ViewHolder {

        public ImageView userIconImageView;
        public TextView userNameTextView;
        public TextView userEmailTextView;
        public ImageView userActionImageView;

        public ChatListViewHolder(View itemView) {
            super(itemView);

            userIconImageView = (ImageView) itemView.findViewById(R.id.user_icon);
            userNameTextView = (TextView) itemView.findViewById(R.id.user_name);
            userEmailTextView = (TextView) itemView.findViewById(R.id.user_email);
            userActionImageView = (ImageView) itemView.findViewById(R.id.user_options);

        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_section, container, false);

        this.userService = new UserService();
        this.chatService = new ChatService();

        chatSectionRecyclerView = (RecyclerView) view.findViewById(R.id.users_recycler_view);
        chatSectionRecyclerView.setHasFixedSize(true);

        switch (getSection()) {
            case 1:
                this.chatSectionAdapter = this.getFirebaseChatsAdapter();
                break;
            case 2:
                this.chatSectionAdapter = this.getFirebaseUsersAdapter();
                break;
            default:
                throw new RuntimeException("Invalid Section");
        }

        chatSectionRecyclerView.setAdapter(this.chatSectionAdapter);
        chatSectionRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        // altera o título da activity
        if (firebaseUser != null) {
            String title = firebaseUser.getDisplayName();
            getActivity().setTitle(title);
        }

        chatSectionLayoutManager = new LinearLayoutManager(getActivity());
        chatSectionRecyclerView.setLayoutManager(chatSectionLayoutManager);

        return view;
    }


    private FirebaseRecyclerAdapter<Chat, ChatListViewHolder> getFirebaseChatsAdapter() {
        final Query query = this.chatService.allChats();

        return new FirebaseRecyclerAdapter<Chat, ChatListViewHolder>(
                Chat.class, R.layout.layout_chat_section_row,
                ChatListViewHolder.class, query) {
            @Override
            protected void populateViewHolder(ChatListViewHolder viewHolder, Chat chatModel, int position) {
                viewHolder.userNameTextView.setText(chatModel.getChatName());
                viewHolder.userActionImageView.setOnClickListener(getRowOptionListenerClick(position, chatModel));
                viewHolder.itemView.setClickable(true);
                viewHolder.itemView.setOnClickListener(getRowSelectedListenerClick(position, chatModel));
            }
        };
    }


    private FirebaseRecyclerAdapter<User, ChatListViewHolder> getFirebaseUsersAdapter() {
        final Query query = this.userService.loggedUsers();

        return new FirebaseRecyclerAdapter<User, ChatListViewHolder>(
                User.class, R.layout.layout_chat_section_row,
                ChatListViewHolder.class, query) {
            @Override
            protected void populateViewHolder(ChatListViewHolder viewHolder, User userModel, int position) {
                viewHolder.userNameTextView.setText(userModel.getName());
                viewHolder.userEmailTextView.setText(userModel.getEmail());
                viewHolder.userActionImageView.setOnClickListener(getRowOptionListenerClick(position, userModel));
                viewHolder.itemView.setClickable(true);
                viewHolder.itemView.setOnClickListener(getRowSelectedListenerClick(position, userModel));
            }
        };
    }

    public View.OnClickListener getRowOptionListenerClick(final int positionRow, final Chat userModel) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(getActivity(), view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.users_row, popup.getMenu());
                popup.show();
                setRowSelectedPosition(positionRow);
                setChatSelected(userModel);
            }
        };
    }

    public View.OnClickListener getRowOptionListenerClick(final int positionRow, final User userModel) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(getActivity(), view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.users_row, popup.getMenu());
                popup.show();
                setRowSelectedPosition(positionRow);
                setUserSelected(userModel);
            }
        };
    }

    public View.OnClickListener getRowSelectedListenerClick(final int positionRow, final Chat userModel) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO start activity for chat
                Intent intent = new Intent(getActivity(), MessageActivity.class);
                startActivity(intent);
            }
        };
    }

    public View.OnClickListener getRowSelectedListenerClick(final int positionRow, final User userModel) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO start activity for chat
                Intent intent = new Intent(getActivity(), MessageActivity.class);
                startActivity(intent);
            }
        };
    }


    //GETTERS AND SETTER
    ///////////////////
    public int getRowSelectedPosition() {
        return rowSelectedPosition;
    }

    public void setRowSelectedPosition(int rowSelectedPosition) {
        this.rowSelectedPosition = rowSelectedPosition;
    }

    public User getUserSelected() {
        return userSelected;
    }

    public void setUserSelected(User userSelected) {
        this.userSelected = userSelected;
    }

    public Chat getChatSelected() {
        return chatSelected;
    }

    public void setChatSelected(Chat chatSelected) {
        this.chatSelected = chatSelected;
    }

    public int getSection() {
        return getArguments().getInt(ARG_SECTION_NUMBER);
    }
}
