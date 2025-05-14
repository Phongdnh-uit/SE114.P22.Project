package com.example.mam.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.mam.gui.screen.authorization.ForgetPasswordScreen
import com.example.mam.gui.screen.authorization.OTPScreen
import com.example.mam.gui.screen.authorization.SignInScreen
import com.example.mam.gui.screen.authorization.SignUpScreen
import com.example.mam.gui.screen.authorization.StartScreen
import com.example.mam.gui.screen.client.CartScreen
import com.example.mam.gui.screen.client.CheckOutScreen
import com.example.mam.gui.screen.client.HomeScreen
import com.example.mam.gui.screen.client.ItemScreen
import com.example.mam.gui.screen.client.MapScreen
import com.example.mam.gui.screen.client.OrderScreen
import com.example.mam.gui.screen.client.SearchScreen
import com.example.mam.gui.screen.management.DashboardScreen
import com.example.mam.gui.screen.management.DashboardScreenPreview
import com.example.mam.gui.screen.management.ListCategoryScreen
import com.example.mam.viewmodel.authorization.ForgetPasswordViewModel
import com.example.mam.viewmodel.authorization.SignInViewModel
import com.example.mam.viewmodel.authorization.SignUpViewModel
import com.example.mam.viewmodel.client.CartViewModel
import com.example.mam.viewmodel.client.CheckOutViewModel
import com.example.mam.viewmodel.client.HomeScreenViewModel
import com.example.mam.viewmodel.client.ItemViewModel
import com.example.mam.viewmodel.client.OrderViewModel
import com.example.mam.viewmodel.client.SearchViewModel
import com.example.mam.viewmodel.management.DashboardViewModel
import com.example.mam.viewmodel.management.ListCategoryViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "Authorization",
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        navigation(
            startDestination = AuthorizationScreen.Start.name,
            route = "Authorization"
        ) {
            composable(
                route = AuthorizationScreen.Start.name,
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000),
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f) },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
            ) { backStackEntry ->
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

            composable(
                route = AuthorizationScreen.SignIn.name,
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )

                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
            ) { backStackEntry ->
                val signInVM: SignInViewModel = viewModel(backStackEntry)
                SignInScreen(
                    onSignInClicked = {
                        if (signInVM.signInState.value.username == "1" || signInVM.signInState.value.password == "1") {
                            navController.navigate(route = "Home") {
                                popUpTo("Authorization") { inclusive = true }
                            }
                        }
                        else if (signInVM.signInState.value.username == "2" || signInVM.signInState.value.password == "2") {
                        navController.navigate(route = "Dashboard") {
                            popUpTo("Authorization") { inclusive = true }
                        }
                    }

//                        CoroutineScope(backStackEntry.lifecycle.coroutineScope.coroutineContext).launch {
//                            val result = signInVM.SignIn()
//                            when (result) {
//                                0 -> signInVM.notifySignInFalse()
//                                1 -> {} // Thành công, xử lý logic ở đây
//                            }
//                        }
                    },
                    onForgotClicked = {
                        navController.navigate(AuthorizationScreen.ForgetPW.name)
                    },
                    onBackClicked = {
                        navController.popBackStack()
                    },
                )
            }

            composable(
                route = AuthorizationScreen.SignUp.name,
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )

                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
            ) { backStackEntry ->
                val signUpVM: SignUpViewModel = viewModel(backStackEntry)
                SignUpScreen(
                    onSignUpClicked = {
                        CoroutineScope(backStackEntry.lifecycle.coroutineScope.coroutineContext).launch {
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

            composable(
                route = AuthorizationScreen.ForgetPW.name,
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )

                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
            ) { backStackEntry ->
                val forgetPasswordVM: ForgetPasswordViewModel = viewModel(backStackEntry)
                ForgetPasswordScreen(
                    onChangeClicked = {

                    },
                    onCloseClicked = {
                        navController.popBackStack()
                    },
                )
            }
            composable(
                route = AuthorizationScreen.OTP.name,
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )

                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
            ) {
            OTPScreen(
                onVerifyClicked = {

                },
                onCloseClicked = {
                    navController.popBackStack()
                }
            )
            }
        }
        navigation(
            startDestination = HomeScreen.HomeSreen.name,
            route = "Home"
        ){
            composable(
                route = HomeScreen.HomeSreen.name,
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )

                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
            ) { backStackEntry ->
                val viewmodel: HomeScreenViewModel = viewModel(backStackEntry)
                HomeScreen(
                    onItemClicked = { item ->
                        navController.navigate("Details/${item.id}")
                    },
                    onSearchClicked = {navController.navigate(HomeScreen.Search.name)},
                    onCartClicked = { navController.navigate("Cart") },
                    onShippingClicked = {navController.navigate("Order")},
                    viewmodel = viewmodel
                )
            }
            composable(
                route = "Details/{itemId}",
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )

                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
            ) {backStackEntry ->
                val viewModel: ItemViewModel = viewModel(backStackEntry)
                ItemScreen(
                    onBackClicked = {navController.popBackStack()},
                    onAddClick = {
                        viewModel.addToCart()
                        navController.popBackStack() },
                    onCartClicked = {navController.navigate("Cart")},
                    viewModel = viewModel
                )
            }
            composable(
                route = HomeScreen.Search.name,
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )

                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
            ) {backStackEntry ->
                val viewModel: SearchViewModel = viewModel(backStackEntry)
                SearchScreen(
                    onItemClicked = { item ->
                        navController.navigate("Details/${item.id}")
                    },
                    onBackClicked = {navController.popBackStack()},
                    viewModel = viewModel
                )
            }
            composable(
                route = "Cart",
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )

                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
            ) { backStackEntry ->
                val viewModel: CartViewModel = viewModel(backStackEntry)
                CartScreen(
                    onBackClicked = {navController.popBackStack()},
                    onAdditionalProductClicked = { item ->
                        navController.navigate("Details/${item}")
                    },
                    onCheckOutClicked = {navController.navigate(("CheckOut"))},
                    viewModel = viewModel
                )
            }
            composable(
                route = "CheckOut",
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )

                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
            ) { backStackEntry ->
                val viewModel: CheckOutViewModel = viewModel(backStackEntry)
                CheckOutScreen(
                    onBackClicked = {navController.popBackStack()},
                    onCheckOutClicked = {
                        //order
                        navController.navigate("Order") },
                    onChangeAddressClicked = {
                        navController.navigate("Address")
                        // get address for saveStateHandle
                        navController.previousBackStackEntry?.savedStateHandle?.getLiveData<String>("address")?.observe(
                            backStackEntry
                        ) { address ->
                            viewModel.address = address
                        }
                    },
                    viewModel = viewModel
                )
            }
            composable(
                route = "Address",
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )

                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
            ){
                MapScreen(
                    onBackClicked = {navController.popBackStack()},
                    onSelectClicked = { address ->
                        navController.previousBackStackEntry?.savedStateHandle?.set("address", address)
                        navController.popBackStack()
                    },
                )
            }
            composable(
                route = "Order",
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
            ){ backStackEntry ->
                val viewModel: OrderViewModel = viewModel(backStackEntry)
                OrderScreen(
                    onBackClicked = {navController.popBackStack()},
                    onVerifyClicked = {},
                    viewModel = viewModel
                )
            }
            composable(
                route = "Dashboard",
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
            ){ backStackEntry ->
                val viewModel: DashboardViewModel = viewModel(backStackEntry)
                DashboardScreen(
                    onBackClicked = {
                        navController.navigate(route = "Authorization") {
                        popUpTo("Dashboard") { inclusive = true }
                        }
                    },
                    onItemClicked = { item ->
                        when(item) {
                            "Danh mục" -> navController.navigate("ListCategory")
                            "Sản phẩm" -> navController.navigate("Details/${item}")
                            "Người dùng" -> navController.navigate("Details/${item}")
                            "Shipper" -> navController.navigate("Details/${item}")
                            "Thông báo" -> navController.navigate("Details/${item}")
                            "Khuyến mãi" -> navController.navigate("Details/${item}")
                            "Đơn hàng" -> navController.navigate("Details/${item}")
                            "Thống kê" -> {}
                        }
                    },
                    onActiveOrderClicked = { order -> },
                    viewModel = viewModel
                )
            }
            composable(
                route = "ListCategory",
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
            ) { backStackEntry ->
                val viewModel: ListCategoryViewModel = viewModel(backStackEntry)
                ListCategoryScreen(
                    onBackClick = {navController.popBackStack()},
                    onAddCategoryClick = {
                        navController.navigate("AddCategory")
                    },
                    onEditCategoryClick = { category ->
                        navController.navigate("EditCategory/${category}")
                    },
                    viewModel = viewModel
                )
            }
        }

    }
}