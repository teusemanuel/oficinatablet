package br.com.oficinatablet.chat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import br.com.oficinatablet.R;
import br.com.oficinatablet.chat.fragments.ChatInputFragment;
import br.com.oficinatablet.chat.fragments.ChatListFragment;

public class ChatActivity extends AppCompatActivity {

    private ChatListFragment listFragment;
    private ChatInputFragment inputFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            listFragment = new ChatListFragment();
            inputFragment = new ChatInputFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, listFragment)
                    .add(R.id.input_chat, inputFragment)
                    .commit();
        }
    }

}
