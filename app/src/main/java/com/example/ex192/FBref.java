package com.example.ex192;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * The type firebase ref
 * @author Itey Weintraub <av5350@bs.amalnet.k12.il>
 * @version	1
 * @since 28.3.2021
 * short description:
 *
 *      This are consts for the database (itself and to the root)
 */
public class FBref {
    /**
     * The constant FBDB.
     */
    public static FirebaseDatabase FBDB = FirebaseDatabase.getInstance("https://ex192-3cdc5-default-rtdb.firebaseio.com/");

    /**
     * The constant ref to Root.
     */
    public static DatabaseReference refRoot = FBDB.getReference();
}
