package com.addkey.midifilemaker;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.lgy.midi_lib.midi.MidiFile;
import com.lgy.midi_lib.midi.MidiTrack;
import com.lgy.midi_lib.midi.event.meta.Tempo;
import com.lgy.midi_lib.midi.event.meta.TimeSignature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String mFilePath;
    private String mFileName = "testMidi.mid";
    private static final String TAG = "MainActivity";
    private Button mCreateMidi;
    private List<MidiTrack> mMidiTracks = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFilePath = getExternalCacheDir()+"/addKey/";
        mCreateMidi = findViewById(R.id.create_midi);
        mCreateMidi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createMidiFile();
            }
        });
    }

    private void createMidiFile() {
        mMidiTracks.clear();
        MidiTrack tempoTrack = new MidiTrack();
        MidiTrack noteTrack = new MidiTrack();

        TimeSignature ts = new TimeSignature();
        ts.setTimeSignature(4,4,TimeSignature.DEFAULT_METER,TimeSignature.DEFAULT_DIVISION);

        Tempo tempo = new Tempo();
        tempo.setBpm(60);
//        tempo.setMpqn(1000000);
        tempoTrack.insertEvent(ts);
        tempoTrack.insertEvent(tempo);

        final  int NOTE_COUNT= 80;

        for(int i = 0; i < NOTE_COUNT; i++)
        {
            int channel = 0;
            int noteId = 22 + i;
            int velocity = 30;
            long position = i * 500;
            long duration = 1000;

            noteTrack.insertNote(channel, noteId, velocity, position, duration);
        }

        mMidiTracks.add(noteTrack);
        mMidiTracks.add(tempoTrack);

        MidiFile midi = new MidiFile(MidiFile.DEFAULT_RESOLUTION, mMidiTracks);

// 4. Write the MIDI data to a file

        File output = new File(mFilePath);
        if (!output.exists()){
            output.mkdir();
        }

        File file = new File(mFilePath+mFileName);

        if (file.exists()){
            file.delete();
        }
        try {
            Log.i(TAG, "createMidiFile: file.exists:"+file.exists());
            midi.writeToFile(file);
        } catch (IOException e) {
            Log.e(TAG, "createMidiFile: "+e);
        }

    }
}
