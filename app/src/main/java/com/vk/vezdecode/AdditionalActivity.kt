package com.vk.vezdecode

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.vk.vezdecode.model.FundEndsCondition
import com.vk.vezdecode.vo.FundView
import kotlinx.android.synthetic.main.additional_activity.*
import kotlinx.android.synthetic.main.additional_activity.buttonNext
import kotlinx.android.synthetic.main.fund_info_edit_activity.*
import java.lang.IllegalStateException
import java.util.*


class AdditionalActivity : AppCompatActivity() {

    private lateinit var fundView: FundView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.additional_activity)

        fundView = ApplicationState.FundCreationState.fundView ?: throw IllegalStateException()

        toolbar.setNavigationOnClickListener(View.OnClickListener {
            finish()
        })
    }

    fun onFundCreateButtonClicked(view: View) {
        val intent = Intent(this, FundTypeChooseActivity::class.java)
        startActivity(intent)
    }

    fun onDateButtonClicked(view: View) {
        val c = Calendar.getInstance()
        val mYear = c[Calendar.YEAR]
        val mMonth = c[Calendar.MONTH]
        val mDay = c[Calendar.DAY_OF_MONTH]

        val datePickerDialog = DatePickerDialog(
            this,
            { view_, year, monthOfYear, dayOfMonth ->
                setDateAndEnableButton(year, monthOfYear, dayOfMonth)
            },
            mYear,
            mMonth,
            mDay
        )
        datePickerDialog.show()
    }

    private fun setDateAndEnableButton(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val datStr = if (dayOfMonth > 9) dayOfMonth.toString() else "0${dayOfMonth}"
        val monthStr = if (monthOfYear > 8) (monthOfYear + 1).toString() else "0${monthOfYear + 1}"
        dateTextView.text = "${datStr}.${monthStr}.${year}"
        buttonNext.isEnabled = true
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            fundView.fundEndsCondition = when (view.getId()) {
                R.id.radioEndsOnSumFunded -> FundEndsCondition.ON_SUM_FUNDED
                R.id.radioEndsAtExactDate -> FundEndsCondition.AT_EXACT_DATE
                else -> throw IllegalStateException()
            }

            // Check which radio button was clicked
            val checked = view.isChecked
            if (checked) {
                when (fundView.fundEndsCondition) {
                    FundEndsCondition.ON_SUM_FUNDED -> {
                        calendarDateLayout.visibility = View.INVISIBLE
                        buttonNext.isEnabled = true
                    }

                    FundEndsCondition.AT_EXACT_DATE -> {
                        calendarDateLayout.visibility = View.VISIBLE
                        buttonNext.isEnabled = dateTextView.text.isNotEmpty()
                    }
                }
            }
        }
    }
}
