package algonquin.cst2335.gogidze;

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

        send.setOnClickListener( click -> {
            String whatIsTyped = edit.getText().toString();
            Date timeNow = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());


            messages.add( new ChatMessage(whatIsTyped, 0, currentDateandTime));
            edit.setText("");

            adt = new MyChatAdapter();
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

            itemView.setOnClickListener( click -> {
                int position = getAdapterPosition();
                ChatMessage whatClicked = messages.get(position);

                MyRowViews newRow = adt.onCreateViewHolder(null, adt.getItemViewType(position));

                AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);

                builder.setMessage("Do you want to delete this message " + whatClicked.getMessage());
                builder.setTitle("Question: ");

                builder.setNegativeButton("No", (dialog, cl) -> {});
                builder.setPositiveButton("Yes", (dialog, cl) -> {
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
