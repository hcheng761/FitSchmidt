package groupg.fitschmidt;

import java.io.Serializable;

/**
 * Created by Noemie on 22-Nov-15.
 */
public class Exercise implements Serializable {
    String name;    // Exercise label
    int fileId;     // Exercise resource ID
    int totalTime;  // Total time performed in ms
    int frequency;  // Amount of times performed

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

}
