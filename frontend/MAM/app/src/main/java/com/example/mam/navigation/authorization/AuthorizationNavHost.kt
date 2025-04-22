package com.example.mam.navigation.authorization

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ActivityScenario.launch
import com.example.mam.gui.screen.authorization.ForgetPasswordScreen
import com.example.mam.gui.screen.authorization.OTPScreen
import com.example.mam.gui.screen.authorization.SignInScreen
import com.example.mam.gui.screen.authorization.SignUpScreen
import com.example.mam.gui.screen.authorization.StartScreen
import com.example.mam.viewmodel.authorization.ForgetPasswordViewModel
import com.example.mam.viewmodel.authorization.SignInViewModel
import com.example.mam.viewmodel.authorization.SignUpViewModel
import kotlinx.coroutines.launch


@Composable
fun AuthorizationNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = AuthorizationScreen.Start.name,
){
    val signInVM: SignInViewModel = viewModel()
    val signUpVM: SignUpViewModel = viewModel()
    val forgetPasswordVM: ForgetPasswordViewModel = viewModel()
    val coroutineScope = rememberCoroutineScope()
        NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ){
        composable(route = AuthorizationScreen.Start.name) {
            StartScreen(
                onSignInClicked = {
                    navController.navigate(AuthorizationScreen.SignIn.name)
                },
                onSignUpClicked = {
                    navController.navigate(AuthorizationScreen.SignUp.name)
                },
                onTermsClicked = {
                    navController.navigate(AuthorizationScreen.Terms.name)
                }
            )
        }
        composable(route = AuthorizationScreen.SignIn.name) {
            SignInScreen(
                onSignInClicked = {
                    coroutineScope.launch {
                        val result = signInVM.SignIn() // Ensure it's awaited properly
                        when (result) {
                            0 -> signInVM.notifySignInFalse()
                            1 -> {}
                        }
                    }
                },
                onForgotClicked = {
                    navController.navigate(AuthorizationScreen.ForgetPW.name)
                },
                onBackClicked = {
                    navController.popBackStack()
                },
            )
        }
        composable(route = AuthorizationScreen.SignUp.name){
            SignUpScreen(
                onSignUpClicked = {
                    coroutineScope.launch {
                        when (signUpVM.SignUp()) {
                            0 -> signUpVM.notifySignUpFalse()
                            1 -> navController.navigate(AuthorizationScreen.SignIn.name)
                        }
                    }

                },
                onSignInClicked = {
                    navController.navigate(AuthorizationScreen.SignIn.name)
                },
                onBackClicked = {
                    navController.popBackStack()
                },
            )
        }
        composable(route = AuthorizationScreen.ForgetPW.name){
            ForgetPasswordScreen(
                onChangeClicked = {
                    coroutineScope.launch {
                        forgetPasswordVM.getPhoneNumber()
                        navController.navigate(AuthorizationScreen.OTP.name)
                    }
                },
                onCloseClicked = {
                    navController.popBackStack()
                },
            )
        }
        composable(route = AuthorizationScreen.OTP.name){
            OTPScreen(
                onVerifyClicked = {
                    coroutineScope.launch {
                        if (forgetPasswordVM.isOTPValid()) {
                            forgetPasswordVM.changePassword()
                            navController.navigate(AuthorizationScreen.SignIn.name)
                        } else {
                            forgetPasswordVM.notifyOTPInValid()
                        }
                    }
                },
                onCloseClicked = {
                    navController.popBackStack()
                }
            )
        }
    }
}