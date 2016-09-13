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
package br.com.oficinatablet.users.lists;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.oficinatablet.R;
import br.com.oficinatablet.model.User;

/**
 * Created by Mateus Emanuel Araújo on 9/12/16.
 * MA Solutions
 * teusemanuel@gmail.com
 */
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    private final List<User> users;

    public UsersAdapter(List<User> friends) {
        this.users = friends;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView userIconImageView;
        public TextView userNameTextView;
        public TextView userEmailTextView;
        public ImageView userActionImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            userIconImageView = (ImageView) itemView.findViewById(R.id.user_icon);
            userNameTextView = (TextView) itemView.findViewById(R.id.user_name);
            userEmailTextView = (TextView) itemView.findViewById(R.id.user_email);
            userActionImageView = (ImageView) itemView.findViewById(R.id.user_action);

        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_cell, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = users.get(position);

        holder.userNameTextView.setText(user.getName());
        holder.userEmailTextView.setText(user.getEmail());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
