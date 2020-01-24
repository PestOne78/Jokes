package com.junior.test.jokes;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.junior.test.jokes.fragments.JokesFragment;
import com.junior.test.jokes.fragments.WebFragment;
import com.junior.test.jokes.jokes.JokeEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {

    private static final String JOKES_TAG = "Jokes";
    private static final String WEB_TAG = "Web";

    private boolean itsJokeFrame = false;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationSelectedListener
            = item -> {
        Fragment fragment;
        switch (item.getItemId()) {
            case R.id.btn_jokes:
                fragment = new JokesFragment();
                loadFragment(fragment, JOKES_TAG);
                itsJokeFrame = true;
                return true;
            case R.id.btn_web:
                fragment = new WebFragment();
                loadFragment(fragment, WEB_TAG);
                itsJokeFrame = false;
                return true;
        }
        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView botNav = findViewById(R.id.bot_nav_view);
        botNav.setOnNavigationItemSelectedListener(mOnNavigationSelectedListener);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) botNav.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewBehavior());
        loadFragment(new JokesFragment(), JOKES_TAG);
        itsJokeFrame = true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    private void loadFragment(Fragment fragment, String TAG) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment, TAG).addToBackStack(null).commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void JokeEventRecived(JokeEvent jokeEvent) {
        Log.i("EventBus", "i update data!");
        JokesFragment jokesFragment = (JokesFragment) getSupportFragmentManager().findFragmentByTag(JOKES_TAG);
        assert jokesFragment != null;
        jokesFragment.updateRecyclerView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(itsJokeFrame){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(R.string.title_exit)
                    .setMessage(R.string.exit_app)
                    .setIcon(R.drawable.chuck_norris)
                    .setCancelable(true)
                    .setPositiveButton(R.string.yes,
                            (dialog, which) -> finish())
                    .setNegativeButton(R.string.no, (dialog, which) -> dialog.cancel());
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
}
