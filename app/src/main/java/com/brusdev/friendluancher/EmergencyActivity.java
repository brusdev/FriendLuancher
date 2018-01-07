package com.brusdev.friendluancher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class EmergencyActivity extends AppCompatActivity {

    private int activityMode;

    private Button contact1Button;
    private Button contact2Button;
    private Button contact3Button;
    private Button contact4Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.emergency_background)));
        getSupportActionBar().setLogo(R.drawable.ic_warning_white_24dp);
        getSupportActionBar().setTitle(getResources().getString(R.string.emergency).toUpperCase());

        activityMode = ACTIVITY_MODE_RUN;

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            activityMode = bundle.getInt(ACTIVITY_MODE_KEY, ACTIVITY_MODE_RUN);
        }

        contact1Button = findViewById(R.id.contact1Button);
        contact2Button = findViewById(R.id.contact2Button);
        contact3Button = findViewById(R.id.contact3Button);
        contact4Button = findViewById(R.id.contact4Button);

        contact1Button.setTag(R.string.index, CONTACT_INDEX_1);
        contact2Button.setTag(R.string.index, CONTACT_INDEX_2);
        contact3Button.setTag(R.string.index, CONTACT_INDEX_3);
        contact4Button.setTag(R.string.index, CONTACT_INDEX_4);

        String nameContact1;
        String nameContact2;
        String nameContact3;
        String nameContact4;
        String numberContact1;
        String numberContact2;
        String numberContact3;
        String numberContact4;
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        nameContact1 = sharedPref.getString(NAME_CONTACT_1, null);
        nameContact2 = sharedPref.getString(NAME_CONTACT_2, null);
        nameContact3 = sharedPref.getString(NAME_CONTACT_3, null);
        nameContact4 = sharedPref.getString(NAME_CONTACT_4, null);
        numberContact1 = sharedPref.getString(NUMBER_CONTACT_1, null);
        numberContact2 = sharedPref.getString(NUMBER_CONTACT_2, null);
        numberContact3 = sharedPref.getString(NUMBER_CONTACT_3, null);
        numberContact4 = sharedPref.getString(NUMBER_CONTACT_4, null);

        if (nameContact1 != null && numberContact1 != null) {
            setContactButton(contact1Button, nameContact1, numberContact1);
        }
        if (nameContact2 != null && numberContact1 != null) {
            setContactButton(contact2Button, nameContact2, numberContact2);
        }
        if (nameContact3 != null && numberContact1 != null) {
            setContactButton(contact3Button, nameContact3, numberContact3);
        }
        if (nameContact4 != null && numberContact1 != null) {
            setContactButton(contact4Button, nameContact4, numberContact4);
        }

        View.OnClickListener contactButtonOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activityMode == ACTIVITY_MODE_RUN) {
                    Object number = view.getTag(R.string.number);

                    if (number != null) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + number));
                        EmergencyActivity.this.startActivity(intent);
                    }
                } else {
                    Object index = view.getTag(R.string.index);
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                    EmergencyActivity.this.startActivityForResult(intent, (int)index);
                }
            }
        };


        contact1Button.setOnClickListener(contactButtonOnClickListener);
        contact2Button.setOnClickListener(contactButtonOnClickListener);
        contact3Button.setOnClickListener(contactButtonOnClickListener);
        contact4Button.setOnClickListener(contactButtonOnClickListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check whether the result is ok
        if (resultCode == RESULT_OK) {
            String name = null;
            String number = null ;
            try {
                Uri uri = data.getData();
                Cursor cursor = null;

                cursor = getContentResolver().query(uri, null, null, null, null);
                cursor.moveToFirst();

                int  nameIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                int  numberIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                name = cursor.getString(nameIndex);
                number = cursor.getString(numberIndex);
            } catch (Exception e) {
                e.printStackTrace();
            }
            SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            switch (requestCode) {
                case CONTACT_INDEX_1:
                    setContactButton(contact1Button, name, number);
                    editor.putString(NAME_CONTACT_1, name);
                    editor.putString(NUMBER_CONTACT_1, number);
                    break;
                case CONTACT_INDEX_2:
                    setContactButton(contact2Button, name, number);
                    editor.putString(NAME_CONTACT_2, name);
                    editor.putString(NUMBER_CONTACT_2, number);
                    break;
                case CONTACT_INDEX_3:
                    setContactButton(contact3Button, name, number);
                    editor.putString(NAME_CONTACT_3, name);
                    editor.putString(NUMBER_CONTACT_3, number);
                    break;
                case CONTACT_INDEX_4:
                    setContactButton(contact4Button, name, number);
                    editor.putString(NAME_CONTACT_4, name);
                    editor.putString(NUMBER_CONTACT_4, number);
                    break;
            }

            editor.commit();
        } else {
            Log.e("MainActivity", "Failed to pick contact");
        }
    }

    private void setContactButton(Button button, String name, String number) {
        button.setText(name + "\n" + number);
        button.setTag(R.string.name, name);
        button.setTag(R.string.number, number);
    }

    private static final int CONTACT_INDEX_1 = 1;
    private static final int CONTACT_INDEX_2 = 2;
    private static final int CONTACT_INDEX_3 = 3;
    private static final int CONTACT_INDEX_4 = 4;

    public static final String NAME_CONTACT_1 = "NAME_CONTACT_1";
    public static final String NAME_CONTACT_2 = "NAME_CONTACT_2";
    public static final String NAME_CONTACT_3 = "NAME_CONTACT_3";
    public static final String NAME_CONTACT_4 = "NAME_CONTACT_4";

    public static final String NUMBER_CONTACT_1 = "NUMBER_CONTACT_1";
    public static final String NUMBER_CONTACT_2 = "NUMBER_CONTACT_2";
    public static final String NUMBER_CONTACT_3 = "NUMBER_CONTACT_3";
    public static final String NUMBER_CONTACT_4 = "NUMBER_CONTACT_4";

    public static final String ACTIVITY_MODE_KEY = "ACTIVITY_MODE";
    public static final int ACTIVITY_MODE_RUN = 1;
    public static final int ACTIVITY_MODE_SET = 2;
}
