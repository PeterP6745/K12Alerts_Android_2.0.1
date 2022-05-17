package messagelogix.com.k12campusalerts.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Program on 4/10/2017.
 */
public class CampPhone extends ApiResponse<List<CampPhone.Data>> {


    public class Data{

        @SerializedName("timestamp")
        @Expose
        String timeStamp;

        public String getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
        }

        @SerializedName("list")
        @Expose
        String list;

        public String getList() {
            return list;
        }

        public void setList(String list) {
            this.list = list;
        }

        @SerializedName("name")
        @Expose
        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @SerializedName("common_id")
        @Expose
        String common_id;

        public String getCommon_id() {
            return common_id;
        }

        public void setCommon_id(String common_id) {
            this.common_id = common_id;
        }
    }

}
