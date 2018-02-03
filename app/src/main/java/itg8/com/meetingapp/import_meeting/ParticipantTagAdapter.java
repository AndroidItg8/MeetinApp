package itg8.com.meetingapp.import_meeting;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.meetingapp.R;

/**
 * Created by Android itg 8 on 2/2/2018.
 */

public class ParticipantTagAdapter extends RecyclerView.Adapter<ParticipantTagAdapter.ViewHolder> {

    private Context applicationContext;
    private int[] listOfColor;

    public ParticipantTagAdapter(Context applicationContext, int[] listOfColor) {

        this.applicationContext = applicationContext;
        this.listOfColor = listOfColor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_document, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int randomAndroidColor = new Random().nextInt(listOfColor.length);
        // holder.txtParticipantName.setBackgroundColor(randomAndroidColor);

        setTextViewDrawableColor(holder.txtParticipantName, randomAndroidColor);

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_participant_name)
        TextView txtParticipantName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void setTextViewDrawableColor(TextView textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(applicationContext, color), PorterDuff.Mode.SRC_IN));
            }
        }
    }
}
