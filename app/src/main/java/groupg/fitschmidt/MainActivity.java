package groupg.fitschmidt;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {
    // MEMBER VARIABLES
    List<Exercise> exerciseList;

    Button startButton;
    TextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = (Button) findViewById(R.id.start_button);
        File file = getApplicationContext().getFileStreamPath("exercises_data.xml");
        if(file.exists())
        {

        }
        else {
            InputStream is = null;
            try {
                is = getResources().getAssets().open("exercises_data.xml");
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                String textfile = convertStreamToString(is);
                FileOutputStream fOut = openFileOutput("exercises_data.xml",
                        MODE_WORLD_WRITEABLE);
                OutputStreamWriter osw = new OutputStreamWriter(fOut);

                // Write the string to the file
                osw.write(textfile);

       /* ensure that everything is
        * really written out and close */
                osw.flush();
                osw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        XMLParser parser = new XMLParser();
        try {
            exerciseList = parser.parseExercises(openFileInput("exercises_data.xml"));
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        //Create a graphview for storing the graph
        GraphView graph = (GraphView) findViewById(R.id.graph);
        //Make a Bar Graph and fill it with points from the xml document
        BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(new DataPoint[]{
                //For now, points use frequency field from xml data
                new DataPoint(0,exerciseList.get(0).getFrequency()),
                new DataPoint(1,exerciseList.get(1).getFrequency()),
                new DataPoint(2,exerciseList.get(2).getFrequency()),
                new DataPoint(3,exerciseList.get(3).getFrequency()),
                new DataPoint(4,exerciseList.get(4).getFrequency()),
                new DataPoint(5,exerciseList.get(5).getFrequency()),
                new DataPoint(6,exerciseList.get(6).getFrequency()),
                new DataPoint(7,exerciseList.get(7).getFrequency()),
                new DataPoint(8,exerciseList.get(8).getFrequency()),
                new DataPoint(9,exerciseList.get(9).getFrequency())
        });
        //Set up formatter for labels
        StaticLabelsFormatter slf = new StaticLabelsFormatter(graph);
        //Set Horizontal Labels for the Graph
        slf.setHorizontalLabels(new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"});
        //Add formatter to the graphview
        graph.getGridLabelRenderer().setLabelFormatter(slf);
        //Add title to graph
        graph.setTitle("Exercises Frequency");
        graph.setTitleTextSize(42);
        //Add graph to series
        graph.addSeries(series);
    }

    /**
     * Start button handler
     */
    public void startWorkout(View view) {
        Intent intent = new Intent(this, RandomizerActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        // super.onBackPressed();
    }

    public static String convertStreamToString(InputStream is)
            throws IOException {
        Writer writer = new StringWriter();

        char[] buffer = new char[2048];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is,
                    "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
            is.close();
        }
        String text = writer.toString();
        return text;
    }
}
