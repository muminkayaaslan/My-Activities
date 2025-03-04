@file:Suppress("NAME_SHADOWING")

package com.aslansoft.myactivities

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.aslansoft.myactivities.Data.ActivityDao
import com.aslansoft.myactivities.Data.ActivityEntity
import com.aslansoft.myactivities.classes.Poppins
import kotlinx.coroutines.launch
import kotlinx.datetime.toJavaLocalDateTime
import myactivities.composeapp.generated.resources.Res
import myactivities.composeapp.generated.resources.background
import network.chaintech.kmp_date_time_picker.ui.datetimepicker.WheelDateTimePickerView
import network.chaintech.kmp_date_time_picker.utils.DateTimePickerView
import network.chaintech.kmp_date_time_picker.utils.TimeFormat
import network.chaintech.kmp_date_time_picker.utils.WheelPickerDefaults
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import sun.swing.SwingUtilities2.drawRect
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@Composable
@Preview
fun App(dao: ActivityDao) {

    MaterialTheme {

        val notes by dao.getAll().collectAsState(initial = emptyList())
        val scope = rememberCoroutineScope()
        val fieldState = remember { mutableStateOf(false) }
        val note = remember { mutableStateOf("") }
        val textFieldFocused = remember { mutableStateOf(false) }
        val focusRequester = remember { FocusRequester() }
        var showDatePicker by remember { mutableStateOf(false) }
        var selectedDate by remember { mutableStateOf<LocalDateTime?>(null) }
        val date = remember { mutableStateOf("") }
        val time = remember { mutableStateOf("") }
        val snackbarHostState = remember { SnackbarHostState() }
        val type = remember { mutableStateOf("Reminder") }
        var selectedTabIndexMain by remember { mutableStateOf(0) }
        //Scaffold ile ekranı hazır bi şekilde sabit yapıları oluşturduk
        Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
            TopAppBar(modifier = Modifier.fillMaxWidth(), title = {
                Row(
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Aktiviteler",
                        textAlign = TextAlign.Center,
                        fontFamily = Poppins(),
                        color = Color.White
                    )
                }

            }, backgroundColor = Color(0, 48, 146))
        }, floatingActionButton = {
            FloatingActionButton(backgroundColor = Color(0, 135, 158),
                contentColor = Color.White,
                shape = RoundedCornerShape(6.dp),
                onClick = { fieldState.value = true }) {
                Icon(Icons.Filled.Edit, contentDescription = "Add")
            }
        }, bottomBar = {
            val tabs = listOf("Hatırlatıcılar", "Aktiviteler")

            TabRow(backgroundColor = Color(0, 48, 146),
                contentColor = Color.White,
                selectedTabIndex = selectedTabIndexMain,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndexMain])
                    )
                }) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndexMain == index,
                        onClick = {
                            selectedTabIndexMain = index

                            if (title == "Hatırlatıcılar") {
                                type.value = "Reminder"
                            } else if (title == "Aktiviteler") {
                                type.value = "TodoList"
                            }
                        },
                        text = { Text(text = title, fontFamily = Poppins()) },
                    )
                }
            }
        }) { innerPadding ->
            //Genel Arayüz
            if (notes.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize().paint(
                        painter = painterResource(Res.drawable.background),
                        contentScale = ContentScale.Crop
                    ).clickable { fieldState.value = true },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Notifications,
                        contentDescription = "Notifications",
                        tint = Color.White
                    )

                    Text(
                        "Henüz not almadınız...", color = Color.White
                    )
                }
            } else {


                Column(
                    modifier = Modifier.fillMaxSize().padding(innerPadding).paint(
                        painter = painterResource(Res.drawable.background),
                        contentScale = ContentScale.Crop
                    )
                ) {
                    Spacer(modifier = Modifier.padding(3.dp))
                    val columnSize = remember { mutableStateOf(0) }

                    if (getPlatform().name == "Android") {
                        columnSize.value = 2
                    } else if (getPlatform().name == "Desktop") {
                        columnSize.value = 4
                    }


                    when (selectedTabIndexMain) {
                        0 -> LazyVerticalStaggeredGrid(
                            modifier = Modifier.padding(end = 10.dp, start = 10.dp),
                            columns = StaggeredGridCells.Fixed(columnSize.value),
                            verticalItemSpacing = 30.dp,
                            horizontalArrangement = Arrangement.spacedBy(15.dp)
                        ) {
                            items(notes.filter { it.type == "Reminder" }.size) { index ->
                                var expandedMenu by remember { mutableStateOf(false) }

                                val note = notes.filter { it.type == "Reminder" }[index]
                                val enabled = remember { mutableStateOf(false) }
                                val noteField = remember { mutableStateOf(false) }
                                enabled.value = note.enabled

                                Card(
                                    modifier = Modifier.width(RandomDp(index))
                                        .height(RandomHeight(index))
                                        .clickable { noteField.value = true }.then(
                                            gestureDetect(onAction = { expandedMenu = true },
                                                secondAction = { noteField.value = true })
                                        ), backgroundColor = RandomColor(index)
                                ) {

                                    if (expandedMenu) {
                                        DropdownMenu(expandedMenu,
                                            onDismissRequest = { expandedMenu = false }) {
                                            DropdownMenuItem(onClick = {
                                                scope.launch {
                                                    note.id?.let { it1 -> dao.deleteById(it1) }
                                                }
                                                expandedMenu = false
                                            }) { Text("Sil") }

                                        }
                                    }
                                    Box(modifier = Modifier.fillMaxSize().padding(4.dp)) {
                                        val formatter =
                                            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
                                                .withZone(
                                                    ZoneId.systemDefault()
                                                )
                                        val noteDate = LocalDateTime.parse(note.date, formatter)
                                        val currentDateTime = LocalDateTime.now()
                                        if (!note.enabled) {
                                            Row(modifier = Modifier.fillMaxSize()) {
                                                Row(modifier = Modifier.fillMaxWidth(0.75f)) {
                                                    Text(
                                                        note.note,
                                                        textAlign = TextAlign.Center,
                                                        fontFamily = Poppins()
                                                    )

                                                }

                                            }
                                        } else {
                                            Row(modifier = Modifier.fillMaxSize()) {
                                                Row(modifier = Modifier.fillMaxWidth(0.75f)) {
                                                    Text(
                                                        note.note,
                                                        textAlign = TextAlign.Center,
                                                        fontFamily = Poppins(),
                                                        textDecoration = TextDecoration.LineThrough
                                                    )


                                                }
                                            }


                                        }

                                        if (noteDate.isBefore(currentDateTime)) {
                                            scope.launch {
                                                try {
                                                    dao.updateEnable(note.id, true)

                                                } catch (e: Exception) {
                                                    println("database error: ${e.message}")
                                                }
                                            }
                                        }
                                        val (dateV, timeV) = note.date.split("T")
                                        val (year, month, day) = dateV.split("-")
                                        if (getPlatform().name == "Desktop") {
                                            Box(
                                                modifier = Modifier.fillMaxSize(),
                                                contentAlignment = BottomEnd
                                            ) {
                                                Column(
                                                    modifier = Modifier.fillMaxWidth(0.25f)
                                                        .fillMaxHeight(0.4f)
                                                ) {
                                                    Text(
                                                        "${day}.${month}.${year}",
                                                        fontFamily = Poppins(),
                                                        fontSize = 15.sp
                                                    )
                                                    Text(
                                                        timeV,
                                                        fontFamily = Poppins(),
                                                        fontSize = 15.sp
                                                    )

                                                }

                                            }
                                        } else {
                                            Box(
                                                modifier = Modifier.fillMaxSize(),
                                                contentAlignment = BottomEnd
                                            ) {
                                                Column(
                                                    modifier = Modifier.fillMaxHeight(0.4f)
                                                ) {

                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.End
                                                    ) {
                                                        Text(
                                                            "${day}.${month}.${year}",
                                                            fontFamily = Poppins(),
                                                            fontSize = 10.sp
                                                        )
                                                    }
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.End
                                                    ) {
                                                        Text(
                                                            timeV,
                                                            fontFamily = Poppins(),
                                                            fontSize = 10.sp
                                                        )
                                                    }


                                                }

                                            }
                                        }

                                    }


                                }

                                if (noteField.value) {
                                    Dialog(onDismissRequest = { noteField.value = false }) {
                                        Card(
                                            modifier = Modifier.width(500.dp).height(300.dp),
                                            backgroundColor = RandomColor(index)
                                        ) {
                                            Column(
                                                modifier = Modifier.fillMaxSize().padding(4.dp),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Row(modifier = Modifier.fillMaxWidth()) {
                                                    if (note.type == "Reminder") {
                                                        Text(
                                                            "Hatırlatıcı",
                                                            fontFamily = Poppins(),
                                                            fontSize = 25.sp
                                                        )
                                                    } else {
                                                        Text(
                                                            "Aktivite",
                                                            fontFamily = Poppins(),
                                                            fontSize = 25.sp
                                                        )
                                                    }
                                                }
                                                Divider(thickness = 1.dp, color = Color.Black)
                                                Row(
                                                    modifier = Modifier.fillMaxWidth()
                                                        .fillMaxHeight(0.9f)
                                                ) {
                                                    Text(note.note, fontFamily = Poppins())
                                                }
                                                Divider(thickness = 1.dp, color = Color.Black)
                                                Row(modifier = Modifier.fillMaxWidth()) {
                                                    val (dateV, timeV) = note.date.split("T")
                                                    val (year, month, day) = dateV.split("-")
                                                    Text(
                                                        "${day}.${month}.${year}-${timeV}",
                                                        fontFamily = Poppins(),
                                                        fontSize = 15.sp
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                            }
                            item {
                                Spacer(Modifier.padding(vertical = 3.dp))
                            }
                        }
                        //Todolist Kısmı
                        1 -> LazyColumn {
                            items(notes.filter { it.type == "TodoList" }.size) { index ->
                                val note = notes.filter { it.type == "TodoList" }[index]
                                var expandedMenu by remember { mutableStateOf(false) }
                                var cardHeight by remember { mutableStateOf(0) }
                                var enabled by remember { mutableStateOf(false) }
                                val chBoxInteractionSource = remember { MutableInteractionSource() }
                                if (getPlatform().name == "Desktop") {
                                    cardHeight = 130
                                } else {
                                    cardHeight = 70
                                }
                                enabled = note.enabled
                                Card(
                                    modifier = Modifier.fillMaxWidth().height(cardHeight.dp)
                                        .padding(start = 4.dp, end = 4.dp).then(
                                            gestureDetect(onAction = { expandedMenu = true },
                                                secondAction = {})
                                        ).clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null
                                        ) {
                                            enabled = !enabled
                                            scope.launch {
                                                dao.updateEnable(note.id, enabled)
                                            }
                                        }.indication(
                                            interactionSource = chBoxInteractionSource,
                                            indication = null
                                        ), backgroundColor = RandomColor(index)
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxSize()
                                            .padding(start = 2.dp, end = 2.dp),
                                        verticalArrangement = Arrangement.Center
                                    ) {

                                        Row(
                                            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.4f),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier.weight(1f),
                                                contentAlignment = CenterStart
                                            ) {
                                                if (note.enabled) {
                                                    Text(
                                                        note.note,
                                                        fontFamily = Poppins(),
                                                        fontSize = 15.sp,
                                                        textDecoration = TextDecoration.LineThrough
                                                    )

                                                } else {
                                                    Text(
                                                        note.note,
                                                        fontFamily = Poppins(),
                                                        fontSize = 15.sp
                                                    )
                                                }
                                            }
                                            Box(contentAlignment = CenterEnd) {
                                                CustomCheckbox(
                                                    checked = enabled,
                                                    onCheckedChange = {
                                                        enabled = !enabled
                                                        scope.launch {
                                                            dao.updateEnable(note.id, enabled)
                                                        }
                                                    },
                                                    modifier = Modifier.size(24.dp),
                                                    size = 24.dp,
                                                    checkedColor = Color.DarkGray,
                                                    uncheckedColor = Color.Transparent
                                                )
                                            }
                                        }

                                        val (date, time) = note.date.split("T")
                                        val (year, month, day) = date.split("-")
                                        Spacer(modifier = Modifier.padding(3.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth().weight(1f)
                                                .padding(end = 2.dp)
                                        ) {
                                            Text(
                                                "${day}.${month}.${year}",
                                                fontFamily = Poppins(),
                                                fontSize = 10.sp
                                            )
                                            Spacer(modifier = Modifier.weight(1f))
                                            Text(time, fontFamily = Poppins(), fontSize = 15.sp)
                                        }

                                    }
                                }


                                Spacer(modifier = Modifier.padding(vertical = 5.dp))



                                if (expandedMenu) {
                                    DropdownMenu(
                                        expandedMenu,
                                        onDismissRequest = { expandedMenu = false }) {
                                        DropdownMenuItem(onClick = {
                                            scope.launch {
                                                note.id?.let { it1 -> dao.deleteById(it1) }
                                            }
                                            expandedMenu = false
                                        }) {
                                            Text("Sil", fontFamily = Poppins())
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }


        }

        //Not aldığımız kısım
        if (fieldState.value) {
            AnimatedVisibility(
                visible = fieldState.value, enter = fadeIn(), exit = fadeOut()
            ) {
                Column(modifier = Modifier.offset(y = if (fieldState.value) 100.dp else 0.dp)) {
                    Dialog(onDismissRequest = { fieldState.value = false }) {
                        Card(
                            shape = RoundedCornerShape(8.dp),
                            elevation = 10.dp,
                            modifier = Modifier.width(700.dp).height(500.dp)
                                .padding(top = 70.dp, bottom = 70.dp).clickable {
                                    textFieldFocused.value = true
                                    focusRequester.requestFocus()
                                },
                            backgroundColor = Color(0, 48, 146)
                        ) {
                            Column(modifier = Modifier.fillMaxSize()) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(onClick = { fieldState.value = !fieldState.value }) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = "Back",
                                            tint = Color.White
                                        )
                                    }
                                    Spacer(modifier = Modifier.padding(horizontal = 15.dp))

                                    Text(
                                        "Not Ekle (Çift tıkla)",
                                        fontSize = 15.sp,
                                        fontFamily = Poppins(),
                                        color = Color.White
                                    )

                                    Box(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentAlignment = CenterEnd
                                    ) {
                                        TextButton(onClick = {
                                            if (note.value.trim()
                                                    .isNotEmpty() && selectedDate != null && note.value.length <= 40 && type.value.isNotEmpty()
                                            ) {
                                                scope.launch {
                                                    dao.insert(
                                                        ActivityEntity(
                                                            note = note.value.trim(),
                                                            date = selectedDate.toString(),
                                                            enabled = false,
                                                            type = type.value
                                                        )
                                                    )


                                                    note.value = ""
                                                    time.value = ""
                                                    type.value = ""
                                                    selectedDate = null
                                                }
                                                fieldState.value = !fieldState.value
                                            } else if (note.value.length > 40) {
                                                scope.launch {
                                                    snackbarHostState.showSnackbar("Notunuz 40 karakter uzunluğunda olmalıdır...")
                                                }

                                            } else {
                                                scope.launch {
                                                    snackbarHostState.showSnackbar("Lütfen tüm alanları doldur")
                                                }

                                            }
                                        }) {
                                            Text(
                                                "Kaydet",
                                                fontFamily = Poppins(),
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }
                                    }


                                }
                                Spacer(modifier = Modifier.padding(vertical = 5.dp))
                                Box {
                                    BasicTextField(
                                        modifier = Modifier.padding(
                                            start = 20.dp, end = 20.dp, bottom = 20.dp
                                        ).focusRequester(focusRequester),
                                        value = note.value,
                                        onValueChange = {
                                            note.value = it
                                        },
                                        enabled = textFieldFocused.value,
                                        maxLines = 7,
                                        minLines = 7,
                                        textStyle = TextStyle(
                                            fontFamily = Poppins(),
                                            color = Color.White,
                                        ),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                        cursorBrush = SolidColor(Color.White),
                                    )

                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth().weight(1f),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        if (date.value.isNotEmpty() && time.value.isNotEmpty()) {
                                            Text(
                                                "${date.value} - ${time.value}",
                                                fontFamily = Poppins(),
                                                color = Color.White,
                                            )
                                        }
                                        Button(
                                            onClick = { showDatePicker = true },
                                            colors = ButtonDefaults.buttonColors(
                                                backgroundColor = Color(
                                                    0, 135, 158
                                                )
                                            )
                                        ) {
                                            Text(
                                                "Tarih Ekle",
                                                fontFamily = Poppins(),
                                                color = Color.White
                                            )
                                        }
                                    }

                                }
                                Spacer(modifier = Modifier.padding(vertical = 5.dp))
                                Divider(thickness = 1.dp, color = Color.White)


                                val tabs = listOf("Hatırlatıcı", "Aktivite")
                                var selectedTabIndex by remember { mutableStateOf(0) }
                                TabRow(backgroundColor = Color(0, 48, 146),
                                    contentColor = Color.White,
                                    selectedTabIndex = selectedTabIndex,
                                    indicator = { tabPositions ->
                                        TabRowDefaults.Indicator(
                                            Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex])
                                        )
                                    }) {
                                    tabs.forEachIndexed { index, title ->
                                        Tab(
                                            selected = selectedTabIndex == index,
                                            onClick = {
                                                selectedTabIndex = index

                                                if (title == "Hatırlatıcı") {
                                                    type.value = "Reminder"
                                                } else if (title == "Aktivite") {
                                                    type.value = "TodoList"
                                                }
                                            },
                                            text = { Text(text = title, fontFamily = Poppins()) },
                                        )
                                    }
                                }
                            }


                        }
                        Box(
                            modifier = Modifier.fillMaxWidth().weight(1f),
                            contentAlignment = BottomCenter
                        ) {
                            SnackbarHost(hostState = snackbarHostState) { data ->
                                Snackbar(
                                    modifier = Modifier.width(730.dp),
                                    snackbarData = data,
                                    contentColor = Color.White,
                                    backgroundColor = Color(0, 48, 146)
                                )

                            }
                        }

                    }
                }

            }

        }


        // tarih kısmı
        val title = remember { mutableStateOf("") }
        if (getPlatform().name == "Desktop") {
            title.value = "Tarih Ekle (Değiştirmek İçin Tekerleği Çevirin)"
        } else if (getPlatform().name == "Android") {
            title.value = "Tarih Ekle"
        }
        if (showDatePicker) {
            val dateTimePickerView =
                if (getPlatform().name == "Android") DateTimePickerView.BOTTOM_SHEET_VIEW else DateTimePickerView.DIALOG_VIEW
            WheelDateTimePickerView(dateTimePickerView = dateTimePickerView,
                modifier = Modifier.fillMaxWidth(),
                containerColor = Color(0, 48, 146),
                showDatePicker = showDatePicker,
                titleStyle = TextStyle(
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Poppins()
                ),
                doneLabelStyle = TextStyle(
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight(600),
                    fontFamily = Poppins()
                ),
                yearsRange = IntRange(2000, 2099),
                doneLabel = "Kaydet",
                dateTextStyle = TextStyle(
                    color = Color.White, fontFamily = Poppins()
                ),
                dateTextColor = Color.White,
                selectorProperties = WheelPickerDefaults.selectorProperties(
                    borderColor = Color.White, enabled = false
                ),
                title = title.value,
                timeFormat = TimeFormat.HOUR_24,
                rowCount = 3,
                height = 170.dp,
                onDoneClick = {
                    selectedDate = it.toJavaLocalDateTime()
                    //gelen saat ve tarih verisini istediğimiz formata dönüştürebilmek için parçaladık
                    //burda tarihi ve saati
                    val (dateV, timeV) = selectedDate.toString().split("T")
                    //burda da tarih bize yıl/ay/gün olarak geldiği için gün.ay.yıl olarak ayırmak için tekrar parçaladık
                    val (year, month, day) = dateV.split("-")

                    //tarih ve saati ayırdık

                    //tarihi istediğimiz şekilde formatladık

                    //parçaladığımız gün ay ve yıl verilerini date değişkenine istediğimiz sırada string olarak ekledik

                    date.value = "${day}.${month}.${year}"

                    //saati formatladık

                    time.value = timeV

                    showDatePicker = false

                },
                onDismiss = { showDatePicker = false })
        }


    }
}

