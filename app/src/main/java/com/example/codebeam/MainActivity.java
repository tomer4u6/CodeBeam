package com.example.codebeam;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * @author Tomer Ben Ari
 * @version 0.2.0
 * @since 0.2.0 (09/01/2020)
 *
 * Main Activity
 */

public class MainActivity extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback {

    Spinner spinner_codes;
    ArrayAdapter<String> adapter;

    String[] codeArray = {
            "00000",
            "00001",
            "00002",
            "00003",
            "00004",
            "00005",
            "00006",
            "00007",
            "00008",
            "00009"
            };

    NfcAdapter nfcAdapter;
    AlertDialog.Builder adb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner_codes = (Spinner)findViewById(R.id.spinner_codes);

        adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,codeArray);
        spinner_codes.setAdapter(adapter);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }


    @Override
    protected void onResume() {
        super.onResume();

        if(nfcAdapter != null && nfcAdapter.isEnabled() && nfcAdapter.isNdefPushEnabled()){
            nfcAdapter.setNdefPushMessageCallback(this, this);
        }
        else{
            Toast.makeText(this, "NFC or Android Beam is not active :(", Toast.LENGTH_SHORT).show();

            adb = new AlertDialog.Builder(this);
            adb.setTitle("Open NFC settings");
            adb.setMessage("Press OPEN to open NFC settings:");
            adb.setCancelable(false);

            adb.setPositiveButton("OPEN", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
                }
            });

            adb.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    dialog.cancel();
                }
            });

            AlertDialog ad = adb.create();
            ad.show();
        }
    }

    /**
     * Sends Ndef message via NFC
     *
     * @param event
     * @return Message to send via NFC
     */

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        String message = spinner_codes.getSelectedItem().toString();
        NdefRecord ndefRecord = NdefRecord.createMime("text/plain", message.getBytes());
        NdefMessage ndefMessage = new NdefMessage(ndefRecord);
        return ndefMessage;
    }
}
