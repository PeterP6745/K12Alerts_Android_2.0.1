package messagelogix.com.k12campusalerts.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ahmed Daou on 2/24/2016.
 */
public class GroupList extends ApiResponse<List<GroupList.Data>> {

    public class Data {

        @SerializedName("l_id")
        @Expose
        private String lId;
        @SerializedName("l_group_id")
        @Expose
        private String lGroupId;
        @SerializedName("l_name")
        @Expose
        private String lName;
        @SerializedName("list_name")
        @Expose
        private String listName;

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
         * The lGroupId
         */
        public String getLGroupId() {
            return lGroupId;
        }

        /**
         *
         * @param lGroupId
         * The l_group_id
         */
        public void setLGroupId(String lGroupId) {
            this.lGroupId = lGroupId;
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
         * The listName
         */
        public String getListName() {
            return listName;
        }

        /**
         *
         * @param listName
         * The list_name
         */
        public void setListName(String listName) {
            this.listName = listName;
        }

        @Override
        public String toString(){
            return this.getLName();
        }



    }
}
