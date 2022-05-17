package messagelogix.com.k12campusalerts.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ahmed Daou on 1/27/2016.
 */
public class ReportPhoneDetails extends ApiResponse<List<ReportPhoneDetails.Data>>{

    public class Data {

        @SerializedName("ttl_calls")
        @Expose
        private String ttlCalls;
        @SerializedName("disposition")
        @Expose
        private String disposition;

        /**
         *
         * @return
         * The ttlCalls
         */
        public String getTtlCalls() {
            return ttlCalls;
        }

        /**
         *
         * @param ttlCalls
         * The ttl_calls
         */
        public void setTtlCalls(String ttlCalls) {
            this.ttlCalls = ttlCalls;
        }

        /**
         *
         * @return
         * The disposition
         */
        public String getDisposition() {
            return disposition;
        }

        /**
         *
         * @param disposition
         * The disposition
         */
        public void setDisposition(String disposition) {
            this.disposition = disposition;
        }

    }
}
