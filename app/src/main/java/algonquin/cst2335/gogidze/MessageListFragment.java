package algonquin.cst2335.gogidze;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;



//Like an activity, but doesn't need to cover the whole screen
public class MessageListFragment extends Fragment {

    Button send;
    Button receive;
    EditText edit;
    RecyclerView chatList;
    MyChatAdapter adt;
    ArrayList<ChatMessage> messages = new ArrayList<>();
    SQLiteDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View chatLayout = inflater.inflate(R.layout.chatlayout, container, false);


        //has onCreate, onStart, onResume, onPause, onStop, onDestroy
        //onCreateView -- load the XML layout, onAttach, onDetach //

        chatList = chatLayout.findViewById(R.id.myRecycler);
        chatList.setAdapter(new MyChatAdapter());
        send = chatLayout.findViewById(R.id.sendButton);
        receive = chatLayout.findViewById(R.id.receiveButton);
        edit = chatLayout.findViewById(R.id.editText);

        MyOpenHelper opener = new MyOpenHelper(getContext());
        SQLiteDatabase db = opener.getWritableDatabase();

        send.setOnClickListener(click -> {

            Date timeNow = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("EE, dd-MMM-yyyy hh-mm-ss a", Locale.getDefault());
            String time = sdf.format(new Date());

            ChatMessage cm = new ChatMessage(edit.getText().toString(), 1, time, 1);
            ContentValues newRow = new ContentValues();
            newRow.put(MyOpenHelper.col_message, cm.getMessage());
            newRow.put(MyOpenHelper.col_send_receive, 1);
            newRow.put(MyOpenHelper.col_time_sent, cm.getTimeSent());
            db.insert(MyOpenHelper.TABLE_NAME, MyOpenHelper.col_message, newRow);

            long newId = db.insert(MyOpenHelper.TABLE_NAME, MyOpenHelper.col_message, newRow);
            cm.setId(newId);

            messages.add(cm);
            edit.setText("");

            db.rawQuery("Select * from " + MyOpenHelper.TABLE_NAME + ";", null);
            //load from the database:
            Cursor results = db.rawQuery("Select * from " + MyOpenHelper.TABLE_NAME + ";", null);

            //Convert column names to indices:
            int _idCol = results.getColumnIndex("_id");
            int messageCol = results.getColumnIndex(MyOpenHelper.col_message);
            int sendCol = results.getColumnIndex(MyOpenHelper.col_send_receive);
            int timeCol = results.getColumnIndex(MyOpenHelper.col_time_sent);
            //cursor is pointing to row -1
            while (results.moveToNext())  // returns false if no more data
            { // pointing to row 2
                int id = results.getInt(_idCol);
                String message = results.getString(messageCol);
                String timeSent = results.getString(timeCol);
                int sendOrReceive = results.getInt(sendCol);
                //add to arrayList:
                messages.add(new ChatMessage(message, sendOrReceive, timeSent, id));
            }

            adt = new MyChatAdapter();
            chatList.setAdapter(adt);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            chatList.setLayoutManager(layoutManager);
            adt.notifyItemChanged(messages.size() - 1);
        });

        receive.setOnClickListener(click -> {
            Date timeNow = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("EE, dd-MMM-yyyy hh-mm-ss a", Locale.getDefault());
            String time = sdf.format(new Date());

            ChatMessage cm = new ChatMessage(edit.getText().toString(), 2, time, 1);
            ContentValues newRow = new ContentValues();
            newRow.put(MyOpenHelper.col_message, cm.getMessage());
            newRow.put(MyOpenHelper.col_send_receive, cm.getSendOrReceive());
            newRow.put(MyOpenHelper.col_time_sent, cm.getTimeSent());
            db.insert(MyOpenHelper.TABLE_NAME, MyOpenHelper.col_message, newRow);

            long newId = db.insert(MyOpenHelper.TABLE_NAME, MyOpenHelper.col_message, newRow);
            cm.setId(newId);


            messages.add(cm);
            edit.setText("");

            db.rawQuery("Select * from " + MyOpenHelper.TABLE_NAME + ";", null);
            Cursor results = db.rawQuery("Select * from " + MyOpenHelper.TABLE_NAME + ";", null);

            int _idCol = results.getColumnIndex("_id");
            int messageCol = results.getColumnIndex(MyOpenHelper.col_message);
            int receiveCol = results.getColumnIndex(MyOpenHelper.col_send_receive);
            int timeCol = results.getColumnIndex(MyOpenHelper.col_time_sent);

            while (results.moveToNext()) {
                long id = results.getInt(_idCol);
                String message = results.getString(messageCol);
                String timeSent = results.getString(timeCol);
                int sendOrReceive = results.getInt(receiveCol);
                messages.add(new ChatMessage(message, sendOrReceive, timeSent, id));
            }

            adt = new MyChatAdapter();
            chatList.setAdapter(adt);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            chatList.setLayoutManager(layoutManager);
            adt.notifyItemChanged(messages.size() - 1);
        });
        return chatLayout;
    }
