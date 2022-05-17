package messagelogix.com.k12campusalerts.activities.bnotified;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import messagelogix.com.k12campusalerts.R;
import messagelogix.com.k12campusalerts.adapters.PushListAdapter;
import messagelogix.com.k12campusalerts.models.PushMessagesList;
import messagelogix.com.k12campusalerts.models.User;
import messagelogix.com.k12campusalerts.utils.FunctionHelper;
import messagelogix.com.k12campusalerts.utils.ServiceGenerator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Program on 4/4/2017.
 */
public class BNotifiedViewPushActivity extends AppCompatActivity {
    Context context = this;
    ListView mList;
    ArrayList <PushMessagesList.Data> pushItems = new ArrayList<>();
    PushListAdapter adapter;

    public interface PushListService{
        @FormUrlEncoded
        @POST(ServiceGenerator.API_BASE_URL)
        Call<ResponseBody> request(@FieldMap HashMap<String, String> parameters);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_list);

        final View recyclerView = findViewById(R.id.push_item_list);

        downloadPushMessages(recyclerView);


//        mList = (ListView) findViewById(R.id.push_listview);

//        adapter = new PushListAdapter(this,R.layout.list_item_push,pushItems);
//        mList.setAdapter(adapter);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void downloadPushMessages(final View recyclerView){

        User mUser = (User) new FunctionHelper(context).retrieveObject(User.class);

        PushListService client = ServiceGenerator.createService(PushListService.class);

        HashMap<String, String> params = ServiceGenerator.getApiMap();
        params.put("controller", "Push");
        params.put("action", "GetPushAlerts");
        params.put("accountId", mUser.getData().getAcctId());

        Call<ResponseBody> call = client.request(params);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                if(response.isSuccess()){
                    try {
                        Gson gson = new Gson();

                        PushMessagesList reports = gson.fromJson(response.body().string(), PushMessagesList.class);
                        if (reports.getSuccess()) {
                            assert recyclerView != null;
                            setUpRecyclerView((RecyclerView) recyclerView, reports.getData());
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }


            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

    }

    private void setUpRecyclerView(@NonNull RecyclerView recyclerView, List<PushMessagesList.Data> reports) {
        recyclerView.setAdapter(new BNotifiedViewPushRecyclerAdapter(reports));
    }


    public class BNotifiedViewPushRecyclerAdapter
            extends RecyclerView.Adapter<BNotifiedViewPushRecyclerAdapter.ViewHolder> {

        private final List<PushMessagesList.Data> mValues;


        public BNotifiedViewPushRecyclerAdapter(List<PushMessagesList.Data> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_push, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.mItem = mValues.get(position);
            holder.dateView.setText(mValues.get(position).getDateTime());
            holder.subjectView.setText(mValues.get(position).getSubject());
            if (position % 2 == 0) {
                //holder.mView.setBackgroundColor(Color.parseColor("#edebc9"));
            }else{
                //holder.mView.setBackgroundColor(Color.parseColor("#eff5fa"));
            }

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        Context context = v.getContext();
                        Intent intent = new Intent(context, ViewPushDetailsActivity.class);
                        intent.putExtra("ID", mValues.get(position).getCampId());
                        context.startActivity(intent);

                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView dateView;
            public final TextView subjectView;
            public PushMessagesList.Data mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                dateView = (TextView) view.findViewById(R.id.push_item_date);
                subjectView = (TextView) view.findViewById(R.id.push_message_content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + subjectView.getText() + "'";
            }
        }
    }
}
