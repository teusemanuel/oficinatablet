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
package br.com.oficinatablet.login.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

import br.com.oficinatablet.R;
import br.com.oficinatablet.model.User;
import br.com.oficinatablet.service.UserService;
import br.com.oficinatablet.chats.UsersActivity;

/**
 * Created by Mateus Emanuel Araújo on 9/10/16.
 * MA Solutions
 * teusemanuel@gmail.com
 */
public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";

    private UserService service;

    //FIREBASE
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private CallbackManager callbackManager;
    private LoginButton loginButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseAuth = FirebaseAuth.getInstance();

        this.service = new UserService();


        /**
         * Listner para ouvir alterações do login do usuário.
         */
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    Log.d(TAG, getString(R.string.firebase_on_auth_state_changed_signed_in, firebaseUser.getUid()));

                    //SALVA OU ATUALIZA USUARIO
                    ////////////
                    User user  = new User(firebaseUser);
                    service.saveUser(user.getId(), user);

                    startActivity(new Intent(getActivity(), UsersActivity.class));
                    getActivity().finish();

                } else {
                    Log.d(TAG, getString(R.string.firebase_on_auth_state_changed_signed_out));
                }
            }
        };

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);



        //FACEBOOK LOGIN
        ////////////////////
        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) view.findViewById(R.id.login_button);

        final String[] permissions = getResources().getStringArray(R.array.login_permissions);

        loginButton.setReadPermissions( Arrays.asList(permissions) );
        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                moldarTokenAcessoFacebook(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
                Log.d(TAG, getString(R.string.facebook_on_cancel));
            }

            @Override
            public void onError(FacebookException error) {
                Log.e(TAG, getString(R.string.facebook_on_error));
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void onBackPressed() {
        getActivity().finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void moldarTokenAcessoFacebook(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.e(TAG, getString(R.string.sign_in_with_facebook), task.getException());
                            Toast.makeText(getActivity(), R.string.authentication_failed, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
