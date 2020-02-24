package com.sherepenko.android.launchiteasy.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.sherepenko.android.launchiteasy.R
import kotlinx.android.synthetic.main.fragment_splash.appMottoView
import kotlinx.coroutines.delay

class SplashFragment : BaseFragment(R.layout.fragment_splash) {

    companion object {
        private const val REQUEST_RUNTIME_PERMISSIONS = 101
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            delay(1000)
            checkPermissions()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_RUNTIME_PERMISSIONS -> {
                var permissionsGranted = permissions.size == grantResults.size

                if (permissionsGranted) {
                    for (grantResult in grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            permissionsGranted = false
                            break
                        }
                    }
                }

                if (permissionsGranted) {
                    navigateToHomeFragment()
                } else {
                    ActivityCompat.finishAffinity(requireActivity())
                }
            }
            else -> {
                // ignore
            }
        }
    }

    private fun checkPermissions() {
        val requestedPermissions = mutableListOf<String>()

        if (!Manifest.permission.ACCESS_COARSE_LOCATION.isGranted()) {
            requestedPermissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        if (requestedPermissions.isEmpty()) {
            navigateToHomeFragment()
        } else {
            requestPermissions(
                requestedPermissions.toTypedArray(),
                REQUEST_RUNTIME_PERMISSIONS
            )
        }
    }

    private fun navigateToHomeFragment() {
        findNavController().navigate(
            SplashFragmentDirections.toHomeFragment(),
            FragmentNavigatorExtras(
                appMottoView to getString(R.string.app_motto_transition)
            )
        )
    }

    private fun String.isGranted(): Boolean =
        ContextCompat.checkSelfPermission(requireActivity(), this@isGranted) ==
            PackageManager.PERMISSION_GRANTED
}
