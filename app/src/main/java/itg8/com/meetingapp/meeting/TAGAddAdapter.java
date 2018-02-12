package itg8.com.meetingapp.meeting;

import android.app.Activity;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.meetingapp.R;
import itg8.com.meetingapp.db.TblContact;
import itg8.com.meetingapp.db.TblTAG;
import itg8.com.meetingapp.meeting.model.Contact;

/**
 * Created by Android itg 8 on 2/7/2018.
 */

public class TAGAddAdapter extends RecyclerView.Adapter<TAGAddAdapter.ViewHolder> {

    onItemClickedListener listener;

    private List<TblTAG> contacts;

    private Activity activity;


    // Provide a suitable constructor (depends on the kind of dataset)
    public TAGAddAdapter(Activity activity, List<TblTAG> contacts, onItemClickedListener listener) {
        this.activity = activity;
        this.contacts = contacts;
        this.listener = listener;
    }

    private void selectItem(int position, ViewHolder holder) {
        List<TblTAG> tagTemp = new ArrayList<>();
        if (contacts.get(position).isSelected()) {
            holder.checkBox.setChecked(true);
            tagTemp.add(contacts.get(position));

        }else
        {
            holder.checkBox.setChecked(false);

        }


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rv_tag, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.lblTagTitle.setText(contacts.get(position).getName());


        selectItem(position, holder);


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        return contacts.size();
    }

    public interface onItemClickedListener {
        void onItemClicked(int position, TblTAG tag, boolean b);

    }

    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        @BindView(R.id.lbl_tag_title)
        TextView lblTagTitle;
        @BindView(R.id.checkBox)
        CheckBox checkBox;


        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, itemView);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    listener.onItemClicked(getAdapterPosition(), contacts.get(getAdapterPosition()), b);

                }
            });
        }





    }
}