package com.example.ex192;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.ex192.FBref.refRoot;
import static com.example.ex192.MainActivity.checkAlphabetic;

public class ShowData extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    ListView dataLv;
    ArrayAdapter<String> adp;
    AlertDialog.Builder adb;
    ArrayList<String> dataList = new ArrayList<String>();
    ArrayList<StudentInfo> infoList = new ArrayList<StudentInfo>();
    ArrayList<String> keysList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);

        dataLv = (ListView) findViewById(R.id.dataLv);

        dataLv.setOnItemClickListener(this);
        dataLv.setOnItemLongClickListener(this);
        dataLv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // Get the database data and ut it in the listView
        refRoot.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dS) {
                for(DataSnapshot data : dS.getChildren()) {
                    StudentInfo infoTmp = data.getValue(StudentInfo.class);
                    infoList.add(infoTmp);
                    keysList.add(data.getKey());
                    dataList.add(infoTmp.getFirstName() + " " + infoTmp.getLastName() + " Stratum: " + infoTmp.getStratum() + " Class: " + infoTmp.getStudClass());
                }
                adp = new ArrayAdapter<String>(ShowData.this, R.layout.support_simple_spinner_dropdown_item, dataList);
                dataLv.setAdapter(adp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void vaccineEdit(int place, int vaccineNum)
    {
        if (infoList.get(place).getFirstVaccine() != null) {
            adb = new AlertDialog.Builder(this);
            adb.setCancelable(false);
            adb.setTitle("Vaccine Information");

            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);

            final EditText placeInput = new EditText(this);
            placeInput.setHint("Vaccine place");

            if (vaccineNum == 1) {
                placeInput.setText(infoList.get(place).getFirstVaccine().getPlace());
            } else {
                placeInput.setText(infoList.get(place).getSecondVaccine().getPlace());
            }

            final EditText dateInput = new EditText(this);
            dateInput.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
            dateInput.setHint("Vaccine date");

            if (vaccineNum == 1) {
                dateInput.setText(infoList.get(place).getFirstVaccine().getDate());
            } else {
                dateInput.setText(infoList.get(place).getSecondVaccine().getDate());
            }

            layout.addView(placeInput);
            layout.addView(dateInput);
            adb.setView(layout);

            adb.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }}
            );

            adb.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // check the input
                            String placeStr = placeInput.getText().toString();
                            String dateStr = dateInput.getText().toString();

                            if (placeStr.equals("") || dateStr.equals("")) {
                                Toast.makeText(ShowData.this, "There is a missing value!", Toast.LENGTH_SHORT).show();
                            } else {
                                if (!checkAlphabetic(placeStr)) {
                                    Toast.makeText(ShowData.this, "PLACE name must contains only letters!", Toast.LENGTH_SHORT).show();
                                } else {
                                    StudentInfo newInfo = infoList.get(place);

                                    // all good
                                    if (vaccineNum == 1) // if first vaccine
                                    {
                                        newInfo.setFirstVaccine(new VaccineInfo(placeStr, dateStr));
                                    } else {
                                        newInfo.setSecondVaccine(new VaccineInfo(placeStr, dateStr));
                                    }
                                    infoList.set(place, newInfo);
                                    refRoot.child(keysList.get(place)).setValue(newInfo);
                                    adp.notifyDataSetChanged();
                                }
                            }
                        }
            });

            AlertDialog ad = adb.create();
            ad.show();
        }
        else
        {
            Toast.makeText(this, "The User Cannot be vaccinated!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "Edit sec vaccine...", Toast.LENGTH_SHORT).show();

        vaccineEdit(position, 2);
    }

    /**
     * Callback method to be invoked when an item in this view has been
     * clicked and held.
     *
     * @param parent   The AbsListView where the click happened
     * @param view     The view within the AbsListView that was clicked
     * @param position The position of the view in the list
     * @param id       The row id of the item that was clicked
     * @return true if the callback consumed the long click
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "Edit first vaccine...", Toast.LENGTH_SHORT).show();

        vaccineEdit(position, 1);

        return true;
    }
}