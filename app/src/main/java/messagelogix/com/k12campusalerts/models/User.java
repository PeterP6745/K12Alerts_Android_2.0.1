package messagelogix.com.k12campusalerts.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ahmed Daou on 1/20/2016.
 */
public class User extends ApiResponse<User.Data> {

    public class Data {

        @SerializedName("pin_id")
        @Expose
        private String pinId;
        @SerializedName("acct_id")
        @Expose
        private String acctId;
        @SerializedName("account_id")
        @Expose
        private String accountId;
        @SerializedName("pin")
        @Expose
        private String pin;
        @SerializedName("cidname")
        @Expose
        private String cidname;
        @SerializedName("cidarea")
        @Expose
        private String cidarea;
        @SerializedName("fname")
        @Expose
        private String fname;
        @SerializedName("lname")
        @Expose
        private String lname;
        @SerializedName("opt_max_retry")
        @Expose
        private String optMaxRetry;
        @SerializedName("opt_retry_time")
        @Expose
        private String optRetryTime;
        @SerializedName("opt_wait_time")
        @Expose
        private String optWaitTime;
        @SerializedName("call_primary")
        @Expose
        private String callPrimary;
        @SerializedName("send_ALERT_via_PGI")
        @Expose
        private String sendALERTViaPGI;
        @SerializedName("send_ATD_via_PGI")
        @Expose
        private String sendATDViaPGI;
        @SerializedName("superuser")
        @Expose
        private String superuser;
        @SerializedName("pin_email1")
        @Expose
        private String pinEmail1;
        @SerializedName("pin_email2")
        @Expose
        private Object pinEmail2;
        @SerializedName("pin_phone_no")
        @Expose
        private Object pinPhoneNo;
        @SerializedName("pin_phone_no_ext")
        @Expose
        private Object pinPhoneNoExt;
        @SerializedName("pin_sms_phone_no")
        @Expose
        private String pinSmsPhoneNo;
        @SerializedName("pin_sms_carrier_id")
        @Expose
        private String pinSmsCarrierId;
        @SerializedName("ftp_notify")
        @Expose
        private String ftpNotify;
        @SerializedName("send_staff_via_PGI")
        @Expose
        private String sendStaffViaPGI;
        @SerializedName("send_retry_via_PGI")
        @Expose
        private String sendRetryViaPGI;
        @SerializedName("bd_phn_dt")
        @Expose
        private String bdPhnDt;
        @SerializedName("play_ding")
        @Expose
        private Object playDing;
        @SerializedName("send_sms_via_PMTA")
        @Expose
        private String sendSmsViaPMTA;
        @SerializedName("pin_list_000")
        @Expose
        private String pinList000;
        @SerializedName("school_code")
        @Expose
        private String schoolCode;
        @SerializedName("aalert_type")
        @Expose
        private String aalertType;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("master_pin")
        @Expose
        private String masterPin;
        @SerializedName("accounttype")
        @Expose
        private String accounttype;
        @SerializedName("call_time_zone")
        @Expose
        private String callTimeZone;
        @SerializedName("main_act_id")
        @Expose
        private String mainActId;
        @SerializedName("dflt_email")
        @Expose
        private String dfltEmail;
        @SerializedName("defaultLat")
        @Expose
        private String defaultLat;
        @SerializedName("defaultLon")
        @Expose
        private String defaultLon;
        @SerializedName("defaultMapZoom")
        @Expose
        private String defaultMapZoom;
        @SerializedName("cust_atd_typ")
        @Expose
        private String custAtdTyp;
        @SerializedName("ttv_trans")
        @Expose
        private String ttvTrans;
        @SerializedName("family_id_login")
        @Expose
        private String familyIdLogin;
        @SerializedName("onebutton_push")
        @Expose
        private String onebuttonPush;
        @SerializedName("powerbuilder")
        @Expose
        private String powerbuilder;
        @SerializedName("upload_updates")
        @Expose
        private String uploadUpdates;
        @SerializedName("onestep_notify")
        @Expose
        private String onestepNotify;
        @SerializedName("max_canned")
        @Expose
        private String maxCanned;
        @SerializedName("prtl_attr")
        @Expose
        private String prtlAttr;
        @SerializedName("lang_filter")
        @Expose
        private String langFilter;
        @SerializedName("ftp_exe")
        @Expose
        private String ftpExe;
        @SerializedName("social_media")
        @Expose
        private String socialMedia;
        @SerializedName("alert_status")
        @Expose
        private String alertStatus;
        @SerializedName("email_by_pin")
        @Expose
        private String emailByPin;
        @SerializedName("aalert_active")
        @Expose
        private String aalertActive;
        @SerializedName("image_library")
        @Expose
        private String imageLibrary;
        @SerializedName("rss_feed")
        @Expose
        private String rssFeed;
        @SerializedName("account_name")
        @Expose
        private String accountName;
        @SerializedName("aalert_groups")
        @Expose
        private String aalertGroups;
        @SerializedName("email_toggle")
        @Expose
        private String emailToggle;
        @SerializedName("call_length")
        @Expose
        private int callLength;
        @SerializedName("aa_contacts_by_building")
        @Expose
        private String aaContactsByBuilding;
        @SerializedName("masterpin_setup")
        @Expose
        private String masterpinSetup;
        @SerializedName("aa_location_services")
        @Expose
        private String aaLocationServices;
        @SerializedName("aa_submitter")
        @Expose
        private String aaSubmitter;
        @SerializedName("push_to_app")
        @Expose
        private String pushToApp;
        @SerializedName("ttv_merged_message")
        @Expose
        private String ttvMergedMessage;
        @SerializedName("onebuttonalert_call")
        @Expose
        private String onebuttonalertCall;
        @SerializedName("app_active")
        @Expose
        private String appActive;
        @SerializedName("rss_feed_folder")
        @Expose
        private String rssFeedFolder;
        @SerializedName("aa_report_type")
        @Expose
        private String aaReportType;
        @SerializedName("locator")
        @Expose
        private String locator;
        @SerializedName("account")
        @Expose
        private String account;

