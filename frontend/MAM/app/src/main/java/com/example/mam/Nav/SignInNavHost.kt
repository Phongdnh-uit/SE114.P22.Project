package com.example.mam.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mam.gui.screen.ChangePasswordScreen
import com.example.mam.gui.screen.SignInScreen
import com.example.mam.gui.screen.SignUpScreen
import com.example.mam.ViewModel.SignInViewModel
import com.example.mam.ViewModel.SignUpViewModel
import com.example.mam.data.SignInState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun SignInSignUpNavHost(
    modifier: Modifier = Modifier,
    signInVM: SignInViewModel = viewModel(),
    signUpVM: SignUpViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
    startDestination: String = SignInSignUpScreen.Login.name,
){
    val coroutineScope = rememberCoroutineScope()
    val signInState: SignInState by signInVM.signInState.collectAsState()
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ){
        composable(route = SignInSignUpScreen.Login.name) {
            SignInScreen(
                onSignInClicked = {
                    username, password ->
                    signInVM.CheckSignIn(username = username, password = password)
                },
                onSignUpClicked = {
                    coroutineScope.launch {
                    delay(100)
                    navController.navigate(SignInSignUpScreen.SignUp.name) }
                    },
                onForgotClicked = {
                    coroutineScope.launch {
                        delay(100)
                        navController.navigate(SignInSignUpScreen.ForgetPW.name)
                    }
                },
                onTermsClicked = {
                    coroutineScope.launch {
                        delay(100)
                        navController.navigate(SignInSignUpScreen.Terms.name)
                    }
                },
            )
        }
        composable(route = SignInSignUpScreen.SignUp.name){
            SignUpScreen()
        }
        composable(route = SignInSignUpScreen.ForgetPW.name){
            ChangePasswordScreen()
        }
    }
}