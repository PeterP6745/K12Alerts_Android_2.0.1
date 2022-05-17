package messagelogix.com.k12campusalerts.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Program on 6/1/2016.
 */
public class VoiceMessage extends ApiResponse<List<VoiceMessage.Data>> {


    public class Data {
        @SerializedName("account_id")
        @Expose
        private String accountId;
        @SerializedName("pin")
        @Expose
        private String pin;
        @SerializedName("canned_slot")
        @Expose
        private String cannedSlot;
        @SerializedName("canned_file")
        @Expose
        private String cannedFile;
        @SerializedName("canned_name")
        @Expose
        private String cannedName;
        @SerializedName("osn_caller_id")
        @Expose
        private Object osnCallerId;
        @SerializedName("url")
        @Expose
        private String url;

        /**
         * @return The accountId
         */
        public String getAccountId() {
            return accountId;
        }

        /**
         * @param accountId The account_id
         */
        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        /**
         * @return The pin
         */
        public String getPin() {
            return pin;
        }

        /**
         * @param pin The pin
         */
        public void setPin(String pin) {
            this.pin = pin;
        }

        /**
         * @return The cannedSlot
         */
        public String getCannedSlot() {
            return cannedSlot;
        }

        /**
         * @param cannedSlot The canned_slot
         */
        public void setCannedSlot(String cannedSlot) {
            this.cannedSlot = cannedSlot;
        }

        /**
         * @return The cannedFile
         */
        public String getCannedFile() {
            return cannedFile;
        }

        /**
         * @param cannedFile The canned_file
         */
        public void setCannedFile(String cannedFile) {
            this.cannedFile = cannedFile;
        }

        /**
         * @return The cannedName
         */
        public String getCannedName() {
            return cannedName;
        }

        /**
         * @param cannedName The canned_name
         */
        public void setCannedName(String cannedName) {
            this.cannedName = cannedName;
        }

        /**
         * @return The osnCallerId
         */
        public Object getOsnCallerId() {
            return osnCallerId;
        }

        /**
         * @param osnCallerId The osn_caller_id
         */
        public void setOsnCallerId(Object osnCallerId) {
            this.osnCallerId = osnCallerId;
        }

        /**
         * @return The url
         */
        public String getUrl() {
            return url;
        }

        /**
         * @param url The url
         */
        public void setUrl(String url) {
            this.url = url;
        }

    }
}