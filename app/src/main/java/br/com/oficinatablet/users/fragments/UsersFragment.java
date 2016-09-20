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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

import br.com.oficinatablet.R;

/**
 * Created by Mateus Emanuel Araújo on 9/11/16.
 * MA Solutions
 * teusemanuel@gmail.com
 */
public class UsersFragment extends Fragment {

    private RecyclerView usersRecyclerView;
    private LinearLayoutManager usersLayoutManager;
    private FirebaseRecyclerAdapter usersAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        usersRecyclerView = (RecyclerView) view.findViewById(R.id.users_recycler_view);
        usersRecyclerView.setHasFixedSize(true);

        usersLayoutManager = new LinearLayoutManager(getActivity());
        usersRecyclerView.setLayoutManager(usersLayoutManager);

        return view;
    }
}
