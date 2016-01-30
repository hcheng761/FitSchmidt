package groupg.fitschmidt;

//Made this because passing bundles kept crashing the app.

public class MessageBoolean {
    private static final MessageBoolean instance = new MessageBoolean();
    private boolean msgBool = false;
    private MessageBoolean(){}

    public static MessageBoolean getInstance(){
        return instance;
    }

    public boolean getValue(){
        return msgBool;
    }

    public void setValue(boolean newValue){
        msgBool = newValue;
    }
}
