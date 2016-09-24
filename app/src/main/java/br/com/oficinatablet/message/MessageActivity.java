package br.com.oficinatablet.message;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import br.com.oficinatablet.R;
import br.com.oficinatablet.message.fragments.MessageInputFragment;
import br.com.oficinatablet.message.fragments.MessageListFragment;

public class MessageActivity extends AppCompatActivity {

    private MessageListFragment listFragment;
    private MessageInputFragment inputFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            listFragment = new MessageListFragment();
            inputFragment = new MessageInputFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, listFragment)
                    .add(R.id.input_chat, inputFragment)
                    .commit();
        }
    }

}
