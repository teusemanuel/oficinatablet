package br.com.oficinatablet.chats;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import br.com.oficinatablet.R;
import br.com.oficinatablet.login.LoginActivity;
import br.com.oficinatablet.chats.fragments.SectionsPagerAdapter;

public class ChatsSwipeActivity extends AppCompatActivity {

    /**
     * O {@link android.support.v4.view.PagerAdapter} que irá fornecer
     * fragmentos para cada uma das secções. Aqui esta sendo utilizado uma classe extendida de
     * {@link FragmentPagerAdapter} uma derivação de {@link android.support.v4.view.PagerAdapter} ,
     * que irá manter todos os fragmento carregado na memória. Se ficar consumindo muita memória,
     * pode ser melhor para mudar para
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * O {@link ViewPager} que irá hospedar o conteúdo da seção.
     */
    private ViewPager mViewPager;

    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_swip);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Criar o adaptador que vai devolver um fragmento para cada uma das secções principais da activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Configure o ViewPager com o sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


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
                LoginManager.getInstance().logOut();
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