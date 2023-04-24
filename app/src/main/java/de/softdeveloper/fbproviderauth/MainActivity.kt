package de.softdeveloper.fbproviderauth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.softdeveloper.fbproviderauth.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    private val authProviderList: MutableList<AuthUI.IdpConfig>
    get() {
        val providers = ArrayList<AuthUI.IdpConfig>().apply {
            add(AuthUI.IdpConfig.GoogleBuilder().build())
        }
        return providers
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        user = auth.currentUser

        activityResultLauncher = registerForActivityResult(
            FirebaseAuthUIActivityResultContract()
        ){
            this.onSignInResult(it)
        }

        if(user != null){
            Toast.makeText(this, "User ist angemeldet", Toast.LENGTH_SHORT).show()
        }else{
            authenticate()
        }
    }

    private fun onSignInResult(result:FirebaseAuthUIAuthenticationResult){
        if(result.resultCode == RESULT_OK) {
            val user = auth.currentUser
            if(user!=null){
                Toast.makeText(this, "User ist angemeldet", Toast.LENGTH_SHORT).show()
            }else{
                authenticate()
            }
        }
    }

    private fun authenticate() {
        activityResultLauncher.launch(
            AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(authProviderList)
                .setLogo(R.drawable.ic_launcher_background)
                .setTheme(com.firebase.ui.auth.R.style.FirebaseUI_DefaultMaterialTheme)
                .setIsSmartLockEnabled(false)
                .build()
        )
    }
}