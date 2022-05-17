package messagelogix.com.k12campusalerts.models;

/**
 * Created by Ahmed Daou on 10/20/2014.
 * This is the model for features item
 */
public class NavDrawerItem {

    private String title;
    private String url;
    private int icon;
    private String count = "0";
    private boolean isCounterVisible = false;
    public int id = 0;


    public NavDrawerItem(int id, String title, int icon){
        this.title = title;
        this.icon = icon;
        this.id = id;
    }

    public NavDrawerItem(int id, String title, String url){
        this.id = id;
        this.title = title;
        this.url = url;
    }

    public NavDrawerItem(String title, int icon, boolean isCounterVisible, String count){
        this.title = title;
        this.icon = icon;
        this.isCounterVisible = isCounterVisible;
        this.count = count;
    }

    public String getTitle(){
        return this.title;
    }

    public int getIcon(){
        return this.icon;
    }

    public String getCount(){
        return this.count;
    }

    public boolean getCounterVisibility(){
        return this.isCounterVisible;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setIcon(int icon){
        this.icon = icon;
    }

    public void setCount(String count){
        this.count = count;
    }

    public void setCounterVisibility(boolean isCounterVisible){
        this.isCounterVisible = isCounterVisible;
    }


    @Override
    public String toString() {
        return  this.title;
    }

    public String getUrl() {
        return url;
    }
}