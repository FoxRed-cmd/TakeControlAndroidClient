package com.pancake.takecontrol

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.net.Socket

class MainActivity : ComponentActivity() {
    private var controller = RemoteController("0.0.0.0",5000)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Container(controller)
        }
    }
}

@Composable
fun Container(controller: RemoteController) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("localAddress", Context.MODE_PRIVATE)

    var backgroundColor = R.color.white
    val onDarkTheme = remember {
        mutableStateOf(sharedPreferences.getBoolean("onDark", false))
    }
    if (onDarkTheme.value) {
        backgroundColor = R.color.background
    }
    else {
        backgroundColor = R.color.white
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(colorResource(id = backgroundColor))){
        Column(modifier = Modifier.align(Alignment.BottomCenter)) {
            CardSettings(controller, onDarkTheme)
            CardPowerController(controller, onDarkTheme.value)
            CardMediaController(controller, onDarkTheme.value)
        }
    }
}

@Composable
fun ButtonsLine(modifierRow: Modifier,
                horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
                modifierButton: Modifier,
                backgroundButton: Color,
                iconSize: Dp,
                firstVectorResource: Int,
                secondVectorResource: Int,
                thirdVectorResource: Int, controller: RemoteController, buttonsLineNumber: Int) {
    Row(modifier = modifierRow,
        horizontalArrangement = horizontalArrangement) {
        FloatingActionButton(modifier = modifierButton,
            shape = CircleShape,
            containerColor = backgroundButton,
            onClick = {
                when (buttonsLineNumber) {
                    0 -> controller.power()
                    1 -> controller.volumeDown()
                    2 -> controller.previousStep()
                    3 -> controller.previousPlay()
                }
            }) {
            Image(imageVector = ImageVector.vectorResource(firstVectorResource),
                contentDescription = "volume low", modifier = Modifier.size(iconSize))

        }
        FloatingActionButton(modifier = modifierButton,
            containerColor = backgroundButton,
            onClick = {
                when (buttonsLineNumber) {
                    0 -> controller.restart()
                    1 -> controller.volumeOff()
                    2 -> controller.pause()
                    3 -> controller.stop()
                }
            }) {
            Image(imageVector = ImageVector.vectorResource(secondVectorResource),
                contentDescription = "volume off", modifier = Modifier.size(iconSize))

        }
        FloatingActionButton(modifier = modifierButton,
            shape = CircleShape,
            containerColor = backgroundButton,
            onClick = {
                when (buttonsLineNumber) {
                    0 -> controller.sleep()
                    1 -> controller.volumeUp()
                    2 -> controller.nextStep()
                    3 -> controller.nextPlay()
                }
            }) {
            Image(imageVector = ImageVector.vectorResource(thirdVectorResource),
                contentDescription = "volume high", modifier = Modifier.size(iconSize))
        }
    }
}

@Composable
fun CardMediaController(controller: RemoteController, onDarkTheme: Boolean = false) {
    val iconSize = 36.dp
    var backgroundButton = R.color.primary
    var backgroundCard = R.color.white

    if (onDarkTheme) {
        backgroundButton = R.color.accent
        backgroundCard = R.color.black
    }
    else {
        backgroundButton = R.color.primary
        backgroundCard = R.color.white
    }

    Card(modifier = Modifier
        .padding(20.dp)
        .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = backgroundCard)
        )) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()) {
            ButtonsLine(
                modifierRow = Modifier.padding(bottom = 20.dp, top = 20.dp),
                modifierButton = Modifier
                    .padding(start = 20.dp, end = 20.dp)
                    .size(64.dp),
                backgroundButton = colorResource(id = backgroundButton),
                iconSize = iconSize,
                firstVectorResource = R.drawable.volume_low_solid,
                secondVectorResource = R.drawable.volume_xmark_solid,
                thirdVectorResource = R.drawable.volume_high_solid,
                buttonsLineNumber = 1, controller = controller
            )
            ButtonsLine(
                modifierRow = Modifier.padding(bottom = 20.dp),
                modifierButton = Modifier
                    .padding(start = 20.dp, end = 20.dp)
                    .size(64.dp),
                backgroundButton = colorResource(id = backgroundButton),
                iconSize = iconSize,
                firstVectorResource = R.drawable.backward_step_solid,
                secondVectorResource = R.drawable.pause_solid,
                thirdVectorResource = R.drawable.forward_step_solid,
                buttonsLineNumber = 2, controller = controller
            )
            ButtonsLine(
                modifierRow = Modifier.padding(bottom = 20.dp),
                modifierButton = Modifier
                    .padding(start = 20.dp, end = 20.dp)
                    .size(64.dp),
                backgroundButton = colorResource(id = backgroundButton),
                iconSize = iconSize,
                firstVectorResource = R.drawable.backward_solid,
                secondVectorResource = R.drawable.stop_solid,
                thirdVectorResource = R.drawable.forward_solid,
                buttonsLineNumber = 3, controller = controller
            )
        }
    }
}

