package messagelogix.com.k12campusalerts.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ahmed Daou on 2/3/2016.
 */
public class SmartButtonRecipient {

    @Expose
    private List<Data> data;

    @Expose
    protected Boolean success;

    public Boolean getSuccess() {

        return success;
    }

    public List<Data> getData() {

        return this.data;
    }

    public boolean isSuccess() {

        return this.success;
    }

    public class Data {

        @SerializedName("id")
        @Expose
        private String id;

        @SerializedName("locator_id")
        @Expose
        private String locatorId;

        @SerializedName("timestamp")
        @Expose
        private String timestamp;

        @SerializedName("sent_to")
        @Expose
        private String sentTo;

        @SerializedName("sent_type")
        @Expose
        private String sentType;

        @SerializedName("pin_id")
        @Expose
        private String pinId;

        @SerializedName("status")
        @Expose
        private String status;

        @SerializedName("fname")
        @Expose
        private String fname;

        @SerializedName("lname")
        @Expose
        private String lname;

        /**
         * @return The id
         */
        public String getId() {

            return id;
        }

        /**
         * @param id The id
         */
        public void setId(String id) {

            this.id = id;
        }

        /**
         * @return The locatorId
         */
        public String getLocatorId() {

            return locatorId;
        }

        /**
         * @param locatorId The locator_id
         */
        public void setLocatorId(String locatorId) {

            this.locatorId = locatorId;
        }

        /**
         * @return The timestamp
         */
        public String getTimestamp() {

            return timestamp;
        }

        /**
         * @param timestamp The timestamp
         */
        public void setTimestamp(String timestamp) {

            this.timestamp = timestamp;
        }

        /**
         * @return The sentTo
         */
        public String getSentTo() {

            return sentTo;
        }

        /**
         * @param sentTo The sent_to
         */
        public void setSentTo(String sentTo) {

            this.sentTo = sentTo;
        }

        /**
         * @return The sentType
         */
        public String getSentType() {

            return sentType;
        }

        /**
         * @param sentType The sent_type
         */
        public void setSentType(String sentType) {

            this.sentType = sentType;
        }

        /**
         * @return The pinId
         */
        public String getPinId() {

            return pinId;
        }

        /**
         * @param pinId The pin_id
         */
        public void setPinId(String pinId) {

            this.pinId = pinId;
        }

        /**
         * @return The status
         */
        public String getStatus() {

            return status;
        }

        /**
         * @param status The status
         */
        public void setStatus(String status) {

            this.status = status;
        }

        /**
         * @return The fname
         */
        public String getFname() {

            return fname;
        }

        /**
         * @param fname The fname
         */
        public void setFname(String fname) {

            this.fname = fname;
        }

        /**
         * @return The lname
         */
        public String getLname() {

            return lname;
        }

        /**
         * @param lname The lname
         */
        public void setLname(String lname) {

            this.lname = lname;
        }
    }
}
