package groupg.fitschmidt;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapted from "How to use Simple XMLPullParser in Android Applications",
 * by Indragni Soft Solutions (Youtube)
 */
public class XMLParser {

    ArrayList<Exercise> exerciseList;

    private Exercise exercise;
    private String data;

    public XMLParser() {
        exerciseList = new ArrayList<>();
    }

    public ArrayList<Exercise> getExercises() { return exerciseList; }

/*    public void increaseScore(Exercise exercise) {
        String toFind = "<name>" + exercise.getName() + "</name>";
        try
        {
            File file = new File( getFilesDir(), "exercises_data.xml");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = "", oldtext = "";
            boolean nextLine = false;
            while((line = reader.readLine()) != null)
            {
                String tmpString = line.trim();
                if(nextLine)
                {
                    tmpString = "";
                    for(int i = 0; i < 14; i++){
                        tmpString += line.charAt(i);
                    }

                    line = line.substring(14, line.length() - 7);
                    int score = Integer.parseInt(line) + 1;
                    tmpString += score + "</time>";

                    line = tmpString;
                    nextLine = false;
                }
                if(tmpString == toFind)
                {
                    nextLine = true;
                }
                oldtext += line + "\r\n";
            }
            reader.close();

            //To replace a line in a file
            //String newtext = oldtext.replaceAll("This is test string 20000", "blah blah blah");

            FileWriter writer = new FileWriter("exercises_data.xml");
            writer.write(oldtext);writer.close();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
*/
    public ArrayList<Exercise> parseExercises(InputStream inputFile) {
        XmlPullParserFactory factory = null;
        XmlPullParser parser = null;

        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);

            parser = factory.newPullParser();
            parser.setInput(inputFile, null);

            int eventType = parser.getEventType();

            // Keep reading till the end of XML file
            while(eventType != XmlPullParser.END_DOCUMENT) {
                String tag = parser.getName();

                switch(eventType) {
                    // First tag
                    case XmlPullParser.START_TAG:
                        if(tag.equalsIgnoreCase("exercise"))
                            exercise = new Exercise();
                        break;
                    case XmlPullParser.TEXT:
                        data = parser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        // If exercise closing tag, add to list
                        if(tag.equalsIgnoreCase("exercise"))
                            exerciseList.add(exercise);
                        else if(tag.equalsIgnoreCase("name"))
                            exercise.setName(data);
                        else if(tag.equalsIgnoreCase("id"))
                            exercise.setFileId(Integer.parseInt(data));
                        else if(tag.equalsIgnoreCase("time"))
                            exercise.setTotalTime(Integer.parseInt(data));
                        else if(tag.equalsIgnoreCase("frequency"))
                            exercise.setFrequency(Integer.parseInt(data));
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return exerciseList;
    }
}
