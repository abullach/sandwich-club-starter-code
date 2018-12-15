package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    private static final int DEFAULT_MARGIN = 8;
    private ImageView mSandwichIv;
    private TextView mName;
    private TextView mOrigin;
    private TextView mPlaceOfOriginLabel;
    private TextView mAlsoKnownAsLabel;
    private TextView mAlsoKnown;
    private TextView mDescription;
    private TextView mIngredients;
    private Sandwich mSandwich;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if(getSupportActionBar() != null){
            // Set up navigation in action bar
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mSandwichIv = findViewById(R.id.image_iv);
        mName = findViewById(R.id.name_tv);
        mOrigin = findViewById(R.id.origin_tv);
        mPlaceOfOriginLabel = findViewById(R.id.place_of_origin_label_tv);
        mAlsoKnownAsLabel = findViewById(R.id.also_known_as_label_tv);
        mAlsoKnown = findViewById(R.id.also_known_tv);
        mDescription = findViewById(R.id.description_tv);
        mIngredients = findViewById(R.id.ingredients_tv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent != null ? intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION) : 0;
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        mSandwich = JsonUtils.parseSandwichJson(json);
        if (mSandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }
        populateUI();
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Dynamically populate the UI on the DetailActivity depending on available sandwich data.
     */
    private void populateUI() {
        // Set action bar title
        setTitle(mSandwich.getMainName());

        Picasso.with(this)
                .load(mSandwich.getImage())
                .error(R.mipmap.ic_launcher) // Display fallback image if image cannot be loaded
                .into(mSandwichIv);
        // Set action bar title
        setTitle(mSandwich.getMainName());
        // Set sandwich name
        mName.setText(mSandwich.getMainName());

        ConstraintSet set = new ConstraintSet();
        ConstraintLayout layout;
        layout = findViewById(R.id.constraintlayout);

        StringBuilder stringBuilder = new StringBuilder();
        // Set 'also known as' name if exist and make visible
        if(mSandwich.getAlsoKnownAs() != null) {
            for (String s: mSandwich.getAlsoKnownAs()) {
                stringBuilder.append(s);
                stringBuilder.append(", ");
            }
            mAlsoKnown.setVisibility(View.VISIBLE);
            mAlsoKnownAsLabel.setVisibility(View.VISIBLE);
            mAlsoKnown.setText(stringBuilder.toString());
        } else {
            set.clone(layout);
            // Break the connection
            set.clear(R.id.origin_tv, ConstraintSet.TOP);
            set.clear(R.id.place_of_origin_label_tv, ConstraintSet.TOP);
            // Make a new connection with name TextView and set constraints
            set.connect(R.id.origin_tv, ConstraintSet.TOP, R.id.name_tv, ConstraintSet.BOTTOM, DEFAULT_MARGIN);
            set.connect(R.id.place_of_origin_label_tv, ConstraintSet.TOP, R.id.name_tv, ConstraintSet.BOTTOM, DEFAULT_MARGIN);
            set.applyTo(layout);
        }

        // Set 'place of origin' name if exist and make visible
        if(mSandwich.getPlaceOfOrigin() != null && !mSandwich.getPlaceOfOrigin().equals("")) {
            mPlaceOfOriginLabel.setVisibility(View.VISIBLE);
            mOrigin.setVisibility(View.VISIBLE);
            mOrigin.setText(mSandwich.getPlaceOfOrigin());
        } else {
            set.clone(layout);
            // Break the connection
            set.clear(R.id.description_tv, ConstraintSet.TOP);
            set.clear(R.id.description_label_tv, ConstraintSet.TOP);
            // Set a new connection in layout depending if also known as name is available
            if(mSandwich.getAlsoKnownAs() != null) {
                // Make a new connection with also known TextView and set constraints
                set.connect(R.id.description_tv, ConstraintSet.TOP, R.id.also_known_tv, ConstraintSet.BOTTOM, DEFAULT_MARGIN);
                set.connect(R.id.description_label_tv, ConstraintSet.TOP, R.id.also_known_as_label_tv, ConstraintSet.BOTTOM, DEFAULT_MARGIN);
            } else {
                // Make a new connection with name TextView and set constraints
                set.connect(R.id.description_tv, ConstraintSet.TOP, R.id.name_tv, ConstraintSet.BOTTOM, DEFAULT_MARGIN);
                set.connect(R.id.description_label_tv, ConstraintSet.TOP, R.id.name_tv, ConstraintSet.BOTTOM, DEFAULT_MARGIN);
            }
            set.applyTo(layout);
        }
        // Set sandwich description text
        mDescription.setText((mSandwich.getDescription()));
        StringBuilder builder = new StringBuilder();
        // Set list of ingredients
        for (String s: mSandwich.getIngredients()) {
            builder.append("* ");
            builder.append(s);
            builder.append("\n");
        }
        mIngredients.setText(builder.toString());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
