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
package br.com.oficinatablet.users.fragments;

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
import br.com.oficinatablet.model.User;
import br.com.oficinatablet.service.UserService;

/**
 * Created by Mateus Emanuel Araújo on 9/11/16.
 * MA Solutions
 * teusemanuel@gmail.com
 */
public class UsersFragment extends Fragment {

    private UserService service;

    private RecyclerView usersRecyclerView;
    private LinearLayoutManager usersLayoutManager;
    private FirebaseRecyclerAdapter usersAdapter;



    public static class UserViewHolder extends RecyclerView.ViewHolder {

        public ImageView userIconImageView;
        public TextView userNameTextView;
        public TextView userEmailTextView;
        public ImageView userActionImageView;

        public UserViewHolder(View itemView) {
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
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        this.service = new UserService();

        usersRecyclerView = (RecyclerView) view.findViewById(R.id.users_recycler_view);
        usersRecyclerView.setHasFixedSize(true);

        this.usersAdapter = this.getFirebaseAdapter();

        usersRecyclerView.setAdapter(this.usersAdapter);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String title = firebaseUser.getDisplayName();
            getActivity().setTitle(title);
        }

        usersLayoutManager = new LinearLayoutManager(getActivity());
        usersRecyclerView.setLayoutManager(usersLayoutManager);

        return view;
    }


    private FirebaseRecyclerAdapter<User, UserViewHolder> getFirebaseAdapter() {
        final Query query = this.service.loggedUsers();

        return new FirebaseRecyclerAdapter<User, UserViewHolder>(
                User.class, R.layout.layout_user_row,
                UserViewHolder.class, query) {
            @Override
            protected void populateViewHolder(UserViewHolder viewHolder, User userModel, int position) {
                viewHolder.userNameTextView.setText(userModel.getName());
                viewHolder.userEmailTextView.setText(userModel.getEmail());
                viewHolder.userActionImageView.setOnClickListener(getRowOptionListenerClick(position));
            }
        };
    }

    public View.OnClickListener getRowOptionListenerClick(final int positionRow) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(getActivity(), view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.users_row, popup.getMenu());
                popup.show();
            }
        };
    }
}
