
package com.retvens.lazyclick

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.retvens.lazyclick.ui.theme.LazyClickTheme

@Immutable
data class CustomItems(
    val id : Int,
    val name : String,
    val amount: String,
    var quantity : Int = 0
)


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val context = LocalContext.current

            val itemList = remember {
                listOf(
                    CustomItems(0, "Amerena", "12",1),
                    CustomItems(1, "IceCream", "172", 1),
                    CustomItems(2, "Dal Chawal", "152", 1),
                    CustomItems(3, "Mater Paneer", "102", 1),
               )
            }

            val addedItemList = remember { mutableStateListOf<CustomItems>() }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = "Select Any three Items", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontSize = 20.sp)
                LazyColumn {
                    items(itemList) {
                        val quantityCounter = remember {
                            mutableStateOf(0)
                        }
                        QuantitySelection(it,
                            onAddOrReduceQuantity = { invoke ->
                                if (invoke < 0) {
                                    if (quantityCounter.value != 0) {
                                        quantityCounter.value--
                                        it.quantity = quantityCounter.value
                                        addedItemList.remove(it)
                                        addedItemList.add(it)
                                        if (quantityCounter.value == 0){
                                            addedItemList.remove(it)
                                        }
                                    }
                                } else {
                                    if(addedItemList.contains(it)) {
                                        quantityCounter.value++
                                        it.quantity = quantityCounter.value
                                        addedItemList.remove(it)
                                        addedItemList.add(it)
                                    }
                                    if (!addedItemList.contains(it) && addedItemList.size < 3){
                                        quantityCounter.value++
                                        it.quantity = quantityCounter.value
                                        addedItemList.add(it)
                                    }
                                }
                                it.quantity = quantityCounter.value
                            },
                            quantityCounter.value
                        )
                    }
                }
                Divider()
                Text(text = "Your Items", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontSize = 20.sp)
                if (addedItemList.size !=0){
                    var totalAmount = 0
                    addedItemList.forEach {
                        totalAmount += (it.amount.toInt())*(it.quantity)
                    }
                    Text(text = "Total Amount : $totalAmount", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontSize = 20.sp)

                    LazyColumn {
                        items(addedItemList) {
                            QuantitySelected(customItems = it, it.quantity)
//                            Text(text = it.quantity.toString())
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun QuantitySelection(
        customItems: CustomItems,
        onAddOrReduceQuantity: (Int) -> Unit,
        quantity: Int
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = null,
                modifier = Modifier.size(60.dp)
            )

            Column(
                modifier = Modifier.wrapContentSize(),
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = customItems.name,
                    style = TextStyle(
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(horizontal = 8.dp),
                )

                Text(
                    text = "${customItems.amount} €",
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(horizontal = 8.dp),
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {

                CustomButton(imageVector = Icons.Filled.Add) {
                    onAddOrReduceQuantity.invoke(+1)
                }
                Text(
                    text = quantity.toString(),
                    fontSize = 18.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                CustomButton(imageVector = Icons.Filled.ArrowDropDown) {
                    onAddOrReduceQuantity.invoke(-1)
                }
            }
        }
    }

    @Composable
    fun CustomButton(imageVector: ImageVector, onClick: () -> Unit) {
        Card(
            elevation = 10.dp,
            modifier = Modifier
                .wrapContentSize()
                .padding(12.dp)
                .clickable {
                    onClick.invoke()
                }, shape = CircleShape
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .padding(4.dp)
            )
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        LazyClickTheme {
            //   Greeting("Android")
        }
    }

    @Composable
    fun QuantitySelected(
        customItems: CustomItems,
        quantity: Int
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = null,
                modifier = Modifier.size(60.dp)
            )

            Column(
                modifier = Modifier.wrapContentSize(),
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = customItems.name,
                    style = TextStyle(
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(horizontal = 8.dp),
                )

                Text(
                    text = "${customItems.amount} €",
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(horizontal = 8.dp),
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {

                Text(
                    text = quantity.toString(),
                    fontSize = 18.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

            }
        }
    }

}
