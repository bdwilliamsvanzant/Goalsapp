package com.example.goals.DatabaseActivities;


import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.example.goals.Goal;
import com.example.goals.Database.GoalDatabase;
import com.example.goals.R;
import com.example.goals.util.OtherReceiver;
import com.google.android.material.textfield.TextInputEditText;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;


import android.app.Notification;
import android.content.Context;
import android.os.SystemClock;

public class AddGoalActivity extends AppCompatActivity {

    private TextInputEditText et_title, et_content, et_startDate, et_endDate;
    private DatePickerDialog datepicker;
    private GoalDatabase goalDatabase;
    private Goal goal;
    private boolean update;
    private int points;
    private Spinner difficulty;
    private int fillcounter = 0;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private NotificationManagerCompat notificationManager;


    private String longToString(long temp)
    {
        Date time = new Date(temp);
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        return (Integer.toString(cal.get(Calendar.MONTH)+1) +"/" + Integer.toString(cal.get(Calendar.DAY_OF_MONTH)) + "/" + Integer.toString(cal.get(Calendar.YEAR)));
    }

    private long stringToLong(String temp)
    {
        Date time = new Date();
        try{
            Date time1 = new SimpleDateFormat("MM/dd/yyyy").parse(temp);

            time.setTime(time1.getTime());

        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return time.getTime();
    }

    private Date start_Date, end_Date;
    private DatePickerDialog.OnDateSetListener sDateSetListener, eDateSetListener;
    String startDateString, endDateString;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);
        et_title = findViewById(R.id.et_title);
        et_content = findViewById(R.id.et_content);
        et_startDate = findViewById(R.id.et_startDate);
        et_endDate = findViewById(R.id.et_endDate);
        difficulty = findViewById(R.id.difficulty_spinner);

        goalDatabase = GoalDatabase.getInstance(AddGoalActivity.this);

        Button save_butt = findViewById(R.id.but_save);
        if( (goal = (Goal) getIntent().getSerializableExtra("goal"))!=null)
        {
            update = true;
            et_title.setText(goal.getGoal_name());
            et_content.setText(goal.getContent());
            et_startDate.setText(dateFormat.format(goal.getStart_time()));
            et_endDate.setText(dateFormat.format(goal.getEnd_time()));
            difficulty.setSelection(goal.getDifficulty());
            end_Date = fromTimestamp(goal.getEnd_time());
            start_Date = fromTimestamp(goal.getStart_time());
        }

