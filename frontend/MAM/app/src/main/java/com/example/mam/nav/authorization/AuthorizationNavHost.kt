package com.example.mam.nav.authorization

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mam.gui.screen.authorization.ChangePasswordScreen
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
    startDestination: String = SignInSignUpScreen.Start.name,
){
    val signInVM: SignInViewModel = viewModel()
    val signUpVM: SignUpViewModel = viewModel()
    val changePasswordVM: ChangePasswordViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ){
        composable(route = SignInSignUpScreen.Start.name) {
            StartScreen(
                onSignInClicked = {
                    navController.navigate(SignInSignUpScreen.SignIn.name)
                },
                onSignUpClicked = {
                    navController.navigate(SignInSignUpScreen.SignUp.name)
                },
                onTermsClicked = {
                    navController.navigate(SignInSignUpScreen.Terms.name)
                }
            )
        }
        composable(route = SignInSignUpScreen.SignIn.name) {
            SignInScreen(
                onSignInClicked = {
                    signInVM.checkSignIn()
                },
                onForgotClicked = {
                    navController.navigate(SignInSignUpScreen.ForgetPW.name)
                },
                onBackClicked = {
                    navController.popBackStack()
                },
            )
        }
        composable(route = SignInSignUpScreen.SignUp.name){
            SignUpScreen(
                onSignUpClicked = {
                    //Xử lý đăng ký
                    navController.navigate(SignInSignUpScreen.SignIn.name)
                },
                onSignInClicked = {
                    navController.navigate(SignInSignUpScreen.SignIn.name)
                },
                onBackClicked = {
                    navController.popBackStack()
                },
            )
        }
        composable(route = SignInSignUpScreen.ForgetPW.name){
            ChangePasswordScreen(
                onChangeClicked = {
                    //Xử lý đổi mật khẩu
                    navController.popBackStack(SignInSignUpScreen.SignIn.name,inclusive = false)
                },
                onCloseClicked = {
                    navController.popBackStack()
                },
            )
        }
    }
}