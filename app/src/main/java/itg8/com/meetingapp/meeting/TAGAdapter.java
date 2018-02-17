package itg8.com.meetingapp.meeting;


import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import java.util.Random;
import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.meetingapp.R;
import itg8.com.meetingapp.db.TblTAG;


/**
 * Created by polis on 2015-11-07.
 */
public class TAGAdapter extends RecyclerView.Adapter<TAGAdapter.ViewHolder> {

    private List<TblTAG> contactList;
    private TAGItemClickedListner listner;

    public TAGAdapter(List<TblTAG> contactList,TAGItemClickedListner listner) {
        this.contactList = contactList;
        this.listner = listner;
    }

    @Override
    public TAGAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_document, parent, false);
        return new TAGAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TAGAdapter.ViewHolder holder, int position) {
        holder.setTextViewDrawableColor();
        holder.txtParticipantName.setText(contactList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_participant_name)
        TextView txtParticipantName;
        int color;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listner.onItemAddTagClicked(getAdapterPosition(), contactList.get(getAdapterPosition()));
                }
            });
        }

        public void setTextViewDrawableColor() {
            color = new Random().nextInt(R.array.androidcolors);
            txtParticipantName.getBackground().setColorFilter(color, PorterDuff.Mode.LIGHTEN);
        }
    }

    public interface TAGItemClickedListner{
        void onItemAddTagClicked(int position,TblTAG item );
    }
}
