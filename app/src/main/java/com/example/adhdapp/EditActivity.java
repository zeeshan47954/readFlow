package com.example.adhdapp;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebResourceRequest;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class EditActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{
    SearchView sv;
    String[] headersArray;
    String[] contentArray;
    TextToSpeech ttp;
    EditText et;
    WebView webView;
    Button letsgoButton;
    private OnBackPressedCallback onBackPressedCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar tb = findViewById(R.id.tb);
        setSupportActionBar(tb);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.dl);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, tb, R.string.nav_open_drawer, R.string.nav_close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationview);
        navigationView.setNavigationItemSelectedListener(this);
        onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.dl);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    finish();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);


        et = findViewById(R.id.et);
        webView = findViewById(R.id.webview);
        webView.setVisibility(View.GONE); // Initially hide the WebView
        letsgoButton = findViewById(R.id.letsgo);
        letsgoButton.setVisibility(View.VISIBLE); // Initially show the button

        // Configure WebView
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT"); // Add JavaScript interface
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (url.endsWith("/")) {
                    letsgoButton.setVisibility(View.VISIBLE);
                } else {
                    letsgoButton.setVisibility(View.GONE);
                }
                view.loadUrl("javascript:window.HTMLOUT.processHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
            }
        }); // Ensures links open within the WebView
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tbmenu, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        sv = (SearchView) searchItem.getActionView(); // Initialize the SearchView from the menu item

        if (sv != null) {
            sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    // Load the website when the query is submitted
                    String url = "https://plato.stanford.edu/search/searcher.py?query=" + query;
                    webView.setVisibility(View.VISIBLE); // Show the WebView
                    webView.loadUrl(url);
                    letsgoButton.setVisibility(View.GONE);// Hide the button when searching
                    et.setVisibility(View.GONE);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    // You can perform filtering or other operations here.
                    return true;
                }
            });

            sv.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    // Hide the WebView when the SearchView is closed
                    webView.setVisibility(View.GONE);
                    et.setVisibility(View.VISIBLE);
                    return false; // Return false to indicate that no further action is needed
                }
            });

            // Listen for search view expand and collapse events
            searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    // No action needed when expanded
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    // Hide the WebView when the SearchView is collapsed
                    webView.setVisibility(View.GONE);
                    et.setVisibility(View.VISIBLE);
                    letsgoButton.setVisibility(View.VISIBLE);
                    return true;
                }
            });
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                et.setText("");
                return true;

            case R.id.search:
                // Handle search action if needed
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void Articlereading(View view) {
        if (webView.getVisibility() == View.VISIBLE) {
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference dbr = db.getReference("Headers");

            // Create a ProgressDialog
            ProgressDialog progressDialog = new ProgressDialog(EditActivity.this);

            // Set the title and message
            progressDialog.setTitle("Loading headers...");
            progressDialog.setMessage("Please wait...");

            // Set the dialog to be non-cancellable
            progressDialog.setCancelable(false);

            // Show the dialog
            progressDialog.show();

            // Create a rotating loading circle
            progressDialog.setIndeterminate(true);



            int headersCount = 0;
            for (String header : headersArray) {
                if (!header.equals("Bibliography")) {
                    headersCount++;
                } else {
                    break;
                }
            }


uploadcontent(headersCount);




            final int totalHeadersCount = headersCount;
            final int[] completedUploads = {0}; // Using array to modify within inner classes

            for (int i = 0; i < headersCount; i++) {
                dbr.child("header" + i).setValue(headersArray[i]).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(EditActivity.this, "success", Toast.LENGTH_SHORT).show();
                        completedUploads[0]++;
                        if (completedUploads[0] == totalHeadersCount) {
                            progressDialog.dismiss();
                        Intent i=new Intent(EditActivity.this,listforencyTopicsActivity.class);
                        startActivity(i);

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the exception here
                        completedUploads[0]++;
                        if (completedUploads[0] == totalHeadersCount) {
                            progressDialog.dismiss();
                        }
                    }
                });





            }
        } else {
            String editTextText = et.getText().toString();
            if (editTextText != null && !editTextText.isEmpty()) {
                Intent i = new Intent(EditActivity.this, ReadActivity.class);
                i.putExtra(ReadActivity.message, editTextText);
                startActivity(i);
            } else {
                Toast.makeText(this, "You did not paste anything", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void uploadcontent(int length) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading content...");
        progressDialog.setMessage("Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(length);
        progressDialog.show();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbrs = db.getReference("contents");
        String[] newContentArray = Arrays.copyOf(contentArray, length);

        for (int i = 0; i < newContentArray.length; i++) {
            if (i < newContentArray.length - 1) {
                String[] brokenheader = headersArray[i+1].split("\\.");
                String b = brokenheader[1];
                String[] brokenlines = newContentArray[i].split("\\.");
                StringBuilder s = new StringBuilder();
                String[] brokenlinesfornextheader=newContentArray[i+1].split("\\.");

                String a = b+" "+brokenlinesfornextheader[0];

                for (int j = 0; j < brokenlines.length; j++) {
                    if (brokenlines[j].equals(a)) {
                        break;
                    } else {
                        s.append(brokenlines[j].trim()).append(". ");
                    }
                }

                dbrs.child("content" + i).setValue(s.toString().trim())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialog.incrementProgressBy(1);
                                if (progressDialog.getProgress() == progressDialog.getMax()) {
                                    progressDialog.dismiss();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.incrementProgressBy(1);
                                if (progressDialog.getProgress() == progressDialog.getMax()) {
                                    progressDialog.dismiss();
                                }
                            }
                        });

            } else {
                String s = contentArray[i];
                dbrs.child("content" + i).setValue(s.trim())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialog.incrementProgressBy(1);
                                if (progressDialog.getProgress() == progressDialog.getMax()) {
                                    progressDialog.dismiss();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.incrementProgressBy(1);
                                if (progressDialog.getProgress() == progressDialog.getMax()) {
                                    progressDialog.dismiss();
                                }
                            }
                        });
            }
        }
    }


    class MyJavaScriptInterface {
        @android.webkit.JavascriptInterface
        public void processHTML(String html) {
            Document doc = Jsoup.parse(html);
            Elements headers = doc.select("h1, h2");

            ArrayList<String> headersList = new ArrayList<>();
            ArrayList<String> contentList = new ArrayList<>();

            StringBuilder contentBuilder = new StringBuilder();
            Element lastHeader = null;

            for (Element header : headers) {
                if (lastHeader != null) {
                    // Store the content accumulated so far
                    headersList.add(lastHeader.text());
                    contentList.add(contentBuilder.toString().trim());
                    contentBuilder.setLength(0); // Clear StringBuilder for new content
                }

                // Start capturing content after this header
                Element nextElement = header.nextElementSibling();
                while (nextElement != null && !nextElement.tagName().equals("h1") && !nextElement.tagName().equals("h2")) {
                    contentBuilder.append(nextElement.text()).append("\n");
                    nextElement = nextElement.nextElementSibling();
                }

                lastHeader = header;
            }

            // Add the last header and its content
            if (lastHeader != null) {
                headersList.add(lastHeader.text());
                contentList.add(contentBuilder.toString().trim());
            }

            headersArray = headersList.toArray(new String[0]);
            contentArray = contentList.toArray(new String[0]);

            // Logging for verification (optional)
            Log.d("MyJavaScriptInterface", "Headers Array: " + Arrays.toString(headersArray));
            Log.d("MyJavaScriptInterface", "Content Array: " + Arrays.toString(contentArray));
        }
    }



    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        Intent intent = null;
        switch(id){
            case R.id.home:
                intent=new Intent(this,MainActivity.class);
                startActivity(intent);
                break;

            case R.id.audiobook:
                intent=new Intent(this,AudioActivity.class);
                startActivity(intent);
                break;

            case R.id.puzzles:
                intent = new Intent(this, PuzzleActivity.class);
                startActivity(intent);
                break;
            case R.id.help:
                intent=new Intent(this,HelpActivity.class);
                startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.dl);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
