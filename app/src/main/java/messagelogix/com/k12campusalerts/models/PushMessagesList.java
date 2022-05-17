package messagelogix.com.k12campusalerts.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Program on 4/4/2017.
 */
public class PushMessagesList extends ApiResponse<List<PushMessagesList.Data>> {



    public class Data{



        @SerializedName("acct_id")
        @Expose
        String accountId;

        @SerializedName("schedule_datetime")
        @Expose
        String dateTime;

        @SerializedName("subject")
        @Expose
        String subject;

        @SerializedName("total_sent")
        @Expose
        String total_sent;

        @SerializedName("id")
        @Expose
        String campId;

        @SerializedName("message")
        @Expose
        String message;


//        public Data(String sub, String date, String cId){
//            subject = sub;
//            dateTime = date;
//            campId = cId;
//        }


        public String getSubject(){
            return subject;
        }

        public void setSubject(String subject){
            this.subject = subject;
        }

        public String getDateTime(){
            return dateTime;
        }

        public void setDateTime(String dateTime){
            this.dateTime = dateTime;
        }

        public String getCampId(){
            return campId;
        }

        public void setCampId(String campId){
            this.campId = campId;
        }

        public String getMessage(){
            return message;
        }

        public void setMessage(String message){
            this.message = message;
        }

    }
}
