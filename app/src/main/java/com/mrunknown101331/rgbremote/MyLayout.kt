package com.mrunknown101331.rgbremote

import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewLayout(isEnabled: Boolean) {
    val database: DatabaseReference = Firebase.database.reference
    var redSliderPosition by rememberSaveable { mutableStateOf(0.0f) }
    var greenSliderPosition by rememberSaveable { mutableStateOf(0.0f) }
    var blueSliderPosition by rememberSaveable { mutableStateOf(0.0f) }
    var rgbVal by rememberSaveable { mutableStateOf("000000") }

    val colorListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val temp = dataSnapshot.getValue<String>() as String
            redSliderPosition = hexToFloat(temp.substring(0, 2))
            greenSliderPosition = hexToFloat(temp.substring(2, 4))
            blueSliderPosition = hexToFloat(temp.substring(4, 6))
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.w(ContentValues.TAG, "loadPost:onCancelled")
        }
    }
    database.child("Color").addValueEventListener(colorListener)


    ConstraintLayout {
        val (redSlide, redText, greenSlide, greenText, blueSlide, blueText, rgb, button) = createRefs()

        Slider(
            value = redSliderPosition,
            onValueChange = {
                redSliderPosition = it.roundToInt().toFloat()
            },
            modifier = Modifier
                .constrainAs(redSlide) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(redText.start)
                }
                .fillMaxWidth(0.90f)
                .padding(start = 10.dp),
            steps = 254,
            valueRange = 0f..255f,
            colors = SliderDefaults.colors(
                thumbColor = Color.Red,
                activeTrackColor = Color.Red,
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            enabled = isEnabled
        )
        Text(text = redSliderPosition.toInt().toString(),
            modifier = Modifier
                .constrainAs(redText) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
                .width(30.dp)
                .padding(top = 13.dp),
            color = Color.Red)

        Slider(
            value = greenSliderPosition,
            onValueChange = {
                greenSliderPosition = it.roundToInt().toFloat()
            },
            modifier = Modifier
                .constrainAs(greenSlide) {
                    top.linkTo(redSlide.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(greenText.start)
                }
                .fillMaxWidth(0.90f)
                .padding(start = 10.dp),
            steps = 254,
            valueRange = 0f..255f,
            colors = SliderDefaults.colors(
                thumbColor = Color.Green,
                activeTrackColor = Color.Green,
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            enabled = isEnabled
        )
        Text(text = greenSliderPosition.toInt().toString(),
            modifier = Modifier
                .constrainAs(greenText) {
                    top.linkTo(redText.bottom)
                    end.linkTo(parent.end)
                }
                .width(30.dp)
                .padding(top = 27.dp),
            color = Color.Green
        )
        Slider(
            value = blueSliderPosition,
            onValueChange = {
                blueSliderPosition = it.roundToInt().toFloat()
            },
            modifier = Modifier
                .constrainAs(blueSlide) {
                    top.linkTo(greenSlide.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(blueText.start)
                }
                .fillMaxWidth(0.90f)
                .padding(start = 10.dp),
            steps = 254,
            valueRange = 0f..255f,
            colors = SliderDefaults.colors(
                thumbColor = Color.Blue,
                activeTrackColor = Color.Blue,
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            enabled = isEnabled
        )
        Text(text = blueSliderPosition.toInt().toString(),
            modifier = Modifier
                .constrainAs(blueText) {
                    top.linkTo(greenText.bottom)
                    end.linkTo(parent.end)
                }
                .width(30.dp)
                .padding(top = 27.dp),
            color = Color.Blue)


        TextField(
            value = rgbVal,
            onValueChange = {
                if (it.length < 6) {
                    redSliderPosition = hexToDec(it.substring(0, 2)).toFloat()
                    greenSliderPosition = hexToDec(it.substring(2, 4)).toFloat()
                    blueSliderPosition = hexToDec(it.substring(4) + "0").toFloat()
                } else {
                    redSliderPosition = hexToDec(it.substring(0, 2)).toFloat()
                    greenSliderPosition = hexToDec(it.substring(2, 4)).toFloat()
                    blueSliderPosition = hexToDec(it.substring(4, 6)).toFloat()
                }
            },
            label = { Text("Hex Value") },
            modifier = Modifier
                .constrainAs(rgb) {
                    top.linkTo(blueSlide.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .wrapContentWidth(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(hexToLong("ff$rgbVal")),
                textColor = Color(hexToLong("ff" + invertColor(rgbVal)))
            ),
            enabled = isEnabled
        )
        rgbVal =
            (decToHex(redSliderPosition.toInt()) + decToHex(greenSliderPosition.toInt()) + decToHex(
                blueSliderPosition.toInt()
            )).uppercase()
        val topGuideline = createGuidelineFromTop(220.dp)
        val context = LocalContext.current
        FilledTonalButton(
            onClick = {
                database.child("Color").setValue(rgbVal)
                update(option1 = false, option2 = true, option3 = false, option4 = false)
                Toast.makeText(context, "Color Uploaded", Toast.LENGTH_LONG).show()
            },
            modifier = Modifier
                .constrainAs(button) {
                    top.linkTo(topGuideline)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(bottom = 20.dp)
                .wrapContentWidth(),
            enabled = isEnabled
        ) {
            Text(text = "Change Color")
        }
    }
}