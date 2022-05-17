package messagelogix.com.k12campusalerts.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Richard on 1/29/2016.
 */
public class SmartButtonReportDetails {

    @SerializedName("data")
    @Expose
    private Data data;

    @SerializedName("success")
    @Expose
    private Boolean success;

    /**
     * @return The data
     */
    public Data getData() {

        return data;
    }

    /**
     * @param data The data
     */
    public void setData(Data data) {

        this.data = data;
    }

    /**
     * @return The success
     */
    public Boolean getSuccess() {

        return success;
    }

    /**
     * @param success The success
     */
    public void setSuccess(Boolean success) {

        this.success = success;
    }

    public class Data {

        @SerializedName("id")
        @Expose
        private String id;

        @SerializedName("acct_id")
        @Expose
        private String acctId;

        @SerializedName("timestamp")
        @Expose
        private String timestamp;

        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("title")
        @Expose
        private String title;

        @SerializedName("message")
        @Expose
        private String message;

        @SerializedName("image")
        @Expose
        private String image;

        @SerializedName("school")
        @Expose
        private String school;

        @SerializedName("map_url")
        @Expose
        private String mapUrl;

        @SerializedName("street_address")
        @Expose
        private String streetAddress;

        @SerializedName("Longitude")
        @Expose
        private String Longitude;

        @SerializedName("Latitude")
        @Expose
        private String Latitude;

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
         * @return The acctId
         */
        public String getAcctId() {

            return acctId;
        }

        /**
         * @param acctId The acct_id
         */
        public void setAcctId(String acctId) {

            this.acctId = acctId;
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
         * @return The title
         */
        public String getTitle() {

            return title;
        }

        /**
         * @param title The name
         */
        public void setTitle(String title) {

            this.title = title;
        }

        /**
         * @return The name
         */
        public String getName() {

            return name;
        }

        /**
         * @param name The name
         */
        public void setName(String name) {

            this.name = name;
        }

        /**
         * @return The message
         */
        public String getMessage() {

            return message;
        }

        /**
         * @param message The message
         */
        public void setMessage(String message) {

            this.message = message;
        }

        /**
         * @return The image
         */
        public String getImage() {

            return image;
        }

        /**
         * @param image The image
         */
        public void setImage(String image) {

            this.image = image;
        }

        /**
         * @return The school
         */
        public String getSchool() {

            return school;
        }

        /**
         * @param school The school
         */
        public void setSchool(String school) {

            this.school = school;
        }

        /**
         * @return The mapUrl
         */
        public String getMapUrl() {

            return mapUrl;
        }

        /**
         * @param mapUrl The map_url
         */
        public void setMapUrl(String mapUrl) {

            this.mapUrl = mapUrl;
        }

        /**
         * @return The streetAddress
         */
        public String getStreetAddress() {

            return streetAddress;
        }

        /**
         * @param streetAddress The street_address
         */
        public void setStreetAddress(String streetAddress) {

            this.streetAddress = streetAddress;
        }

        /**
         * @return The Longitude
         */
        public String getLongitude() {

            return Longitude;
        }

        /**
         * @param Longitude The Longitude
         */
        public void setLongitude(String Longitude) {

            this.Longitude = Longitude;
        }

        /**
         * @return The Latitude
         */
        public String getLatitude() {

            return Latitude;
        }

        /**
         * @param Latitude The Latitude
         */
        public void setLatitude(String Latitude) {

            this.Latitude = Latitude;
        }
    }
}
