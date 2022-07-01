package com.example.wise_memory_optimizer.utils

import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation

class NavigationUtils {

    companion object {
        fun navigate(view: View, action: Int, bundle: Bundle?) {
            Navigation.findNavController(view)
                .navigate(action, bundle)
        }

        fun back(view: View) {
            Navigation.findNavController(view).popBackStack()
        }
    }


}