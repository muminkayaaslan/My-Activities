package com.aslansoft.myactivities

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.aslansoft.myactivities.Data.ActivityDao
import com.aslansoft.myactivities.Data.ActivityEntity
import com.aslansoft.myactivities.classes.PixelFontFamily
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.datetime.toJavaLocalDateTime
import network.chaintech.kmp_date_time_picker.ui.datetimepicker.WheelDateTimePickerView
import network.chaintech.kmp_date_time_picker.utils.DateTimePickerView
import network.chaintech.kmp_date_time_picker.utils.TimeFormat
import network.chaintech.kmp_date_time_picker.utils.WheelPickerDefaults
import org.jetbrains.compose.ui.tooling.preview.Preview
import java.time.LocalDateTime
import kotlin.random.Random

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

        //Scaffold ile ekranı hazır bi şekilde sabit yapıları oluşturduk
      Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
          TopAppBar(modifier = Modifier.fillMaxWidth(),title = {
                      Text("Aktiviteler", textAlign = TextAlign.Center, fontFamily = PixelFontFamily())
          }, actions = {
              IconButton(onClick = { dialogState.value = true }) {
                  Icon(Icons.Filled.Notifications, contentDescription = "Notifications")
              }
          }, backgroundColor = Color(182,0,113))


      }, floatingActionButton = { FloatingActionButton(backgroundColor = Color(228,0,58),shape = RoundedCornerShape(6.dp),onClick = { fieldState.value = true }) {
          Icon(Icons.Filled.Add, contentDescription = "Add")
      }
      }){
          println(notes)
          //Genel Arayüz

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

                        val note = notes[index]
                        Card(modifier = Modifier.size(RandomDp()), backgroundColor = RandomColor()) {
                            Text("${note.note}", textAlign = TextAlign.Center, fontFamily = PixelFontFamily())
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
                            backgroundColor =  Color(182,0,113)) {
                            Column(modifier = Modifier.fillMaxSize()) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(onClick = { fieldState.value = !fieldState.value }) {
                                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                                    }
                                    Spacer(modifier = Modifier.padding(horizontal = 15.dp))

                                        Text("Not Ekle (Çift tıkla)", fontSize = 15.sp, fontFamily = PixelFontFamily())

                                    Box (modifier = Modifier.fillMaxWidth(),contentAlignment = CenterEnd){
                                        TextButton(onClick = {
                                            if (note.value.trim().isNotEmpty()&& selectedDate != null){
                                                scope.launch {
                                                    dao.insert(ActivityEntity(
                                                        note = note.value.trim(),
                                                        date = "${date.value}/${time.value}",
                                                        enabled = false,
                                                        type = "reminder"
                                                    ))
                                                    //dao.deleteAll()
                                                    note.value = ""
                                                    date.value = ""
                                                    time.value = ""
                                                }
                                                fieldState.value = !fieldState.value
                                            }else{
                                               scope.launch {
                                                snackbarHostState.showSnackbar("Lütfen tüm alanları doldur")
                                               }

                                            }
                                            }) {
                                            Text("Kaydet", fontFamily = PixelFontFamily(), fontWeight = FontWeight.Bold,color = Color.Black)
                                        }
                                    }


                                }
                                Spacer(modifier = Modifier.padding(vertical = 5.dp))
                                Box {
                                    BasicTextField(modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
                                        .focusRequester(focusRequester),
                                        value = note.value,
                                        onValueChange = { note.value = it },
                                        enabled = textFieldFocused.value,
                                        maxLines = 7,
                                        minLines = 7,
                                        textStyle = TextStyle(
                                            fontFamily = PixelFontFamily()
                                        ),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                                    )

                                }

                                Row(modifier = Modifier.fillMaxWidth().weight(1f), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Bottom) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        if (date.value.isNotEmpty() && time.value.isNotEmpty()){
                                            Text("${date.value} - ${time.value}", fontFamily = PixelFontFamily())
                                        }
                                        Button(onClick = {showDatePicker = true},
                                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(228,0,58) ),){
                                            Text("Tarih Ekle", fontFamily = PixelFontFamily())
                                        }
                                    }

                                }
                            }


                        }
                        Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = BottomCenter) {
                            SnackbarHost(hostState = snackbarHostState){data ->
                                Snackbar( modifier = Modifier.width(730.dp)
                                    ,snackbarData = data,
                                    contentColor = Color.Black,
                                    backgroundColor = Color(182,0,113))

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
                containerColor = Color(26, 33, 48) ,
                showDatePicker = showDatePicker,
                titleStyle = TextStyle(color = Color(182,0,113),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = PixelFontFamily()),
                doneLabelStyle = TextStyle(color = Color(0, 151, 178),
                    fontSize = 14.sp,
                    fontWeight = FontWeight(600),
                    fontFamily = PixelFontFamily()
                    ),
                yearsRange = IntRange(2000,2099),
                doneLabel = "Kaydet",
                dateTextStyle = TextStyle(color = Color.White,
                    fontFamily = PixelFontFamily()),
                    dateTextColor = Color(182,0,113),
                    selectorProperties = WheelPickerDefaults.selectorProperties(
                    borderColor = Color(182,0,113)
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

fun RandomDp(): Dp {
    return (Random.nextInt(50,150)).dp
}
fun RandomColor(): Color {
    return Color(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
}