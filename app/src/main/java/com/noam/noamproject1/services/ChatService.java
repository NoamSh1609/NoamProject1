package com.noam.noamproject1.services;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.database.*;
import com.noam.noamproject1.models.Message;
import java.util.*;

public class ChatService {
    private DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference("messages");

    // יצירת מזהה ייחודי לצ'אט בין שני המשתמשים
    private String getChatId(String user1, String user2) {
        return user1.compareTo(user2) < 0 ? user1 + "_" + user2 : user2 + "_" + user1;
    }

    // שליחת הודעה
    public void sendMessage(String senderId, String receiverId, String content) {
        String chatId = getChatId(senderId, receiverId); // יצירת מזהה ייחודי לצ'אט
        String messageId = messagesRef.child(chatId).push().getKey(); // יצירת מזהה להודעה

        Message message = new Message(senderId, receiverId, content, System.currentTimeMillis()); // יצירת הודעה

        if (messageId != null) {
            // שמירת ההודעה ב-Firebase
            messagesRef.child(chatId).child(messageId).setValue(message)
                    .addOnSuccessListener(aVoid -> Log.d("ChatService", "Message sent"))
                    .addOnFailureListener(e -> Log.e("ChatService", "Error sending message", e));
        }
    }

    // שליפת הודעות בין שני משתמשים
    public void getMessages(String user1, String user2, OnMessagesReceivedListener listener) {
        String chatId = getChatId(user1, user2); // יצירת מזהה ייחודי לצ'אט

        // הוספת מאזין לשינויים בהודעות
        messagesRef.child(chatId).addChildEventListener(new ChildEventListener() {

            // נקרא כאשר הודעה חדשה התווספה
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
                Message message = snapshot.getValue(Message.class); // המרת ה-DataSnapshot להודעה
                if (message != null) {
                    listener.onNewMessage(message); // מעבירים את ההודעה למאזין
                }
            }

            // נקרא כאשר יש שינוי בהודעה קיימת
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, String previousChildName) {
                // אפשר להוסיף לוגיקה במקרה של שינוי בהודעה אם צריך
                Message message = snapshot.getValue(Message.class);
                if (message != null) {
                    listener.onNewMessage(message); // לעדכן את ההודעה המשתנה
                }
            }

            // נקרא כאשר הודעה נמחקה
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // אפשר להוסיף לוגיקה במקרה של מחיקת הודעה אם צריך
                Log.d("ChatService", "Message removed");
            }

            // נקרא כאשר הודעה שינתה את המיקום שלה (למשל אם מיון מחדש קרה)
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, String previousChildName) {
                // אפשר להוסיף לוגיקה במקרה של שינוי מיקום הודעה אם צריך
                Log.d("ChatService", "Message moved");
            }

            // נקרא כאשר יש שגיאה בשליפה מהנתונים
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ChatService", "Database error: " + error.getMessage()); // הצגת שגיאה
            }
        });
    }

    // ממשק לקבלת הודעות חדשות
    public interface OnMessagesReceivedListener {
        void onNewMessage(Message message); // פונקציה שמתקבלת בהודעה חדשה
    }
}
