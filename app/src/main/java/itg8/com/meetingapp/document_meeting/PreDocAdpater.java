package itg8.com.meetingapp.document_meeting;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import itg8.com.meetingapp.R;

/**
 * Created by Android itg 8 on 2/5/2018.
 */

 public class PreDocAdpater extends RecyclerView.Adapter<PreDocAdpater.PreDocViewHolder> {
    private Context activity;

    public PreDocAdpater(Context activity) {
        this.activity = activity;
    }

    @Override
    public PreDocViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pre_doc,parent,false);

        return new PreDocViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PreDocViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class PreDocViewHolder extends RecyclerView.ViewHolder {
        public PreDocViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
