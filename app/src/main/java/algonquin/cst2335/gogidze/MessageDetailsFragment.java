package algonquin.cst2335.gogidze;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MessageDetailsFragment extends Fragment {

    MessageListFragment.ChatMessage chosenMessage;
    int chosenPosition;

    public MessageDetailsFragment(MessageListFragment.ChatMessage message, int position) {
        chosenMessage = message;
        chosenPosition = position;
    }

//    public void userClickedMessage(MessageListFragment.ChatMessage chatMessage, int position) {
//
//        messageDetailsFragment mdFragment = new MessageDetailsFragment(chatMessage, position);
//
//        if(isTablet) {
//
//            Fragment fMgr = getSupportFragmentManager();
//
//
//
//        } else {
//
//        }
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //load the layout
        View detailsView = inflater.inflate(R.layout.details_layout, container, false);

        TextView messageView = detailsView.findViewById(R.id.messageView);
        TextView sendView = detailsView.findViewById(R.id.sendView);
        TextView timeView = detailsView.findViewById(R.id.timeView);
        TextView idView = detailsView.findViewById(R.id.databaseView);

        messageView.setText(("Message is: " + chosenMessage.getMessage()));
        sendView.setText("Send or Receive?" + chosenMessage.getSendOrReceive());
        timeView.setText("Time send:" + chosenMessage.getTimeSent());
        idView.setText("Database id is:" + chosenMessage.getId());
        return detailsView;
    }


}
