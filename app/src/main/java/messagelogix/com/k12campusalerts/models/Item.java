package messagelogix.com.k12campusalerts.models;

/**
 * Created by Ahmed Daou on 9/28/2015.
 * This is a simple class model to contain basic data
 */
public class Item {

    private String title;

    private String value;

    private int iconSrc = 0;

    public Item(String title, String value) {

        this.title = title;
        this.value = value;
      //  this.iconSrc = icon;
    }

    public Item(String title, String value, int icon) {

        this.title = title;
        this.value = value;
        this.iconSrc = icon;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public String getValue() {

        return value;
    }

    public void setIconSrc(int icon){
        this.iconSrc = icon;
    }

    public int getIconSrc(){
        return iconSrc;
    }
}
