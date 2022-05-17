package messagelogix.com.k12campusalerts.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ahmed Daou on 1/27/2016.
 */
public class ReportEmailTextDetails extends ApiResponse<ReportEmailTextDetails.Data>{
    public class Data {
        @SerializedName("msg_id")
        @Expose
        private String msgId;
        @SerializedName("acct_id")
        @Expose
        private String acctId;
        @SerializedName("common_id")
        @Expose
        private String commonId;
        @SerializedName("e_camp_id")
        @Expose
        private String eCampId;
        @SerializedName("l_id")
        @Expose
        private String lId;
        @SerializedName("sendname")
        @Expose
        private String sendname;
        @SerializedName("sender")
        @Expose
        private String sender;
        @SerializedName("subject")
        @Expose
        private String subject;
        @SerializedName("senddate")
        @Expose
        private String senddate;
        @SerializedName("reply_name")
        @Expose
        private String replyName;
        @SerializedName("reply_email")
        @Expose
        private String replyEmail;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("aol")
        @Expose
        private String aol;
        @SerializedName("edate")
        @Expose
        private String edate;
        @SerializedName("rpt_views")
        @Expose
        private String rptViews;
        @SerializedName("rpt_optout")
        @Expose
        private String rptOptout;
        @SerializedName("rpt_link01")
        @Expose
        private String rptLink01;
        @SerializedName("rpt_link02")
        @Expose
        private String rptLink02;
        @SerializedName("rpt_link03")
        @Expose
        private String rptLink03;
        @SerializedName("rpt_link04")
        @Expose
        private String rptLink04;
        @SerializedName("rpt_link05")
        @Expose
        private String rptLink05;
        @SerializedName("rpt_link06")
        @Expose
        private String rptLink06;
        @SerializedName("rpt_link07")
        @Expose
        private String rptLink07;
        @SerializedName("rpt_link08")
        @Expose
        private String rptLink08;
        @SerializedName("rpt_link09")
        @Expose
        private String rptLink09;
        @SerializedName("rpt_link10")
        @Expose
        private String rptLink10;
        @SerializedName("camp_name")
        @Expose
        private String campName;
        @SerializedName("l_name")
        @Expose
        private String lName;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("rpt_sent")
        @Expose
        private String rptSent;
        @SerializedName("rpt_returned")
        @Expose
        private String rptReturned;
        @SerializedName("rpt_reply")
        @Expose
        private String rptReply;
        @SerializedName("rpt_outofoffice")
        @Expose
        private String rptOutofoffice;
        @SerializedName("link1")
        @Expose
        private Object link1;
        @SerializedName("link2")
        @Expose
        private Object link2;
        @SerializedName("link3")
        @Expose
        private Object link3;
        @SerializedName("link4")
        @Expose
        private Object link4;
        @SerializedName("link5")
        @Expose
        private Object link5;
        @SerializedName("link6")
        @Expose
        private Object link6;
        @SerializedName("link7")
        @Expose
        private Object link7;
        @SerializedName("link8")
        @Expose
        private Object link8;
        @SerializedName("link9")
        @Expose
        private Object link9;
        @SerializedName("link10")
        @Expose
        private Object link10;
        @SerializedName("link1_name")
        @Expose
        private Object link1Name;
        @SerializedName("link2_name")
        @Expose
        private Object link2Name;
        @SerializedName("link3_name")
        @Expose
        private Object link3Name;
        @SerializedName("link4_name")
        @Expose
        private Object link4Name;
        @SerializedName("link5_name")
        @Expose
        private Object link5Name;
        @SerializedName("link6_name")
        @Expose
        private Object link6Name;
        @SerializedName("link7_name")
        @Expose
        private Object link7Name;
        @SerializedName("link8_name")
        @Expose
        private Object link8Name;
        @SerializedName("link9_name")
        @Expose
        private Object link9Name;
        @SerializedName("link10_name")
        @Expose
        private Object link10Name;
        @SerializedName("l_type")
        @Expose
        private String lType;

        /**
         *
         * @return
         * The msgId
         */
        public String getMsgId() {
            return msgId;
        }

