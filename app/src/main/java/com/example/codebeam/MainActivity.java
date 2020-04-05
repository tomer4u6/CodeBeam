package com.example.codebeam;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;


/**
 * <h1>Main Activity</h1>
 *
 * The only activity of the application
 * where the user can choose to beam
 * a code to another phone.
 *
 * @author Tomer Ben Ari
 * @version 0.2.1
 * @since 0.2.0 (09/01/2020)
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

    /**
     * On activity create: connects widgets to their view in xml.
     *
     * @param savedInstanceState Containing the activity's previously saved state.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner_codes = (Spinner)findViewById(R.id.spinner_codes);

        adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,codeArray);
        spinner_codes.setAdapter(adapter);
    }

    /**
     * On activity resume:
     * <br>checks if NFC and Android Beam is enabled in the phone:
     * if false creates a dialog to open NFC settings,
     * if true sets a callback that dynamically generates NDEF messages to send using Android Beam.
     */

    @Override
    protected void onResume() {
        super.onResume();

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
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
     * Called to provide a NdefMessage to push.
     *
     * @param event NfcEvent with the
     *              NfcEvent#nfcAdapter(The NfcAdapter associated with the NFC event) field set.
     * @return NDEF Message to push.
     */

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        String message = spinner_codes.getSelectedItem().toString();
        NdefRecord ndefRecord = NdefRecord.createMime("text/plain", message.getBytes());
        NdefMessage ndefMessage = new NdefMessage(ndefRecord);
        return ndefMessage;
    }
}
