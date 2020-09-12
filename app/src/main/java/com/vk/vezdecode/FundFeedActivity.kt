package com.vk.vezdecode

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.vk.vezdecode.vo.FundView
import kotlinx.android.synthetic.main.fund_feed_activity.*
import kotlinx.android.synthetic.main.snippet.*
import java.util.*

class FundFeedActivity : AppCompatActivity() {

    private lateinit var fundView: FundView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fund_feed_activity)

        fundView = ApplicationState.FundCreationState.fundView ?: throw IllegalStateException()

        authorName.text = fundView.author

        nameTextView.text = fundView.name

        if (fundView.image != null) {
            thumbnailImageView.setImageBitmap(fundView.image)
        }

        val timeUntilString = fundView.fundEndsDate?.let { Utils.datesToDifferenceText(Date(), it) }
        shortDescriptionTextView.text = if (timeUntilString == null) {
            fundView.author
        } else {
            resources.getString(R.string.snippet_short_description_format)
                .format(fundView.author, timeUntilString)
        }

        fundedTextView.text = resources.getString(R.string.snippet_funded_format).format(Constants.RUSSIAN_PRICE_FORMAT.format(ApplicationState.CurrentFundState.currentFundMoney), Constants.RUSSIAN_PRICE_FORMAT.format(fundView.price))

        thumbnailImageView.setOnClickListener(startFundPostActivityOnClickViewListener())
        nameTextView.setOnClickListener(startFundPostActivityOnClickViewListener())
        shortDescriptionTextView.setOnClickListener(startFundPostActivityOnClickViewListener())
        helpButton.setOnClickListener(startFundPostActivityOnClickViewListener())
    }

    private fun startFundPostActivityOnClickViewListener() = View.OnClickListener {
        val intent = Intent(this, FundPostActivity::class.java)
        startActivity(intent)
    }
}