package com.makki.languageapp.views

import android.content.Context
import android.widget.LinearLayout
import android.widget.TextView
import com.makki.languageapp.R

/**
 * @author Maksym.Popovych
 */
class SubjectHeaderView(context: Context?) : LinearLayout(context) {

    private val title: TextView
    private val grade: TextView
    private val notices: TextView

    init {
        inflate(getContext(), R.layout.v_subject,this)

        title = findViewById(R.id.title)
        grade = findViewById(R.id.grade)
        notices = findViewById(R.id.notices)

        title.setText(R.string.grades_header_name)
        grade.setText(R.string.grades_header_grade)
        notices.setText(R.string.grades_header_notices)
    }
}