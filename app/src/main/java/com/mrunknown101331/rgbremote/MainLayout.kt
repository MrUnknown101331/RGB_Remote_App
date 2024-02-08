package com.mrunknown101331.rgbremote

import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mrunknown101331.rgbremote.ui.theme.RGBRemoteTheme

@Composable
fun NewCard(modifier: Modifier, isEnabled: Boolean) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(5.dp),
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth(0.9f)
    ) {
        Text(text = "")
        NewLayout(isEnabled)
    }
}


@Composable
fun FinalLayout() {
    val database: DatabaseReference = Firebase.database.reference
    var option1 by rememberSaveable { mutableStateOf(true) }
    var option2 by rememberSaveable { mutableStateOf(false) }
    var option3 by rememberSaveable { mutableStateOf(false) }
    var option4 by rememberSaveable { mutableStateOf(false) }

    val isOffListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            option1 = dataSnapshot.getValue<Boolean>() as Boolean
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.w(ContentValues.TAG, "loadPost:onCancelled")
        }
    }
    val isCustomListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            option2 = dataSnapshot.getValue<Boolean>() as Boolean
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.w(ContentValues.TAG, "loadPost:onCancelled")
        }
    }
    val smoothListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            option3 = dataSnapshot.getValue<Boolean>() as Boolean
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.w(ContentValues.TAG, "loadPost:onCancelled")
        }
    }
    val rgbListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            option4 = dataSnapshot.getValue<Boolean>() as Boolean
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.w(ContentValues.TAG, "loadPost:onCancelled")
        }
    }
    database.child("isOff").addValueEventListener(isOffListener)
    database.child("isCustom").addValueEventListener(isCustomListener)
    database.child("smoothLight").addValueEventListener(smoothListener)
    database.child("rgbLight").addValueEventListener(rgbListener)
    ConstraintLayout {
        val (img, row1, row2, row3, card, row4, card2, sp, txtRow) = createRefs()
        Image(
            painter = painterResource(id = R.drawable.flicker),
            contentDescription = "Logo for App",
            modifier = Modifier.constrainAs(img) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.constrainAs(row1) {
                top.linkTo(img.bottom)
                start.linkTo(parent.start)
            }) {
            RadioButton(selected = option1, onClick = {
                option1 = true
                option2 = false
                option3 = false
                option4 = false
                update(option1 = true, option2 = false, option3 = false, option4 = false)
            })
            Text(text = "Turn Off")

        }
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.constrainAs(row2) {
                top.linkTo(row1.bottom)
                start.linkTo(parent.start)
            }) {
            RadioButton(selected = option2, onClick = {
                option1 = false
                option2 = true
                option3 = false
                option4 = false
                update(option1 = false, option2 = true, option3 = false, option4 = false)
            })
            Text(text = "Custom Color")
        }
        NewCard(modifier = Modifier.constrainAs(card) {
            top.linkTo(row2.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }, option2)
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.constrainAs(row3) {
                top.linkTo(card.bottom)
                start.linkTo(parent.start)
            }) {
            RadioButton(selected = option3, onClick = {
                option1 = false
                option2 = false
                option3 = true
                option4 = false
                update(option1 = false, option2 = false, option3 = true, option4 = false)
            })
            Text(text = "Smooth Transition")
        }
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.constrainAs(row4) {
                top.linkTo(row3.bottom)
                start.linkTo(parent.start)
            }) {
            RadioButton(selected = option4, onClick = {
                option1 = false
                option2 = false
                option3 = false
                option4 = true
                update(option1 = false, option2 = false, option3 = false, option4 = true)
            })
            Text(text = "RGB Transition")
        }
        Spacer(modifier = Modifier
            .height(50.dp)
            .constrainAs(sp) {
                top.linkTo(row4.bottom)
            })
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .constrainAs(txtRow)
                {
                    top.linkTo(sp.bottom)
                    start.linkTo(parent.start)
                }
                .padding(start = 11.dp, bottom = 15.dp)
        ) {
            Text(text = "Control Appliances : ")
        }
        SwitchLayout(modifier = Modifier.constrainAs(card2) {
            top.linkTo(txtRow.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        })
    }
}

fun update(option1: Boolean, option2: Boolean, option3: Boolean, option4: Boolean) {
    val database: DatabaseReference = Firebase.database.reference
    database.child("isOff").setValue(option1)
    database.child("isCustom").setValue(option2)
    database.child("smoothLight").setValue(option3)
    database.child("rgbLight").setValue(option4)
}


@Preview(showBackground = true)
@Composable
fun FinalPreview() {
    RGBRemoteTheme {
        FinalLayout()
    }
}