package ru.ikom.basecomponent

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import ru.ikom.basecomponent.example.MainFragment

class MainActivity : AppCompatActivity() {

    private val fragmentFactory = FragmentFactoryImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        supportFragmentManager.fragmentFactory = fragmentFactory

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.content, fragmentFactory.mainFragment())
                addToBackStack(null)
            }
        }
    }

    private fun goBack() {
        supportFragmentManager.popBackStack()
    }

    private inner class FragmentFactoryImpl : FragmentFactory() {
        override fun instantiate(classLoader: ClassLoader, className: String): Fragment =
            when (loadFragmentClass(classLoader, className)) {
                MainFragment::class.java -> mainFragment()
                else -> throw NotImplementedError()
            }

        fun mainFragment() = MainFragment(onBack = ::goBack)
    }
}