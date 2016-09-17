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
package br.com.oficinatablet.service;

import com.google.firebase.database.DatabaseReference;

import br.com.oficinatablet.model.User;
import br.com.oficinatablet.singletons.ServerURL;

/**
 * Created by Mateus Emanuel Araújo on 9/15/16.
 * MA Solutions
 * teusemanuel@gmail.com
 */
public class UserService extends GenericService {

    private DatabaseReference myRef;

    public UserService() {
        this.myRef = getDatabase().getReference(ServerURL.getInstance().usersUrl());
    }

    public void saveUser(String userId, User user) {
        this.myRef.child(userId).setValue(user);
    }

    public void loggedUsers(String userId) {
        this.myRef.child(userId).orderByValue();
    }
}
