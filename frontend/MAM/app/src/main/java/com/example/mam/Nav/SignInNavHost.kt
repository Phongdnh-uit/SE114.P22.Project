package com.example.mam.Nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mam.ViewModel.SignInViewModel
import com.example.mam.ViewModel.SignUpViewModel

@Composable
fun SignInSignUpNavHost(
    modifier: Modifier = Modifier,
    signInVM: SignInViewModel = viewModel(),
    signUpVM: SignUpViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
    startDestination: String = SignInSignUpScreen.Login.name,
){

}