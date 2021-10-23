package algonquin.cst2335.gogidze;

import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChatRoom extends AppCompatActivity {

    Button send;
    EditText edit;
    RecyclerView chatList;
    ArrayList<ChatMessage> messages = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.chatlayout);
        chatList = findViewById(R.id.myrecycler);
        chatList.setAdapter(new MyChatAdapter());
        send = findViewById(R.id.sendButton);
        edit = findViewById(R.id.editText);

        send.setOnClickListener( click -> {
            String whatIsTyped = edit.getText().toString();
            Date timeNow = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());


            messages.add(currentDateandTime, whatIsTyped);
            edit.setText("");

            MyChatAdapter adt = new MyChatAdapter();
            chatList.setAdapter(adt);
            chatList.setLayoutManager(new LinearLayoutManager(this));
            adt.notifyItemChanged(messages.size() - 1);
        });
    }
    private class MyRowViews extends RecyclerView.ViewHolder {

        TextView messageText;
        TextView timeText;

        public MyRowViews(View itemView) {
            super(itemView);

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

                ChatMessage thisRow = messages.get(position);

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
        String timeSent;


        public ChatMessage(String message, int sendOrReceive, String timeSent) {
            this.message = message;
            this.sendOrReceive = sendOrReceive;
            this.timeSent = timeSent;
        }

        public String getMessage() {
            return message;
        }

        public int getSendOrReceive() {
            return sendOrReceive;
        }

        public String getTimeSent() {
            return timeSent;
        }
    }
}
