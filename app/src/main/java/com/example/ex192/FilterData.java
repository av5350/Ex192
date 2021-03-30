package com.example.ex192;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.ex192.FBref.refRoot;

/**
 * The type filter data activity.
 * @author Itey Weintraub <av5350@bs.amalnet.k12.il>
 * @version	1
 * @since 28.3.2021
 * short description:
 *
 *      This activity let the user to show the data by some filters
 */
public class FilterData extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ArrayList<String> stratumKeys = new ArrayList<String>();
    ArrayList<String> allClasses, allSchool, cantVaccine, currStratum, currClass;
    ArrayList<StudentInfo> stratumClasses;
    HashMap<String, Set<String>> stratums = new HashMap<String, Set<String>>(); // stratums and classes
    Spinner allStratums, filterOptions, classes;
    ListView shownData;
    ArrayAdapter<String> adp;
    String selectedStratum;
    int option = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_data);

        shownData = (ListView) findViewById(R.id.shownData);
        allStratums = (Spinner) findViewById(R.id.allStratums);
        filterOptions = (Spinner) findViewById(R.id.filterOptions);
        classes = (Spinner) findViewById(R.id.classes);

        filterOptions.setOnItemSelectedListener(this);
        allStratums.setOnItemSelectedListener(this);
        classes.setOnItemSelectedListener(this);

        allSchool = new ArrayList<>();
        cantVaccine = new ArrayList<>();
        currStratum = new ArrayList<>();
        stratumClasses = new ArrayList<>();
        currClass = new ArrayList<>();

        ArrayAdapter<String> optionsAdp = new ArrayAdapter<String>(FilterData.this,
                R.layout.support_simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.filterOptions));
        filterOptions.setAdapter(optionsAdp);

        stratumKeys.add("Stratums");
        getDbData();
    }

    /**
     * Get the database data and init stratums spinner
     */
    public void getDbData()
    {
        refRoot.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dS) {
                for(DataSnapshot data : dS.getChildren()) {
                    String keyTmp = data.getKey();

                    // if there isnt that key(stratum one) - create this one (put there the class in this stratum)
                    if (stratums.get(keyTmp.substring(0, 2)) == null) {
                        stratums.put(keyTmp.substring(0, 2), new HashSet<String>(Collections.singleton(keyTmp.substring(2, 4))));
                        stratumKeys.add(keyTmp.substring(0, 2));
                    } else {
                        stratums.get(keyTmp.substring(0, 2)).add(keyTmp.substring(2, 4));
                    }

                    // Add the student info object to array
                    StudentInfo student = data.getValue(StudentInfo.class);

                    // if cant vaccinated
                    if (keyTmp.substring(4, 5).equals("0"))
                    {
                        cantVaccine.add(student.getFirstName() + " " + student.getLastName() + " Stratum: " + student.getStratum() + " Class: " + student.getStudClass());
                    }
                    else // else - can be vaccinated
                    {
                        allSchool.add(student.getFirstName() + " " + student.getLastName() + " Stratum: " + student.getStratum() + " Class: " + student.getStudClass());
                    }
                }

                adp = new ArrayAdapter<String>(FilterData.this, R.layout.support_simple_spinner_dropdown_item, stratumKeys);
                allStratums.setAdapter(adp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Callback method to be invoked when an item in this view has been
     * selected. This callback is invoked only when the newly selected
     * position is different from the previously selected position or if
     * there was no selected item. - show the resault of selected filter
     *
     * @param parent   The AdapterView where the selection happened
     * @param view     The view within the AdapterView that was clicked
     * @param position The position of the view in the adapter
     * @param id       The row id of the item that is selected
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        classes.setVisibility(View.INVISIBLE);

        switch (parent.getId())
        {
            case R.id.filterOptions:
                if (position == 1 || position == 2)
                {
                    allStratums.setVisibility(View.VISIBLE);

                    // save the option for another time
                    if (position == 1)
                    {
                        option = 1;
                    }
                    else
                    {
                        option = 2;
                    }
                }
                else // school / cant be vaccinated/ noting was selected
                {
                    allStratums.setVisibility(View.INVISIBLE);

                    if (position == 3)
                    {
                        // DO THE All School FILTERING (GET ALL...)
                        adp = new ArrayAdapter<String>(this,
                                R.layout.support_simple_spinner_dropdown_item,
                                allSchool);
                        shownData.setAdapter(adp);
                    }
                    else if (position == 4)
                    {
                        // do the Cant be vaccinated filtering
                        adp = new ArrayAdapter<String>(this,
                                R.layout.support_simple_spinner_dropdown_item,
                                cantVaccine);
                        shownData.setAdapter(adp);
                    }
                }
                break;
            case R.id.allStratums:
                if (position != 0)
                {
                    stratumClasses.clear();
                    selectedStratum = stratumKeys.get(position);

                    if (option == 1) // class
                    {
                        classes.setVisibility(View.VISIBLE);
                        // FILL THE classes SPINNER
                        allClasses = new ArrayList<String>();
                        allClasses.add("Classes");
                        allClasses.addAll(stratums.get(selectedStratum));
                        adp = new ArrayAdapter<String>(FilterData.this, R.layout.support_simple_spinner_dropdown_item, allClasses);
                        classes.setAdapter(adp);
                    }

                    // DO THE STRATUM FILTERING
                    Query query = refRoot.orderByChild("stratum").equalTo(Integer.parseInt(selectedStratum));
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dS) {
                            currStratum.clear();
                            for (DataSnapshot data : dS.getChildren())
                            {
                                StudentInfo student = data.getValue(StudentInfo.class);

                                if (student.getFirstVaccine() != null) // if was vaccinated
                                {
                                    currStratum.add(student.getFirstName() + " " + student.getLastName() + " Stratum: " + student.getStratum() + " Class: " + student.getStudClass());
                                    stratumClasses.add(student);
                                }
                            }

                            if (option == 2) {
                                adp = new ArrayAdapter<String>(FilterData.this, R.layout.support_simple_spinner_dropdown_item, currStratum);
                                shownData.setAdapter(adp);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                break;
            case R.id.classes:
                classes.setVisibility(View.VISIBLE);

                if (position != 0)
                {
                    int selectedClass = Integer.parseInt(allClasses.get(position));

                    currClass.clear();

                    // DO THE CLASS FILTERING
                    for (StudentInfo student : stratumClasses)
                    {
                        // if the student in the spesific class
                        if (student.getStudClass() == selectedClass)
                        {
                            currClass.add(student.getFirstName() + " " + student.getLastName() + " Stratum: " + student.getStratum() + " Class: " + student.getStudClass());
                        }
                    }
                    adp = new ArrayAdapter<String>(FilterData.this, R.layout.support_simple_spinner_dropdown_item, currClass);
                    shownData.setAdapter(adp);
                }
                break;
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
        else if (id == R.id.addStudent)
        {
            Intent si = new Intent(this, MainActivity.class);
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