        save_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (update) {
                    if(et_content.getText().toString().equals("") || et_title.getText().toString().equals("") || et_startDate.getText().toString().equals("") || et_endDate.getText().toString().equals("") ){
                        fillcounter += 1;
                    }
                    if(fillcounter == 0){
                    if (et_content.getText() != null) {
                        goal.setContent(et_content.getText().toString());
                    }
                    if (et_title.getText() != null) {
                        goal.setGoal_name(et_title.getText().toString());
                    }
                    if (et_startDate.getText() != null) {
                        //goal.setStart_time(stringToLong(et_startDate.getText().toString()));
                        goal.setStart_time(start_Date.getTime());
                    }
                    if (et_endDate.getText() != null) {
                        goal.setEnd_time(end_Date.getTime());
                    }
                    goalDatabase.getGoalDao().updateGoal(goal);
                    setResult(goal, 2);
                }
                else{
                    AlertDialog alertDialog = new AlertDialog.Builder(AddGoalActivity.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("Fill in all fields");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    fillcounter = 0;
                }
                } else {
                    points = (difficulty.getSelectedItemPosition()+1) * 100;

                    //String content, String goal_name, int difficulty, int points, String start_time, String end_time
                if(et_content.getText().toString().equals("") || et_title.getText().toString().equals("") || et_startDate.getText().toString().equals("") || et_endDate.getText().toString().equals("") ){
                    fillcounter += 1;
                }
                if (et_content.getText() != null && et_title.getText() != null && et_startDate.getText() != null && et_endDate.getText() != null && fillcounter == 0) {
                        try {


                            //Date start = new SimpleDateFormat("MM/dd/yyyy",Locale.US).parse(et_startDate.getText().toString());
                            Date end = new SimpleDateFormat("MM/dd/yyyy",Locale.US).parse(et_endDate.getText().toString());
                            goal = new Goal(et_content.getText().toString(),
                                    et_title.getText().toString(),
                                    difficulty.getSelectedItemPosition(),
                                    points,
                                    start_Date.getTime(),
                                    end_Date.getTime());
                            new InsertTask(AddGoalActivity.this, goal).execute();
                            createNotification(end_Date.getTime());
                        }
                        catch (ParseException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else{
                    AlertDialog alertDialog = new AlertDialog.Builder(AddGoalActivity.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("Fill in all fields");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    fillcounter = 0;
                }
                }
            }

        });


        //final Button startDate_butt = findViewById(R.id.startDatebutton);
        //Button endDate_butt = findViewById(R.id.endDateButton);

        et_startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                datepicker = new DatePickerDialog(AddGoalActivity.this,
                        sDateSetListener,
                        year, month, day);
                if(end_Date != null){
                    if(getDateFromDatePicker(datepicker.getDatePicker()).after(end_Date)){
                        datepicker.getDatePicker().setMaxDate(dateToTimestamp(end_Date));
                    }

                }
                datepicker.show();
            }
        });

        sDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                start_Date = getDateFromDatePicker(datepicker.getDatePicker());
                Date currDate = new Date(System.currentTimeMillis());
                end_Date = (start_Date.before(currDate)?currDate:start_Date);
                startDateString = dateFormat.format(start_Date);
                //end_Date = (end_Date.before(start_Date))?start_Date:end_Date;
                et_startDate.setText(startDateString);
                et_endDate.setText(dateFormat.format(end_Date));
            }
        };



        et_endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!et_startDate.getText().toString().equals("")){

                    final Calendar cldr = Calendar.getInstance();

                    int day = cldr.get(Calendar.DAY_OF_MONTH);
                    int month = cldr.get(Calendar.MONTH);
                    int year = cldr.get(Calendar.YEAR);


                    try {
                        //Date temp was start_Date
                        Date tem = new SimpleDateFormat("MM/dd/yyyy", Locale.US).parse(et_startDate.getText().toString());
                        Date temp = start_Date;
                        //Date newDate is end_Date
                        Date newDate = new Date(System.currentTimeMillis());
                        datepicker = new DatePickerDialog(AddGoalActivity.this,
                                eDateSetListener, year, month, day);
                        long current = temp.getTime() > newDate.getTime() ? temp.getTime(): newDate.getTime();
                        datepicker = new DatePickerDialog(AddGoalActivity.this, eDateSetListener,year, month, day);
                        datepicker.getDatePicker().setMinDate(current);

                        datepicker.show();
                    }
                    catch (ParseException e) {              // Insert this block.
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        });

        eDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                end_Date = getDateFromDatePicker(datepicker.getDatePicker());
                endDateString = dateFormat.format(end_Date);
                et_endDate.setText(endDateString);
            }
        };


        //Spinner spinner = findViewById(R.id.difficulty_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.difficulty_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficulty.setAdapter(adapter);
    }

    public Long dateToTimestamp(Date date) {
        if (date == null) {
            return null;
        } else {
            return date.getTime();
        }
    }

    public Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }



    public static java.util.Date getDateFromDatePicker(DatePicker datePicker) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }

    private void setResult(Goal goal, int flag){
        setResult(flag, new Intent().putExtra("Goal", goal));
        finish();
    }

    private void createNotification(long end_Date){
        Date today = new Date();

        end_Date = end_Date - today.getTime();
        long oneDay = 86400000;
        if (end_Date <= oneDay){
            end_Date = 0;
        }

        Log.i("createNotification","entered");
        scheduleNotification(getNotification("delay time"), (int)end_Date);
    }

    private void scheduleNotification(Notification notification, int delay) {
        Log.i("scheduleNotification","entered");
        Intent notificationIntent = new Intent(this, OtherReceiver.class);
        notificationIntent.putExtra(OtherReceiver.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(OtherReceiver.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    private Notification getNotification(String content) {
        Log.i("getNotification","entered");
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Scheduled Notification");
        builder.setContentText(content);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        return builder.build();
    }

    private static class InsertTask extends AsyncTask<Void, Void, Boolean> {
        private WeakReference<AddGoalActivity> activityReference;
        private Goal goal;

        InsertTask(AddGoalActivity context, Goal goal){
            activityReference = new WeakReference<>(context);
            this.goal = goal;
        }

        @Override
        protected Boolean doInBackground(Void... objs){
            long j = activityReference.get().goalDatabase.getGoalDao().insertGoal(goal);
            goal.setGoal_id(j);
            Log.e("ID", "doInBackground: " + j);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean bool){
            if(bool){
                activityReference.get().setResult(goal,1);
                activityReference.get().finish();
            }
        }
    }
}

