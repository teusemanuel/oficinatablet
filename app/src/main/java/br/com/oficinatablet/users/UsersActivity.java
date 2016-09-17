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
package br.com.oficinatablet.users;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.Profile;
import com.google.firebase.auth.FirebaseAuth;

import br.com.oficinatablet.R;
import br.com.oficinatablet.login.LoginActivity;
import br.com.oficinatablet.users.fragments.UsersFragment;

/**
 * Created by Mateus Emanuel Araújo on 9/11/16.
 * MA Solutions
 * teusemanuel@gmail.com
 */
public class UsersActivity extends AppCompatActivity {

    private UsersFragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_users);

        Profile currentProfile = Profile.getCurrentProfile();

        if (savedInstanceState == null) {
            fragment = new UsersFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.users, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.exit_app:

                //LOGOFF Facebook sdk
                /*LoginManager.getInstance().logOut();*/
                FirebaseAuth.getInstance().signOut();

                // START Login screen
                startActivity(new Intent(this, LoginActivity.class));

                //FINISH APP
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
