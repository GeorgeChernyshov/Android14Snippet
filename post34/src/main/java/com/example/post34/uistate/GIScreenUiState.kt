package com.example.post34.uistate

data class GIScreenUiState(val option: GenderOption) {

    enum class GenderOption(val gender: String) {
        MALE("Male"), FEMALE("Female"), NEUTRAL("Neutral")
    }

    companion object {
        val radioOptions = listOf(GenderOption.MALE, GenderOption.FEMALE, GenderOption.NEUTRAL)

        val DEFAULT = GIScreenUiState(GenderOption.NEUTRAL)
    }
}