package com.mrunknown101331.rgbremote

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@Composable
fun SwitchLayout(modifier: Modifier) {
    var sw1 by rememberSaveable { mutableStateOf(false) }
    var sw2 by rememberSaveable { mutableStateOf(false) }
    var sw3 by rememberSaveable { mutableStateOf(false) }
    var sw4 by rememberSaveable { mutableStateOf(false) }

    ElevatedCard(
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = modifier.wrapContentSize()
    ) {
        Spacer(modifier = Modifier.height(15.dp))
        Row(
            modifier = Modifier.fillMaxWidth(0.85f),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column {
                Text(text = stringResource(id = R.string.switch_1))
                Switch(checked = sw1, onCheckedChange = {
                    sw1 = it
                    updateSw(1, sw1)
                })
            }
            Column {
                Text(text = stringResource(id = R.string.switch_2))
                Switch(checked = sw2, onCheckedChange = {
                    sw2 = it
                    updateSw(2, sw2)
                })
            }
            Column {
                Text(text = stringResource(id = R.string.switch_3))
                Switch(checked = sw3, onCheckedChange = {
                    sw3 = it
                    updateSw(3, sw3)
                })
            }
            Column {
                Text(text = stringResource(id = R.string.switch_4))
                Switch(checked = sw4, onCheckedChange = {
                    sw4 = it
                    updateSw(4, sw4)
                })
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
    val database: DatabaseReference = Firebase.database.reference
    val sw1Listener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            sw1 = dataSnapshot.getValue<Boolean>() as Boolean
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.w(TAG, "loadPost:onCancelled")
        }
    }
    val sw2Listener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            sw2 = dataSnapshot.getValue<Boolean>() as Boolean
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.w(TAG, "loadPost:onCancelled")
        }
    }
    val sw3Listener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            sw3 = dataSnapshot.getValue<Boolean>() as Boolean
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.w(TAG, "loadPost:onCancelled")
        }
    }
    val sw4Listener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            sw4 = dataSnapshot.getValue<Boolean>() as Boolean
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.w(TAG, "loadPost:onCancelled")
        }
    }
    database.child("Sw1").addValueEventListener(sw1Listener)
    database.child("Sw2").addValueEventListener(sw2Listener)
    database.child("Sw3").addValueEventListener(sw3Listener)
    database.child("Sw4").addValueEventListener(sw4Listener)
}

fun updateSw(i: Int, swVal: Boolean) {
    val database: DatabaseReference = Firebase.database.reference
    if (i == 1)
        database.child("Sw1").setValue(swVal)
    if (i == 2)
        database.child("Sw2").setValue(swVal)
    if (i == 3)
        database.child("Sw3").setValue(swVal)
    if (i == 4)
        database.child("Sw4").setValue(swVal)
}
