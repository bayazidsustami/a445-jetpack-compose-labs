package com.dicoding.mycomposenavigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dicoding.mycomposenavigation.ui.theme.MyComposeNavigationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyComposeNavigationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MyApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "first",
        modifier = modifier
    ) {
        composable(route = "first") {
            FirstScreen { messageContent ->
                navController.navigate("second/$messageContent/10?nullableContent=Welcome")
            }
        }
        composable(
//            route = "second/{content}",
            route = "second/{content}/{otherContent}?optionalContent={optionalContent}&otherOptionalContent={otherOptionalContent}",
            arguments = listOf(
                navArgument("content") {
                    type = NavType.StringType
                },
                navArgument("otherContent") {
                    type = NavType.IntType
                },
                navArgument("optionalContent") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("otherOptionalContent") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = "!"
                }
            )
        ) { backStackEntry ->
            SecondScreen(
                content = backStackEntry.arguments?.getString("content")
                        + backStackEntry.arguments?.getInt("otherContent")
                        + backStackEntry.arguments?.getString("optionalContent")
                        + backStackEntry.arguments?.getString("otherOptionalContent"),
                navigateBack = { navController.navigateUp() },
            )
        }
    }
}

@Composable
fun FirstScreen(
    modifier: Modifier = Modifier,
    navigateToSecondScreen: (String) -> Unit,
) {
    var content by remember { mutableStateOf("") }
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Button(
            onClick = {
                navigateToSecondScreen(content)
            }
        ) {
            Text(text = "Second Screen")
        }
    }
}

@Composable
fun SecondScreen(
    content: String?,
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
) {
    if (content != null) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = content, style = MaterialTheme.typography.displayLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = navigateBack
            ) {
                Text(text = "Go back")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyComposeNavigationTheme {
        MyApp()
    }
}