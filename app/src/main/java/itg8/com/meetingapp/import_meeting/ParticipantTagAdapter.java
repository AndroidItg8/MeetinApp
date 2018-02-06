package itg8.com.meetingapp.import_meeting;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
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
import itg8.com.meetingapp.db.TblContact;


public class ParticipantTagAdapter extends RecyclerView.Adapter<ParticipantTagAdapter.ViewHolder> {

    private List<TblContact> contactList;

    public ParticipantTagAdapter(List<TblContact> contactList) {
        this.contactList = contactList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_document, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
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
        }

        public void setTextViewDrawableColor() {
            color = new Random().nextInt(R.array.androidcolors);
            txtParticipantName.getBackground().setColorFilter(color, PorterDuff.Mode.LIGHTEN);

        }
    }
}
