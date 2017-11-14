package se.sugarest.jane.viaplaysections;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolBar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ImageButton mImageButtonDrawerMenu;
    private TextView mTextViewTitle;
    private TextView mTextViewDescription;

    private String[] mSectionTitles = {"SERIER", "FILM", "SPORT", "BARN", "STORE"};
    private RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolBar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mRecyclerView = findViewById(R.id.left_drawer);

        mTextViewTitle = findViewById(R.id.section_title);
        mTextViewDescription = findViewById(R.id.section_description);

        mImageButtonDrawerMenu = findViewById(R.id.button_navigation);

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mImageButtonDrawerMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDrawerLayout.isDrawerOpen(mRecyclerView)) {
                    mDrawerLayout.closeDrawer(mRecyclerView);
                } else if (!mDrawerLayout.isDrawerOpen(mRecyclerView)) {
                    mDrawerLayout.openDrawer(mRecyclerView);
                }
            }
        });

        showSectionInformation();
    }

    private void showSectionInformation() {
    }
}
