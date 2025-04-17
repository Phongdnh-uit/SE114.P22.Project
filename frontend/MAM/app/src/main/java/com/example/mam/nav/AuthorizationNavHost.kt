package com.example.mam.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mam.viewmodel.AuthorizationViewModel
import com.example.mam.gui.screen.ChangePasswordScreen
import com.example.mam.gui.screen.SignInScreen
import com.example.mam.gui.screen.SignUpScreen
import com.example.mam.gui.screen.StartScreen


@Composable
fun AuthorizationNavHost(
    modifier: Modifier = Modifier,
    authorizationVM: AuthorizationViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
    startDestination: String = SignInSignUpScreen.Start.name,
){
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
                    authorizationVM.checkSignIn()
                },
                onForgotClicked = {
                    navController.navigate(SignInSignUpScreen.ForgetPW.name)
                },
            )
        }
        composable(route = SignInSignUpScreen.SignUp.name){
            SignUpScreen(
                onSignUpClicked = {

                },
                onSignInClicked = {
                    navController.navigate(SignInSignUpScreen.SignIn.name)
                }
            )
        }
        composable(route = SignInSignUpScreen.ForgetPW.name){
            ChangePasswordScreen()
        }
    }
}