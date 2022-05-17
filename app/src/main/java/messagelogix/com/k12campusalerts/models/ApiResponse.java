package messagelogix.com.k12campusalerts.models;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ahmed Daou on 1/18/2016.
 */
public abstract class ApiResponse<T> {

    @SerializedName("data")
    @Expose
    private T data;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("errormsg")
    @Expose
    private String errormsg;

    /**
     * @return The data
     */
    public T getData() {
        return data;
    }

    public String getResponseObj() {
        try {
            Gson gson = new Gson();
            String successResponse = gson.toJson(this);
            return successResponse;
        } catch(Exception e) {
            return "Exception encountered when converting response obj to String object: "+e;
        }
    }

    //public abstract T getData();

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

    /**
     * @return The errormsg
     */
    public String getErrormsg() {
        return errormsg;
    }

    /**
     * @param errormsg The errormsg
     */
    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }

}
