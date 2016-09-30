package br.com.oficinatablet.message;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.oficinatablet.R;
import br.com.oficinatablet.message.fragments.MessageInputFragment;
import br.com.oficinatablet.message.fragments.MessageListFragment;
import br.com.oficinatablet.model.Chat;
import br.com.oficinatablet.model.User;

public class MessageActivity extends AppCompatActivity {

    private static final String TAG = "MessageActivity";

    public static final String ARG_CHAT = "arg_chat";
    public static final String ARG_CHAT_KEY = "arg_chat_key";

    private User loggedUser;

    private Chat chatModel;
    private String chatKey;

    private MessageListFragment listFragment;
    private MessageInputFragment inputFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        // altera o título da activity
        if (firebaseUser != null) {
            loggedUser = new User(firebaseUser);
        }

        chatModel = (Chat) getIntent().getSerializableExtra(ARG_CHAT);

        setTitle(chatModel.getChatName());

        chatKey = getIntent().getStringExtra(ARG_CHAT_KEY);

        if (savedInstanceState == null) {
            listFragment = MessageListFragment.newInstance(chatKey);
            inputFragment = new MessageInputFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, listFragment)
                    .add(R.id.input_chat, inputFragment)
                    .commit();
        }
    }

}
