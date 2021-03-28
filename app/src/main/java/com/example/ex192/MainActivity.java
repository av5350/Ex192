package com.example.ex192;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import static com.example.ex192.FBref.refRoot;

/**
 * The type main activity.
 * @author Itey Weintraub <av5350@bs.amalnet.k12.il>
 * @version	1
 * @since 28.3.2021
 * short description:
 *
 *      This activity gets the info about student and his vaccines
 */
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner vaccineTypes;
    EditText firstName;
    EditText lastName;
    EditText studClass;
    EditText stratum;
    LinearLayout vaccineLayout;
    LinearLayout noVaccineLayout;

    int vaccineNum;
    AlertDialog.Builder adb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vaccineTypes = (Spinner) findViewById(R.id.vaccineTypes);
        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        studClass = (EditText) findViewById(R.id.studClass);
        stratum = (EditText) findViewById(R.id.stratum);
        vaccineLayout = (LinearLayout) findViewById(R.id.vaccineLayout);
        noVaccineLayout = (LinearLayout) findViewById(R.id.noVaccineLayout);

        vaccineNum = -1; // default - cant vaccinated

        vaccineTypes.setOnItemSelectedListener(this);

        ArrayAdapter<String> vaccineTypesAdp = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.VaccineOptions));
        vaccineTypes.setAdapter(vaccineTypesAdp);
    }

    /**
     * Gets data for the student and pop up for fill his vaccine information.
     *
     * @param view the view
     */
    public void getData(View view) {
        String firstNameStr = firstName.getText().toString();
        String lastNameStr = lastName.getText().toString();
        String classStr = studClass.getText().toString();
        String stratumStr = stratum.getText().toString();

        if (firstNameStr.equals("") || lastNameStr.equals("") || classStr.equals("") || stratumStr.equals(""))
        {
            Toast.makeText(this, "There is a missing value!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (!checkAlphabetic(firstNameStr) || !checkAlphabetic(lastNameStr))
            {
                Toast.makeText(this, "first or last name must contains only letters!", Toast.LENGTH_SHORT).show();
            }
            else // if everything is good
            {
                adb = new AlertDialog.Builder(this);
                adb.setCancelable(false);
                adb.setTitle("Vaccine Information");

                LinearLayout layout = new LinearLayout(this);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText placeInput = new EditText(this);
                placeInput.setHint("Vaccine place");

                final EditText dateInput = new EditText(this);
                dateInput.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
                dateInput.setHint("Vaccine date");

                layout.addView(placeInput);
                layout.addView(dateInput);

                adb.setView(layout);

                adb.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // check the input
                        String placeStr = placeInput.getText().toString();
                        String dateStr = dateInput.getText().toString();

                        if (placeStr.equals("") || dateStr.equals(""))
                        {
                            Toast.makeText(MainActivity.this, "There is a missing value!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if (!checkAlphabetic(placeStr)) {
                                Toast.makeText(MainActivity.this, "PLACE name must contains only letters!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                // all good
                                if (vaccineNum == 1) // if first vaccine
                                {
                                    StudentInfo student = new StudentInfo(firstNameStr, lastNameStr, Integer.parseInt(stratumStr), Integer.parseInt(classStr), new VaccineInfo(placeStr, dateStr), null);
                                    String childName = String.format("%02d", Integer.parseInt(stratumStr)) + String.format("%02d", Integer.parseInt(classStr)) + "1" + "_" + firstNameStr + "_" + lastNameStr;
                                    Toast.makeText(MainActivity.this, childName, Toast.LENGTH_SHORT).show();
                                    refRoot.child(childName).setValue(student);
                                }
                                else {
                                    String childName = String.format("%02d", Integer.parseInt(stratumStr)) + String.format("%02d", Integer.parseInt(classStr)) + "1" + "_" + firstNameStr + "_" + lastNameStr;
                                    // do a edit command (with the sec vaccine)

                                    refRoot.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot ds) {
                                            boolean wasFound = false;

                                            for(DataSnapshot data : ds.getChildren()) {
                                                if (data.getKey().equals(childName))
                                                {
                                                    wasFound = true;

                                                    // get the spesific student
                                                    StudentInfo student = data.getValue(StudentInfo.class);

                                                    if (student.getFirstVaccine() != null)
                                                    {
                                                        student.setSecondVaccine(new VaccineInfo(placeStr, dateStr));
                                                        refRoot.child(childName).setValue(student);
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(MainActivity.this, "This User Cant get the sec vaccine!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }

                                            if (!wasFound)
                                            {
                                                Toast.makeText(MainActivity.this, "Cant Find User With This Details!", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }
                        }
                    }
                });

                AlertDialog ad = adb.create();
                ad.show();
            }
        }
    }

    /**
     * Submit data for a student that cannot been vaccinated.
     *
     * @param view the view
     */
    public void submitData(View view) {
        String firstNameStr = firstName.getText().toString();
        String lastNameStr = lastName.getText().toString();
        String classStr = studClass.getText().toString();
        String stratumStr = stratum.getText().toString();

        if (firstNameStr.equals("") || lastNameStr.equals("") || classStr.equals("") || stratumStr.equals(""))
        {
            Toast.makeText(this, "There is a missing value!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (!checkAlphabetic(firstNameStr) || !checkAlphabetic(lastNameStr))
            {
                Toast.makeText(this, "first or last name must contains only letters!", Toast.LENGTH_SHORT).show();
            }
            else // if everything is good
            {
                String childName = String.format("%02d", Integer.parseInt(stratumStr)) + String.format("%02d", Integer.parseInt(classStr)) + "0" + "_" + firstNameStr + "_" + lastNameStr;
                Toast.makeText(MainActivity.this, childName, Toast.LENGTH_SHORT).show();
                StudentInfo student = new StudentInfo(firstNameStr, lastNameStr, Integer.parseInt(stratumStr), Integer.parseInt(classStr), null, null);
                refRoot.child(childName).setValue(student);
            }
        }
    }

    /**
     * Check if a string is an full alphabetic.
     *
     * @param input the input to check
     * @return if a string is an alphabetic
     */
    public static boolean checkAlphabetic(String input) {
        for (int i = 0; i != input.length(); ++i) {
            if (!Character.isLetter(input.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Callback method to be invoked when an item in this view has been
     * selected
     *
     * @param parent   The AdapterView where the selection happened
     * @param view     The view within the AdapterView that was clicked
     * @param position The position of the view in the adapter
     * @param id       The row id of the item that is selected
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 3)
        {
            vaccineLayout.setVisibility(View.GONE);
            noVaccineLayout.setVisibility(View.VISIBLE);
            vaccineNum = -1; // cant get the vaccine
        }
        else if (position != 0) {
            vaccineLayout.setVisibility(View.VISIBLE);
            noVaccineLayout.setVisibility(View.GONE);
            vaccineNum = position; // 1 or 2 vaccine
        }
        else
        {
            noVaccineLayout.setVisibility(View.GONE);
            vaccineLayout.setVisibility(View.GONE);
        }
    }

    /**
     * Callback method to be invoked when the selection disappears from this
     * view.
     *
     * @param parent The AdapterView that now contains no selected item.
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * Create the options menu
     *
     * @param menu the menu
     * @return ture if success
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Where to go when the menu item was selected
     *
     * @param item The menu item that was selected.
     *
     * @return boolean Return false to allow normal menu processing to
     *         proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // go to edit Vaccines activity if clicked
        if (id == R.id.editVaccines)
        {
            Intent si = new Intent(this, ShowData.class);
            startActivity(si);
        }
        else if (id == R.id.filterData)
        {
            Intent si = new Intent(this, FilterData.class);
            startActivity(si);
        }
        else if (id == R.id.appCredits)
        {
            Intent si = new Intent(this, CreditsActivity.class);
            startActivity(si);
        }

        return true;
    }
}