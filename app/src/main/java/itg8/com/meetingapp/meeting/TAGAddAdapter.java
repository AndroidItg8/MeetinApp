package itg8.com.meetingapp.meeting;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.meetingapp.R;
import itg8.com.meetingapp.db.TblTAG;

/**
 * Created by Android itg 8 on 2/7/2018.
 */

public class TAGAddAdapter extends RecyclerView.Adapter<TAGAddAdapter.ViewHolder> {

    onItemClickedListener listener;
    private List<TblTAG> contacts;
    private int checkBoxCount = 0;
    private ActionMode mActionMode;
    private Activity activity;
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);

            // searchButton.setVisibility(View.GONE);
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
// may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            int id = item.getItemId();
            if (id == R.id.menu_ok) {
                finishWithResult();
//                   //activity.finish();
//
//                   // mode.finish();
                return true;
            } else {
                return false;
            }

        }

        private void finishWithResult() {
            ArrayList<TblTAG> selectedContacts = new ArrayList<TblTAG>();
            for (TblTAG c : contacts) {
                if (c.isSelected()) {
                    selectedContacts.add(c);
                }
            }

            Bundle resultData = new Bundle();
            resultData.putParcelableArrayList("tag", selectedContacts);
            Intent intent = new Intent();
            intent.putExtras(resultData);
            activity.setResult(activity.RESULT_OK, intent);
            activity.finish();
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {

            boolean isAnySelected = false;
            for (int i = 0; i < contacts.size(); i++) {
                if (contacts.get(i).isSelected()) {
                    contacts.get(i).setSelected(false);
                    isAnySelected = true;
                }
            }

            if (isAnySelected) {
                //contactIconLazyLoad.Reset();
                notifyDataSetChanged();
            }


            //searchButton.setVisibility(View.VISIBLE);
            mActionMode = null;

        }
    };

    // Provide a suitable constructor (depends on the kind of dataset)
    public TAGAddAdapter(Activity activity, List<TblTAG> contacts, onItemClickedListener listener) {
        this.activity = activity;
        this.contacts = contacts;
        this.listener = listener;
    }

    private void selectItem(int position, ViewHolder holder) {
        if (!contacts.get(position).isSelected()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.lblTagTitle.setBackground(ContextCompat.getDrawable(activity, R.drawable.bg_tag));
                holder.lblTagTitle.setTextColor(ContextCompat.getColor(activity, R.color.theme_primary));
                holder.imgDelete.setBackground(ContextCompat.getDrawable(activity, R.drawable.ic_check_white_24dp));


            } else {
                holder.lblTagTitle.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.bg_tag));
                holder.imgDelete.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.ic_check_white_24dp));
                holder.lblTagTitle.setTextColor(ContextCompat.getColor(activity, R.color.theme_primary));
            }
        } else {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.imgDelete.setBackground(ContextCompat.getDrawable(activity, R.drawable.ic_close_black_24dp));
                holder.lblTagTitle.setBackground(ContextCompat.getDrawable(activity, R.drawable.bg_tag_primary));
                holder.lblTagTitle.setTextColor(ContextCompat.getColor(activity, R.color.colorWhite));

            } else {
                holder.lblTagTitle.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.bg_tag_primary));
                holder.imgDelete.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.ic_close_black_24dp));
                holder.lblTagTitle.setTextColor(ContextCompat.getColor(activity, R.color.colorWhite));
            }

        }

//            holder.rowView.setActivated(false);


        updateActionBar();
    }

    private void updateActionBar() {
        checkBoxCount = 0;
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).isSelected()) checkBoxCount++;
        }

        if (checkBoxCount == 0) {
            if (mActionMode != null) {
                mActionMode.finish();
                mActionMode = null;
            }

        } else {
            if (mActionMode == null)
                mActionMode = ((AppCompatActivity) activity).startSupportActionMode(mActionModeCallback);
        }
        if (mActionMode != null) {
            if (checkBoxCount == 1) {
                mActionMode.setTitle(activity.getString(R.string.add) + " " + String.valueOf(checkBoxCount) + " " + activity.getString(R.string.friend));
            } else {
                mActionMode.setTitle(activity.getString(R.string.add) + " " + String.valueOf(checkBoxCount) + " " + activity.getString(R.string.friends));
            }
        }

    }

    // Create new views (invoked by the layout manager)
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
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.lblTagTitle.setText(contacts.get(position).getName());

        //        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                contacts.get(position).setSelected(!contacts.get(position).isSelected());
//                contacts.get(position).setSelected(b);
//                selectItem(position, holder);
//            }
//        });
        selectItem(position, holder);


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        return contacts.size();
    }

    public interface onItemClickedListener {
        void onItemClicked(int position, TblTAG tag);

        void onTagItemDelete(int position, TblTAG tag);
    }

    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        @BindView(R.id.lbl_tag_title)
        TextView lblTagTitle;
        @BindView(R.id.img_delete)
        ImageView imgDelete;


        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    contacts.get(getAdapterPosition()).setSelected(!contacts.get(position).isSelected());
//                    contacts.get(getAdapterPosition()).setSelected(true);
//                    selectItem(position, holder);
                    listener.onItemClicked(getAdapterPosition(), contacts.get(getAdapterPosition()));
                }
            });
            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onTagItemDelete(getAdapterPosition(), contacts.get(getAdapterPosition()));

                }
            });


        }
    }
}