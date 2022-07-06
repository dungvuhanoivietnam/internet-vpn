package com.example.wise_memory_optimizer.ui.menu.ui

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.*
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.wise_memory_optimizer.MainActivity
import com.example.wise_memory_optimizer.R
import com.example.wise_memory_optimizer.custom.ToolbarCustomer
import com.example.wise_memory_optimizer.databinding.FragmentLanguageBinding
import com.example.wise_memory_optimizer.ui.battery.PreferenceUtil
import com.example.wise_memory_optimizer.ui.menu.adapter.ChangeLanguageAdapter
import com.example.wise_memory_optimizer.ui.menu.adapter.Language
import java.util.*

class LanguageFragment() : Fragment(R.layout.fragment_language), Parcelable {
    private var _binding: FragmentLanguageBinding? = null

    private val binding get() = _binding!!
    private val lstLanguage = ArrayList<Language>()
    private var adapter: ChangeLanguageAdapter? = null
    private var position : Int = -1
    private var lang : Language?= null
    private var firsTimeInApp = false

    constructor(parcel: Parcel) : this() {
        position = parcel.readInt()
    }

    constructor(arg1: Boolean) : this() {
        firsTimeInApp = arg1;
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLanguageBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()

        val language = PreferenceUtil.getString(requireContext(),PreferenceUtil.SETTING_ENGLISH,"")
        if (language.isNotEmpty()){
            for (i in 0 until lstLanguage.size){
                if (language == lstLanguage[i].value){
                    position = i
                    lang = lstLanguage[position]
                    lang?.selected = true
                    break
                }
            }
        }

        lang?.let {
            lstLanguage[position] = it
            binding.cbb.text = lang?.language
            adapter?.updateData(lstLanguage)
        }

        binding.toolbar.setListener(object : ToolbarCustomer.Listener {
            override fun onClick() {
                findNavController().popBackStack()
            }
        })
        if (!firsTimeInApp) {
            binding.save.setOnClickListener {
                PreferenceUtil.saveString(
                    requireContext(),
                    PreferenceUtil.SETTING_ENGLISH,
                    lang?.value
                )
                Handler(Looper.getMainLooper()).postDelayed({
                    /* Create an Intent that will start the Menu-Activity. */
                    activity?.finish()
                    startActivity(Intent(activity, MainActivity::class.java))
                }, 500)

            }
        }
    }

    private fun loadEnglish(){
        val language = PreferenceUtil.getString(requireContext(), PreferenceUtil.SETTING_ENGLISH,"")

        val res: Resources = requireContext().resources
        val dm: DisplayMetrics = res.displayMetrics
        val conf: Configuration = res.configuration
        conf.setLocale(Locale(language)) // API 17+ only.
        res.updateConfiguration(conf, dm)
    }

    private fun initData() {
        //set ui first time in app
        if (firsTimeInApp){
            binding.toolbar.visibility = View.GONE
            binding.llFirstTime.visibility = View.VISIBLE
            binding.save.setText(R.string.next)
            binding.save.setOnClickListener {
                PreferenceUtil.saveString(
                    requireContext(),
                    PreferenceUtil.SETTING_ENGLISH,
                    lang?.value
                )
                activity?.onBackPressed()
//                Handler(Looper.getMainLooper()).postDelayed({
//                    /* Create an Intent that will start the Menu-Activity. */
//                    activity?.supportFragmentManager?.popBackStack()
//                }, 500)

            }
        }

        lstLanguage.add(Language(getString(R.string.txt_language_en), false, "en"))
        lstLanguage.add(Language(getString(R.string.txt_language_chi), false, "zh"))
        lstLanguage.add(Language(getString(R.string.txt_language_ja), false, "ja"))
        lstLanguage.add(Language(getString(R.string.txt_language_vi), false, "vi"))
        lstLanguage.add(Language(getString(R.string.txt_language_spanish), false, "es"))
        lstLanguage.add(Language(getString(R.string.txt_language_portuguese), false, "pt"))
        lstLanguage.add(Language(getString(R.string.txt_language_russiane), false, "ru"))
        lstLanguage.add(Language(getString(R.string.txt_language_korean), false, "ko"))
        lstLanguage.add(Language(getString(R.string.txt_language_french), false, "fr"))
        lstLanguage.add(Language(getString(R.string.txt_language_german), false, "de"))
        lstLanguage.add(Language(getString(R.string.txt_language_arabic), false, "ar"))
        adapter = ChangeLanguageAdapter(requireContext()) {
            lang = it
            binding.cbb.text = it.language
        }
        adapter?.updateData(lstLanguage)
        binding.rclEnglish.adapter = adapter
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(position)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LanguageFragment> {
        override fun createFromParcel(parcel: Parcel): LanguageFragment {
            return LanguageFragment(parcel)
        }

        override fun newArray(size: Int): Array<LanguageFragment?> {
            return arrayOfNulls(size)
        }
    }
}