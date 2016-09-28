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
import com.google.firebase.database.Query;

import br.com.oficinatablet.model.Chat;
import br.com.oficinatablet.singletons.ServerURL;

/**
 * Created by Mateus Emanuel Araújo on 9/15/16.
 * MA Solutions
 * teusemanuel@gmail.com
 */
public class ChatService extends GenericService {

    private DatabaseReference myRef;

    public ChatService() {
        this.myRef = getDatabase().getReference(ServerURL.getInstance().chatsUrl());
    }

    public String createChat(Chat chat, DatabaseReference.CompletionListener listener) {
        DatabaseReference pushReference = this.myRef.push();
        if (listener != null) {
            pushReference.setValue(chat, listener);
        } else {
            pushReference.setValue(chat);
        }

        return pushReference.getKey();
    }

    public void updateChat(String chatId, Chat chat) {
        this.myRef.child(chatId).setValue(chat);
    }

    public Query getChatForUsers(String originUser, String destinationUser) {
        Query query = myRef.equalTo(originUser);

        //TODO create query for search usesr in chatMembers with singleChat
        return query;
    }

    public Query allChats(String originUser) {
        return this.myRef.equalTo(originUser).orderByValue();
    }
}
