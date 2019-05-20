package com.makki.languageapp.views

import android.annotation.SuppressLint
import android.content.Context
import android.widget.LinearLayout
import android.widget.TextView
import com.makki.basic.model.GradeAsset
import com.makki.languageapp.R


/**
 * @author Maksym.Popovych
 */
@SuppressLint("ViewConstructor")
class SubjectView(asset: GradeAsset, context: Context?) : LinearLayout(context) {

    private val title: TextView
    private val grade: TextView
    private val notices: TextView

    init {
        inflate(getContext(),R.layout.v_subject,this)

        title = findViewById(R.id.title)
        grade = findViewById(R.id.grade)
        notices = findViewById(R.id.notices)

        title.text = asset.subjectName
        grade.text = if (asset.value.isNotEmpty()) asset.value else "-"
        notices.text = if (asset.notices.isNotEmpty()) asset.notices else "-"
    }

}
