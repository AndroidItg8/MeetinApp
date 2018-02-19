package itg8.com.meetingapp.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.meetingapp.R;
import itg8.com.meetingapp.common.Helper;
import itg8.com.meetingapp.db.TblMeeting;


/**
 * Created by Android itg 8 on 2/19/2018.
 */

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchViewHolder> {

    private static final String TAG = SearchResultAdapter.class.getSimpleName();


    private Context context;
    private List<TblMeeting> listSearchResult;
    MeetingItemClicked listener;

    public SearchResultAdapter(Context context, List<TblMeeting> listSearchResult, MeetingItemClicked listener) {
        this.context = context;
        this.listSearchResult = listSearchResult;
        this.listener = listener;
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
//        holder.txtValue.setText(listSearchResult.get(position).getTitle());
//        holder.txtPlace.setText(listSearchResult.get(position).getAddress());
//        holder.txtTime.setText(Helper.getStringTimeFromDate(listSearchResult.get(position).getStartTime()) + "-" + Helper.getStringTimeFromDate(listSearchResult.get(position).getEndTime()));
//        holder.txtValue.setText("position: "+position);
        holder.setContentMeeting(listSearchResult.get(position));
        Log.d(TAG, "onBindViewHolder: position: " + position);
    }


    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: size" + listSearchResult.size());
        return listSearchResult.size();
//        return 20;
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.view_agenda_event_title)
        TextView txtTitle;
        @BindView(R.id.view_agenda_event_time)
        TextView txtTime;
        @BindView(R.id.view_agenda_event_time_container)
        LinearLayout timeContainer;
        @BindView(R.id.view_agenda_event_location)
        TextView txtLocation;
        @BindView(R.id.view_agenda_event_location_container)
        LinearLayout locationContainer;
        @BindView(R.id.view_agenda_event_description_container)
        LinearLayout descriptionContainer;

        public SearchViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClicked(getAdapterPosition(), listSearchResult.get(getAdapterPosition()));
                }
            });
        }

        public void setContentMeeting(TblMeeting event) {


            descriptionContainer.setVisibility(View.VISIBLE);
            txtTitle.setTextColor(context.getResources().getColor(android.R.color.black));

            txtTitle.setText(event.getTitle());
            txtTime.setText(Helper.getStringTimeFromDate(event.getStartTime()) + "-" + Helper.getStringTimeFromDate(event.getEndTime()));



            if (event.getAddress().length() > 0) {
                txtLocation.setText(event.getAddress());
                locationContainer.setVisibility(View.VISIBLE);
            } else {
                locationContainer.setVisibility(View.GONE);
                timeContainer.setVisibility(View.GONE);
            }

            if (event.getTitle().equals(context.getResources().getString(com.github.tibolte.agendacalendarview.R.string.agenda_event_no_events))) {
                txtTitle.setTextColor(context.getResources().getColor(android.R.color.black));
            } else {
                txtTitle.setTextColor(context.getResources().getColor(com.github.tibolte.agendacalendarview.R.color.theme_text_icons));
            }

            descriptionContainer.setBackgroundColor( Helper.getColorFromPriority(context,event.getPriority()));
            txtLocation.setTextColor(context.getResources().getColor(com.github.tibolte.agendacalendarview.R.color.theme_text_icons));
            txtTime.setTextColor(context.getResources().getColor(com.github.tibolte.agendacalendarview.R.color.theme_text_icons));
        }
    }
     public  interface MeetingItemClicked{
        void onItemClicked(int position, TblMeeting meeting);
     }
}
