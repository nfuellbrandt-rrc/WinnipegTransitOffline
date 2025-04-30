package com.example.winnipegtransitoffline.screens

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.winnipegtransitoffline.MainActivity
import com.google.firebase.auth.FirebaseAuth

/**
 * A screen used to sign in.
 * @param context The context used to display toasts.
 * @param modifier The modifier to be applied to SignInScreen.
 */
@Composable
fun SignInScreen(
    context: Context,
    modifier: Modifier = Modifier
){
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column (
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        TextField(
            value = email,
            onValueChange = {email = it},
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            label = {
                Text(text = "email")
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
        )
        TextField(
            value = password,
            onValueChange = {password = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .padding(top = 8.dp),
            label = {
                Text(text = "password")
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password)
        )
        Button(
            onClick = {
                performSignIn(email, password, context, keyboardController)
            },

            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = "Sign In")
        }
        Button(
            onClick = {
                performAnonSignIn(context)
            },

            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
            ) {
            Text(text = "Sign In Anonymously")
        }

    }
}

/**
 * Sign in to firebase using an email and password.
 * @param email Email to sign in with.
 * @param password Password to sign in with.
 * @param context Context used to show toasts.
 * @param keyboardController KeyboardController used to ensure the keyboard is hidden before moving to the next screen.
 */
private fun performSignIn(
    email: String,
    password: String,
    context: Context,
    keyboardController: SoftwareKeyboardController?
) {
    val auth = FirebaseAuth.getInstance()

    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Sign In Successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("UserID", FirebaseAuth.getInstance().currentUser?.uid)
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "Sign In Failed", Toast.LENGTH_LONG).show()
            }
            keyboardController?.hide()
        }
}

/**
 * Sign in to firebase anonymously.
 * @param context Context used to show toasts.
 */
private fun performAnonSignIn(context: Context) {
    val auth = FirebaseAuth.getInstance()

    auth.signInAnonymously()
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Sign In Successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("UserID", FirebaseAuth.getInstance().currentUser?.uid)
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "Sign In Failed", Toast.LENGTH_LONG).show()
            }
        }
}