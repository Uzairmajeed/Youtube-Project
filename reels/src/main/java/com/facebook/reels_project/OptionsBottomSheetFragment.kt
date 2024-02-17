package com.facebook.reels_project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class OptionsBottomSheetFragment : BottomSheetDialogFragment() {

    interface OptionsClickListener {
        fun onCaptionClicked()
        fun onDescriptionClicked()
        fun onReportClicked()
    }

    private var optionsClickListener: OptionsClickListener? = null

    fun setOptionsClickListener(listener: OptionsClickListener) {
        optionsClickListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_options_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find your TextViews in the layout
        val captionTextView = view.findViewById<TextView>(R.id.captionTextView)
        val descriptionTextView = view.findViewById<TextView>(R.id.descriptionTextView)
        val reportTextView = view.findViewById<TextView>(R.id.reportTextView)

        // Set click listeners for each TextView
        captionTextView.setOnClickListener {
            optionsClickListener?.onCaptionClicked()
            dismiss()
        }

        descriptionTextView.setOnClickListener {
            optionsClickListener?.onDescriptionClicked()
            dismiss()
        }

        reportTextView.setOnClickListener {
            optionsClickListener?.onReportClicked()
            dismiss()
        }
    }
}
