package com.aslansoft.myactivities

import NotificationManager
import ReminderRepo
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import chackandshowReminder
import com.aslansoft.myactivities.Data.ActivityDao
import com.aslansoft.myactivities.Data.ActivityEntity
import com.aslansoft.myactivities.classes.PixelFontFamily
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDateTime
import network.chaintech.kmp_date_time_picker.ui.datetimepicker.WheelDateTimePickerView
import network.chaintech.kmp_date_time_picker.utils.DateTimePickerView
import network.chaintech.kmp_date_time_picker.utils.TimeFormat
import network.chaintech.kmp_date_time_picker.utils.WheelPickerDefaults
import network.chaintech.kmp_date_time_picker.utils.now
import org.jetbrains.compose.ui.tooling.preview.Preview
import java.time.LocalDateTime

@Composable
@Preview
fun App(dao: ActivityDao) {

    MaterialTheme {

        val notes by dao.getAll().collectAsState(initial = emptyList())
        val scope = rememberCoroutineScope()
        val dialogState = remember { mutableStateOf(false) }
        val fieldState = remember{ mutableStateOf(false) }
        val note = remember { mutableStateOf("") }
        val textFieldFocused = remember { mutableStateOf(false) }
        val focusRequester = remember { FocusRequester() }
        var showDatePicker by remember { mutableStateOf(false) }
        var selectedDate by remember { mutableStateOf<LocalDateTime?>(null) }
        val date = remember { mutableStateOf("") }
        val  time =remember { mutableStateOf("") }
        val snackbarHostState = remember { SnackbarHostState() }
        val type = remember { mutableStateOf("Reminder") }

        //Scaffold ile ekranı hazır bi şekilde sabit yapıları oluşturduk
      Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
          TopAppBar(modifier = Modifier.fillMaxWidth(),title = {
                      Text("Aktiviteler", textAlign = TextAlign.Center, fontFamily = PixelFontFamily(), color = Color.White)
          }, actions = {
              IconButton(onClick = { dialogState.value = true }) {
                  Icon(Icons.Filled.Notifications, contentDescription = "Notifications",tint = Color.White)
              }
          }, backgroundColor = Color(57, 26, 120))


      }, floatingActionButton = { FloatingActionButton(backgroundColor = Color(56, 149, 192),shape = RoundedCornerShape(6.dp),onClick = { fieldState.value = true }) {
          Icon(Icons.Filled.Add, contentDescription = "Add")
      }
      }){
          println(LocalDateTime.now().toString())
          //Genel Arayüz
          if (notes.size == 0){
              Column(modifier = Modifier.fillMaxSize().background(color = Color(26, 33, 48)), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                  Icon(imageVector = Icons.Filled.Notifications, contentDescription = "Notifications",tint = Color.White)
                  Text("Henüz not almadınız...",color = Color.White)
              }
          }else{
              Column(modifier = Modifier.fillMaxSize()
                  .background(color = Color(26, 33, 48))) {

                  Spacer(modifier = Modifier.padding(vertical = 15.dp))
                  val columnSize = remember { mutableStateOf(0) }
                  //Text(getPlatform().name, color = Color.White)
                  if (getPlatform().name == "Android"){
                      columnSize.value = 2
                  }else if (getPlatform().name == "Desktop"){
                      columnSize.value = 4
                  }
                  LazyVerticalStaggeredGrid(modifier = Modifier.padding(10.dp),columns = StaggeredGridCells.Fixed(columnSize.value),
                      verticalItemSpacing = 30.dp,
                      horizontalArrangement = Arrangement.spacedBy(15.dp)) {
                      items(notes.size){ index ->
                          var expandedMenu by remember { mutableStateOf(false) }

                          val note = notes[index]
                          val enabled = remember { mutableStateOf(false) }
                          val noteField = remember { mutableStateOf(false) }
                          enabled.value = note.enabled
                          Card(modifier = Modifier.width(RandomDp(index)).height(RandomHeight(index)).clickable { noteField.value = true }
                              .then(gestureDetect(onAction = {expandedMenu = true}, secondAction = {noteField.value = true})), backgroundColor = RandomColor(index)) {

                              if (expandedMenu){
                                  DropdownMenu(expandedMenu, onDismissRequest = {expandedMenu = false}) {
                                      DropdownMenuItem(onClick = {
                                          scope.launch {
                                              note.id?.let { it1 -> dao.deleteById(it1) }
                                          }
                                          expandedMenu = false
                                      }){ Text("Sil") }

                                  }
                              }
                              Box(modifier = Modifier.fillMaxSize().padding(4.dp)){
                                  if (note.enabled){
                                          Row(modifier = Modifier.fillMaxSize()) {
                                              Row (modifier = Modifier.fillMaxWidth(0.75f)){
                                                  Text("${note.note}", textAlign = TextAlign.Center, fontFamily = PixelFontFamily(), textDecoration = TextDecoration.LineThrough)


                                              }
                                              Box(modifier = Modifier.fillMaxWidth(),contentAlignment = TopEnd){
                                                  Checkbox(checked = enabled.value, onCheckedChange = {
                                                      enabled.value = it
                                                      scope.launch {
                                                          dao.updateEnable(note.id,enabled.value)
                                                      }
                                                  })

                                              }
                                          }




                                  }else{
                                      Row(modifier = Modifier.fillMaxSize()) {
                                          Row (modifier = Modifier.fillMaxWidth(0.75f)){
                                              Text("${note.note}", textAlign = TextAlign.Center, fontFamily = PixelFontFamily())


                                          }
                                          Box(modifier = Modifier.fillMaxWidth(),contentAlignment = TopEnd){
                                              Checkbox(checked = enabled.value, onCheckedChange = {
                                                  enabled.value = it
                                                  scope.launch {
                                                      dao.updateEnable(note.id,enabled.value)
                                                  }
                                              })

                                          }
                                      }

                                  }

                                  val (dateV,timeV) = note.date.split("/")
                                  if (getPlatform().name == "Desktop"){
                                      Box(modifier = Modifier.fillMaxSize(),contentAlignment = BottomEnd){
                                          Column(modifier = Modifier.fillMaxWidth(0.25f).fillMaxHeight(0.4f)) {
                                              Text(dateV, fontFamily = PixelFontFamily(), fontSize = 15.sp)
                                              Text(timeV,fontFamily = PixelFontFamily(), fontSize = 15.sp)

                                          }

                                      }
                                  }else{
                                      Box(modifier = Modifier.fillMaxSize(),contentAlignment = BottomEnd){
                                          Column(modifier = Modifier.fillMaxWidth(0.33f).fillMaxHeight(0.4f)) {
                                              Text(dateV, fontFamily = PixelFontFamily(), fontSize = 10.sp)
                                              Text(timeV,fontFamily = PixelFontFamily(), fontSize = 10.sp)

                                          }

                                      }
                                  }

                              }



                          }

                          if (noteField.value){
                              Dialog(onDismissRequest = { noteField.value = false }) {
                                  Card(modifier = Modifier.width(500.dp).height(300.dp), backgroundColor = RandomColor(index)) {
                                      Column(modifier = Modifier.fillMaxSize().padding(4.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                            Row(modifier = Modifier.fillMaxWidth()) {
                                                if (note.type == "Reminder"){
                                                    Text("Hatırlatıcı",fontFamily = PixelFontFamily(), fontSize = 25.sp)
                                                }else{
                                                    Text("Aktivite",fontFamily = PixelFontFamily(), fontSize = 25.sp)
                                                }
                                            }
                                          Divider(thickness = 1.dp, color = Color.Black)
                                          Row(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.9f)) {
                                              Text(note.note, fontFamily = PixelFontFamily())
                                          }
                                          Divider(thickness = 1.dp, color = Color.Black)
                                          Row (modifier = Modifier.fillMaxWidth()) {
                                              Text(note.date, fontFamily = PixelFontFamily(), fontSize = 15.sp)
                                          }
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
        if (fieldState.value){
            AnimatedVisibility(
                visible = fieldState.value,
                enter = fadeIn(),
                exit = fadeOut()
            ){
                Column(modifier = Modifier.offset(y = if (fieldState.value) 100.dp else 0.dp)) {
                    Dialog(onDismissRequest = { fieldState.value = false }) {
                        Card(shape = RoundedCornerShape(8.dp),
                            elevation = 10.dp,
                            modifier = Modifier.width(700.dp).height(500.dp)
                                .padding(top = 70.dp, bottom = 70.dp)
                                .clickable{textFieldFocused.value = true
                                    focusRequester.requestFocus()
                                },
                            backgroundColor =  Color(57, 26, 120)) {
                            Column(modifier = Modifier.fillMaxSize()) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(onClick = { fieldState.value = !fieldState.value }) {
                                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                                    }
                                    Spacer(modifier = Modifier.padding(horizontal = 15.dp))

                                        Text("Not Ekle (Çift tıkla)", fontSize = 15.sp, fontFamily = PixelFontFamily(), color = Color.White)

                                    Box (modifier = Modifier.fillMaxWidth(),contentAlignment = CenterEnd){
                                        TextButton(onClick = {
                                            if (note.value.trim().isNotEmpty()&& date.value.isNotEmpty() && time.value.isNotEmpty()&& note.value.length <= 40&& type.value.isNotEmpty() ){
                                                scope.launch {
                                                    dao.insert(ActivityEntity(
                                                        note = note.value.trim(),
                                                        date = "${date.value}/${time.value}",
                                                        enabled = false,
                                                        type = type.value
                                                    ))
                                                    //dao.deleteAll()
                                                    note.value = ""
                                                    date.value = ""
                                                    time.value = ""
                                                    type.value = ""
                                                }
                                                fieldState.value = !fieldState.value
                                            }else if(note.value.length > 40) {
                                                scope.launch {
                                                    snackbarHostState.showSnackbar("Notunuz 40 karakter uzunluğunda olmalıdır...")
                                                }

                                            }else{
                                               scope.launch {
                                                snackbarHostState.showSnackbar("Lütfen tüm alanları doldur")
                                               }

                                            }
                                            }) {
                                            Text("Kaydet", fontFamily = PixelFontFamily(), fontWeight = FontWeight.Bold,color = Color.White)
                                        }
                                    }


                                }
                                Spacer(modifier = Modifier.padding(vertical = 5.dp))
                                Box {
                                    BasicTextField(modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
                                        .focusRequester(focusRequester),
                                        value = note.value,
                                        onValueChange = {
                                                note.value = it
                                             },
                                        enabled = textFieldFocused.value,
                                        maxLines = 7,
                                        minLines = 7,
                                        textStyle = TextStyle(
                                            fontFamily = PixelFontFamily(),
                                            color = Color.White,
                                        ),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                        cursorBrush = SolidColor(Color.White),
                                    )

                                }

                                Row(modifier = Modifier.fillMaxWidth().weight(1f), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Bottom) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        if (date.value.isNotEmpty() && time.value.isNotEmpty()){
                                            Text("${date.value} - ${time.value}", fontFamily = PixelFontFamily(), color = Color.White)
                                        }
                                        Button(onClick = {showDatePicker = true},
                                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(56, 149, 192) )){
                                            Text("Tarih Ekle", fontFamily = PixelFontFamily(), color = Color.White)
                                        }
                                    }

                                }
                                Spacer(modifier = Modifier.padding(vertical = 5.dp))
                                Divider(thickness = 1.dp, color = Color.White)



                                    val tabs = listOf("Hatırlatıcı", "Aktivite",)
                                    var selectedTabIndex by remember { mutableStateOf(0) }
                                    TabRow(backgroundColor = Color(57, 26, 120),
                                        contentColor = Color.White,
                                        selectedTabIndex = selectedTabIndex,
                                        indicator = { tabPositions ->
                                            TabRowDefaults.Indicator(
                                                Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex])
                                            )
                                        }
                                    ) {
                                        tabs.forEachIndexed { index, title ->
                                            Tab(
                                                selected = selectedTabIndex == index,
                                                onClick = { selectedTabIndex = index

                                                            if (title == "Hatırlatıcı"){
                                                                type.value = "Reminder"
                                                            }else if (title == "Aktivite"){
                                                                type.value = "TodoList"
                                                            }
                                                            },
                                                text = { Text(text = title,fontFamily = PixelFontFamily()) },
                                            )
                                        }
                                    }
                            }


                        }
                        Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = BottomCenter) {
                            SnackbarHost(hostState = snackbarHostState){data ->
                                Snackbar( modifier = Modifier.width(730.dp)
                                    ,snackbarData = data,
                                    contentColor = Color.White,
                                    backgroundColor = Color(57, 26, 120))

                            }
                        }

                    }
                }

            }

        }


        // tarih kısmı
        val title = remember { mutableStateOf("") }
        if (getPlatform().name == "Desktop"){
            title.value = "Tarih Ekle (Değiştirmek İçin Tekerleği Çevirin)"
        } else if (getPlatform().name == "Android"){
            title.value = "Tarih Ekle"
        }
        if (showDatePicker){
            WheelDateTimePickerView(modifier = Modifier.padding(top = 16.dp, bottom = 18.dp).fillMaxWidth(),
                containerColor = Color(57, 26, 120) ,
                showDatePicker = showDatePicker,
                titleStyle = TextStyle(color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = PixelFontFamily()),
                doneLabelStyle = TextStyle(color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight(600),
                    fontFamily = PixelFontFamily()
                    ),
                yearsRange = IntRange(2000,2099),
                doneLabel = "Kaydet",
                dateTextStyle = TextStyle(color = Color.White,
                    fontFamily = PixelFontFamily()),
                    dateTextColor = Color(56, 149, 192),
                    selectorProperties = WheelPickerDefaults.selectorProperties(
                    borderColor = Color(56, 149, 192)
                ),
                title = title.value,
                timeFormat = TimeFormat.HOUR_24,
                rowCount = 5,
                height = 170.dp,
                onDoneClick = {
                    selectedDate = it.toJavaLocalDateTime()
                    //gelen saat ve tarih verisini istediğimiz formata dönüştürebilmek için parçaladık
                    //burda tarihi ve saati
                    val (dateV,timeV) = selectedDate.toString().split("T")
                    //burda da tarih bize yıl/ay/gün olarak geldiği için gün.ay.yıl olarak ayırmak için tekrar parçaladık
                    val(year,month,day) = dateV.split("-")

                    //tarih ve saati ayırdık

                    //tarihi istediğimiz şekilde formatladık

                    //parçaladığımız gün ay ve yıl verilerini date değişkenine istediğimiz sırada string olarak ekledik

                    date.value = "${day}.${month}.${year}"

                    //saati formatladık

                    time.value = timeV

                    showDatePicker = false

                },
                dateTimePickerView = DateTimePickerView.DIALOG_VIEW,
                onDismiss = { showDatePicker = false }
            )
        }


    }
}
val sizeList: List<Dp> = listOf(
    80.dp,
    100.dp,
    130.dp,
    110.dp,
    120.dp
)
val heightList: List<Dp> = listOf(
    120.dp,
    140.dp,
    170.dp,
    150.dp,
    160.dp
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
fun RandomHeight(index: Int): Dp{
    return heightList[index % heightList.size]
}
fun RandomColor(index: Int): Color {

    return colorList[index % colorList.size]
}


