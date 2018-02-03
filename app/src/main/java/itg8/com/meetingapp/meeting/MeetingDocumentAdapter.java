package itg8.com.meetingapp.meeting;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.meetingapp.R;
import itg8.com.meetingapp.db.TblDocument;

/**
 * Created by swapnilmeshram on 02/02/18.
 */

public class MeetingDocumentAdapter extends RecyclerView.Adapter<MeetingDocumentAdapter.ViewHolder> {


    private List<TblDocument> documents;
    private OnRecyclerviewItemClickedListener listener;

    public MeetingDocumentAdapter(List<TblDocument> document, OnRecyclerviewItemClickedListener listener) {

        this.documents = document;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_document, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(documents.get(position)==null){
            holder.txtDoc1.setVisibility(View.GONE);
        }else {
            holder.txtDoc1.setVisibility(View.VISIBLE);
            holder.txtDoc1.setText(documents.get(position).getFileName());
            Picasso
                    .with(holder.itemView.getContext())
                    .load(new File(documents.get(position).getFileActPath()))
                    .fit()
                    .into(holder.imgDoc1);
        }
    }

    @Override
    public int getItemCount() {
        return documents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.imgDoc1)
        ImageView imgDoc1;
        @BindView(R.id.txtDoc1)
        TextView txtDoc1;
        @BindView(R.id.img_cross)
        ImageView imgCross;
        @BindView(R.id.frmDoc1)
        FrameLayout frmDoc1;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            txtDoc1.setOnClickListener(this);
            imgCross.setOnClickListener(this);
            imgDoc1.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view==txtDoc1) {
//                else {
                    listener.onPreviewClicked(getAdapterPosition(), documents.get(getAdapterPosition()));
//                }
            }else if(view==imgCross){
                listener.onDeleteDocumentClicked(getAdapterPosition(),documents.get(getAdapterPosition()));
            }else if(view==imgDoc1){
                if(documents.get(getAdapterPosition())==null){
                    listener.onNewEntryClicked();
                }
            }
        }
    }

    public interface OnRecyclerviewItemClickedListener{
        void onNewEntryClicked();
        void onDeleteDocumentClicked(int position, TblDocument document);
        void onPreviewClicked(int position, TblDocument document);
    }
}
