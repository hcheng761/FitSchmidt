package groupg.fitschmidt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class CountdownActivity extends AppCompatActivity {
    public final static String EXTRA_FAIL = "groupg.fitschmidt.EXTRA_FAIL";

    // Exercise data
    ArrayList<Exercise> exerciseList;   // List of exercises obtained from Randomizer
    TextView exerciseTextView;

    // Animation variables
    AnimationDrawable animation;
    ImageView schmidtImageView;

    int exerciseIndex;              // Controls iteration of exercise list
    int exerciseFileId;             // Holds the Exercise object corresponding res ID
    int animationFileId;            // Holds the animation XML file ID
    boolean stopAnimationUpdate;    //  Controls when to stop animation update
    TypedArray schmidtAnimations;

    // Timer variables
    private final static int INTERVAL = 1000;           // Always a 1 second interval
    private final static int TIME_PER_EXERCISE = 30000; // Default 30 seconds per exercise
    Button giveUpButton;
    TextView timerTextView;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);

        // Get list of exercises from Randomizer
        Intent intent = getIntent();
        exerciseList = (ArrayList) intent.getSerializableExtra(RandomizerActivity.EXTRA_EXERCISES);
        int numExercises = exerciseList.size();

        // Place view basic elements
        schmidtImageView = (ImageView) findViewById(R.id.schmidt_image_view);

        exerciseTextView = (TextView) findViewById(R.id.exercise_text_view);
        giveUpButton = (Button) findViewById(R.id.give_up_button);


        // Animation setup
        exerciseIndex = 0;
        stopAnimationUpdate = false;
        schmidtAnimations = getResources().obtainTypedArray(R.array.animation_files);

        // Every defined interval, the exercise name and animation will be updated
        new Runnable() {
            @Override
            public void run() {
                // You want to update animations until:
                // All exercises are done (check end of list) or
                // The user clicks give up or
                // The user leaves the activity
                if(stopAnimationUpdate == false) {
                    exerciseTextView.setText(exerciseList.get(exerciseIndex).getName());

                    // Update animation
                    updateAnimation(exerciseIndex);
                    schmidtImageView.postDelayed(this, TIME_PER_EXERCISE);

                    // Check if has reached the end of list
                    if(exerciseIndex == exerciseList.size())
                        stopAnimationUpdate = true;

                    // Increase the score of previous exercise (means it was completed)
                    if(exerciseIndex > 0)
                         increaseScore(exerciseList.get(exerciseIndex-1));

                    // Update the exercise
                    exerciseIndex++;
                }
            }
        }.run();

        // Setup the timer and start
        int totalTime = numExercises * TIME_PER_EXERCISE;

        timerTextView = (TextView) findViewById(R.id.timer_text_view);
        timerTextView.setText(Integer.toString(totalTime));

        timer = new Timer(totalTime, INTERVAL);
        timer.start();
    }

    /**
     * Configures the correct animation for the exercise
     * @param index Index value for the ArrayList of exercises
     */
    private void updateAnimation(int index) {
        // Get the resource id of the exercise
        exerciseFileId = exerciseList.get(index).getFileId();

        // Get the list of animation files and corresponding one for exercise of list
        animationFileId = schmidtAnimations.getResourceId(exerciseFileId, -1);

        // First remove any res that could be there, then set new one
        schmidtImageView.setBackgroundResource(0);
        schmidtImageView.setBackgroundResource(animationFileId);

        animation = (AnimationDrawable) schmidtImageView.getBackground();
        animation.start();

    }

    @Override
    public void onBackPressed() {
        stopAnimationUpdate = true;
        animation.stop();
        timer.cancel();

        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(animation.isRunning())
            animation.stop();
        stopAnimationUpdate = true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus)
            animation.start();
    }

    public void finalizeTimer() {
        timerTextView.setText("Fertig!");

        // Start the message activity
        Intent intent = new Intent(this, MessageActivity.class);

        // Carry this key value pair to the Message Activity
        intent.putExtra(EXTRA_FAIL, true);
        MessageBoolean.getInstance().setValue(true);
        startActivity(intent);
    }

    /**
     * Give up button handler
     */
    public void interruptTimer(View view) {     //Give up button pressed
        // Stop timer and animation
        timer.cancel();
        animation.stop();

        // Start the message activity
        Intent intent = new Intent(this, MessageActivity.class);
        MessageBoolean.getInstance().setValue(false);
        // Carry this key value pair to the Message Activity
        intent.putExtra(EXTRA_FAIL, false);
        startActivity(intent);
    }

    //Write increase score to memory
    public void increaseScore(Exercise exercise) {
        if(exercise == null) return;

        String toFind = "<name>" + exercise.getName() + "</name>";
        Log.v("READER", toFind);
        try
        {
            File file = new File(getApplicationContext().getFilesDir(), "exercises_data.xml");
           // String address = getApplicationContext().getFilesDir() + "exercises_data.xml";
            BufferedReader reader = new BufferedReader(new FileReader(file));
            Log.v("Update exercise", "called");
            String line;
            String oldtext = "";
            boolean nextLine = false;
            while((line = reader.readLine()) != null)
            {
                String tmpString = line.trim();
                Log.v("READER", tmpString);
                if(nextLine)
                {
                    oldtext += line + "\r\n";
                    line = reader.readLine();
                    Log.v("READER", line);
                    oldtext += line + "\r\n";
                    line = reader.readLine();
                    Log.v("READER", line);
                    tmpString = "";
                    for(int i = 0; i < 19; i++){
                        tmpString += line.charAt(i);
                    }
                    Log.v("READER", tmpString);
                    line = line.substring(19, line.length() - 12);
                    int score = Integer.parseInt(line) + 1;
                    tmpString += score + "</frequency>";
                    Log.v("READER", tmpString);
                    line = tmpString;
                    nextLine = false;
                }
                if(tmpString.equals(toFind))
                {
                    nextLine = true;
                    Log.v("Update exercise", "matched found");
                }
                oldtext += line + "\r\n";
            }
            reader.close();

            //To replace a line in a file
            //String newtext = oldtext.replaceAll("This is test string 20000", "blah blah blah");

            FileWriter writer = new FileWriter(file);
            writer.write(oldtext);writer.close();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    //--------------------------------------------------------------------------------
    // Timer Class
    //--------------------------------------------------------------------------------
    public class Timer extends CountDownTimer {

        public Timer(long time, long interval) {
            super(time, interval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            long msLeft = millisUntilFinished;

            // Calculate remaining minutes
            long minLeft = TimeUnit.MILLISECONDS.toMinutes(msLeft);

            // Calculate remaining seconds
            long secLeft = TimeUnit.MILLISECONDS.toSeconds(msLeft) - TimeUnit.MINUTES.toSeconds(minLeft);

            String timeLeft = String.format("%02d:%02d", minLeft, secLeft);

            timerTextView.setText(timeLeft);
        }

        @Override
        public void onFinish() {
            finalizeTimer();
        }
    }
}
