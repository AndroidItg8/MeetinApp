package itg8.com.meetingapp.document_meeting;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.meetingapp.R;
import itg8.com.meetingapp.common.CommonMethod;
import itg8.com.meetingapp.common.Helper;
import itg8.com.meetingapp.db.TblDocument;

/**
 * Created by Android itg 8 on 2/5/2018.
 */

public class PreDocAdpater extends RecyclerView.Adapter<PreDocAdpater.PreDocViewHolder> {

    private Context activity;
    private List<TblDocument> list;
    private ItemClickListner listner;
    private int from;
    private File file;

    public PreDocAdpater(Context activity, List<TblDocument> list, ItemClickListner listner, int from) {
        this.activity = activity;
        this.list = list;
        this.listner = listner;


    }

    @Override
    public PreDocViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pre_doc, parent, false);

        return new PreDocViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PreDocViewHolder holder, int position) {
        holder.document = list.get(position);
        holder.lblDocumentTitle.setText(list.get(position).getFileName());
        holder.lblDocumentSize.setText(getFileSize(list.get(position).getFileActPath()));
        holder.lblDate.setText(getDateFromFile(list.get(position).getFileActPath()));
        setImageDrawable(holder);


    }

    private String getDateFromFile(String fileActPath) {
        return Helper.getDateFromMilliseconds(new File(fileActPath).lastModified());
    }

    private String getFileSize(String fileActPath) {
        file=new File(fileActPath);
        return (String.valueOf(file.length()/1024))+" Kb";
    }

    private void setImageDrawable(PreDocViewHolder holder) {
        holder.lblDocumentType.setText("-"+holder.document.getFileExt());
        if (holder.document.getFileExt().equalsIgnoreCase(CommonMethod.EXT_DOC)) {
            holder.imgFile.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_doc));
        }
        if (holder.document.getFileExt().equalsIgnoreCase(CommonMethod.EXT_JPG)) {
            holder.imgFile.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_jpg));
        }
        if (holder.document.getFileExt().equalsIgnoreCase(CommonMethod.EXT_PNG)) {
            holder.imgFile.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_png));
        }

        if (holder.document.getFileExt().equalsIgnoreCase(CommonMethod.EXT_PDF)) {
            holder.imgFile.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_pdf));
        }

        if (holder.document.getFileExt().equalsIgnoreCase(CommonMethod.EXT_PPT)) {
            holder.imgFile.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_ppt));
        }

        if (holder.document.getFileExt().equalsIgnoreCase(CommonMethod.EXT_ZIP)) {
            holder.imgFile.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_zip));
        }

        if (holder.document.getFileExt().equalsIgnoreCase(CommonMethod.EXT_TXT)) {
            holder.imgFile.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_txt));
        }

        if (holder.document.getFileExt().equalsIgnoreCase(CommonMethod.EXT_EXL)) {
            holder.imgFile.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_xls));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface ItemClickListner {
        void onItemClcikedListener(int position, TblDocument item, ImageView img);
        void onItemClickedShowListener(int position, TblDocument item);
    }

    public class PreDocViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_file)
        ImageView imgFile;
        @BindView(R.id.lbl_document_title)
        TextView lblDocumentTitle;
        @BindView(R.id.lbl_document_size)
        TextView lblDocumentSize;
        @BindView(R.id.lbl_document_type)
        TextView lblDocumentType;
        @BindView(R.id.lbl_date)
        TextView lblDate;
        @BindView(R.id.img_more)
        ImageView imgMore;
        @BindView(R.id.rl_document)
        RelativeLayout rlDocument;
        TblDocument document;

        public PreDocViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            imgMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    listner.onItemClcikedListener(getAdapterPosition(), list.get(getAdapterPosition()), imgMore);


                }
            });
            lblDocumentTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    listner.onItemClickedShowListener(getAdapterPosition(), list.get(getAdapterPosition()));
                }
            });
        }
    }

}
