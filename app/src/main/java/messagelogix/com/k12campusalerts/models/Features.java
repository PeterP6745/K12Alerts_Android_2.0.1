package messagelogix.com.k12campusalerts.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ahmed Daou on 1/18/2016.
 */
public class Features extends ApiResponse<Features.Data>{

    public static class Data {

        @SerializedName("1StepAlert")
        @Expose
        private String _1StepAlert;
        @SerializedName("1StepCall")
        @Expose
        private String _1StepCall;
        @SerializedName("rss_feed")
        @Expose
        private String rssFeed;
        @SerializedName("BNotified")
        @Expose
        private String BNotified;
        @SerializedName("TextToSpeech")
        @Expose
        private String TextToSpeech;
        @SerializedName("ViewReports")
        @Expose
        private String ViewReports;
        @SerializedName("SmartButton_push")
        @Expose
        private String sbAlerts;

        /**
         *
         * @return
         * The _1StepAlert
         */
        public String get1StepAlert() {
            return _1StepAlert;
        }

        /**
         *
         * @param _1StepAlert
         * The 1StepAlert
         */
        public void set1StepAlert(String _1StepAlert) {
            this._1StepAlert = _1StepAlert;
        }

        /**
         *
         * @return
         * The _1StepCall
         */
        public String get1StepCall() {
            return _1StepCall;
        }

        /**
         *
         * @param _1StepCall
         * The 1StepCall
         */
        public void set1StepCall(String _1StepCall) {
            this._1StepCall = _1StepCall;
        }

        /**
         *
         * @return
         * The rssFeed
         */
        public String getRssFeed() {
            return rssFeed;
        }

        /**
         *
         * @param rssFeed
         * The rss_feed
         */
        public void setRssFeed(String rssFeed) {
            this.rssFeed = rssFeed;
        }

        /**
         *
         * @return
         * The BNotified
         */
        public String getBNotified() {
            return BNotified;
        }

        public String getSbAlerts() {
            return sbAlerts;
        }

        /**
         *
         * @param BNotified
         * The BNotified
         */
        public void setBNotified(String BNotified) {
            this.BNotified = BNotified;
        }

        /**
         *
         * @return
         * The TextToSpeech
         */
        public String getTextToSpeech() {
            return TextToSpeech;
        }

        /**
         *
         * @param TextToSpeech
         * The TextToSpeech
         */
        public void setTextToSpeech(String TextToSpeech) {
            this.TextToSpeech = TextToSpeech;
        }

        /**
         *
         * @return
         * The ViewReports
         */
        public String getViewReports() {
            return ViewReports;
        }

        /**
         *
         * @param ViewReports
         * The ViewReports
         */
        public void setViewReports(String ViewReports) {
            this.ViewReports = ViewReports;
        }



        public void setSbAlerts(String sbAlerts) {
            this.sbAlerts = sbAlerts;
        }
    }

}