val sizeList: List<Dp> = listOf(
    80.dp, 100.dp, 130.dp, 110.dp, 120.dp
)
val heightList: List<Dp> = listOf(
    120.dp, 140.dp, 170.dp, 150.dp, 160.dp
)
val colorList: List<Color> = listOf(
    Color(159, 213, 179),
    Color(126, 162, 198),
    Color(251, 160, 172),
    Color(231, 185, 107),
)

fun RandomDp(index: Int): Dp {
    return sizeList[index % sizeList.size]
}

fun RandomHeight(index: Int): Dp {
    return heightList[index % heightList.size]
}

fun RandomColor(index: Int): Color {

    return colorList[index % colorList.size]
}


@Composable
fun CustomCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    checkedColor: Color = Color.White,
    uncheckedColor: Color = Color.Gray
) {
    val borderColor = if(!checked) checkedColor else Color.Transparent

    Box(modifier = modifier.height(size)
        .background(
            color = if (checked) checkedColor else uncheckedColor, shape = RectangleShape
        ).width(size).clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        ) {
            onCheckedChange(!checked)
        }.border(width = 1.dp, color = borderColor, RectangleShape)
        .clip(RectangleShape)) {
        if (checked) {
            Box(modifier = Modifier.height(size).width(size)) {
                // Çekiş işlemi burada yapılabilir
                // Örneğin: Çizim yapılabilir veya içeriği değiştirebilirsiniz
                if (checked) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = "", tint = Color.White)
                    }
                }
            }
        }
    }
}


