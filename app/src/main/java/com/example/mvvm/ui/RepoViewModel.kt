package com.example.mvvm.ui

import android.content.Context
import android.net.ConnectivityManager
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.mvvm.repositories.RepoRepository
import dagger.hilt.android.qualifiers.ApplicationContext

class RepoViewModel @ViewModelInject constructor(
    private val repository: RepoRepository,
    @ApplicationContext app: Context
) :
    ViewModel() {
    val net = haveNetworkConnection(app)
    val repos = repository.getRepos(net).cachedIn(viewModelScope)


    fun haveNetworkConnection(context: Context): Boolean {
        var haveConnectedWifi = false
        var haveConnectedMobile = false
        val cm =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val netInfo = cm!!.allNetworkInfo
        for (ni in netInfo) {
            if (ni.typeName
                    .equals("WIFI", ignoreCase = true)
            ) if (ni.isConnected) haveConnectedWifi = true
            if (ni.typeName
                    .equals("MOBILE", ignoreCase = true)
            ) if (ni.isConnected) haveConnectedMobile = true
        }
        return haveConnectedWifi || haveConnectedMobile
    }

}

