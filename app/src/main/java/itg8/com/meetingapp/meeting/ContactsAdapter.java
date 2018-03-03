package itg8.com.meetingapp.meeting;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
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

import itg8.com.meetingapp.R;
import itg8.com.meetingapp.meeting.model.Contact;


/**
 * Created by polis on 2015-11-07.
 */
public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {
    private static final String TAG = "ContactsAdapter";
    private ArrayList<Contact> contacts;
    private int checkBoxCount = 0;
    private ActionMode mActionMode;
    private Activity activity;
    private SearchView searchView;
    private ArrayList<Contact> tempContactList;
    private OnSearchInteractonListener listener;
    private boolean isTempContact = false;


    // Provide a suitable constructor (depends on the kind of dataset)
    public ContactsAdapter(Activity activity, ArrayList<Contact> contacts, OnSearchInteractonListener listener) {
        this.activity = activity;
        this.contacts = contacts;
        this.listener = listener;
    }

    private void selectItem(int position, ViewHolder holder) {
        if (contacts.get(position).isSelected()) {
            holder.rowView.setSelected(true);
            holder.rowView.setActivated(true);
        } else {
            holder.rowView.setSelected(false);
            holder.rowView.setActivated(false);

        }
    }





    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item_view, parent, false);
        // set the view's size, margins, paddings and layout parameters


        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.nameView.setText(contacts.get(position).getName());
        holder.emailView.setText(contacts.get(position).getEmail());


        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(Resources.getSystem(), contacts.get(position).getPicture());
        drawable.setCircular(true);

        holder.pictureView.setImageDrawable(drawable);

        holder.rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    listener.changeSelected(contacts.get(position).getContactId());
                contacts.get(position).setSelected(!contacts.get(position).isSelected());
                selectItem(position, holder);

                listener.updateActionBar();
            }
        });
        selectItem(position, holder);


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

            return contacts.size();

    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case


        public View rowView;
        public ImageView pictureView;
        public TextView nameView;
        public TextView emailView;


        public ViewHolder(View v) {
            super(v);

            rowView = v;
            nameView = (TextView) v.findViewById(R.id.contact_name);
            emailView = (TextView) v.findViewById(R.id.contact_email);
            pictureView = (ImageView) v.findViewById(R.id.contact_picture);


        }


    }

     public interface OnSearchInteractonListener
    {
        void onQueryTextListener(String query);

        void changeSelected(long position);

        void updateActionBar();
    }
}