        @Expose
        private String contactByBuilding;


        public String getContactByBuilding() {
            return contactByBuilding;
        }

        public void setContactByBuilding(String contactByBuilding) {
            this.contactByBuilding = contactByBuilding;
        }

        /**
         *
         * @return
         * The pinId
         */
        public String getPinId() {
            return pinId;
        }

        /**
         *
         * @param pinId
         * The pin_id
         */
        public void setPinId(String pinId) {
            this.pinId = pinId;
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
         * The accountId
         */
        public String getAccountId() {
            return accountId;
        }

        /**
         *
         * @param accountId
         * The account_id
         */
        public void setAccountId(String accountId) {
            this.accountId = accountId;
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
         * The cidname
         */
        public String getCidname() {
            return cidname;
        }

        /**
         *
         * @param cidname
         * The cidname
         */
        public void setCidname(String cidname) {
            this.cidname = cidname;
        }

        /**
         *
         * @return
         * The cidarea
         */
        public String getCidarea() {
            return cidarea;
        }

        /**
         *
         * @param cidarea
         * The cidarea
         */
        public void setCidarea(String cidarea) {
            this.cidarea = cidarea;
        }

        /**
         *
         * @return
         * The fname
         */
        public String getFname() {
            return fname;
        }

        /**
         *
         * @param fname
         * The fname
         */
        public void setFname(String fname) {
            this.fname = fname;
        }

        /**
         *
         * @return
         * The lname
         */
        public String getLname() {
            return lname;
        }

        /**
         *
         * @param lname
         * The lname
         */
        public void setLname(String lname) {
            this.lname = lname;
        }

        /**
         *
         * @return
         * The optMaxRetry
         */
        public String getOptMaxRetry() {
            return optMaxRetry;
        }

        /**
         *
         * @param optMaxRetry
         * The opt_max_retry
         */
        public void setOptMaxRetry(String optMaxRetry) {
            this.optMaxRetry = optMaxRetry;
        }

        /**
         *
         * @return
         * The optRetryTime
         */
        public String getOptRetryTime() {
            return optRetryTime;
        }

        /**
         *
         * @param optRetryTime
         * The opt_retry_time
         */
        public void setOptRetryTime(String optRetryTime) {
            this.optRetryTime = optRetryTime;
        }

        /**
         *
         * @return
         * The optWaitTime
         */
        public String getOptWaitTime() {
            return optWaitTime;
        }

        /**
         *
         * @param optWaitTime
         * The opt_wait_time
         */
        public void setOptWaitTime(String optWaitTime) {
            this.optWaitTime = optWaitTime;
        }

        /**
         *
         * @return
         * The callPrimary
         */
        public String getCallPrimary() {
            return callPrimary;
        }

        /**
         *
         * @param callPrimary
         * The call_primary
         */
        public void setCallPrimary(String callPrimary) {
            this.callPrimary = callPrimary;
        }

        /**
         *
         * @return
         * The sendALERTViaPGI
         */
        public String getSendALERTViaPGI() {
            return sendALERTViaPGI;
        }

        /**
         *
         * @param sendALERTViaPGI
         * The send_ALERT_via_PGI
         */
        public void setSendALERTViaPGI(String sendALERTViaPGI) {
            this.sendALERTViaPGI = sendALERTViaPGI;
        }

        /**
         *
         * @return
         * The sendATDViaPGI
         */
        public String getSendATDViaPGI() {
            return sendATDViaPGI;
        }

        /**
         *
         * @param sendATDViaPGI
         * The send_ATD_via_PGI
         */
        public void setSendATDViaPGI(String sendATDViaPGI) {
            this.sendATDViaPGI = sendATDViaPGI;
        }

        /**
         *
         * @return
         * The superuser
         */
        public String getSuperuser() {
            return superuser;
        }

        /**
         *
         * @param superuser
         * The superuser
         */
        public void setSuperuser(String superuser) {
            this.superuser = superuser;
        }

        /**
         *
         * @return
         * The pinEmail1
         */
        public String getPinEmail1() {
            return pinEmail1;
        }

        /**
         *
         * @param pinEmail1
         * The pin_email1
         */
        public void setPinEmail1(String pinEmail1) {
            this.pinEmail1 = pinEmail1;
        }

        /**
         *
         * @return
         * The pinEmail2
         */
        public Object getPinEmail2() {
            return pinEmail2;
        }

        /**
         *
         * @param pinEmail2
         * The pin_email2
         */
        public void setPinEmail2(Object pinEmail2) {
            this.pinEmail2 = pinEmail2;
        }

        /**
         *
         * @return
         * The pinPhoneNo
         */
        public Object getPinPhoneNo() {
            return pinPhoneNo;
        }

        /**
         *
         * @param pinPhoneNo
         * The pin_phone_no
         */
        public void setPinPhoneNo(Object pinPhoneNo) {
            this.pinPhoneNo = pinPhoneNo;
        }

        /**
         *
         * @return
         * The pinPhoneNoExt
         */
        public Object getPinPhoneNoExt() {
            return pinPhoneNoExt;
        }

        /**
         *
         * @param pinPhoneNoExt
         * The pin_phone_no_ext
         */
        public void setPinPhoneNoExt(Object pinPhoneNoExt) {
            this.pinPhoneNoExt = pinPhoneNoExt;
        }

        /**
         *
         * @return
         * The pinSmsPhoneNo
         */
        public String getPinSmsPhoneNo() {
            return pinSmsPhoneNo;
        }

        /**
         *
         * @param pinSmsPhoneNo
         * The pin_sms_phone_no
         */
        public void setPinSmsPhoneNo(String pinSmsPhoneNo) {
            this.pinSmsPhoneNo = pinSmsPhoneNo;
        }

        /**
         *
         * @return
         * The pinSmsCarrierId
         */
        public String getPinSmsCarrierId() {
            return pinSmsCarrierId;
        }

        /**
         *
         * @param pinSmsCarrierId
         * The pin_sms_carrier_id
         */
        public void setPinSmsCarrierId(String pinSmsCarrierId) {
            this.pinSmsCarrierId = pinSmsCarrierId;
        }

        /**
         *
         * @return
         * The ftpNotify
         */
        public String getFtpNotify() {
            return ftpNotify;
        }

        /**
         *
         * @param ftpNotify
         * The ftp_notify
         */
        public void setFtpNotify(String ftpNotify) {
            this.ftpNotify = ftpNotify;
        }

        /**
         *
         * @return
         * The sendStaffViaPGI
         */
        public String getSendStaffViaPGI() {
            return sendStaffViaPGI;
        }

        /**
         *
         * @param sendStaffViaPGI
         * The send_staff_via_PGI
         */
        public void setSendStaffViaPGI(String sendStaffViaPGI) {
            this.sendStaffViaPGI = sendStaffViaPGI;
        }

        /**
         *
         * @return
         * The sendRetryViaPGI
         */
        public String getSendRetryViaPGI() {
            return sendRetryViaPGI;
        }

        /**
         *
         * @param sendRetryViaPGI
         * The send_retry_via_PGI
         */
        public void setSendRetryViaPGI(String sendRetryViaPGI) {
            this.sendRetryViaPGI = sendRetryViaPGI;
        }

        /**
         *
         * @return
         * The bdPhnDt
         */
        public String getBdPhnDt() {
            return bdPhnDt;
        }

        /**
         *
         * @param bdPhnDt
         * The bd_phn_dt
         */
        public void setBdPhnDt(String bdPhnDt) {
            this.bdPhnDt = bdPhnDt;
        }

        /**
         *
         * @return
         * The playDing
         */
        public Object getPlayDing() {
            return playDing;
        }

        /**
         *
         * @param playDing
         * The play_ding
         */
        public void setPlayDing(Object playDing) {
            this.playDing = playDing;
        }

        /**
         *
         * @return
         * The sendSmsViaPMTA
         */
        public String getSendSmsViaPMTA() {
            return sendSmsViaPMTA;
        }

        /**
         *
         * @param sendSmsViaPMTA
         * The send_sms_via_PMTA
         */
        public void setSendSmsViaPMTA(String sendSmsViaPMTA) {
            this.sendSmsViaPMTA = sendSmsViaPMTA;
        }

        /**
         *
         * @return
         * The pinList000
         */
        public String getPinList000() {
            return pinList000;
        }

        /**
         *
         * @param pinList000
         * The pin_list_000
         */
        public void setPinList000(String pinList000) {
            this.pinList000 = pinList000;
        }

        /**
         *
         * @return
         * The schoolCode
         */
        public String getSchoolCode() {
            return schoolCode;
        }

        /**
         *
         * @param schoolCode
         * The school_code
         */
        public void setSchoolCode(String schoolCode) {
            this.schoolCode = schoolCode;
        }

        /**
         *
         * @return
         * The aalertType
         */
        public String getAalertType() {
            return aalertType;
        }

        /**
         *
         * @param aalertType
         * The aalert_type
         */
        public void setAalertType(String aalertType) {
            this.aalertType = aalertType;
        }

        /**
         *
         * @return
         * The title
         */
        public String getTitle() {
            return title;
        }

        /**
         *
         * @param title
         * The title
         */
        public void setTitle(String title) {
            this.title = title;
        }

        /**
         *
         * @return
         * The masterPin
         */
        public String getMasterPin() {
            return masterPin;
        }

        /**
         *
         * @param masterPin
         * The master_pin
         */
        public void setMasterPin(String masterPin) {
            this.masterPin = masterPin;
        }

        /**
         *
         * @return
         * The accounttype
         */
        public String getAccounttype() {
            return accounttype;
        }

        /**
         *
         * @param accounttype
         * The accounttype
         */
        public void setAccounttype(String accounttype) {
            this.accounttype = accounttype;
        }

        /**
         *
         * @return
         * The callTimeZone
         */
        public String getCallTimeZone() {
            return callTimeZone;
        }

        /**
         *
         * @param callTimeZone
         * The call_time_zone
         */
        public void setCallTimeZone(String callTimeZone) {
            this.callTimeZone = callTimeZone;
        }

        /**
         *
         * @return
         * The mainActId
         */
        public String getMainActId() {
            return mainActId;
        }

        /**
         *
         * @param mainActId
         * The main_act_id
         */
        public void setMainActId(String mainActId) {
            this.mainActId = mainActId;
        }

        /**
         *
         * @return
         * The dfltEmail
         */
        public String getDfltEmail() {
            return dfltEmail;
        }

        /**
         *
         * @param dfltEmail
         * The dflt_email
         */
        public void setDfltEmail(String dfltEmail) {
            this.dfltEmail = dfltEmail;
        }

        /**
         *
         * @return
         * The defaultLat
         */
        public String getDefaultLat() {
            return defaultLat;
        }

        /**
         *
         * @param defaultLat
         * The defaultLat
         */
        public void setDefaultLat(String defaultLat) {
            this.defaultLat = defaultLat;
        }

        /**
         *
         * @return
         * The defaultLon
         */
        public String getDefaultLon() {
            return defaultLon;
        }

        /**
         *
         * @param defaultLon
         * The defaultLon
         */
        public void setDefaultLon(String defaultLon) {
            this.defaultLon = defaultLon;
        }

        /**
         *
         * @return
         * The defaultMapZoom
         */
        public String getDefaultMapZoom() {
            return defaultMapZoom;
        }

        /**
         *
         * @param defaultMapZoom
         * The defaultMapZoom
         */
        public void setDefaultMapZoom(String defaultMapZoom) {
            this.defaultMapZoom = defaultMapZoom;
        }

        /**
         *
         * @return
         * The custAtdTyp
         */
        public String getCustAtdTyp() {
            return custAtdTyp;
        }

        /**
         *
         * @param custAtdTyp
         * The cust_atd_typ
         */
        public void setCustAtdTyp(String custAtdTyp) {
            this.custAtdTyp = custAtdTyp;
        }

        /**
         *
         * @return
         * The ttvTrans
         */
        public String getTtvTrans() {
            return ttvTrans;
        }

        /**
         *
         * @param ttvTrans
         * The ttv_trans
         */
        public void setTtvTrans(String ttvTrans) {
            this.ttvTrans = ttvTrans;
        }

        /**
         *
         * @return
         * The familyIdLogin
         */
        public String getFamilyIdLogin() {
            return familyIdLogin;
        }

        /**
         *
         * @param familyIdLogin
         * The family_id_login
         */
        public void setFamilyIdLogin(String familyIdLogin) {
            this.familyIdLogin = familyIdLogin;
        }

        /**
         *
         * @return
         * The onebuttonPush
         */
        public String getOnebuttonPush() {
            return onebuttonPush;
        }

        /**
         *
         * @param onebuttonPush
         * The onebutton_push
         */
        public void setOnebuttonPush(String onebuttonPush) {
            this.onebuttonPush = onebuttonPush;
        }

        /**
         *
         * @return
         * The powerbuilder
         */
        public String getPowerbuilder() {
            return powerbuilder;
        }

        /**
         *
         * @param powerbuilder
         * The powerbuilder
         */
        public void setPowerbuilder(String powerbuilder) {
            this.powerbuilder = powerbuilder;
        }

        /**
         *
         * @return
         * The uploadUpdates
         */
        public String getUploadUpdates() {
            return uploadUpdates;
        }

        /**
         *
         * @param uploadUpdates
         * The upload_updates
         */
        public void setUploadUpdates(String uploadUpdates) {
            this.uploadUpdates = uploadUpdates;
        }

        /**
         *
         * @return
         * The onestepNotify
         */
        public String getOnestepNotify() {
            return onestepNotify;
        }

        /**
         *
         * @param onestepNotify
         * The onestep_notify
         */
        public void setOnestepNotify(String onestepNotify) {
            this.onestepNotify = onestepNotify;
        }

        /**
         *
         * @return
         * The maxCanned
         */
        public String getMaxCanned() {
            return maxCanned;
        }

        /**
         *
         * @param maxCanned
         * The max_canned
         */
        public void setMaxCanned(String maxCanned) {
            this.maxCanned = maxCanned;
        }

        /**
         *
         * @return
         * The prtlAttr
         */
        public String getPrtlAttr() {
            return prtlAttr;
        }

        /**
         *
         * @param prtlAttr
         * The prtl_attr
         */
        public void setPrtlAttr(String prtlAttr) {
            this.prtlAttr = prtlAttr;
        }

        /**
         *
         * @return
         * The langFilter
         */
        public String getLangFilter() {
            return langFilter;
        }

        /**
         *
         * @param langFilter
         * The lang_filter
         */
        public void setLangFilter(String langFilter) {
            this.langFilter = langFilter;
        }

        /**
         *
         * @return
         * The ftpExe
         */
        public String getFtpExe() {
            return ftpExe;
        }

        /**
         *
         * @param ftpExe
         * The ftp_exe
         */
        public void setFtpExe(String ftpExe) {
            this.ftpExe = ftpExe;
        }

        /**
         *
         * @return
         * The socialMedia
         */
        public String getSocialMedia() {
            return socialMedia;
        }

        /**
         *
         * @param socialMedia
         * The social_media
         */
        public void setSocialMedia(String socialMedia) {
            this.socialMedia = socialMedia;
        }

        /**
         *
         * @return
         * The alertStatus
         */
        public String getAlertStatus() {
            return alertStatus;
        }

        /**
         *
         * @param alertStatus
         * The alert_status
         */
        public void setAlertStatus(String alertStatus) {
            this.alertStatus = alertStatus;
        }

        /**
         *
         * @return
         * The emailByPin
         */
        public String getEmailByPin() {
            return emailByPin;
        }

        /**
         *
         * @param emailByPin
         * The email_by_pin
         */
        public void setEmailByPin(String emailByPin) {
            this.emailByPin = emailByPin;
        }

        /**
         *
         * @return
         * The aalertActive
         */
        public String getAalertActive() {
            return aalertActive;
        }

        /**
         *
         * @param aalertActive
         * The aalert_active
         */
        public void setAalertActive(String aalertActive) {
            this.aalertActive = aalertActive;
        }

        /**
         *
         * @return
         * The imageLibrary
         */
        public String getImageLibrary() {
            return imageLibrary;
        }

        /**
         *
         * @param imageLibrary
         * The image_library
         */
        public void setImageLibrary(String imageLibrary) {
            this.imageLibrary = imageLibrary;
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
         * The accountName
         */
        public String getAccountName() {
            return accountName;
        }

        /**
         *
         * @param accountName
         * The account_name
         */
        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }

        /**
         *
         * @return
         * The aalertGroups
         */
        public String getAalertGroups() {
            return aalertGroups;
        }

        /**
         *
         * @param aalertGroups
         * The aalert_groups
         */
        public void setAalertGroups(String aalertGroups) {
            this.aalertGroups = aalertGroups;
        }

        /**
         *
         * @return
         * The emailToggle
         */
        public String getEmailToggle() {
            return emailToggle;
        }

        /**
         *
         * @param emailToggle
         * The email_toggle
         */
        public void setEmailToggle(String emailToggle) {
            this.emailToggle = emailToggle;
        }

        /**
         *
         * @return
         * The callLength
         */
        public int getCallLength() {
            return callLength;
        }

        /**
         *
         * @param callLength
         * The call_length
         */
        public void setCallLength(int callLength) {
            this.callLength = callLength;
        }

        /**
         *
         * @return
         * The aaContactsByBuilding
         */
        public String getAaContactsByBuilding() {
            return aaContactsByBuilding;
        }

        /**
         *
         * @param aaContactsByBuilding
         * The aa_contacts_by_building
         */
        public void setAaContactsByBuilding(String aaContactsByBuilding) {
            this.aaContactsByBuilding = aaContactsByBuilding;
        }

        /**
         *
         * @return
         * The masterpinSetup
         */
        public String getMasterpinSetup() {
            return masterpinSetup;
        }

        /**
         *
         * @param masterpinSetup
         * The masterpin_setup
         */
        public void setMasterpinSetup(String masterpinSetup) {
            this.masterpinSetup = masterpinSetup;
        }

        /**
         *
         * @return
         * The aaLocationServices
         */
        public String getAaLocationServices() {
            return aaLocationServices;
        }

        /**
         *
         * @param aaLocationServices
         * The aa_location_services
         */
        public void setAaLocationServices(String aaLocationServices) {
            this.aaLocationServices = aaLocationServices;
        }

        /**
         *
         * @return
         * The aaSubmitter
         */
        public String getAaSubmitter() {
            return aaSubmitter;
        }

        /**
         *
         * @param aaSubmitter
         * The aa_submitter
         */
        public void setAaSubmitter(String aaSubmitter) {
            this.aaSubmitter = aaSubmitter;
        }

        /**
         *
         * @return
         * The pushToApp
         */
        public String getPushToApp() {
            return pushToApp;
        }

        /**
         *
         * @param pushToApp
         * The push_to_app
         */
        public void setPushToApp(String pushToApp) {
            this.pushToApp = pushToApp;
        }

        /**
         *
         * @return
         * The ttvMergedMessage
         */
        public String getTtvMergedMessage() {
            return ttvMergedMessage;
        }

        /**
         *
         * @param ttvMergedMessage
         * The ttv_merged_message
         */
        public void setTtvMergedMessage(String ttvMergedMessage) {
            this.ttvMergedMessage = ttvMergedMessage;
        }

        /**
         *
         * @return
         * The onebuttonalertCall
         */
        public String getOnebuttonalertCall() {
            return onebuttonalertCall;
        }

        /**
         *
         * @param onebuttonalertCall
         * The onebuttonalert_call
         */
        public void setOnebuttonalertCall(String onebuttonalertCall) {
            this.onebuttonalertCall = onebuttonalertCall;
        }

        /**
         *
         * @return
         * The appActive
         */
        public String getAppActive() {
            return appActive;
        }

        /**
         *
         * @param appActive
         * The app_active
         */
        public void setAppActive(String appActive) {
            this.appActive = appActive;
        }

        /**
         *
         * @return
         * The rssFeedFolder
         */
        public String getRssFeedFolder() {
            return rssFeedFolder;
        }

        /**
         *
         * @param rssFeedFolder
         * The rss_feed_folder
         */
        public void setRssFeedFolder(String rssFeedFolder) {
            this.rssFeedFolder = rssFeedFolder;
        }

        /**
         *
         * @return
         * The aaReportType
         */
        public String getAaReportType() {
            return aaReportType;
        }

        /**
         *
         * @param aaReportType
         * The aa_report_type
         */
        public void setAaReportType(String aaReportType) {
            this.aaReportType = aaReportType;
        }

        /**
         *
         * @return
         * The locator
         */
        public String getLocator() {
            return locator;
        }

        /**
         *
         * @param locator
         * The locator
         */
        public void setLocator(String locator) {
            this.locator = locator;
        }

        /**
         *
         * @return
         * The account
         */
        public String getAccount() {
            return account;
        }

        /**
         *
         * @param account
         * The account
         */
        public void setAccount(String account) {
            this.account = account;
        }

    }
}
