package messagelogix.com.k12campusalerts.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ahmed Daou on 1/22/2016.
 */
public class ReportPhone extends ApiResponse<List<ReportPhone.Data>>{

    public final String id;
    public final String content;
    public final String details;

    public ReportPhone(String id, String content, String details) {
        this.id = id;
        this.content = content;
        this.details = details;
    }

    @Override
    public String toString() {
        return content;
    }

    public class Data {

        @SerializedName("camp_id")
        @Expose
        private String campId;
        @SerializedName("acct_id")
        @Expose
        private String acctId;
        @SerializedName("common_id")
        @Expose
        private String commonId;
        @SerializedName("pin")
        @Expose
        private String pin;
        @SerializedName("timestamp")
        @Expose
        private String timestamp;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("list")
        @Expose
        private String list;
        @SerializedName("msg_num")
        @Expose
        private String msgNum;
        @SerializedName("alert_type")
        @Expose
        private String alertType;
        @SerializedName("polling")
        @Expose
        private String polling;
        @SerializedName("status_typ")
        @Expose
        private String statusTyp;
        @SerializedName("count_primary")
        @Expose
        private String countPrimary;
        @SerializedName("count_nonprimary")
        @Expose
        private String countNonprimary;

        /**
         *
         * @return
         * The campId
         */
        public String getCampId() {
            return campId;
        }

        /**
         *
         * @param campId
         * The camp_id
         */
        public void setCampId(String campId) {
            this.campId = campId;
        }

        /**
         *
         * @return
         * The acctId
         */
        public String getAcctId() {
            return acctId;
        }

        /**
         *
         * @param acctId
         * The acct_id
         */
        public void setAcctId(String acctId) {
            this.acctId = acctId;
        }

        /**
         *
         * @return
         * The commonId
         */
        public String getCommonId() {
            return commonId;
        }

        /**
         *
         * @param commonId
         * The common_id
         */
        public void setCommonId(String commonId) {
            this.commonId = commonId;
        }

        /**
         *
         * @return
         * The pin
         */
        public String getPin() {
            return pin;
        }

        /**
         *
         * @param pin
         * The pin
         */
        public void setPin(String pin) {
            this.pin = pin;
        }

        /**
         *
         * @return
         * The timestamp
         */
        public String getTimestamp() {
            return timestamp;
        }

        /**
         *
         * @param timestamp
         * The timestamp
         */
        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        /**
         *
         * @return
         * The status
         */
        public String getStatus() {
            return status;
        }

        /**
         *
         * @param status
         * The status
         */
        public void setStatus(String status) {
            this.status = status;
        }

        /**
         *
         * @return
         * The list
         */
        public String getList() {
            return list;
        }

        /**
         *
         * @param list
         * The list
         */
        public void setList(String list) {
            this.list = list;
        }

        /**
         *
         * @return
         * The msgNum
         */
        public String getMsgNum() {
            return msgNum;
        }

        /**
         *
         * @param msgNum
         * The msg_num
         */
        public void setMsgNum(String msgNum) {
            this.msgNum = msgNum;
        }

        /**
         *
         * @return
         * The alertType
         */
        public String getAlertType() {
            return alertType;
        }

        /**
         *
         * @param alertType
         * The alert_type
         */
        public void setAlertType(String alertType) {
            this.alertType = alertType;
        }

        /**
         *
         * @return
         * The polling
         */
        public String getPolling() {
            return polling;
        }

        /**
         *
         * @param polling
         * The polling
         */
        public void setPolling(String polling) {
            this.polling = polling;
        }

        /**
         *
         * @return
         * The statusTyp
         */
        public String getStatusTyp() {
            return statusTyp;
        }

        /**
         *
         * @param statusTyp
         * The status_typ
         */
        public void setStatusTyp(String statusTyp) {
            this.statusTyp = statusTyp;
        }

        /**
         *
         * @return
         * The countPrimary
         */
        public String getCountPrimary() {
            return countPrimary;
        }

        /**
         *
         * @param countPrimary
         * The count_primary
         */
        public void setCountPrimary(String countPrimary) {
            this.countPrimary = countPrimary;
        }

        /**
         *
         * @return
         * The countNonprimary
         */
        public String getCountNonprimary() {
            return countNonprimary;
        }

        /**
         *
         * @param countNonprimary
         * The count_nonprimary
         */
        public void setCountNonprimary(String countNonprimary) {
            this.countNonprimary = countNonprimary;
        }

    }
}
