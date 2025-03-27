package com.noam.noamproject1.screens;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.noam.noamproject1.R;
import com.noam.noamproject1.adapters.ChatAdapter;
import com.noam.noamproject1.models.Message;
import com.noam.noamproject1.services.AuthenticationService;
import com.noam.noamproject1.services.ChatService;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private ChatService chatService;
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private List<Message> messagesList = new ArrayList<>();
    private EditText editMessage;
    private Button btnSend;

    private String userId;
    private String receiverId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        userId = AuthenticationService.getInstance().getCurrentUserId();
        receiverId = "כובל צק"; //TODO getIntent().getStringExtra("OtherUser");

        chatService = new ChatService();

        // אתחול UI
        recyclerView = findViewById(R.id.recyclerView);
        editMessage = findViewById(R.id.editMessage);
        btnSend = findViewById(R.id.btnSend);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter = new ChatAdapter(messagesList);
        recyclerView.setAdapter(chatAdapter);

        // קבלת הודעות בזמן אמת
        chatService.getMessages(userId, receiverId, new ChatService.OnMessagesReceivedListener() {
            @Override
            public void onNewMessage(Message message) {
                messagesList.add(message);
                chatAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messagesList.size() - 1);
            }
        });

        // שליחת הודעה
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = editMessage.getText().toString().trim();
                if (!content.isEmpty()) {
                    chatService.sendMessage(userId, receiverId, content);
                    editMessage.setText("");
                }
            }
        });
    }
}
