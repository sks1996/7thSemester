package com.example.a1405264.jarvis;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import mohitbadwal.rxconnect.RxConnect;

public class MainActivity extends AppCompatActivity {
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
      /*  speech = SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
                Log.d("qwerty","ready Speech begin");
            }

            @Override
            public void onBeginningOfSpeech() {
                Log.d("qwerty","Speech begin");
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {
                Log.d("qwerty","Error : " + i);
            }

            @Override
            public void onResults(Bundle bundle) {

                ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String text = "";
                for (String result : matches)
                    text += result + "\n";
                Log.d("qwerty",matches.get(0));
                if( matches.get(0).equalsIgnoreCase("hey jarvis"))
                {
                    startRecognition();
                    speech.stopListening();
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,"en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
        speech.startListening(recognizerIntent);

*/
        setRecyclerView();
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRecognition();
            }
        });

    }
    Button send;
    EditText chatMessage;
    List<ChatModel> chatModels;
    void setRecyclerView()
    {   chatModels=new ArrayList<>();
        send = (Button) findViewById(R.id.send_button);
        chatMessage = (EditText) findViewById(R.id.chat_message);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_chat);
        mLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        //recyclerView.setHasFixedSize(true);
        adapter = new ChatAdapter(MainActivity.this, chatModels);
        recyclerView.setAdapter(adapter);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chatMessage.getText().toString().trim().length()>0)
                {   ChatModel c=new ChatModel(chatMessage.getText().toString(),true,0,0,"");
                    chatModels.add(c);
                    adapter.notifyItemRangeInserted(0, chatModels.size());
                    adapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(chatModels.size() - 1);
                    chatMessage.setText("");
                    sendMessage(c);
                }
            }
        });
        textToSpeech=new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });
    }
    TextToSpeech textToSpeech;

    void sendMessage(final ChatModel chatModel)
    {
        RxConnect rxConnect=new RxConnect(MainActivity.this);
        rxConnect.setCachingEnabled(false);
        rxConnect.setAddToQueue(false);
        rxConnect.setParam("speech",chatModel.getMessage());
        rxConnect.execute("http://192.168.43.225:8080/jarvis/logicaction", RxConnect.GET, new RxConnect.RxResultHelper() {
            @Override
            public void onResult(String result) {

                Log.d("qwerty","result ="+ result);
                if (result != null && result.length() > 0) {
                    Gson gson = new Gson();
                    ChatModel ch = gson.fromJson(result, ChatModel.class);
                    chatModels.add(ch);
                    adapter.notifyItemRangeInserted(0, chatModels.size());
                    adapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(chatModels.size() - 1);
                    textToSpeech.speak(ch.getMessage(), TextToSpeech.QUEUE_FLUSH, null);
                }
            }
            @Override
            public void onNoResult() {
                ChatModel ch = new ChatModel("I am not that smart.",true,0,1,"");
                chatModels.add(ch);
                adapter.notifyItemRangeInserted(0, chatModels.size());
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(chatModels.size() - 1);
                textToSpeech.speak("I am not that smart.", TextToSpeech.QUEUE_FLUSH, null);
            }

            @Override
            public void onError(Throwable throwable) {
                ChatModel ch = new ChatModel("Jarvis is not available right now.",true,0,1,"");
                chatModels.add(ch);
                adapter.notifyItemRangeInserted(0, chatModels.size());
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(chatModels.size() - 1);
                textToSpeech.speak("Jarvis is not available right now.", TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }
    private static final int REQ_CODE_SPEECH_INPUT=0;
    void startRecognition()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Speak Now");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                   "Speech not supported",
                    Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> results = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String text = "";
                    for (String result : results)
                        text += result + "\n";
                    ChatModel ch=new ChatModel(results.get(0),true,0,0,"");
                    chatModels.add(ch);
                    adapter.notifyItemRangeInserted(0, chatModels.size());
                    adapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(chatModels.size() - 1);
                    sendMessage(ch);
                  //  speech.startListening(recognizerIntent);
                }
                break;
            }

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
      //  speech.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {

      //      speech.startListening(recognizerIntent);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
