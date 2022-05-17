package messagelogix.com.k12campusalerts.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ahmed Daou on 1/21/2016.
 */
public class CannedMessage extends ApiResponse<List<CannedMessage.Data>>{

    public class Data {

        @SerializedName("val")
        @Expose
        private String val;
        @SerializedName("opt")
        @Expose
        private String opt;
        @SerializedName("camp_txt")
        @Expose
        private String campTxt;

        /**
         *
         * @return
         * The val
         */
        public String getVal() {
            return val;
        }

        /**
         *
         * @param val
         * The val
         */
        public void setVal(String val) {
            this.val = val;
        }

        /**
         *
         * @return
         * The opt
         */
        public String getOpt() {
            return opt;
        }

        /**
         *
         * @param opt
         * The opt
         */
        public void setOpt(String opt) {
            this.opt = opt;
        }

        /**
         *
         * @return
         * The campTxt
         */
        public String getCampTxt() {
            return campTxt;
        }

        /**
         *
         * @param campTxt
         * The camp_txt
         */
        public void setCampTxt(String campTxt) {
            this.campTxt = campTxt;
        }

        @Override
        public String toString(){
            return this.getCampTxt();
        }

    }
}
