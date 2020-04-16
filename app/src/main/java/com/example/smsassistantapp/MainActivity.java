package com.example.smsassistantapp;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;

import java.io.StreamTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends Activity implements View.OnClickListener ,SearchView.OnQueryTextListener {

    Button btnSent, btnInbox, btnPay;
    // Cursor Adapter
    SimpleCursorAdapter adapter;
    String colName;

    EditText amountEt, noteEt, nameEt, upiIdEt;
    Button send;

    final int UPI_PAYMENT = 0;

    private static final String TAG = "CardListActivity";
    private CardArrayAdapter cardArrayAdapter;
    private SentCardArrayAdapter sentArrayAdapter;
    private ListView listView;
    private SearchView mSearchView;

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

        btnPay = (Button) findViewById(R.id.billbtn);

        cardArrayAdapter = new CardArrayAdapter(getApplicationContext(), R.layout.list_item_card);
        sentArrayAdapter = new SentCardArrayAdapter(getApplicationContext(),R.layout.list_item_card);

        mSearchView=(SearchView) findViewById(R.id.searchView1);
    }
    @Override
    public void onClick(View v) {

        String re = "(?:Rs\\.?|INR)\\s*(\\d+(?:[.,]\\d+)*)|(\\d+(?:[.,]\\d+)*)\\s*(?:Rs\\.?|INR)";
        Pattern credittransaction = Pattern.compile("\\bcredited\\b");
        Pattern debittransaction = Pattern.compile("\\bdebited\\b");
        Pattern offercode = Pattern.compile("\\bcode\\b");
        Pattern bill = Pattern.compile("\\bBill|bill\\b");
        Pattern amount = Pattern.compile(re);
        String o = "(?<=\\s|^)[A-Z0-9]{2,}(?=\\s|$)";
        Pattern offer = Pattern.compile(o);


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
            String body = "", address = "", label = "Namaste", value = "";


            if (inboxcursor.moveToFirst()) {
                while (!inboxcursor.isAfterLast()) {
                    value = "";
                    label = "";
                    body = inboxcursor.getString(inboxcursor.getColumnIndex("body"));
                    address = inboxcursor.getString(inboxcursor.getColumnIndex("address"));
                    // do what ever you want here

                    Matcher matcher = credittransaction.matcher(body);
                    if (matcher.find()) {
                        label = matcher.group() + " : ";
                        matcher = amount.matcher(body);
                        if (matcher.find()) {
                            value = matcher.group();
                        }

                    }
                    matcher = debittransaction.matcher(body);
                    if (matcher.find()) {
                        label = matcher.group() + " : ";
                        matcher = amount.matcher(body);
                        if (matcher.find()) {
                            value = matcher.group();
                        }
                    }

                    matcher = offercode.matcher(body);
                    if (matcher.find()) {
                        label = matcher.group() + " : ";
                        matcher = offer.matcher(body);
                        if (matcher.find()) {
                            value = matcher.group();
                        }
                    }
                    matcher = bill.matcher(body);
                    if (matcher.find()) {
                        label = matcher.group() + " : ";
                        matcher = amount.matcher(body);
                        if (matcher.find()) {
                            value = matcher.group();
                        }
                    }
                    Card card = new Card("Sender :" + address, label + value, "Message :" + body, btnPay);
                    cardArrayAdapter.add(card);
                    inboxcursor.moveToNext();
                }
            }
            inboxcursor.close();

            listView.setAdapter(cardArrayAdapter);


          /*  listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    //Dessert dessert = desserts.get(i);
                    Log.i("listview","listview is pressed");
                    Intent donut = new Intent(MainActivity.this, PayMoney.class);
                    startActivity(donut);
                }
            });
            */
            //for searching
            listView.setTextFilterEnabled(true);
            setupSearchView();
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
            String body = "", address = "", label="", value="";


            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    label = "";
                    value = "";
                    body = cursor.getString(cursor.getColumnIndex("body"));
                    address = cursor.getString(cursor.getColumnIndex("address"));
                    Matcher matcher = credittransaction.matcher(body);
                     if(matcher.find()) {
                        label = matcher.group()+" : ";
                         matcher = amount.matcher(body);
                         if(matcher.find()){
                             value = matcher.group();
                         }

                    }
                     matcher = debittransaction.matcher(body);
                     if (matcher.find()){
                         label = matcher.group()+" : ";
                         matcher = amount.matcher(body);
                         if(matcher.find()){
                             value = matcher.group();
                         }
                     }
                    matcher = offercode.matcher(body);
                    if (matcher.find()){
                        label = matcher.group() + " : ";
                        matcher = offer.matcher(body);
                        if (matcher.find()){
                            value = matcher.group();
                        }
                    }

                    matcher = bill.matcher(body);
                    if (matcher.find()){
                        label = matcher.group() + " : ";
                        matcher = amount.matcher(body);
                        if (matcher.find()){
                            value = matcher.group();
                        }
                    }

                    // do what ever you want here
                    SentCard sentcard = new SentCard("Sender :" +address ,label + value , "Message :"+body,btnPay);
                    sentArrayAdapter.add(sentcard);
                    cursor.moveToNext();
                }
            }
            cursor.close();
            listView.setAdapter(sentArrayAdapter);

            //for searching
            listView.setTextFilterEnabled(true);
            setupSearchView();
        }
    }
    private void setupSearchView()
    {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Search Here");
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {

        if (TextUtils.isEmpty(newText)) {
            listView.clearTextFilter();
        } else {
            listView.setFilterText(newText);
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query)
    {
        return false;
    }


}
