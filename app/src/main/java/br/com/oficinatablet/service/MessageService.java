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

import br.com.oficinatablet.model.Message;
import br.com.oficinatablet.model.User;
import br.com.oficinatablet.singletons.ServerURL;

/**
 * Created by Mateus Emanuel Araújo on 9/15/16.
 * MA Solutions
 * teusemanuel@gmail.com
 */
public class MessageService extends GenericService {

    private DatabaseReference myRef;

    public MessageService() {
        this.myRef = getDatabase().getReference(ServerURL.getInstance().chatMessagesUrl());
    }

    public Query getMessagesForChat(String chatId) {

        return this.myRef.child(chatId).orderByChild("timestamp");
    }

    public void send(String chatId, String message, User user, DatabaseReference.CompletionListener listener) {
        Message msg = new Message(message, user);
        if(listener != null) {
            this.myRef.child(chatId).push().setValue(msg, listener);
        } else {
            this.myRef.child(chatId).push().setValue(msg);
        }
    }
}
