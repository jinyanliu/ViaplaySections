package se.sugarest.jane.viaplaysections.ui;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import se.sugarest.jane.viaplaysections.R;
import se.sugarest.jane.viaplaysections.data.SectionAdapter;

public class MainActivity extends AppCompatActivity implements SectionAdapter.SectionAdapterOnClickHandler {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private Toolbar mToolBar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ImageButton mImageButtonDrawerMenu;
    private TextView mTextViewTitle;
    private TextView mTextViewDescription;

    private RecyclerView mRecyclerView;
    private SectionAdapter mSectionAdapter;

    private ArrayList<String> mFakeTitleData = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolBar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mDrawerLayout = findViewById(R.id.drawer_layout);


        setUpRecyclerViewWithAdapter();
        setSectionTitleDataToRecyclerView();


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

    }

    private void setSectionTitleDataToRecyclerView() {
        getSectionInformation();
        mSectionAdapter.setSectionData(mFakeTitleData);
    }

    private void setUpRecyclerViewWithAdapter() {
        mRecyclerView = findViewById(R.id.left_drawer);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        if (mSectionAdapter == null) {
            mSectionAdapter = new SectionAdapter(this);
        }
        mRecyclerView.setAdapter(mSectionAdapter);
    }

    private void getSectionInformation() {
        mFakeTitleData.add("SERIER");
        mFakeTitleData.add("FILM");
        mFakeTitleData.add("SPORT");
        mFakeTitleData.add("BARN");
        mFakeTitleData.add("STORE");
    }


    @Override
    public void onClick(int position) {
        Log.i(LOG_TAG, "User clicked: " + mFakeTitleData.get(position));
        mDrawerLayout.closeDrawer(mRecyclerView);
    }
}
