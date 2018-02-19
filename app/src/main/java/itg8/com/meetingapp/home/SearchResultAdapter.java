package itg8.com.meetingapp.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.meetingapp.R;
import itg8.com.meetingapp.widget.search.SearchResult;



/**
 * Created by Android itg 8 on 2/19/2018.
 */

class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchViewHolder> {

    private static final String TAG = SearchResultAdapter.class.getSimpleName();
    private Context context;
    private List<SearchResult> listSearchResult;

    public SearchResultAdapter(Context context, List<SearchResult> listSearchResult) {
        this.context = context;
        this.listSearchResult = listSearchResult;
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        holder.txtValue.setText(listSearchResult.get(position).getTitle().getTitle());

    }


    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: size"+listSearchResult.size());
        return listSearchResult.size();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_value)
        TextView txtValue;
        public SearchViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
