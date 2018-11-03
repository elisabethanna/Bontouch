package com.example.android.optimeradse;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements HttpGetRequest.AsyncResponse {
    HttpGetRequest asyncTask = new HttpGetRequest();
    ArrayList<String> wordsFromMap= new ArrayList<>();
    String[] items;
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;
    Map<String, List<String>> hmap= new HashMap<>();


    ListView listView;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //connects the classes through the interface
        asyncTask.delegate = this;

        setContentView(R.layout.searchable);
        listView = (ListView) findViewById(R.id.listview);
        editText = (EditText) findViewById(R.id.txtsearch);

        getData();

        //listens to what the user types
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            //when user has typed anyhting then searchItems is called
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchItem(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void getData() {
        //gets the words from the URL
        asyncTask.execute("http://runeberg.org/words/ss100.txt");
    }
    //this function works so we can get the output from the HttpGetRequest
    @Override
    public void storeWordsInHMap(String output){
        //items is the entire wordlist as a String[]
        items = output.split(" ");

        //iterate over each word
        for (String item : items) {
            //takes out the first letter of the word and converts it to a string
            char key = item.charAt(0);
            String stringKey = Character.toString(key);

            //if the first letter already is a key, than add to the value of the key (is a list)
            if (hmap.containsKey(stringKey)) {
                hmap.get(stringKey).add(item);
            } else {
                //if the letter is unique, then create a slot in the hashmap with the letter as key and the word in a list
                //as value
                hmap.put(stringKey, new ArrayList<String>(Arrays.asList(item)));
            }
        }

        //creates the list that shows all suggestions
        listItems = new ArrayList<>();
        //connects the list to the design through the adapter
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.txtitem, listItems);
        listView.setAdapter(adapter);
    }


    public void searchItem(String textToSearch) {
        //clear the list that shows the suggestions
        listItems.clear();

        int len = textToSearch.length();

        //if it's only one character in the user input, then use this character as key an get the value. The value is added
        //to listItems
        if (len == 1) {
            listItems.addAll(hmap.get(textToSearch));
            //makes copy of listItems
            wordsFromMap = new ArrayList<>(listItems);
        }

        //if the user input is longer than one word
        else if (len > 1) {
            //iterate through the copy
            for (String item : wordsFromMap) {
                //add the words that starts with the user input to listItems
                if(item.matches(textToSearch + "(.*)")) {
                    listItems.add(item);
                }

            }
        }
        //update adapter
        adapter.notifyDataSetChanged();

    }








}


