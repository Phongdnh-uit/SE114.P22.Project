package com.example.mam.viewmodel.authentication.otp

sealed interface OtpAction {
    data class OnEnterCharacter(val number: String?, val index: Int) : OtpAction
    data class OnChangeFieldFocused(val index: Int): OtpAction
    data object OnKeyboardBack: OtpAction
}