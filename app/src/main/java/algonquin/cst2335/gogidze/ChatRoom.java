package algonquin.cst2335.gogidze;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChatRoom extends AppCompatActivity {

    Button send;
    Button receive;
    EditText edit;
    RecyclerView chatList;
    MyChatAdapter adt;
    ArrayList<ChatMessage> messages = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatlayout);

        chatList = findViewById(R.id.myrecycler);
        chatList.setAdapter(new MyChatAdapter());
        send = findViewById(R.id.sendButton);
        receive = findViewById(R.id.receiveButton);
        edit = findViewById(R.id.editText);

        MyOpenHelper opener = new MyOpenHelper(this);
        SQLiteDatabase db = opener.getWritableDatabase();

        send.setOnClickListener( click -> {

            Date timeNow = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("EE, dd-MMM-yyyy hh-mm-ss a", Locale.getDefault());
            String time = sdf.format(new Date());

            ChatMessage cm = new ChatMessage( edit.getText().toString(), 1, time, 1 );
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
            int sendCol = results.getColumnIndex(MyOpenHelper.col_send_receive);
            int timeCol = results.getColumnIndex(MyOpenHelper.col_time_sent);

        while(results.moveToNext()) {
            long id = results.getInt(_idCol);
            String message = results.getString(messageCol);
            String timeSent = results.getString(timeCol);
            int sendOrReceive = results.getInt(sendCol);
            messages.add(new ChatMessage(message, sendOrReceive, timeSent, id));
        }

            adt = new MyChatAdapter();
            chatList.setAdapter(adt);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            chatList.setLayoutManager( layoutManager);
            adt.notifyItemChanged(messages.size() - 1);
        });

        receive.setOnClickListener( click -> {
             Date timeNow = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("EE, dd-MMM-yyyy hh-mm-ss a", Locale.getDefault());
            String time = sdf.format(new Date());

            ChatMessage cm = new ChatMessage( edit.getText().toString(), 2, time, 1 );
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

            while(results.moveToNext()) {
                long id = results.getInt(_idCol);
                String message = results.getString(messageCol);
                String timeSent = results.getString(timeCol);
                int sendOrReceive = results.getInt(receiveCol);
                messages.add(new ChatMessage(message, sendOrReceive, timeSent, id));
            }

            adt = new MyChatAdapter();
            chatList.setAdapter(adt);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            chatList.setLayoutManager( layoutManager);
            adt.notifyItemChanged(messages.size() - 1);
        });
    }
    private class MyRowViews extends RecyclerView.ViewHolder {

        TextView messageText;
        TextView timeText;

        public MyRowViews(View itemView) {
            super(itemView);

            itemView.setOnClickListener( click -> {
                int position = getAbsoluteAdapterPosition();
                ChatMessage whatClicked = messages.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);

                builder.setTitle("Question:")
                    .setMessage("Do you want to delete this message " + whatClicked.getMessage())
                    .setNegativeButton("No", (dialog, cl1) -> {})
                    .setPositiveButton("Yes", (dialog, cl2) -> {
                    messages.remove(position);
                    adt.notifyItemRemoved(position);

                    Snackbar.make(messageText, "You deleted message # "+ position, Snackbar.LENGTH_LONG)
                            .setAction("Undo", clk -> {

                                messages.add(position, whatClicked);
                                adt.notifyItemInserted(position);
                            })
                            .show();



                })
                .create().show();
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


                holder.messageText.setText(thisMessage.getMessage());
                holder.timeText.setText(thisMessage.getTimeSent());

            }

            public int getItemViewType(int position) {



                return 5;
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }
    }

    private class ChatMessage {
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
