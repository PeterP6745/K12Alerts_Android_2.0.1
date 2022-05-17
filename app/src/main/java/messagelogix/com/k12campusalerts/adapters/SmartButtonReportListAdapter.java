package messagelogix.com.k12campusalerts.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


import messagelogix.com.k12campusalerts.R;
import messagelogix.com.k12campusalerts.models.SmartButtonReport;

/**
 * Created by Richard on 1/22/2016.
 */
public class SmartButtonReportListAdapter extends ArrayAdapter<SmartButtonReport.SmartButtonReportItem> {

    private static final String LOG_TAG = SmartButtonReportListAdapter.class.getSimpleName();

    Context context;

    int layoutResourceId;

    private List<SmartButtonReport.SmartButtonReportItem> reportItems = new ArrayList<>();

    /**
     * Constructor
     *
     * @param context          The current context.
     * @param layoutResourceId The resource ID for a layout file containing a TextView to use when
     *                         instantiating views.
     */
    public SmartButtonReportListAdapter(Context context, int layoutResourceId, List<SmartButtonReport.SmartButtonReportItem> data) {

        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.reportItems = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        MenuHolder holder;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new MenuHolder();
            holder.txtDate = (TextView) row.findViewById(R.id.timeStamp);
            holder.txtSchName = (TextView) row.findViewById(R.id.schName);
            holder.imgIcon = (ImageView) row.findViewById(R.id.imageIcon);
            row.setTag(holder);
        } else {
            holder = (MenuHolder) row.getTag();
        }
        SmartButtonReport.SmartButtonReportItem reportItem = reportItems.get(position);
        holder.txtDate.setText(reportItem.getTimestamp());
        holder.txtSchName.setText(reportItem.getSchool().equals(" ") ? "School was not selected" : reportItem.getSchool());
        // show The Image in a ImageView
        if (!reportItem.getImage().equals(" ")) {
            new DownloadImageTask(holder.imgIcon).execute(reportItem.getImage());
        }
        return row;
    }

    static class MenuHolder {

        TextView txtDate;

        TextView txtSchName;

        ImageView imgIcon;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {

            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {

            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {

            bmImage.setImageBitmap(result);
        }
    }
}
