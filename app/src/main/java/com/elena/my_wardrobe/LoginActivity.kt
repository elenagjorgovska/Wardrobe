package com.elena.my_wardrobe


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.elena.mywardrobe.databinding.FragmentLoginBinding
import com.elena.my_wardrobe.ui.dashboard.AddViewModel
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging


class LoginActivity : AppCompatActivity() {

    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    // [END declare_auth]
    private lateinit var binding: FragmentLoginBinding

    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var callbackManager: CallbackManager


    public override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding = FragmentLoginBinding.inflate(layoutInflater)
        FacebookSdk.sdkInitialize(this)
        setContentView(binding.root)
//        println("Facebook hash key: ${FacebookSdk.getApplicationSignature(this)}")


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(com.elena.mywardrobe.R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        callbackManager = CallbackManager.Factory.create()


        supportActionBar?.hide()

        auth = Firebase.auth

        val dashboardViewModel =
            ViewModelProvider(this).get(AddViewModel::class.java)

        binding.localeEn.setOnClickListener{
            setLocale("en-US")
        }
        binding.localeMk.setOnClickListener{
            setLocale("mk-MK")
        }
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        var email : String = ""
        binding.email.addTextChangedListener (object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                email = s.toString()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        var password : String = ""
        binding.password.addTextChangedListener (object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                password = s.toString()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })



        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
//            val msg = getString(R.string.msg_token_fmt, token)
            Log.d("Token", "Token:$token")
//            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })


        binding.loginButton.setOnClickListener {
            if(email.length > 6 && password.length>=6) {
                val sharedPreferences = getSharedPreferences("LoginCredentials", MODE_PRIVATE)
                val myEdit = sharedPreferences.edit()

                if (binding.rememberMe.isChecked) {
                    myEdit.putString("email", email)
                    myEdit.putString("password", password)
                    myEdit.apply()
                }


                signIn(email, password)
            }else{
                Toast.makeText(this, "Enter valid login details", Toast.LENGTH_SHORT).show()
            }

        }

        val sh = getSharedPreferences("LoginCredentials", MODE_PRIVATE)
        val s1 = sh.getString("email", "")
        val s2 = sh.getString("password", "")

        if (s1 != "" && s2 != "")
        {
            signIn(s1!!,s2!!)
        }

        binding.loginGoogle.setOnClickListener {signInGoogle()}
        binding.loginFacebook.setOnClickListener {LoginManager.getInstance().logInWithReadPermissions(this, listOf("email", "public_profile"))
        LoginManager.getInstance().registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    Log.d(TAGFacebook, "facebook:onSuccess:$loginResult")
                    handleFacebookAccessToken(loginResult.accessToken)
                }

                override fun onCancel() {
                    Log.d(TAGFacebook, "facebook:onCancel")
                }

                override fun onError(error: FacebookException) {
                    Log.d(TAGFacebook, "facebook:onError", error)
                }
            },)}

        binding.loginGuest.setOnClickListener {signInAnonymously()}


    }

    public override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAGGoogle, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAGGoogle, "Google sign in failed", e)
            }
        }else
        {
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }

    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAGGoogle, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAGGoogle, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }



    private fun createAccount(email: String, password: String) {
        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    updateUI(null)
                }
            }
       }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("AUTH",  auth!!.getAccessToken(true).toString())
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    updateUI(null)
                }
            }
       }

    private fun sendEmailVerification() {
        // [START send_email_verification]
        val user = auth.currentUser!!
        user.sendEmailVerification()
            .addOnCompleteListener(this) { task ->
                // Email Verification sent
            }
         }

    private fun updateUI(user: FirebaseUser?) {

        if(user !=null){
            var firebaseAnalytics: FirebaseAnalytics = Firebase.analytics
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.METHOD, "login")
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle)

            GlobalData.firebaseUser = user
            val intent: Intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
        }

    }

    private fun reload() {
    }

    companion object {
        private const val TAG = "EmailPassword"
        private const val RC_SIGN_IN = 9001
        private const val TAGGoogle = "GoogleActivity"
        private const val TAGFacebook = "FacebookLogin"
        private const val TAGAno = "AnonymousAuth"
    }

    private fun setLocale(locale : String){
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(locale)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }


    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAGFacebook, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAGFacebook, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAGFacebook, "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    updateUI(null)
                }
            }
    }

    private fun signInAnonymously() {
        // [START signin_anonymously]
        auth.signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAGAno, "signInAnonymously:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAGAno, "signInAnonymously:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    updateUI(null)
                }
            }
    }

        private fun linkAccount() {
            val credential = EmailAuthProvider.getCredential("", "")
            // [START link_credential]
            auth.currentUser!!.linkWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAGAno, "linkWithCredential:success")
                        val user = task.result?.user
                        updateUI(user)
                    } else {
                        Log.w(TAGAno,"linkWithCredential:failure",task.exception)
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                        updateUI(null)
                    }
                }
        }
}