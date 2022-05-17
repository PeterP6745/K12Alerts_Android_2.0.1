package messagelogix.com.k12campusalerts.models;

/**
 * Created by Ahmed Daou on 1/22/2016.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * A report item representing a piece of content.
 */
public class ReportEmailText extends ApiResponse<List<ReportEmailText.Data>>{
    public final String id;
    public final String content;
    public final String details;

    public ReportEmailText(String id, String content, String details) {
        this.id = id;
        this.content = content;
        this.details = details;
    }

    @Override
    public String toString() {
        return content;
    }



    public class Data {

        @SerializedName("senddate")
        @Expose
        private String senddate;
        @SerializedName("common_id")
        @Expose
        private String commonId;
        @SerializedName("l_name")
        @Expose
        private String lName;
        @SerializedName("camp_name")
        @Expose
        private String campName;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("l_type")
        @Expose
        private String lType;

        /**
         *
         * @return
         * The senddate
         */
        public String getSenddate() {
            return senddate;
        }

        /**
         *
         * @param senddate
         * The senddate
         */
        public void setSenddate(String senddate) {
            this.senddate = senddate;
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
         * The lName
         */
        public String getLName() {
            return lName;
        }

        /**
         *
         * @param lName
         * The l_name
         */
        public void setLName(String lName) {
            this.lName = lName;
        }

        /**
         *
         * @return
         * The campName
         */
        public String getCampName() {
            return campName;
        }

        /**
         *
         * @param campName
         * The camp_name
         */
        public void setCampName(String campName) {
            this.campName = campName;
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
         * The lType
         */
        public String getLType() {
            return lType;
        }

        /**
         *
         * @param lType
         * The l_type
         */
        public void setLType(String lType) {
            this.lType = lType;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }

    }
}