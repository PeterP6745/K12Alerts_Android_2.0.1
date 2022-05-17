package messagelogix.com.k12campusalerts.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ahmed Daou on 3/9/2016.
 */
public class TTSPreviewResponse extends ApiResponse<TTSPreviewResponse.Data>{

    public final static String URL = "url";
    public final static String FILENAME = "filename";
    public class Data {

        @SerializedName("filename")
        @Expose
        private String filename;
        @SerializedName("url")
        @Expose
        private String url;

        /**
         *
         * @return
         * The filename
         */
        public String getFilename() {
            return filename;
        }

        /**
         *
         * @param filename
         * The filename
         */
        public void setFilename(String filename) {
            this.filename = filename;
        }

        /**
         *
         * @return
         * The url
         */
        public String getUrl() {
            return url;
        }

        /**
         *
         * @param url
         * The url
         */
        public void setUrl(String url) {
            this.url = url;
        }

    }
}