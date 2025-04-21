package com.example.mam.nav.authorization

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mam.gui.screen.authorization.ChangePasswordScreen
import com.example.mam.gui.screen.authorization.OTPScreen
import com.example.mam.gui.screen.authorization.SignInScreen
import com.example.mam.gui.screen.authorization.SignUpScreen
import com.example.mam.gui.screen.authorization.StartScreen
import com.example.mam.viewmodel.authorization.ChangePasswordViewModel
import com.example.mam.viewmodel.authorization.SignInViewModel
import com.example.mam.viewmodel.authorization.SignUpViewModel


@Composable
fun AuthorizationNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = AuthorizationScreen.Start.name,
){
    val signInVM: SignInViewModel = viewModel()
    val signUpVM: SignUpViewModel = viewModel()
    val changePasswordVM: ChangePasswordViewModel = viewModel()

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
                    signInVM.checkSignIn()
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
                    //Xử lý đăng ký
                    navController.navigate(AuthorizationScreen.SignIn.name)
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
            ChangePasswordScreen(
                onChangeClicked = {
                    //Xử lý đổi mật khẩu
                    navController.navigate(AuthorizationScreen.OTP.name)
                },
                onCloseClicked = {
                    navController.popBackStack()
                },
            )
        }
        composable(route = AuthorizationScreen.OTP.name){
            OTPScreen(
                onVerifyClicked = {
                    if (changePasswordVM.isOTPValid()) {
                        //xu ly
                        navController.navigate(AuthorizationScreen.SignIn.name)
                    }
                    else {

                    }
                },
                onCloseClicked = {
                    navController.popBackStack()
                }
            )
        }
    }
}