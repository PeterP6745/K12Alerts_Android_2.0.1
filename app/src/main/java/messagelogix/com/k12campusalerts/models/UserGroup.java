package messagelogix.com.k12campusalerts.models;

/**
 * Created by Richard on 8/23/2018.
 */
public class UserGroup {

    private String usrTypeName;
    private String usrTypeId;

    public UserGroup(String name, String id){

        usrTypeName = name;
        usrTypeId = id;

    }

    public String getUsrTypeId() {

        return usrTypeId;
    }

    public String getUsrTypeName() {

        return usrTypeName;
    }

    @Override
    public String toString() {

        return this.getUsrTypeName();
    }
}
