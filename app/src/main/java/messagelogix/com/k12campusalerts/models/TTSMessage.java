package messagelogix.com.k12campusalerts.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ahmed Daou on 3/9/2016.
 */
public class TTSMessage extends ApiResponse<List<TTSMessage.Data>> {

    public class Data {

        @SerializedName("ttv_id")
        @Expose
        private String ttvId;
        @SerializedName("ttv_acct_id")
        @Expose
        private String ttvAcctId;
        @SerializedName("ttv_u_id")
        @Expose
        private String ttvUId;
        @SerializedName("ttv_name")
        @Expose
        private String ttvName;
        @SerializedName("ttv_message")
        @Expose
        private String ttvMessage;

        /**
         *
         * @return
         * The ttvId
         */
        public String getTtvId() {
            return ttvId;
        }

        /**
         *
         * @param ttvId
         * The ttv_id
         */
        public void setTtvId(String ttvId) {
            this.ttvId = ttvId;
        }

        /**
         *
         * @return
         * The ttvAcctId
         */
        public String getTtvAcctId() {
            return ttvAcctId;
        }

        /**
         *
         * @param ttvAcctId
         * The ttv_acct_id
         */
        public void setTtvAcctId(String ttvAcctId) {
            this.ttvAcctId = ttvAcctId;
        }

        /**
         *
         * @return
         * The ttvUId
         */
        public String getTtvUId() {
            return ttvUId;
        }

        /**
         *
         * @param ttvUId
         * The ttv_u_id
         */
        public void setTtvUId(String ttvUId) {
            this.ttvUId = ttvUId;
        }

        /**
         *
         * @return
         * The ttvName
         */
        public String getTtvName() {
            return ttvName;
        }

        /**
         *
         * @param ttvName
         * The ttv_name
         */
        public void setTtvName(String ttvName) {
            this.ttvName = ttvName;
        }

        /**
         *
         * @return
         * The ttvMessage
         */
        public String getTtvMessage() {
            return ttvMessage;
        }

        /**
         *
         * @param ttvMessage
         * The ttv_message
         */
        public void setTtvMessage(String ttvMessage) {
            this.ttvMessage = ttvMessage;
        }

        @Override
        public String toString(){
            return this.getTtvMessage();
        }
    }
}