@Composable
fun CardPowerController(controller: RemoteController, onDarkTheme: Boolean = false) {
    val iconSize = 36.dp
    var backgroundButton = R.color.primary
    var backgroundCard = R.color.white

    if (onDarkTheme) {
        backgroundButton = R.color.accent
        backgroundCard = R.color.black
    }
    else {
        backgroundButton = R.color.primary
        backgroundCard = R.color.white
    }
    Card(modifier = Modifier
        .padding(20.dp, bottom = 0.dp, end = 20.dp, top = 20.dp)
        .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = backgroundCard)
        )) {
        ButtonsLine(
            modifierRow = Modifier
                .padding(bottom = 20.dp, top = 20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            modifierButton = Modifier
                .padding(start = 20.dp, end = 20.dp)
                .size(64.dp),
            backgroundButton = colorResource(id = backgroundButton),
            iconSize = iconSize,
            firstVectorResource = R.drawable.power_off_solid,
            secondVectorResource = R.drawable.spinner_solid,
            thirdVectorResource = R.drawable.bolt_solid,
            buttonsLineNumber = 0, controller = controller
        )
    }
}

@Composable
fun CardSettings(controller: RemoteController, onDarkTheme: MutableState<Boolean>) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val sharedPreferences = context.getSharedPreferences("localAddress", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    val ipAddress = remember {
        mutableStateOf(sharedPreferences.getString("ipAddress", "") ?: "")
    }
    val port = remember {
        mutableStateOf(sharedPreferences.getString("port", "")  ?: "")
    }

    controller.ipAddress = sharedPreferences.getString("ipAddress", "0.0.0.0") ?: "0.0.0.0"
    controller.port = sharedPreferences.getString("port", "5000")?.toInt() ?: 5000

    var backgroundCard = R.color.white
    var focusedColor = R.color.primary
    var unfocusedColor = R.color.black
    var textColor = R.color.black

    if (onDarkTheme.value) {
        backgroundCard = R.color.black
        focusedColor = R.color.accent
        unfocusedColor = R.color.white
        textColor = R.color.white
    }
    else {
        backgroundCard = R.color.white
        focusedColor = R.color.primary
        unfocusedColor = R.color.black
        textColor = R.color.black
    }

    Card(modifier = Modifier
        .padding(20.dp, bottom = 0.dp, end = 20.dp)
        .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = backgroundCard)
        )) {
        Column {
            Row(modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 20.dp)
                .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically) {
                Image(imageVector = ImageVector.vectorResource(R.drawable.moon_solid),
                    contentDescription = "night theme", modifier = Modifier.size(32.dp),
                    colorFilter = ColorFilter.tint(colorResource(id = if (onDarkTheme.value)
                        R.color.accent else R.color.primary)))
                Switch(checked = onDarkTheme.value, onCheckedChange = {
                    onDarkTheme.value = !onDarkTheme.value
                    editor.putBoolean("onDark", onDarkTheme.value).apply()
                },
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = colorResource(id = R.color.accent),
                    ))
            }
            Row(modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(value = ipAddress.value,
                    onValueChange = {value -> ipAddress.value = value},
                    placeholder = { Text(text = "0.0.0.0")},
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorResource(focusedColor),
                        unfocusedBorderColor = colorResource(unfocusedColor),
                        focusedTextColor = colorResource(textColor),
                        unfocusedTextColor = colorResource(textColor)
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            editor.putString("ipAddress", ipAddress.value)
                            editor.putString("port", port.value)
                            controller.ipAddress = ipAddress.value
                            controller.port = port.value.toInt()
                            editor.apply()
                        }
                    ),
                    modifier = Modifier.width(150.dp))
                OutlinedTextField(value = port.value,
                    onValueChange = {value -> port.value = value},
                    placeholder = { Text(text = "Port")},
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorResource(focusedColor),
                        unfocusedBorderColor = colorResource(unfocusedColor),
                        focusedTextColor = colorResource(textColor),
                        unfocusedTextColor = colorResource(textColor)
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            editor.putString("ipAddress", ipAddress.value)
                            editor.putString("port", port.value)
                            controller.ipAddress = ipAddress.value
                            controller.port = port.value.toInt()
                            editor.apply()
                        }
                    ),
                    modifier = Modifier.width(150.dp))
            }
        }
    }
}