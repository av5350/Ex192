package com.example.ex192;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FBref {
    public static FirebaseDatabase FBDB = FirebaseDatabase.getInstance("https://ex192-3cdc5-default-rtdb.firebaseio.com/");

    public static DatabaseReference refRoot = FBDB.getReference();
}
