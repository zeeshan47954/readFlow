package com.example.adhdapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AiSummaryGenerator {
    private Tokenizer tokenizer;

    public AiSummaryGenerator(Context context) throws IOException {
        try {
            InputStream modelIn = context.getAssets().open("en-token.bin");
            TokenizerModel model = new TokenizerModel(modelIn);
            tokenizer = new TokenizerME(model);
        } catch (IOException e) {
            Log.e("AiSummaryGenerator", "Error loading OpenNLP model", e);
            // Handle the error, e.g., by displaying an error message to the user
        }
    }

    public String generateSummary(String text) {
        String[] sentences = text.split("\\.");
        List<String> sentenceList = new ArrayList<>();
        for (String sentence : sentences) {
            sentenceList.add(sentence.trim());
        }

        // Calculate sentence scores using OpenNLP
        double[] sentenceScores = new double[sentenceList.size()];
        for (int i = 0; i < sentenceList.size(); i++) {
            String sentence = sentenceList.get(i);
            String[] tokens = tokenizer.tokenize(sentence);
            double score = 0;
            for (String token : tokens) {
                score += token.length();
            }
            sentenceScores[i] = score;
        }

        // Select top-scoring sentences for summary
        int numSentences = 4; // adjust this value to change the summary length
        String[] summarySentences = new String[numSentences];
        for (int i = 0; i < numSentences; i++) {
            int maxIndex = -1;
            double maxScore = 0;
            for (int j = 0; j < sentenceScores.length; j++) {
                if (sentenceScores[j] > maxScore) {
                    maxScore = sentenceScores[j];
                    maxIndex = j;
                }
            }
            if (maxIndex != -1) {
                summarySentences[i] = sentenceList.get(maxIndex);
                sentenceScores[maxIndex] = 0; // reset score to avoid duplicates
            } else {
                // Handle the case where no sentence score is greater than 0
                Log.e("AiSummaryGenerator", "No sentence score is greater than 0");
                break; // or return an empty summary, depending on your requirements
            }
        }

        // Join summary sentences
        String summary = "";
        for (String sentence : summarySentences) {
            if (sentence != null) {
                summary += sentence + ". ";
            }
        }

        FirebaseDatabase fbd = FirebaseDatabase.getInstance();
        DatabaseReference dbr = fbd.getReference("figga");
        dbr.child("zigga").setValue(summary).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        });

        return summary.trim();
    }
}