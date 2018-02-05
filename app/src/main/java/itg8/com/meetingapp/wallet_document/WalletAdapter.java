package itg8.com.meetingapp.wallet_document;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.meetingapp.R;
import itg8.com.meetingapp.document_meeting.PreDocAdpater;

/**
 * Created by Android itg 8 on 2/5/2018.
 */

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.WalletViewHolder> {


    private Context context;

    public WalletAdapter(Context context) {

        this.context = context;
    }

    @Override
    public WalletViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wallet_doc, parent, false);

        return new WalletViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WalletViewHolder itemRowHolder, int position) {
        itemRowHolder.recyclerView.setHasFixedSize(true);
        itemRowHolder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        itemRowHolder.recyclerView.addItemDecoration(itemDecoration);
        itemRowHolder.recyclerView.setAdapter(new PreDocAdpater(context));

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class WalletViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.viewEnd)
        View viewEnd;
        @BindView(R.id.startTime)
        TextView startTime;
        @BindView(R.id.cardViewTimeStart)
        CardView cardViewTimeStart;
        @BindView(R.id.endTime)
        TextView endTime;
        @BindView(R.id.cardViewTimeEnd)
        CardView cardViewTimeEnd;
        @BindView(R.id.lbl_title)
        TextView lblTitle;
        @BindView(R.id.cardView)
        CardView cardView;
        @BindView(R.id.frame)
        FrameLayout frame;
        @BindView(R.id.recyclerView)
        RecyclerView recyclerView;
        public WalletViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
