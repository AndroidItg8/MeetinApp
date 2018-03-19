package itg8.com.meetingapp.home;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.tibolte.agendacalendarview.AgendaCalendarView;
import com.github.tibolte.agendacalendarview.CalendarManager;
import com.github.tibolte.agendacalendarview.CalendarPickerController;
import com.github.tibolte.agendacalendarview.models.BaseCalendarEvent;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;
import com.github.tibolte.agendacalendarview.models.DayItem;
import com.github.tibolte.agendacalendarview.models.IDayItem;
import com.github.tibolte.agendacalendarview.models.IWeekItem;
import com.github.tibolte.agendacalendarview.models.WeekItem;
import com.github.tibolte.agendacalendarview.widgets.CalenderSaveInstance;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import itg8.com.meetingapp.R;
import itg8.com.meetingapp.common.CommonMethod;
import itg8.com.meetingapp.common.Helper;
import itg8.com.meetingapp.custom_calender.DrawableEventRenderer;
import itg8.com.meetingapp.db.DaoMeetingInteractor;
import itg8.com.meetingapp.db.TblMeeting;
import itg8.com.meetingapp.import_meeting.MeetingDetailActivity;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements CalendarPickerController {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "HomeFragment";
    private static final String LOG_TAG = HomeFragment.class.getSimpleName();
    private static final int RC_CHANGE_MEETING = 12;
    private static final String MEETING_LIST = "MEETING_LIST";
    private static final String EVENTS_ID = "EVENTS_ID";
    private static final String BASE_EVENT = "BASE_EVENT";


    @BindView(R.id.agenda_calendar_view)
    AgendaCalendarView agendaCalendarView;
    Unbinder unbinder;

    // TODO: Rename and change types of parameters
    private HashMap<Integer,Boolean> priority;
    private String mParam2;
    private DaoMeetingInteractor daoMeeting;
    private CalendarManager calendarManager;
    private boolean checkedOnces=false;
    private boolean hasView;
    private List<TblMeeting> listMeeting;
    private CalendarEvent singleEvent;
    private long eventId;
    private CalenderSaveInstance listener;


    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     * @param priority
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(HashMap<Integer, Boolean> priority) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, priority);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            priority = (HashMap<Integer, Boolean>) getArguments().getSerializable(ARG_PARAM1);
            assert priority != null;
            Log.d(TAG, "onCreate: Priority: "+priority.toString());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MEETING_LIST, (ArrayList<? extends Parcelable>) listMeeting);
}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        hasView=true;
        checkedOnces=false;
        daoMeeting= new DaoMeetingInteractor(getActivity());
            getEntriesFromDb();
        return view;
    }

    Scheduler DB=Schedulers.newThread();
    private void getEntriesFromDb() {
        Observable.fromCallable(new Callable<List<TblMeeting>>() {
            @Override
            public List<TblMeeting> call() throws Exception {
        listMeeting = daoMeeting.getMeetingSortByWithPriority(TblMeeting.START_TIME,priority);
                    return listMeeting;
            }
        }).flatMap(new Function<List<TblMeeting>, ObservableSource<List<TblMeeting>>>() {
            @Override
            public ObservableSource<List<TblMeeting>> apply(List<TblMeeting> tblMeetings) throws Exception {
                return getCalanderObservable(tblMeetings);
            }
        }).subscribeOn(DB).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<TblMeeting>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<TblMeeting> tblMeetings) {
                if(hasView)
                    calenderStuff(tblMeetings);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                checkedOnces=true;
            }
        });
    }


    private ObservableSource<List<TblMeeting>> getCalanderObservable(final List<TblMeeting> tblMeetings) {
        return Observable.create(new ObservableOnSubscribe<List<TblMeeting>>() {
            @Override
            public void subscribe(ObservableEmitter<List<TblMeeting>> e) throws Exception {
                Log.d(TAG, "subscribe: TblMeetingList: "+tblMeetings.size());
                Calendar minDate = Calendar.getInstance();
                Calendar maxDate = Calendar.getInstance();
                if(tblMeetings.size()>0)
                {
                    minDate.setTime(tblMeetings.get(0).getStartTime());
                    maxDate.setTime(tblMeetings.get(tblMeetings.size() - 1).getStartTime());
                }
                calendarManager = CalendarManager.getInstance(getActivity());
                calendarManager.buildCal(minDate,maxDate, Locale.getDefault(),new DayItem(), new WeekItem());
                e.onNext(tblMeetings);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public void onResume() {
        super.onResume();
        if(checkedOnces){
            getEntriesFromDb();
        }
    }

    private void calenderStuff(List<TblMeeting> tblMeetings)
    {
        agendaCalendarView.enableCalenderView(true);
        agendaCalendarView.enableFloatingIndicator(true);
        initEvent(tblMeetings);


    }

    private void initEvent(final List<TblMeeting> tblMeetings) {
        Observable.fromCallable(new Callable<List<CalendarEvent>>() {
            @Override
            public List<CalendarEvent> call() throws Exception {
                List<CalendarEvent> events=new ArrayList<>();
                BaseCalendarEvent event=null;
                for (TblMeeting meeting :
                        tblMeetings) {
                    Calendar startTime=Calendar.getInstance();
                    Calendar endTime=Calendar.getInstance();
                    startTime.setTime(meeting.getStartTime());
                    endTime.setTime(meeting.getEndTime());
                    event = new BaseCalendarEvent(meeting.getPkid(),meeting.getTitle(), Helper.getStringTimeFromDate(meeting.getStartTime())+" - "+Helper.getStringTimeFromDate(meeting.getEndTime()),getAddress(meeting.getAddress()),
                            Helper.getColorFromPriority(getActivity(),meeting.getPriority()), startTime, endTime, true);
                    events.add(event);
                }
                return events;
            }
        }).subscribeOn(DB)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<CalendarEvent>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<CalendarEvent> calendarEvents) {
                        calendarManager.loadEvents(calendarEvents,new BaseCalendarEvent());
                        initOtherRenderer();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private String getAddress(String address) {
        if(TextUtils.isEmpty(address))
            return " ";
        return address;
    }

    private void initOtherRenderer() {
        List<CalendarEvent> readyEvents = calendarManager.getEvents();
        List<IDayItem> readyDays = calendarManager.getDays();
        List<IWeekItem> readyWeeks = calendarManager.getWeeks();
        Log.d(TAG, "initOtherRenderer:Locale.getDefault() "+Locale.getDefault()+"readyWeeks:"+readyWeeks.toString()+"readyDays:"+readyDays.toString()+"readyEvents");
        agendaCalendarView.init(Locale.getDefault(),readyWeeks,readyDays,readyEvents,this);
        agendaCalendarView.addEventRenderer(new DrawableEventRenderer());
    }

    // endregion

    // region Interface - CalendarPickerController

    @Override
    public void onDaySelected(IDayItem dayItem) {
        Log.d(LOG_TAG, String.format("Selected day: %s", dayItem));

    }

    @Override
    public void onEventSelected(CalendarEvent event) {
        Log.d(LOG_TAG, String.format("Selected event: %s", event.getId()));
       eventId = event.getId();

        if(event.getId()==0)
            return;
        try {
            TblMeeting meeting=daoMeeting.getMeetingById(event.getId());

            Intent intent=new Intent(getActivity(), MeetingDetailActivity.class);
            intent.putExtra(CommonMethod.EXTRA_MEETING,meeting);
            startActivity(intent);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onScrollToDate(Calendar calendar) {
        if (((HomeActivity)getActivity()).getSupportActionBar() != null) {
            getActivity().setTitle(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));

        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hasView=false;
        listMeeting=null;
       agendaCalendarView.onViewDestoyed();
        unbinder.unbind();
    }




//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        if(listMeeting == null)
//        {
//            daoMeeting= new DaoMeetingInteractor(getActivity());
//            getEntriesFromDb();
//
//        }else
//        {
//            listMeeting= savedInstanceState.getParcelableArrayList(MEETING_LIST);
//            eventId = savedInstanceState.getLong(EVENTS_ID);
//            if(hasView)
//                calenderStuff(listMeeting);
//            if(listener!=null)
//                listener.onSaveInstanceState(true,this);
//        }
//
//    }
//
//    @Override
//    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
//        super.onViewStateRestored(savedInstanceState);
//        if (savedInstanceState != null) {
//            if (savedInstanceState.getParcelableArrayList(MEETING_LIST) != null) {
//                listMeeting = savedInstanceState.getParcelableArrayList(MEETING_LIST);
//                eventId = savedInstanceState.getLong(EVENTS_ID);
//            }
//        }
//    }



}
