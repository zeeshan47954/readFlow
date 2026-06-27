package com.example.adhdapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.slider.Slider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AiSummaryFragment extends Fragment {
    private boolean isPlaying = true;
    private Boolean speaking;
    private int TextColor= 0xFF000000;
    ;
    private Boolean wasplaying;
    private int currentLine = 0;
    private int TextSize=18;
    private  int setspeed=1000;
    private boolean isReadingAloud = false;
    private TextToSpeech ttp;
    private String entireText;
    private String previousentireText;
    private String[] lines;
    private int counter = 0;
    private TextView tv;
    private int pausedWordIndex = 0;
    private Handler handler;
    private Dialog dialog;
    private SeekBar slider;
    private Button btnOk;
    private StringBuilder textToBeSent = new StringBuilder();
    private SpannableStringBuilder spannableText = new SpannableStringBuilder();
    private Runnable wordRunnable;
    private Runnable lineRunnable;
    ProgressDialog progressDialog;

    private   Boolean previousornextclicked=false;
    private AiSummaryGenerator aiSummaryGenerator;
    View view;
    public AiSummaryFragment(String entireText) {
        this.previousentireText = entireText;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_reading, container, false);

        try {
            aiSummaryGenerator = new AiSummaryGenerator(getContext());
            entireText = aiSummaryGenerator.generateSummary(previousentireText);

        } catch (IOException e) {
            Log.e("AiSummaryFragment", "Error creating AiSummaryGenerator", e);
            // Handle the exception, e.g., by displaying an error message to the user
        }
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Generating summary...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        ImageButton pause=view.findViewById(R.id.pause);
        ImageButton previous=view.findViewById(R.id.previous);
        ImageButton next=view.findViewById(R.id.forward);
        FloatingActionMenu fam=view.findViewById(R.id.fab_menu);
        FloatingActionButton speed=view.findViewById(R.id.speed);
        FloatingActionButton size=view.findViewById(R.id.size);
        FloatingActionButton color=view.findViewById(R.id.color);
        progressDialog.show();

        progressDialog.dismiss();
        tv = view.findViewById(R.id.textView);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter > 0) {
                    handler.removeCallbacks(wordRunnable);
                    handler.removeCallbacks(lineRunnable);
                    counter--;
                    pausedWordIndex = 0;
                    textToBeSent = new StringBuilder(lines[counter]);
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, TextSize);
                    tv.setTextColor(TextColor);
                    tv.setText(textToBeSent.toString());
                }
            }
        });

        // Modify the next button's OnClickListener
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter < lines.length - 1) {
                    handler.removeCallbacks(wordRunnable);
                    handler.removeCallbacks(lineRunnable);
                    counter++;
                    pausedWordIndex = 0;
                    textToBeSent = new StringBuilder(lines[counter]);
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, TextSize);
                    tv.setTextColor(TextColor);
                    tv.setText(textToBeSent.toString());
                }
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    pause.setImageResource(R.drawable.play);
                    isPlaying = false;
                    handler.removeCallbacks(wordRunnable);
                    handler.removeCallbacks(lineRunnable);
                    previous.setVisibility(View.VISIBLE);
                    next.setVisibility(View.VISIBLE);


                    addClickableSpans();
                } else {
                    pause.setImageResource(R.drawable.pause);
                    isPlaying = true;
                    startDisplayingText();
                    previous.setVisibility(View.GONE);
                    next.setVisibility(View.GONE);


                    removeClickableSpans();
                }
            }
        });



        // Initialize TextToSpeech
        ttp = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    ttp.setLanguage(Locale.getDefault());
                } else {
                    Toast.makeText(getContext(), "TextToSpeech initialization failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (savedInstanceState != null) {
            counter = savedInstanceState.getInt("lastLineIndex");
        }

        lines = entireText.split("\\.");

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startDisplayingText();
            }
        }, 1000); // Delay initial start for 1 second

        speed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wasplaying=isPlaying;
                isPlaying=false;
                handler.removeCallbacks(wordRunnable);
                handler.removeCallbacks(lineRunnable);

                showspeedDialog();
            }
        });

        size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wasplaying=isPlaying;
                isPlaying=false;
                handler.removeCallbacks(wordRunnable);
                handler.removeCallbacks(lineRunnable);
                showsizedialog();
            }
        });
        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wasplaying=isPlaying;
                isPlaying=false;
                handler.removeCallbacks(wordRunnable);
                handler.removeCallbacks(lineRunnable);
                showcolordialog();
            }
        });





        return view;
    }

    private void addClickableSpans() {
        String[] words = textToBeSent.toString().split(" ");
        spannableText.clear();
        spannableText.append(textToBeSent);
        int startIndex = 0;

        for (final String word : words) {
            // Find the next occurrence of the word starting from startIndex
            startIndex = spannableText.toString().indexOf(word, startIndex);
            if (startIndex == -1) break;  // If the word is not found, exit the loop

            int endIndex = startIndex + word.length();
            spannableText.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    showBottomSheet(word);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(true);
                }
            }, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            startIndex = endIndex;  // Move the startIndex to the end of the current word
        }

        tv.setMovementMethod(LinkMovementMethod.getInstance());
        tv.setText(spannableText);
    }

    // Method to remove clickable spans
    private void removeClickableSpans() {
        spannableText.clearSpans();
        tv.setText(textToBeSent);
    }
    private void showBottomSheet(String word) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_layout);
        TextView wordTextView = bottomSheetDialog.findViewById(R.id.word);
        TextView meaningTextView = bottomSheetDialog.findViewById(R.id.meaning);
        wordTextView.setText(word);

        getMeaning(word, meaningTextView);
        bottomSheetDialog.show();
    }
    public void getMeaning(String text, final TextView meaningTextView) {
        // Split the text into words
        String[] words = text.split("\\s+");

        // Initialize a StringBuilder to hold the results
        final StringBuilder results = new StringBuilder();

        // Create a request queue
        RequestQueue queue = Volley.newRequestQueue(getContext());

        // Loop over each word
        for (final String word : words) {
            String url = "https://api.dictionaryapi.dev/api/v2/entries/en/" + word;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                if (jsonArray.length() > 0) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    JSONArray meaningsArray = jsonObject.getJSONArray("meanings");
                                    if (meaningsArray.length() > 0) {
                                        JSONObject meaningObject = meaningsArray.getJSONObject(0);
                                        JSONArray definitionsArray = meaningObject.getJSONArray("definitions");
                                        if (definitionsArray.length() > 0) {
                                            String definition = definitionsArray.getJSONObject(0).getString("definition");
                                            results.append(word).append(": ").append(definition).append("\n\n");
                                        } else {
                                            results.append(word).append(": No definition found\n\n");
                                        }
                                    } else {
                                        results.append(word).append(": No definition found\n\n");
                                    }
                                } else {
                                    results.append(word).append(": No definition found\n\n");
                                }
                            } catch (JSONException e) {
                                Log.e("DictionaryAPI", "Error parsing JSON response", e);
                                results.append(word).append(": Error fetching definition\n\n");
                            }

                            // Update the TextView with the results
                            meaningTextView.setText(results.toString());
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("DictionaryAPI", "Error fetching definition", error);
                    results.append(word).append(": Error fetching definition\n\n");

                    // Update the TextView with the results
                    meaningTextView.setText(results.toString());
                }
            });

            // Add the request to the queue
            queue.add(stringRequest);
        }
    }


    public void showcolordialog()
    {


        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.color_picker_dialog);

        TextView dialogTitle = (TextView) dialog.findViewById(R.id.dialog_title);
        final View colorPreview = dialog.findViewById(R.id.color_preview);
        final TextView redLabel = (TextView) dialog.findViewById(R.id.red_label);
        final TextView greenLabel = (TextView) dialog.findViewById(R.id.green_label);
        final TextView blueLabel = (TextView) dialog.findViewById(R.id.blue_label);
        final SeekBar redSeekBar = (SeekBar) dialog.findViewById(R.id.red_seekbar);
        final SeekBar greenSeekBar = (SeekBar) dialog.findViewById(R.id.green_seekbar);
        final SeekBar blueSeekBar = (SeekBar) dialog.findViewById(R.id.blue_seekbar);
        Button btnOk = (Button) dialog.findViewById(R.id.btn_ok);

        // Set initial values
        redSeekBar.setProgress(255);
        greenSeekBar.setProgress(0);
        blueSeekBar.setProgress(0);

        // Update color preview and labels
        SeekBar.OnSeekBarChangeListener colorChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int red = redSeekBar.getProgress();
                int green = greenSeekBar.getProgress();
                int blue = blueSeekBar.getProgress();
                int color = Color.rgb(red, green, blue);
                colorPreview.setBackgroundColor(color);
                redLabel.setText("Red: " + red);
                greenLabel.setText("Green: " + green);
                blueLabel.setText("Blue: " + blue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        };

        redSeekBar.setOnSeekBarChangeListener(colorChangeListener);
        greenSeekBar.setOnSeekBarChangeListener(colorChangeListener);
        blueSeekBar.setOnSeekBarChangeListener(colorChangeListener);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int red = redSeekBar.getProgress();
                int green = greenSeekBar.getProgress();
                int blue = blueSeekBar.getProgress();
                int selectedColor = Color.rgb(red, green, blue);
                // Apply the selected color
                TextColor=selectedColor;
                dialog.dismiss();
            }
        });

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // Handle cancel action here
                isPlaying = wasplaying;
            }
        });

        dialog.show();


    }
    public void showspeedDialog() {
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.activity_speed_dialog);
        TextView speedValue=(TextView)dialog.findViewById(R.id.speed_value);
        TextView dialogTitle = (TextView) dialog.findViewById(R.id.dialog_title);
        slider = (SeekBar) dialog.findViewById(R.id.slider);
        btnOk = (Button) dialog.findViewById(R.id.btn_ok);
        slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int seconds = progress / 1000; // Convert progress to seconds
                speedValue.setText(seconds + " seconds");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = slider.getProgress()+1; // +1 because min is 1
                // Do something with the chosen value
                setspeed=value;
                isPlaying=true;
                startDisplayingText();
                dialog.dismiss();
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // Handle cancel action here
                isPlaying = wasplaying;
            }
        });
        dialog.show();
    }


    public void showsizedialog()
    {
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.text_size_dialog);

        TextView dialogTitle = (TextView) dialog.findViewById(R.id.dialog_title);
        TextView textSizeLabel = (TextView) dialog.findViewById(R.id.text_size_label);
        final TextView textSizeValue = (TextView) dialog.findViewById(R.id.text_size_value);
        SeekBar slider = (SeekBar) dialog.findViewById(R.id.slider);
        Button btnOk = (Button) dialog.findViewById(R.id.btn_ok);

        slider.setMax(40); // Max value is 50sp, so max progress is 40 (50 - 10)
        slider.setProgress(0); // Initial value is 10sp, so initial progress is 0

        slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int textSize = progress + 10; // Convert progress to text size (10sp to 50sp)
                textSizeValue.setText(textSize + "sp");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int textSize = Integer.parseInt(textSizeValue.getText().toString().replace("sp", ""));
                // Do something with the chosen text size
                TextSize=textSize;
                dialog.dismiss();
            }
        });

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                isPlaying = wasplaying;
                // Handle cancel action here
            }
        });

        dialog.show();

    }







    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("lastLineIndex", counter);
    }



    private void startDisplayingText() {
        if (counter < lines.length) {
            String line = lines[counter];
            String[] words = line.split(" ");
            if (pausedWordIndex == 0) {
                textToBeSent = new StringBuilder();
                tv.setText("");
            }


            wordRunnable = new Runnable() {
                int wordIndex = pausedWordIndex;

                @Override
                public void run() {
                    if (wordIndex < words.length) {
                        textToBeSent.append(words[wordIndex]).append(" ");
                        spannableText.append(words[wordIndex]).append(" ");
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, TextSize);
                        tv.setTextColor(TextColor);
                        tv.setText(textToBeSent.toString());
                        wordIndex++;
                        pausedWordIndex = wordIndex;
                        if (isPlaying) {
                            handler.postDelayed(this, 100); // Delay between each word
                        }
                    } else {
                        if (isPlaying) {
                            lineRunnable = new Runnable() {
                                @Override
                                public void run() {
                                    if (isPlaying) {
                                        if (!isReadingAloud || !ttp.isSpeaking()) {
                                            counter++;
                                            pausedWordIndex = 0;
                                            startDisplayingText();
                                        } else if (isReadingAloud || ttp.isSpeaking()) {
                                            handler.postDelayed(this, 3000);
                                        }
                                    } else {
                                        handler.postDelayed(lineRunnable, setspeed);
                                    }
                                }
                            };
                            handler.postDelayed(lineRunnable, setspeed); // Delay between each line
                        }
                    }
                }
            };

            handler.post(wordRunnable);
        }
    }
    public void startThread() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(new Runnable() {
            int lastCounter = -1;

            @Override
            public void run() {
                while (isReadingAloud && counter < lines.length) {
                    if (counter != lastCounter) {
                        lastCounter = counter;
                        speaking = true;
                        ttp.stop(); // Stop any ongoing speech
                        ttp.speak(lines[lastCounter], TextToSpeech.QUEUE_FLUSH, null, null);
                    }

                    // Check if TTS is still speaking
                    if (!ttp.isSpeaking()) {
                        speaking = false;
                        // Increment only if TTS has finished speaking

                    }
                }
            }
        });
    }








    public void stopThread() {
        // Implement thread stopping mechanism if needed
    }

    @Override
    public void onResume() {
        super.onResume();
        startThread(); // Start reading thread when fragment resumes
    }

    @Override
    public void onPause() {
        super.onPause();
        stopThread(); // Stop reading thread when fragment pauses
    }

    public void toggleSpeech() {
        if (isReadingAloud) {
            ttp.stop();
            isReadingAloud = false;
            speaking=false;
            stopThread(); // Stop reading thread if speech is toggled off
        } else {
            isReadingAloud = true;
            startThread(); // Start reading thread if speech is toggled on
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ttp != null) {
            ttp.stop();
            ttp.shutdown(); // Shutdown TextToSpeech engine on fragment destroy
        }
    }



}