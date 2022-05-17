package messagelogix.com.k12campusalerts.models;

/**
 * Created by Program on 3/8/2018.
 */
public class SpinnerItem_Incidents {
    private String message;



    public SpinnerItem_Incidents(String xMessage) {

        message = xMessage;

    }

    public String getMessage() {

        return message;
    }

    public void setMessage(String message) {

        this.message = message;
    }
}
