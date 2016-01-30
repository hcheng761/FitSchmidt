package groupg.fitschmidt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomizerActivity extends AppCompatActivity {
    public final static String EXTRA_EXERCISES = "groupg.fitschmidt.EXTRA_EXERCISES";

//    private ArrayList<String> possibleList = new ArrayList<>();
//    private ArrayList<String> exerciseList = new ArrayList<>();

    private ArrayList<Exercise> possibleList = new ArrayList<>();
    private ArrayList<Exercise> exerciseList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_randomizer);

        popList();  //poplist finds all possible exercises
        genList();  //genlist randomizes exercises and populates the view
    }

    private void popList(){ //populates a list of all possible exercises from xml file <- need to implement
//        List<Exercise> exerciseList = null;
        XMLParser parser = new XMLParser();

        try {
//            exerciseList = parser.parseExercises(getAssets().open("exercises_data.xml"));
            possibleList = parser.parseExercises(getAssets().open("exercises_data.xml"));
        } catch (IOException exception) {
            exception.printStackTrace();
        }

//        for(int i = 0; i < exerciseList.size(); i++) {
//            possibleList.add(exerciseList.get(i).getName());
//        }
    }

    public void genList(){  //generates a new list of exercises to do
        LinearLayout exerciseListLayout = (LinearLayout)findViewById(R.id.exercise_list_layout);

        // Clear the exercise list and layout
        exerciseList.clear();
        if(exerciseListLayout.getChildCount() > 0)
            exerciseListLayout.removeAllViews();


        // Generate a random series of exercises
        for(int i = 0; i < possibleList.size(); i++){
            exerciseList.add(
                    possibleList.get((int) (Math.random() * ((possibleList.size())))));
        }

        for(int i = 0; i< exerciseList.size(); i++) {
            // Setup text view for each exercise
            TextView textView = new TextView(this);

            textView.setPadding(10, 10, 0, 10);
            textView.setTextSize(24);
            textView.setText(exerciseList.get(i).getName());

            // Add text view to layout
            exerciseListLayout.addView(textView);
        }
    }

    /**
     * Regenerate button handler
     */
    public void regenList(View v) { genList(); }

    /**
     * Accept button handler
     * @param view
     */
    public void sendList(View view){
        Intent intent = new Intent(this, CountdownActivity.class);

        // Array List implements Serializable and can be passed on as so
        intent.putExtra(EXTRA_EXERCISES, exerciseList);
        startActivity(intent);
    }
}
