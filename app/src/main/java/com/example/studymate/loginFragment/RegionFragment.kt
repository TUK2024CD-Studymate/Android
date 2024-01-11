package com.example.studymate.loginFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import com.example.studymate.R
import com.example.studymate.databinding.FragmentRegionBinding

class RegionFragment : Fragment() {
    lateinit var binding: FragmentRegionBinding
    lateinit var currentRegion: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegionBinding.inflate(inflater, container, false)
        setupNumberPickerForStringValues()

        return binding.root
    }

    private fun setupNumberPickerForStringValues() {
        val list = getRegionList()
        binding.numberPicker1.also {
            currentRegion = list[0]
            it.minValue = 0
            it.maxValue = list.size - 1
            it.displayedValues = list
            it.wrapSelectorWheel = true
            it.setOnValueChangedListener { picker, oldVal, newVal ->
                currentRegion = list[newVal]
//                Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getRegionList(): Array<String> = arrayOf(
        getString(R.string.activityRegion_fragment_region1),
        getString(R.string.activityRegion_fragment_region2),
        getString(R.string.activityRegion_fragment_region3),
        getString(R.string.activityRegion_fragment_region4),
        getString(R.string.activityRegion_fragment_region5),
        getString(R.string.activityRegion_fragment_region6),
        getString(R.string.activityRegion_fragment_region7),
        getString(R.string.activityRegion_fragment_region8),
        getString(R.string.activityRegion_fragment_region9),
        getString(R.string.activityRegion_fragment_region10),
        getString(R.string.activityRegion_fragment_region11),
        getString(R.string.activityRegion_fragment_region12),
        getString(R.string.activityRegion_fragment_region13),
    )

}