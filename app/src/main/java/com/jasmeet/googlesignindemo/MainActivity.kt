package com.jasmeet.googlesignindemo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import coil.compose.SubcomposeAsyncImage
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jasmeet.googlesignindemo.ui.theme.GoogleSignInDemoTheme
import java.net.URLDecoder
import java.net.URLEncoder

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GoogleSignInDemoTheme {
                val navHostController = rememberNavController()
                MainNavigation(navHostController = navHostController)
            }
        }
    }
}

@Composable
fun LoginScreen(navHostController: NavHostController) {

    val context = LocalContext.current

    val token = stringResource(R.string.default_web_client_id)
    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(token)
            .requestEmail()
            .requestProfile()
            .build()
    }
    val googleSignInClient = remember {
        GoogleSignIn.getClient(context, gso)
    }

    val user = rememberSaveable { mutableStateOf(Firebase.auth.currentUser) }

    val isLoading = rememberSaveable { mutableStateOf(false) }

    val launcher = rememberFirebaseAuthLauncher(
        onAuthComplete = { result ->
            user.value = result.user
            isLoading.value = false

            val encodedImgUrl = URLEncoder.encode(result.user?.photoUrl.toString(), "utf-8")
            val encodedEmail = URLEncoder.encode(result.user?.email.toString(), "utf-8")

            val navOptions = NavOptions.Builder()
                .setPopUpTo(Screens.Login.route, inclusive = true)
                .build()
            navHostController.navigate(
                Screens.Home.passData(
                    imgUrl = encodedImgUrl,
                    email = encodedEmail,
                    name = result.user?.displayName.toString()
                ),
                navOptions
            )
        },
        onAuthError = {
            user.value = null
            isLoading.value = false
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()

        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {

        OutlinedButton(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 15.dp)
                .align(Alignment.CenterHorizontally),
            onClick = {
                isLoading.value = true
                launcher.launch(googleSignInClient.signInIntent)

            }
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = null,
                modifier = Modifier
                    .padding(5.dp)
                    .size(22.dp)
                    .align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = "Sign in with Google",
                modifier = Modifier.align(Alignment.CenterVertically)
            )

        }

    }
    if (isLoading.value) {
        Dialog(
            onDismissRequest = { /*TODO*/
            },

            ) {
            Box(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.large)
                    .background(Color.White, MaterialTheme.shapes.large)
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .size(150.dp)


            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navHostController: NavHostController,
    imgUrl: String?,
    name: String?,
    email: String?
) {

    val context = LocalContext.current
    val decodeImg = URLDecoder.decode(imgUrl, "utf-8")
    val token = stringResource(R.string.default_web_client_id)
    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(token)
            .requestEmail()
            .requestProfile()
            .build()
    }
    val googleSignInClient = remember {
        GoogleSignIn.getClient(context, gso)
    }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = {
            Text(text = "Profile")
        })
    }) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            SubcomposeAsyncImage(
                model = decodeImg,
                loading = {
                    CircularProgressIndicator()
                },
                modifier = Modifier
                    .padding(vertical = 15.dp)
                    .clip(CircleShape)
                    .size(150.dp)
                    .align(Alignment.CenterHorizontally),
                contentDescription = " User Image"
            )
            Text(
                text = "Name: $name",
                modifier = Modifier
                    .padding(vertical = 5.dp)
                    .align(Alignment.CenterHorizontally),
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 20.sp
                )
            )
            Text(
                text = "Email: $email",
                modifier = Modifier
                    .padding(vertical = 5.dp)
                    .align(Alignment.CenterHorizontally),
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 20.sp
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            OutlinedButton(

                onClick = {
                    googleSignInClient.signOut().addOnSuccessListener {
                        val navOptions = NavOptions.Builder()
                            .setPopUpTo(Screens.Home.route, inclusive = true)
                            .build()
                        navHostController.navigate(Screens.Login.route, navOptions)
                    }.addOnFailureListener {
                        Toast.makeText(context, "Sign Out Failed", Toast.LENGTH_SHORT).show()
                    }

                },
                modifier = Modifier
                    .padding(vertical = 15.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Sign Out")
            }


        }
    }

}