        /**
         *
         * @param msgId
         * The msg_id
         */
        public void setMsgId(String msgId) {
            this.msgId = msgId;
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
         * The eCampId
         */
        public String getECampId() {
            return eCampId;
        }

        /**
         *
         * @param eCampId
         * The e_camp_id
         */
        public void setECampId(String eCampId) {
            this.eCampId = eCampId;
        }

        /**
         *
         * @return
         * The lId
         */
        public String getLId() {
            return lId;
        }

        /**
         *
         * @param lId
         * The l_id
         */
        public void setLId(String lId) {
            this.lId = lId;
        }

        /**
         *
         * @return
         * The sendname
         */
        public String getSendname() {
            return sendname;
        }

        /**
         *
         * @param sendname
         * The sendname
         */
        public void setSendname(String sendname) {
            this.sendname = sendname;
        }

        /**
         *
         * @return
         * The sender
         */
        public String getSender() {
            return sender;
        }

        /**
         *
         * @param sender
         * The sender
         */
        public void setSender(String sender) {
            this.sender = sender;
        }

        /**
         *
         * @return
         * The subject
         */
        public String getSubject() {
            return subject;
        }

        /**
         *
         * @param subject
         * The subject
         */
        public void setSubject(String subject) {
            this.subject = subject;
        }

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
         * The replyName
         */
        public String getReplyName() {
            return replyName;
        }

        /**
         *
         * @param replyName
         * The reply_name
         */
        public void setReplyName(String replyName) {
            this.replyName = replyName;
        }

        /**
         *
         * @return
         * The replyEmail
         */
        public String getReplyEmail() {
            return replyEmail;
        }

        /**
         *
         * @param replyEmail
         * The reply_email
         */
        public void setReplyEmail(String replyEmail) {
            this.replyEmail = replyEmail;
        }

        /**
         *
         * @return
         * The type
         */
        public String getType() {
            return type;
        }

        /**
         *
         * @param type
         * The type
         */
        public void setType(String type) {
            this.type = type;
        }

        /**
         *
         * @return
         * The aol
         */
        public String getAol() {
            return aol;
        }

        /**
         *
         * @param aol
         * The aol
         */
        public void setAol(String aol) {
            this.aol = aol;
        }

        /**
         *
         * @return
         * The edate
         */
        public String getEdate() {
            return edate;
        }

        /**
         *
         * @param edate
         * The edate
         */
        public void setEdate(String edate) {
            this.edate = edate;
        }

        /**
         *
         * @return
         * The rptViews
         */
        public String getRptViews() {
            return rptViews;
        }

        /**
         *
         * @param rptViews
         * The rpt_views
         */
        public void setRptViews(String rptViews) {
            this.rptViews = rptViews;
        }

        /**
         *
         * @return
         * The rptOptout
         */
        public String getRptOptout() {
            return rptOptout;
        }

        /**
         *
         * @param rptOptout
         * The rpt_optout
         */
        public void setRptOptout(String rptOptout) {
            this.rptOptout = rptOptout;
        }

        /**
         *
         * @return
         * The rptLink01
         */
        public String getRptLink01() {
            return rptLink01;
        }

        /**
         *
         * @param rptLink01
         * The rpt_link01
         */
        public void setRptLink01(String rptLink01) {
            this.rptLink01 = rptLink01;
        }

        /**
         *
         * @return
         * The rptLink02
         */
        public String getRptLink02() {
            return rptLink02;
        }

        /**
         *
         * @param rptLink02
         * The rpt_link02
         */
        public void setRptLink02(String rptLink02) {
            this.rptLink02 = rptLink02;
        }

        /**
         *
         * @return
         * The rptLink03
         */
        public String getRptLink03() {
            return rptLink03;
        }

        /**
         *
         * @param rptLink03
         * The rpt_link03
         */
        public void setRptLink03(String rptLink03) {
            this.rptLink03 = rptLink03;
        }

        /**
         *
         * @return
         * The rptLink04
         */
        public String getRptLink04() {
            return rptLink04;
        }

        /**
         *
         * @param rptLink04
         * The rpt_link04
         */
        public void setRptLink04(String rptLink04) {
            this.rptLink04 = rptLink04;
        }

        /**
         *
         * @return
         * The rptLink05
         */
        public String getRptLink05() {
            return rptLink05;
        }

        /**
         *
         * @param rptLink05
         * The rpt_link05
         */
        public void setRptLink05(String rptLink05) {
            this.rptLink05 = rptLink05;
        }

        /**
         *
         * @return
         * The rptLink06
         */
        public String getRptLink06() {
            return rptLink06;
        }

        /**
         *
         * @param rptLink06
         * The rpt_link06
         */
        public void setRptLink06(String rptLink06) {
            this.rptLink06 = rptLink06;
        }

        /**
         *
         * @return
         * The rptLink07
         */
        public String getRptLink07() {
            return rptLink07;
        }

        /**
         *
         * @param rptLink07
         * The rpt_link07
         */
        public void setRptLink07(String rptLink07) {
            this.rptLink07 = rptLink07;
        }

        /**
         *
         * @return
         * The rptLink08
         */
        public String getRptLink08() {
            return rptLink08;
        }

        /**
         *
         * @param rptLink08
         * The rpt_link08
         */
        public void setRptLink08(String rptLink08) {
            this.rptLink08 = rptLink08;
        }

        /**
         *
         * @return
         * The rptLink09
         */
        public String getRptLink09() {
            return rptLink09;
        }

        /**
         *
         * @param rptLink09
         * The rpt_link09
         */
        public void setRptLink09(String rptLink09) {
            this.rptLink09 = rptLink09;
        }

        /**
         *
         * @return
         * The rptLink10
         */
        public String getRptLink10() {
            return rptLink10;
        }

        /**
         *
         * @param rptLink10
         * The rpt_link10
         */
        public void setRptLink10(String rptLink10) {
            this.rptLink10 = rptLink10;
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
         * The rptSent
         */
        public String getRptSent() {
            return rptSent;
        }

        /**
         *
         * @param rptSent
         * The rpt_sent
         */
        public void setRptSent(String rptSent) {
            this.rptSent = rptSent;
        }

        /**
         *
         * @return
         * The rptReturned
         */
        public String getRptReturned() {
            return rptReturned;
        }

        /**
         *
         * @param rptReturned
         * The rpt_returned
         */
        public void setRptReturned(String rptReturned) {
            this.rptReturned = rptReturned;
        }

        /**
         *
         * @return
         * The rptReply
         */
        public String getRptReply() {
            return rptReply;
        }

        /**
         *
         * @param rptReply
         * The rpt_reply
         */
        public void setRptReply(String rptReply) {
            this.rptReply = rptReply;
        }

        /**
         *
         * @return
         * The rptOutofoffice
         */
        public String getRptOutofoffice() {
            return rptOutofoffice;
        }

        /**
         *
         * @param rptOutofoffice
         * The rpt_outofoffice
         */
        public void setRptOutofoffice(String rptOutofoffice) {
            this.rptOutofoffice = rptOutofoffice;
        }

        /**
         *
         * @return
         * The link1
         */
        public Object getLink1() {
            return link1;
        }

        /**
         *
         * @param link1
         * The link1
         */
        public void setLink1(Object link1) {
            this.link1 = link1;
        }

        /**
         *
         * @return
         * The link2
         */
        public Object getLink2() {
            return link2;
        }

        /**
         *
         * @param link2
         * The link2
         */
        public void setLink2(Object link2) {
            this.link2 = link2;
        }

        /**
         *
         * @return
         * The link3
         */
        public Object getLink3() {
            return link3;
        }

        /**
         *
         * @param link3
         * The link3
         */
        public void setLink3(Object link3) {
            this.link3 = link3;
        }

        /**
         *
         * @return
         * The link4
         */
        public Object getLink4() {
            return link4;
        }

        /**
         *
         * @param link4
         * The link4
         */
        public void setLink4(Object link4) {
            this.link4 = link4;
        }

        /**
         *
         * @return
         * The link5
         */
        public Object getLink5() {
            return link5;
        }

        /**
         *
         * @param link5
         * The link5
         */
        public void setLink5(Object link5) {
            this.link5 = link5;
        }

        /**
         *
         * @return
         * The link6
         */
        public Object getLink6() {
            return link6;
        }

        /**
         *
         * @param link6
         * The link6
         */
        public void setLink6(Object link6) {
            this.link6 = link6;
        }

        /**
         *
         * @return
         * The link7
         */
        public Object getLink7() {
            return link7;
        }

        /**
         *
         * @param link7
         * The link7
         */
        public void setLink7(Object link7) {
            this.link7 = link7;
        }

        /**
         *
         * @return
         * The link8
         */
        public Object getLink8() {
            return link8;
        }

        /**
         *
         * @param link8
         * The link8
         */
        public void setLink8(Object link8) {
            this.link8 = link8;
        }

        /**
         *
         * @return
         * The link9
         */
        public Object getLink9() {
            return link9;
        }

        /**
         *
         * @param link9
         * The link9
         */
        public void setLink9(Object link9) {
            this.link9 = link9;
        }

        /**
         *
         * @return
         * The link10
         */
        public Object getLink10() {
            return link10;
        }

        /**
         *
         * @param link10
         * The link10
         */
        public void setLink10(Object link10) {
            this.link10 = link10;
        }

        /**
         *
         * @return
         * The link1Name
         */
        public Object getLink1Name() {
            return link1Name;
        }

        /**
         *
         * @param link1Name
         * The link1_name
         */
        public void setLink1Name(Object link1Name) {
            this.link1Name = link1Name;
        }

        /**
         *
         * @return
         * The link2Name
         */
        public Object getLink2Name() {
            return link2Name;
        }

        /**
         *
         * @param link2Name
         * The link2_name
         */
        public void setLink2Name(Object link2Name) {
            this.link2Name = link2Name;
        }

        /**
         *
         * @return
         * The link3Name
         */
        public Object getLink3Name() {
            return link3Name;
        }

        /**
         *
         * @param link3Name
         * The link3_name
         */
        public void setLink3Name(Object link3Name) {
            this.link3Name = link3Name;
        }

        /**
         *
         * @return
         * The link4Name
         */
        public Object getLink4Name() {
            return link4Name;
        }

        /**
         *
         * @param link4Name
         * The link4_name
         */
        public void setLink4Name(Object link4Name) {
            this.link4Name = link4Name;
        }

        /**
         *
         * @return
         * The link5Name
         */
        public Object getLink5Name() {
            return link5Name;
        }

        /**
         *
         * @param link5Name
         * The link5_name
         */
        public void setLink5Name(Object link5Name) {
            this.link5Name = link5Name;
        }

        /**
         *
         * @return
         * The link6Name
         */
        public Object getLink6Name() {
            return link6Name;
        }

        /**
         *
         * @param link6Name
         * The link6_name
         */
        public void setLink6Name(Object link6Name) {
            this.link6Name = link6Name;
        }

        /**
         *
         * @return
         * The link7Name
         */
        public Object getLink7Name() {
            return link7Name;
        }

        /**
         *
         * @param link7Name
         * The link7_name
         */
        public void setLink7Name(Object link7Name) {
            this.link7Name = link7Name;
        }

        /**
         *
         * @return
         * The link8Name
         */
        public Object getLink8Name() {
            return link8Name;
        }

        /**
         *
         * @param link8Name
         * The link8_name
         */
        public void setLink8Name(Object link8Name) {
            this.link8Name = link8Name;
        }

        /**
         *
         * @return
         * The link9Name
         */
        public Object getLink9Name() {
            return link9Name;
        }

        /**
         *
         * @param link9Name
         * The link9_name
         */
        public void setLink9Name(Object link9Name) {
            this.link9Name = link9Name;
        }

        /**
         *
         * @return
         * The link10Name
         */
        public Object getLink10Name() {
            return link10Name;
        }

        /**
         *
         * @param link10Name
         * The link10_name
         */
        public void setLink10Name(Object link10Name) {
            this.link10Name = link10Name;
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

    }
}
