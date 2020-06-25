package com.ksquarej.edunomics.NavigationDrawer;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.ksquarej.edunomics.BuildConfig;
import com.ksquarej.edunomics.R;

import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

import ai.api.AIDataService;
import ai.api.AIServiceContext;
import ai.api.AIServiceContextBuilder;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;

public class Botfragment extends Fragment {

    public Botfragment(){

    }

    private View v;

    Activity mListener;

    //private static final String TAG = .getSimpleName();
    private static final int USER = 10001;
    public static final int BOT = 10002;

    private String uuid = UUID.randomUUID().toString();
    private LinearLayout chatLayout;
    private EditText queryEditText;
    ImageView chatimg;
    SpeechRecognizer mspeechRecog;
    Intent mSpeechRecogInt;

    // Android client
    private AIRequest aiRequest;
    private AIDataService aiDataService;
    private AIServiceContext customAIServiceContext;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.bot_fragment, container, false);



        final ScrollView scrollview = v.findViewById(R.id.chatScrollView);
        chatimg=v.findViewById(R.id.mediimg);
        scrollview.post(new Runnable() {
            @Override
            public void run() {
                scrollview.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

        chatLayout = v.findViewById(R.id.chatLayout);

        ImageView sendBtn = v.findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Botfragment.this.sendMessage();
            }
        });

        queryEditText = v.findViewById(R.id.queryEditText);
        queryEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            Botfragment.this.sendMessage();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        // Android client
        initChatbot();
        ImageButton speakbtn = v.findViewById(R.id.speach_button);

        mspeechRecog = SpeechRecognizer.createSpeechRecognizer(getContext());
        mSpeechRecogInt = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecogInt.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecogInt.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        mspeechRecog.setRecognitionListener(new RecognitionListener() {

            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {

                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null) {
                    String recordstr = matches.get(0);
                    queryEditText.setText(recordstr);
                    sendMessage();
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });

        speakbtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                checkPermission(Manifest.permission.RECORD_AUDIO,1);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        mspeechRecog.stopListening();
                        break;
                    case MotionEvent.ACTION_DOWN:
                        mspeechRecog.startListening(mSpeechRecogInt);
                        break;
                }
                return false;
            }
        });

        return v;
    }

    public void checkPermission(String permission, int requestCode)
    {

        // Checking if permission is not granted
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(
                getContext(),
                permission)) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{permission},
                    requestCode);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener =  activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnItemClickedListener");
        }
    }



    private void initChatbot() {
        final AIConfiguration config = new AIConfiguration(BuildConfig.ClientAccessToken,
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);
        aiDataService = new ai.api.android.AIDataService(getContext(), config);
        customAIServiceContext = AIServiceContextBuilder.buildFromSessionId(uuid);// helps to create new session whenever app restarts
        aiRequest = new AIRequest();
    }

    private void sendMessage() {
        String msg = queryEditText.getText().toString();
        if (msg.trim().isEmpty()) {
            Toast.makeText(getContext(), "Please enter your query!", Toast.LENGTH_LONG).show();
        } else {
            showTextView(msg, USER);
//            chatimg.setVisibility(View.GONE);
            chatimg.setAlpha(50);
            queryEditText.setText("");
            RelativeLayout layout1=getBotLayout();
            layout1.setFocusableInTouchMode(true);
            chatLayout.addView(layout1);
            layout1.requestFocus();
            queryEditText.requestFocus();
            // Android client
            aiRequest.setQuery(msg);
            RequestTask requestTask = new RequestTask(getActivity(),layout1, (ai.api.android.AIDataService) aiDataService, customAIServiceContext);
            requestTask.execute(aiRequest);
        }
    }



    public void callback(AIResponse aiResponse,RelativeLayout layout) {
        if (aiResponse != null) {

            // process aiResponse here
            String botReply = aiResponse.getResult().getFulfillment().getSpeech();
            ImageView dot1,dot2,dot3;
            dot1=layout.findViewById(R.id.dot_1);
            dot2=layout.findViewById(R.id.dot_2);
            dot3=layout.findViewById(R.id.dot_3);
            dot1.setVisibility(View.GONE);
            dot2.setVisibility(View.GONE);
            dot3.setVisibility(View.GONE);
            TextView tv = layout.findViewById(R.id.chatMsg);
            tv.setVisibility(View.VISIBLE);
            tv.setText(botReply);
            layout.setFocusableInTouchMode(true);
            layout.requestFocus();
            queryEditText.requestFocus();
        } else {

            showTextView("There was some communication issue. Please Try again!", BOT);
        }
    }


    public void showTextView(String message, int type) {
        RelativeLayout layout;

        switch (type) {
            case USER:
                layout = getUserLayout();
                break;
            case BOT:
                layout = getBotLayout();
                break;
            default:
                layout = getBotLayout();
                break;
        }
        layout.setFocusableInTouchMode(true);
        chatLayout.addView(layout); // move focus to text view to automatically make it scroll up if softfocus
        TextView tv = layout.findViewById(R.id.chatMsg);
        tv.setText(message);
        layout.requestFocus();
        queryEditText.requestFocus(); // change focus back to edit text to continue typing
    }

    RelativeLayout getUserLayout() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        return (RelativeLayout) inflater.inflate(R.layout.user_msg_layout, null);
    }

    RelativeLayout getBotLayout() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        return (RelativeLayout) inflater.inflate(R.layout.bot_msg_layout, null);
    }

    void blink(RelativeLayout layout){
        ImageView dot1,dot2,dot3;
        dot1=layout.findViewById(R.id.dot_1);
        dot2=layout.findViewById(R.id.dot_2);
        dot3=layout.findViewById(R.id.dot_3);
        Animation fadein = AnimationUtils.loadAnimation(getContext(),R.anim.fade_in);
        Animation fadeout=AnimationUtils.loadAnimation(getContext(),R.anim.fade_out);
        dot1.startAnimation(fadeout);
        dot1.startAnimation(fadein);
        dot2.startAnimation(fadeout);
        dot2.startAnimation(fadein);
        dot3.startAnimation(fadeout);
        dot3.startAnimation(fadein);
    }

    //inner class

    public class RequestTask extends AsyncTask<AIRequest, Void, AIResponse> {

        Activity activity;
        private ai.api.android.AIDataService aiDataService;
        private AIServiceContext customAIServiceContext;
        RelativeLayout layout;

        RequestTask(Activity activity, RelativeLayout layout1,ai.api.android.AIDataService aiDataService, AIServiceContext customAIServiceContext){
            this.activity = activity;
            this.aiDataService = aiDataService;
            this.customAIServiceContext = customAIServiceContext;
            this.layout=layout1;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            blink(layout);
        }

        @Override
        protected AIResponse doInBackground(AIRequest... aiRequests) {
            final AIRequest request = aiRequests[0];
            try {
                return aiDataService.request(request, customAIServiceContext);
            } catch (AIServiceException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(AIResponse aiResponse) {
            super.onPostExecute(aiResponse);
            callback(aiResponse,layout);
        }
    }

}