ReadFlow (formerly ADHDApp)

Read at your pace. One line at a time.

An Android reading assistant that breaks long text into manageable chunks, with speed control, text-to-speech, dictionary lookup, and AI-powered summaries. Built for people who get overwhelmed by dense articles — especially readers with ADHD who struggle with sustained focus on long passages.

───

Table of Contents

• What This App Does (#what-this-app-does)
• Who It's For (#who-its-for)
• Main Features (#main-features)
• How It Works (#how-it-works)
• Screens & Navigation (#screens--navigation)
• Tech Stack (#tech-stack)
• Project Structure (#project-structure)
• Firebase Data Model (#firebase-data-model)
• Getting Started (#getting-started)
• Configuration & Secrets (#configuration--secrets)
• What It Is Not (#what-it-is-not)
• Suggested App Name (#suggested-app-name)
• License (#license)

───

What This App Does

ReadFlow is not a full ADHD management app. It does not track medication, habits, or moods. It focuses on one problem:

│ Long, complex text is hard to read without losing focus.

The app helps by:

• Displaying text word-by-word or line-by-line instead of all at once
• Letting you control speed, font size, and text color
• Offering text-to-speech read-aloud
• Letting you tap any word for an instant dictionary definition
• Generating a short AI summary of the article
• Providing focus audio tracks streamed from the cloud
• Linking to trusted ADHD / mental health resources

───

Who It's For

┌───────────────────┬────────────────────────────────────────────────────────┐
│ User              │ How they might use it                                  │
├───────────────────┼────────────────────────────────────────────────────────┤
│ Readers with ADHD │ Struggle to finish long articles without zoning out    │
├───────────────────┼────────────────────────────────────────────────────────┤
│ Students          │ Dense academic or philosophy text feels overwhelming   │
├───────────────────┼────────────────────────────────────────────────────────┤
│ Anyone            │ Wants slower, calmer reading with built-in definitions │
└───────────────────┴────────────────────────────────────────────────────────┘

Note: ADHD affects people differently. Not everyone with ADHD struggles with reading — this app targets those who do.

───

Main Features

1. Read Article
Paste your own text or search the Stanford Encyclopedia of Philosophy (https://plato.stanford.edu). The app scrapes article sections and stores them in Firebase for structured reading.

2. ADHD-Friendly Reader
The core reading experience includes:

┌─────────────────────┬──────────────────────────────────────────────┐
│ Feature             │ Description                                  │
├─────────────────────┼──────────────────────────────────────────────┤
│ Progressive display │ Words/lines appear one at a time             │
├─────────────────────┼──────────────────────────────────────────────┤
│ Speed control       │ Adjust how fast text advances                │
├─────────────────────┼──────────────────────────────────────────────┤
│ Text size           │ 10sp – 50sp                                  │
├─────────────────────┼──────────────────────────────────────────────┤
│ Text color          │ Custom color picker                          │
├─────────────────────┼──────────────────────────────────────────────┤
│ Pause / Play        │ Stop and resume anytime                      │
├─────────────────────┼──────────────────────────────────────────────┤
│ Previous / Next     │ Navigate between lines                       │
├─────────────────────┼──────────────────────────────────────────────┤
│ Text-to-speech      │ Listen to content read aloud                 │
├─────────────────────┼──────────────────────────────────────────────┤
│ Tap-to-define       │ Dictionary lookup via Free Dictionary API    │
├─────────────────────┼──────────────────────────────────────────────┤
│ AI summary          │ OpenNLP extractive summary (top 4 sentences) │
└─────────────────────┴──────────────────────────────────────────────┘

3. Reading Challenges
Five difficulty levels with progressively denser academic text:

┌─────────┬─────────────────────────────────────────────────────────┐
│ Level   │ Content style                                           │
├─────────┼─────────────────────────────────────────────────────────┤
│ Level 1 │ Long but readable paragraphs                            │
├─────────┼─────────────────────────────────────────────────────────┤
│ Level 2 │ More complex academic language                          │
├─────────┼─────────────────────────────────────────────────────────┤
│ Level 3 │ Increasingly dense prose                                │
├─────────┼─────────────────────────────────────────────────────────┤
│ Level 4 │ Heavy academic vocabulary                               │
├─────────┼─────────────────────────────────────────────────────────┤
│ Level 5 │ Advanced philosophy (Lacan, Deleuze, Foucault, Derrida) │
└─────────┴─────────────────────────────────────────────────────────┘

Each level uses the same reading tools: speed, size, color, dictionary, and TTS.

4. Focus Audio
Three ADHD-related audio tracks streamed from Firebase Storage:

• ADHD and how to improve focus
• Second focus/ADHD track
• ADHD in Adults

Includes play/pause, seek bar, and saved playback position between sessions.

5. Talk to an Expert
In-app links to trusted ADHD and mental health resources:

• Talkspace
• BetterHelp
• SAMHSA
• Verywell Mind
• Psych Central
• HelpGuide

───

How It Works

Flow A — Paste and read

Home → Read Article → Paste text → Read
  → Current tab (progressive reader)
  → AI Summary tab (OpenNLP summary)

Flow B — Search Stanford Encyclopedia

Home → Read Article → Search topic
  → WebView loads plato.stanford.edu results
  → App scrapes article (Jsoup)
  → Sections uploaded to Firebase RTDB
  → Topic list → Pick section → Read

Flow C — Reading challenges

Home → Challenges → Pick Level 1–5
  → Same progressive reader with level-specific text

Flow D — Focus audio

Home → Increase Focus → Pick track
  → Stream from Firebase Storage (adhd1.mp3, adhd2.mp3, adhd3.mp3)

───

Screens & Navigation

┌───────────────────────────┬──────────────────────────────────────────────────┐
│ Screen                    │ Purpose                                          │
├───────────────────────────┼──────────────────────────────────────────────────┤
│ MainActivity              │ Home hub with 4 feature cards                    │
├───────────────────────────┼──────────────────────────────────────────────────┤
│ EditActivity              │ Paste text or search Stanford Encyclopedia       │
├───────────────────────────┼──────────────────────────────────────────────────┤
│ ReadActivity              │ Reader with "Current" + "AI Summary" tabs        │
├───────────────────────────┼──────────────────────────────────────────────────┤
│ ReadActivityforonlineone  │ Reader for Firebase-stored encyclopedia sections │
├───────────────────────────┼──────────────────────────────────────────────────┤
│ listforencyTopicsActivity │ Browse scraped article sections                  │
├───────────────────────────┼──────────────────────────────────────────────────┤
│ PuzzleActivity            │ Challenge level picker                           │
├───────────────────────────┼──────────────────────────────────────────────────┤
│ LevelActivity             │ Loads level1–level5 reading fragments            │
├───────────────────────────┼──────────────────────────────────────────────────┤
│ AudioActivity             │ Focus audio track selector                       │
├───────────────────────────┼──────────────────────────────────────────────────┤
│ listenToaudios            │ Audio player with seek + resume                  │
├───────────────────────────┼──────────────────────────────────────────────────┤
│ HelpActivity              │ Expert resource card grid                        │
├───────────────────────────┼──────────────────────────────────────────────────┤
│ getrealhelp               │ WebView for mental health sites                  │
└───────────────────────────┴──────────────────────────────────────────────────┘

Home screen cards

┌────────────────┬────────────────┐
│ Card           │ Opens          │
├────────────────┼────────────────┤
│ Read Article   │ EditActivity   │
├────────────────┼────────────────┤
│ Challenges     │ PuzzleActivity │
├────────────────┼────────────────┤
│ Increase Focus │ AudioActivity  │
├────────────────┼────────────────┤
│ Talk to Expert │ HelpActivity   │
└────────────────┴────────────────┘

───

Tech Stack

┌───────────────┬───────────────────────────────────────────────────────────┐
│ Layer         │ Technology                                                │
├───────────────┼───────────────────────────────────────────────────────────┤
│ Language      │ Java                                                      │
├───────────────┼───────────────────────────────────────────────────────────┤
│ Min SDK       │ 24 (Android 7.0)                                          │
├───────────────┼───────────────────────────────────────────────────────────┤
│ Target SDK    │ 33                                                        │
├───────────────┼───────────────────────────────────────────────────────────┤
│ Compile SDK   │ 34                                                        │
├───────────────┼───────────────────────────────────────────────────────────┤
│ Build         │ Gradle, View Binding                                      │
├───────────────┼───────────────────────────────────────────────────────────┤
│ UI            │ Material Components, Bottom Sheets, FAB menus, ViewPager2 │
├───────────────┼───────────────────────────────────────────────────────────┤
│ Backend       │ Firebase Realtime Database                                │
├───────────────┼───────────────────────────────────────────────────────────┤
│ Storage       │ Firebase Storage (audio files)                            │
├───────────────┼───────────────────────────────────────────────────────────┤
│ Web scraping  │ Jsoup                                                     │
├───────────────┼───────────────────────────────────────────────────────────┤
│ Summarization │ OpenNLP (en-token.bin in assets)                          │
├───────────────┼───────────────────────────────────────────────────────────┤
│ Dictionary    │ Free Dictionary API (no key required)                     │
├───────────────┼───────────────────────────────────────────────────────────┤
│ TTS           │ Android TextToSpeech                                      │
├───────────────┼───────────────────────────────────────────────────────────┤
│ Networking    │ Volley                                                    │
├───────────────┼───────────────────────────────────────────────────────────┤
│ Media         │ MediaPlayer + Firebase Storage download URLs              │
└───────────────┴───────────────────────────────────────────────────────────┘

───

Project Structure

ADHDApp/
├── app/
│   ├── build.gradle
│   ├── google-services.json          # Firebase config (contains API key)
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── assets/
│       │   └── en-token.bin          # OpenNLP tokenizer model
│       ├── java/com/example/adhdapp/
│       │   ├── MainActivity.java     # Home screen
│       │   ├── EditActivity.java     # Paste / search articles
│       │   ├── ReadActivity.java     # Reader + summary tabs
│       │   ├── CurrentReadingfragment.java
│       │   ├── AiSummaryFragment.java
│       │   ├── AiSummaryGenerator.java
│       │   ├── PuzzleActivity.java   # Challenge picker
│       │   ├── level1.java – level5.java
│       │   ├── AudioActivity.java
│       │   ├── listenToaudios.java
│       │   ├── HelpActivity.java
│       │   └── getrealhelp.java
│       └── res/
├── build.gradle
├── settings.gradle
├── gradle.properties
└── local.properties                  # Gitignored — local SDK path

Package ID: com.example.adhdapp
Firebase project: adhdapp-77c1f

───

Firebase Data Model

Headers/
  ├── header0: "1. Introduction"
  ├── header1: "2. Main Argument"
  └── ...

contents/
  ├── content0: "Section text..."
  ├── content1: "Section text..."
  └── ...

Firebase Storage:

adhd1.mp3
adhd2.mp3
adhd3.mp3

───

Getting Started

Prerequisites
• Android Studio (latest stable)
• JDK 8+
• Firebase project with Realtime Database and Storage enabled
• google-services.json from Firebase Console

1. Clone and open

git clone <your-repo-url>
cd ADHDApp
Open in Android Studio and let Gradle sync.

2. Firebase setup
1. Create a Firebase project (or use existing adhdapp-77c1f)
2. Add an Android app with package com.example.adhdapp
3. Download google-services.json → place in app/
4. Enable Realtime Database and Firebase Storage
5. Upload audio files (adhd1.mp3, adhd2.mp3, adhd3.mp3) to Storage root

3. Build and run

./gradlew assembleDebug
Or press Run in Android Studio (API 24+ device/emulator).

───

Configuration & Secrets

┌──────────────────────────┬──────────────────────────────┬────────────────┐
│ File                     │ Contains                     │ Gitignored?    │
├──────────────────────────┼──────────────────────────────┼────────────────┤
│ app/google-services.json │ Firebase API key, project ID │ No — should be │
├──────────────────────────┼──────────────────────────────┼────────────────┤
│ local.properties         │ Local Android SDK path       │ Yes            │
└──────────────────────────┴──────────────────────────────┴────────────────┘

APIs used (no keys required)
• https://api.dictionaryapi.dev — free dictionary
• https://plato.stanford.edu — encyclopedia search
• External help sites (Talkspace, BetterHelp, etc.)

Security recommendation
Before pushing to a public repo:

1. Add google-services.json to .gitignore
2. Add google-services.json.example with placeholders
3. Rotate the Firebase API key if it was ever committed

───

What It Is Not

This app does not:

• Diagnose or treat ADHD
• Track medication or symptoms
• Replace therapy or professional care
• Use cloud LLM AI (summaries are local OpenNLP, not ChatGPT)
• Manage tasks, reminders, or daily routines
• Help with every ADHD challenge (organization, time blindness, emotional regulation, etc.)

It is a reading and focus tool — useful for some ADHD readers, not a complete ADHD app.

───

Suggested App Name

The current name ADHDApp is generic. Better options based on what the app actually does:

┌────────────┬────────────────────────────────────────┐
│ Name       │ Tagline                                │
├────────────┼────────────────────────────────────────┤
│ ReadFlow   │ Read at your pace. One line at a time. │
├────────────┼────────────────────────────────────────┤
│ SteadyRead │ For readers who get overwhelmed        │
├────────────┼────────────────────────────────────────┤
│ ClearPage  │ ADHD-friendly reading                  │
└────────────┴────────────────────────────────────────┘

Recommended: ReadFlow — describes the feature, not just the audience.

───

License

No license file is included. Add one before open-source distribution.

───

Contributing

1. Edit sources under app/src/main/java/com/example/adhdapp/
2. Do not commit google-services.json to public repos
3. Test all four home flows: Read, Challenges, Audio, Help
4. Keep the reading experience (speed, TTS, dictionary) working across all levels
