package messagelogix.com.k12campusalerts.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Created by Program on 4/4/2017.
 */
public class TotalDevices extends ApiResponse<TotalDevices.Data>{
    public final String id;
    public final String content;
    public final String details;

    public TotalDevices(String id, String content, String details){
        this.id = id;
        this.content = content;
        this.details = details;
    }

    @Override
    public String toString() {
        return content;
    }

    public class Data{
        @SerializedName("total_device_1")
        @Expose
        private String iOSDevices;

        @SerializedName("total_device_2")
        @Expose
        private String androidDevices;

        @SerializedName("total_devices")
        @Expose
        private String totalDevices;

        public String getiOSDevices(){
            return iOSDevices;
        }
        public void setiOSDevices(String iOSDevices){
            this.iOSDevices = iOSDevices;
        }


        public String getAndroidDevices(){
            return androidDevices;
        }
        public void setAndroidDevices(String androidDevices){
            this.androidDevices = androidDevices;
        }


        public String getTotalDevices(){
            return totalDevices;
        }

        public void setTotalDevices(String totalDevices){
            this.totalDevices = totalDevices;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }



}
