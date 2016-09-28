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

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.Map;

import br.com.oficinatablet.R;
import br.com.oficinatablet.chats.decoration.DividerItemDecoration;
import br.com.oficinatablet.message.MessageActivity;
import br.com.oficinatablet.model.Chat;
import br.com.oficinatablet.model.User;
import br.com.oficinatablet.service.ChatService;
import br.com.oficinatablet.service.MemberService;
import br.com.oficinatablet.service.UserService;
import br.com.oficinatablet.utils.SelectableFirebaseAdapter;

/**
 * Created by Mateus Emanuel Araújo on 9/11/16.
 * MA Solutions
 * teusemanuel@gmail.com
 */
public class ChatSectionFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private User loggedUser;

    private int rowSelectedPosition;
    private User userSelected;
    private Chat chatSelected;

    private UserService userService;
    private ChatService chatService;
    private MemberService memberService;

    private RecyclerView chatSectionRecyclerView;
    private LinearLayoutManager chatSectionLayoutManager;
    private FirebaseRecyclerAdapter chatSectionAdapter;

    //TOOLBAR ACTION
    private ActionModeCallback actionModeCallback = new ActionModeCallback();
    private ActionMode actionMode;



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

    private class ActionModeCallback implements ActionMode.Callback {
        @SuppressWarnings("unused")
        private final String TAG = ActionModeCallback.class.getSimpleName();

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate (R.menu.action_select, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_done:
                    loadDialog();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            ((SelectableFirebaseAdapter) chatSectionAdapter).clearSelection();
            actionMode = null;
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_section, container, false);

        this.userService = new UserService();
        this.chatService = new ChatService();
        this.memberService = new MemberService();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        // altera o título da activity
        if (firebaseUser != null) {
            this.loggedUser = new User(firebaseUser);
            String title = this.loggedUser.getName();
            getActivity().setTitle(title);
        }

        chatSectionRecyclerView = (RecyclerView) view.findViewById(R.id.users_recycler_view);
        chatSectionRecyclerView.setHasFixedSize(true);

        switch (getSection()) {
            case 1:
                this.chatSectionAdapter = this.getFirebaseChatsAdapter();
                break;
            case 2:
                this.chatSectionAdapter = this.getFirebaseUsersAdapter();
                /*this.chatSectionRecyclerView.setcho*/
                break;
            default:
                throw new RuntimeException("Invalid Section");
        }

        chatSectionRecyclerView.setItemAnimator(new DefaultItemAnimator());
        chatSectionRecyclerView.setAdapter(this.chatSectionAdapter);
        chatSectionRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));

        chatSectionLayoutManager = new LinearLayoutManager(getActivity());
        chatSectionRecyclerView.setLayoutManager(chatSectionLayoutManager);

        return view;
    }


    private FirebaseRecyclerAdapter<Chat, ChatListViewHolder> getFirebaseChatsAdapter() {
        final Query query = this.chatService.allChats(this.loggedUser.getId());

        return new FirebaseRecyclerAdapter<Chat, ChatListViewHolder>(
                Chat.class, R.layout.layout_chat_section_row,
                ChatListViewHolder.class, query) {
            @Override
            protected void populateViewHolder(ChatListViewHolder viewHolder, Chat chatModel, int position) {

                //key reference
                String chatKey = getRef(position).getKey();
                viewHolder.userNameTextView.setText(chatModel.getChatName());
                viewHolder.userActionImageView.setOnClickListener(getRowOptionListenerClick(position, chatModel));
                viewHolder.itemView.setClickable(true);
                viewHolder.itemView.setOnClickListener(getRowSelectedListenerClick(position, chatKey, chatModel));
            }
        };
    }


    private SelectableFirebaseAdapter<User, ChatListViewHolder> getFirebaseUsersAdapter() {
        final Query query = this.userService.loggedUsers();

        return new SelectableFirebaseAdapter<User, ChatListViewHolder>(
                User.class, R.layout.layout_chat_section_row,
                ChatListViewHolder.class, query) {
            @Override
            protected void populateViewHolder(ChatListViewHolder viewHolder, User userModel, int position) {

                //key reference
                String userKey = getRef(position).getKey();
                viewHolder.userNameTextView.setText(userModel.getName());
                viewHolder.userEmailTextView.setText(userModel.getEmail());
                viewHolder.userActionImageView.setOnClickListener(getRowOptionListenerClick(position, userModel));
                viewHolder.itemView.setClickable(true);
                viewHolder.itemView.setOnClickListener(getRowSelectedListenerClick(position, userKey, userModel));

                viewHolder.userIconImageView.setImageResource(isSelected(position) ? R.drawable.ic_check_circle : R.drawable.ic_user_circle);
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

    public View.OnClickListener getRowSelectedListenerClick(final int positionRow, final String chatKey, final Chat chatModel) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO start activity for chat
                Intent intent = new Intent(getActivity(), MessageActivity.class);
                intent.putExtra(MessageActivity.ARG_CHAT_KEY, chatKey);
                intent.putExtra(MessageActivity.ARG_CHAT, chatModel);
                startActivity(intent);
            }
        };
    }

    public View.OnClickListener getRowSelectedListenerClick(final int positionRow, final String userKey, final User userModel) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (actionMode == null) {
                    actionMode = getActivity().startActionMode(actionModeCallback);
                }

                //TODO group users form create chat
                toggleSelection(positionRow);
               /* Intent intent = new Intent(getActivity(), MessageActivity.class);
                intent.putExtra(MessageActivity.ARG_USER_CONTACT_KEY, userKey);
                intent.putExtra(MessageActivity.ARG_USER_CONTACT, userModel);
                startActivity(intent);*/
            }
        };
    }

    /**
     * Toggle the selection state of an item.
     *
     * If the item was the last one in the selection and is unselected, the selection is stopped.
     * Note that the selection must already be started (actionMode must not be null).
     *
     * @param position Position of the item to toggle the selection state
     */
    private void toggleSelection(int position) {
        ((SelectableFirebaseAdapter) chatSectionAdapter).toggleSelection(position);
        int count = ((SelectableFirebaseAdapter) chatSectionAdapter).getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    private void loadDialog() {

        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_with_edit_text, (ViewGroup) getView(), false);
        // Set up do textInput
        final TextInputLayout textInput = (TextInputLayout) viewInflated.findViewById(R.id.dialog_input);

        final AlertDialog builder = new AlertDialog.Builder(getActivity(), R.style.CustomDialog)
                //adciona Titulo
                .setTitle("Criar Grupo")
                //adciona view customizada ao dialog
                .setView(viewInflated)
                //add botoes
                .setPositiveButton(R.string.create, null)
                .setNegativeButton(R.string.cancel, null)
                .create();


        // ações customizadas para os botões
        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button positive = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String chatGroupName = textInput.getEditText().getText().toString();

                        if(TextUtils.isEmpty(chatGroupName)) {
                            textInput.setErrorEnabled(true);
                            textInput.setError("Nome Obrigatorio");
                            return;
                        }

                        textInput.setErrorEnabled(false);
                        textInput.setError("");

                        createGroupChat(chatGroupName, builder);
                    }
                });

                Button negative = builder.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        builder.cancel();
                    }
                });
            }
        });

        /* Show the dialog */
        builder.show();
    }

    private void createGroupChat(String groupChatName, final AlertDialog builder) {
        final Chat chatModel = new Chat();
        chatModel.setChatName(groupChatName);

        final String chatKey = this.chatService.createChat(chatModel, null);

        Map<String, User> selectedMembers = ((SelectableFirebaseAdapter) chatSectionAdapter).getSelectedObjects();

        selectedMembers.put(this.loggedUser.getId(), loggedUser);

        this.memberService.createMemberChat(chatKey, selectedMembers, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    System.out.println("Data could not be saved. " + databaseError.getMessage());
                } else {
                    System.out.println("Data saved successfully.");

                    Intent intent = new Intent(getActivity(), MessageActivity.class);
                    intent.putExtra(MessageActivity.ARG_CHAT_KEY, chatKey);
                    intent.putExtra(MessageActivity.ARG_CHAT, chatModel);
                    startActivity(intent);


                    ((SelectableFirebaseAdapter) chatSectionAdapter).clearSelection();
                    actionMode.finish();
                }

                builder.dismiss();
            }
        });

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
