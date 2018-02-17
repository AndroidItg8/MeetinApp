package itg8.com.meetingapp.meeting;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

    private static final String TAG = TAGAddAdapter.class.getSimpleName() ;
    onItemClickedListener listener;

    private List<TblTAG> contacts;
    private Activity activity;
    private int checkBoxCount;
    private ActionMode mActionMode;
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_tag, menu);

             menu.findItem(R.id.m_search).setVisible(false);
             menu.findItem(R.id.menu_ok).setVisible(true);
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
                   activity.finish();
//                   // mode.finish();
                return true;
            } else {
                return false;
            }

        }

        public void finishWithResult() {
//            DaoContactInteractor contactInteractor = new DaoContactInteractor(activity) ;
            ArrayList<TblTAG> selectedContacts = new ArrayList<TblTAG>();
            for (TblTAG c : contacts) {
                if (c.isSelected()) {
                    selectedContacts.add(c);
////                    tagTemp.add(contacts.get(position));
//                    contactInteractor.insert(c);

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

//            boolean isAnySelected = false;
//            for (int i = 0; i < contacts.size(); i++) {
//                if (contacts.get(i).isSelected()) {
//                    contacts.get(i).setSelected(false);
//                    isAnySelected = true;
//                }
//            }
//
//            if (isAnySelected) {
//                //contactIconLazyLoad.Reset();
//                notifyDataSetChanged();
//            }
//
//
//            //searchButton.setVisibility(View.VISIBLE);
//            mActionMode = null;

        }
    };

    public TAGAddAdapter(Activity activity, List<TblTAG> contacts, onItemClickedListener listener) {
        this.activity = activity;
        this.contacts = contacts;
        this.listener = listener;

    }

    private void selectItem(int position, ViewHolder holder) {
        if (contacts.get(position).isSelected()) {

            setItemBackground(holder, activity.getResources().getDrawable(R.drawable.bg_tag_primary), activity.getResources().getColor(R.color.colorWhite), activity.getResources().getDrawable(R.drawable.bg_item_correct));

        } else {

            setItemBackground(holder, activity.getResources().getDrawable(R.drawable.bg_tag), activity.getResources().getColor(R.color.colorWhite), activity.getResources().getDrawable(R.drawable.bg_item_cross));

        }
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
                mActionMode.setTitle(activity.getString(R.string.add) + " " + String.valueOf(checkBoxCount) + " " + activity.getString(R.string.tag));
            } else {
                mActionMode.setTitle(activity.getString(R.string.add) + " " + String.valueOf(checkBoxCount) + " " + activity.getString(R.string.tags));
            }
        }

    }

    private void setItemBackground(ViewHolder holder, Drawable drawable, int color, Drawable imgDrawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            holder.lblTagTitle.setBackground(drawable);
            holder.lblTagTitle.setTextColor(color);
            holder.imgDelete.setBackground(imgDrawable);

        } else {
            holder.lblTagTitle.setBackgroundDrawable(drawable);
            holder.lblTagTitle.setTextColor(color);
            holder.imgDelete.setBackgroundDrawable(imgDrawable);



        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rv_tag, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.lblTagTitle.setText(contacts.get(position).getName());
        selectItem(position, holder);


    }

    @Override
    public int getItemCount() {
        Log.d(TAG,"Size:"+contacts.size());
        return contacts.size();
    }



    public interface onItemClickedListener {
        void onItemClicked(int position, TblTAG tag);
        void onItemDelete(int position,TblTAG tag);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
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
//                    contacts.get(getAdapterPosition()).setSelected(!contacts.get(getAdapterPosition()).isSelected());
//                    notifyDataSetChanged();

                    listener.onItemClicked(getAdapterPosition(), contacts.get(getAdapterPosition()));


                }
            });

            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemDelete(getAdapterPosition(), contacts.get(getAdapterPosition()));
                }
            });
        }



    }
}