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
package br.com.oficinatablet.model;

import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;

/**
 * Created by Mateus Emanuel Araújo on 9/12/16.
 * MA Solutions
 * teusemanuel@gmail.com
 */
public class User implements Serializable {

    private String id;
    private String name;
    private String iconUrl;
    private String email;

    // Requer construtor default para o mapeamento do objeto pelo Firebase
    @SuppressWarnings("unused")
    public User() {
    }

    public User(String id, String name, String iconUrl, String email, String token) {
        this.id = id;
        this.name = name;
        this.iconUrl = iconUrl;
        this.email = email;
    }

    public User(FirebaseUser user) {
        this.id = user.getUid();
        this.name = user.getDisplayName();

        if(user.getPhotoUrl() != null) {
            this.iconUrl = user.getPhotoUrl().toString();
        }
        this.email = user.getEmail();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
