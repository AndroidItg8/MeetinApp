package itg8.com.meetingapp.wallet_document;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.meetingapp.R;
import itg8.com.meetingapp.common.Helper;
import itg8.com.meetingapp.db.TblDocument;
import itg8.com.meetingapp.db.TblMeeting;
import itg8.com.meetingapp.document_meeting.PreDocAdpater;

/**
 * Created by Android itg 8 on 2/5/2018.
 */

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.WalletViewHolder>  implements PreDocAdpater.ItemClickListner{



    private Context context;
    private List<TblMeeting> list;
    cardOnLongPressListerner listner;

    public WalletAdapter(Context context, List<TblMeeting> list, cardOnLongPressListerner listner) {
        this.context = context;
        this.list = list;
        this.listner = listner;
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
        itemRowHolder.recyclerView.setAdapter(new PreDocAdpater(context, (List<TblDocument>) list.get(position).getDocuments(),this));
        itemRowHolder.lblTitleFull.setVisibility(View.GONE);
        itemRowHolder.lblTitle.setVisibility(View.VISIBLE);
        itemRowHolder.lblTitleFull.setText(list.get(position).getTitle());
        itemRowHolder.lblTitle.setText(list.get(position).getTitle());
        itemRowHolder.startTime.setText(Helper.getStringTimeFromDate(list.get(position).getStartTime()));
        itemRowHolder.endTime.setText(Helper.getStringTimeFromDate(list.get(position).getEndTime()));

    }

    @Override
    public int getItemCount() {
        return 7;

    }

    @Override
    public void onItemClcikedListener(int position, TblDocument item, ImageView img) {
        listner.onItemImgMoreClickListner(position,  list.get(position), img);
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
        @BindView(R.id.rl_root)
        RelativeLayout rlRoot;
        @BindView(R.id.lbl_title_full)
        TextView lblTitleFull;

        public WalletViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            cardView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                   boolean b= listner.onLongPressClickListner(getAdapterPosition(), list.get(getAdapterPosition()), rlRoot, cardView, lblTitle,lblTitleFull, motionEvent);
                    return true;
                }
            });
        }}

    public interface cardOnLongPressListerner {

        boolean  onLongPressClickListner(int position, TblMeeting list, RelativeLayout view, CardView cradView, TextView textView, TextView lblTitleFull, MotionEvent motionEvent);
        void  onItemImgMoreClickListner(int position, TblMeeting meeting  , ImageView img);
    }
}
