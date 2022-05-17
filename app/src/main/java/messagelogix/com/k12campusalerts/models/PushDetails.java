package messagelogix.com.k12campusalerts.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Program on 4/5/2017.
 */
public class PushDetails extends ApiResponse<PushDetails.Data> {

    public class Data{
        @SerializedName("schedule_datetime")
        @Expose
        String dateTime;

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        @SerializedName("subject")
        @Expose
        String subject;

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        @SerializedName("message")
        @Expose
        String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @SerializedName("total_device_1")
        @Expose
        String iosDevices;

        public String getIosDevices() {
            return iosDevices;
        }

        public void setIosDevices(String iosDevices) {
            this.iosDevices = iosDevices;
        }

        @SerializedName("total_device_2")
        @Expose
        String otherDevices;

        public String getOtherDevices() {
            return otherDevices;
        }

        public void setOtherDevices(String otherDevices) {
            this.otherDevices = otherDevices;
        }

        @SerializedName("total_sent")
        @Expose
        String totalDevices;

        public String getTotalDevices() {
            return totalDevices;
        }

        public void setTotalDevices(String totalDevices) {
            this.totalDevices = totalDevices;
        }
    }
}
