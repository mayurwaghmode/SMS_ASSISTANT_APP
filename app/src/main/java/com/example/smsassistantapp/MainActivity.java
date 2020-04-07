package com.example.smsassistantapp;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends Activity implements View.OnClickListener {

    Button btnSent, btnInbox, btnDraft;
    // Cursor Adapter
    SimpleCursorAdapter adapter;
    String colName;


    private static final String TAG = "CardListActivity";
    private CardArrayAdapter cardArrayAdapter;
    private SentCardArrayAdapter sentArrayAdapter;
    private DraftCardArrayAdapter draftCardArrayAdapter;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.card_listView);

        listView.addHeaderView(new View(this));
        listView.addFooterView(new View(this));
        btnInbox = (Button) findViewById(R.id.btnInbox);
        btnInbox.setOnClickListener(this);

        btnSent = (Button) findViewById(R.id.btnSentBox);
        btnSent.setOnClickListener(this);

        btnDraft = (Button) findViewById(R.id.btnDraft);
        btnDraft.setOnClickListener(this);

        cardArrayAdapter = new CardArrayAdapter(getApplicationContext(), R.layout.list_item_card);
        sentArrayAdapter = new SentCardArrayAdapter(getApplicationContext(),R.layout.list_item_card);
        draftCardArrayAdapter = new DraftCardArrayAdapter(getApplicationContext(),R.layout.list_item_card);
    }

    @Override
    public void onClick(View v) {

        if (v == btnDraft) {
            // Create Draft box URI
            Uri draftURI = Uri.parse("content://sms/draft");

            // List required columns
            String[] reqCols = new String[]{"_id", "address", "body"};

            // Get Content Resolver object, which will deal with Content
            // Provider
            ContentResolver cr = getContentResolver();

            // Fetch Sent SMS Message from Built-in Content Provider
            Cursor draftcursor = cr.query(draftURI, reqCols, null, null, null);

            // Attached Cursor with adapter and display in listview
            String body = "", address = "";
            if (draftcursor.moveToFirst()) {
                while (!draftcursor.isAfterLast()) {
                    body = draftcursor.getString(draftcursor.getColumnIndex("body"));
                    address = draftcursor.getString(draftcursor.getColumnIndex("address"));
                    // do what ever you want here
                    DraftCard draftcard = new DraftCard("Sender :" +address ,"Message :"+body);
                    draftCardArrayAdapter.add(draftcard);
                    draftcursor.moveToNext();
                }
            }
            draftcursor.close();
            listView.setAdapter(draftCardArrayAdapter);
        }
        if (v == btnInbox) {
            cardArrayAdapter.clear();
            cardArrayAdapter.notifyDataSetChanged();

            // Create Draft box URI
            Uri draftURI = Uri.parse("content://sms/inbox");

            // List required columns
            String[] reqCols = new String[]{"_id", "address", "body"};

            // Get Content Resolver object, which will deal with Content
            // Provider
            ContentResolver cr = getContentResolver();

            // Fetch Sent SMS Message from Built-in Content Provider
            Cursor inboxcursor = cr.query(draftURI, reqCols, null, null, null);

            // Attached Cursor with adapter and display in listview
            String body = "", address = "";
            if (inboxcursor.moveToFirst()) {
                while (!inboxcursor.isAfterLast()) {
                    body = inboxcursor.getString(inboxcursor.getColumnIndex("body"));
                    address = inboxcursor.getString(inboxcursor.getColumnIndex("address"));
                    // do what ever you want here
                    Card card = new Card("Sender :" +address ,"Message :"+body);
                    cardArrayAdapter.add(card);
                    inboxcursor.moveToNext();
                }
            }
            inboxcursor.close();
            listView.setAdapter(cardArrayAdapter);
        }
        if (v == btnSent) {
            cardArrayAdapter.clear();
            cardArrayAdapter.notifyDataSetChanged();
            // Create Draft box URI
            Uri draftURI = Uri.parse("content://sms/sent");

            // List required columns
            String[] reqCols = new String[]{"_id", "address", "body"};

            // Get Content Resolver object, which will deal with Content
            // Provider
            ContentResolver cr = getContentResolver();

            // Fetch Sent SMS Message from Built-in Content Provider
            Cursor cursor = cr.query(draftURI, reqCols, null, null, null);

            // Attached Cursor with adapter and display in listview
            String body = "", address = "";
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    body = cursor.getString(cursor.getColumnIndex("body"));
                    address = cursor.getString(cursor.getColumnIndex("address"));
                    // do what ever you want here
                    SentCard sentcard = new SentCard("Sender :" +address ,"Message :"+body);
                    sentArrayAdapter.add(sentcard);
                    cursor.moveToNext();
                }
            }
            cursor.close();
            listView.setAdapter(sentArrayAdapter);
        }
    }
}