// this holds TextViews on a row:
private class MyRowViews extends RecyclerView.ViewHolder {

    TextView messageText;
    TextView timeText;

    public MyRowViews(View itemView) {
        super(itemView);

        itemView.setOnClickListener(click -> {
            ChatRoom parentActivity = (ChatRoom)getContext();
            int position = getAbsoluteAdapterPosition();
            parentActivity.userClickedMessage(messages.get(position), position);
//            ChatMessage removedMessage = messages.get(position);
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//
//            builder.setTitle("Question:")
//                    .setMessage("Do you want to delete this message " + removedMessage.getMessage())
//                    .setNegativeButton("No", (dialog, cl1) -> {
//                    })
//                    .setPositiveButton("Yes", (dialog, cl2) -> {
//                        messages.remove(position);
//                        adt.notifyItemRemoved(position);
//
//                        Snackbar.make(messageText, "You deleted message # " + position, Snackbar.LENGTH_LONG)
//                                .setAction("Undo", clk -> {
//
//                                    messages.add(position, removedMessage);
//                                    adt.notifyItemInserted(position);
//
//                                    // reinsert into the Database
//                                    db.execSQL("Insert into " + MyOpenHelper.TABLE_NAME + "values('" + removedMessage.getId()
//                                            + "','" + removedMessage.getMessage() +
//                                            "','" + removedMessage.getSendOrReceive() +
//                                            "','" + removedMessage.getTimeSent() + "');");
//                                })
//                                .show();
//
//                        // Delete from Database: returns number of rows deleted
//                        db.delete(MyOpenHelper.TABLE_NAME, "_id=?", new String[]{Long.toString(removedMessage.getId())});
//
//
//                    })
//                    .create().show();
        });

        messageText = itemView.findViewById(R.id.message);
        timeText = itemView.findViewById(R.id.time);


    }
    }
private class MyChatAdapter extends RecyclerView.Adapter<MyRowViews> {



    @Override
    public MyRowViews onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = getLayoutInflater();
        int layoutID;
        if(viewType == 1)  //Send
            layoutID = R.layout.sent_message;
        else  //Receive
            layoutID = R.layout.receive_message;
        View loadedRow = inflater.inflate(R.layout.sent_message, parent, false);
        MyRowViews initRow = new MyRowViews(loadedRow);



        return  initRow;
    }

    @Override
    public void onBindViewHolder(MyRowViews holder, int position) {



        ChatMessage thisMessage = messages.get(position);

        // String object:
        holder.messageText.setText(thisMessage.getMessage());
        holder.timeText.setText(thisMessage.getTimeSent());

    }

    public int getItemViewType(int position) {



        return 5;

    }

    @Override
    public int getItemCount() {
        return messages.size(); // how many items in the list
    }

}

class ChatMessage {
    String message;
    int sendOrReceive;
    String time;
    long id;

    public void setId(long l) { id = l;}
    public long getId() { return id; }

    public ChatMessage(String message, int sendOrReceive, String time, long id) {
        this.message = message;
        this.sendOrReceive = sendOrReceive;
        this.time = time;
        setId(id);
    }

    public String getMessage() {
        return message;
    }

    public int getSendOrReceive() {
        return sendOrReceive;
    }

    public String getTimeSent() {
        return time;
    }
}


}

