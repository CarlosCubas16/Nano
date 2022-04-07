package com.example.multigps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.example.multigps.Adapters.ViewPagerAdapter

class OnBoardActivity : AppCompatActivity() {
    private var viewPager: ViewPager? = null
    private var btnLeft: Button? = null
    private var btnRight: Button? = null
    private var adapter: ViewPagerAdapter? = null
    private var dotsLayout: LinearLayout? = null
    private lateinit var dots: Array<TextView?>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_board)
        init()
    }

    private fun init() {
        viewPager = findViewById(R.id.view_pager)
        btnLeft = findViewById(R.id.btnLeft)
        btnRight = findViewById(R.id.btnRight)
        dotsLayout = findViewById(R.id.dotsLayout)
        adapter = ViewPagerAdapter(this)
        addDots(0)

        viewPager?.addOnPageChangeListener(listener)
        viewPager?.setAdapter(adapter)
        btnRight!!.setOnClickListener(View.OnClickListener { v: View? ->
            if (btnRight!!.getText().toString() == "Siguiente") {
                viewPager?.setCurrentItem(viewPager!!.getCurrentItem() + 1)
            }
            else {
                val i = Intent(this@OnBoardActivity, AuthActivity::class.java).apply {
                    putExtra("localize", "auth")
                }
                startActivity(i)
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
                finish()
            }
        })

        btnLeft!!.setOnClickListener(View.OnClickListener { v: View? ->
            if (btnLeft!!.getText().toString() == "Saltar") {
                viewPager?.setCurrentItem(
                    viewPager!!.getCurrentItem() + 2
                )
            }
            else {
                viewPager?.setCurrentItem(viewPager!!.getCurrentItem() - 1)
            }
        })
    }

    private fun addDots(position: Int) {
        dotsLayout!!.removeAllViews()
        dots = arrayOfNulls(3)
        for (i in dots.indices) {
            dots[i] = TextView(this)
            dots[i]!!.text = Html.fromHtml("&#8226")
            dots[i]!!.textSize = 35f
            dots[i]!!.setTextColor(resources.getColor(R.color.teal_200))
            dotsLayout!!.addView(dots[i])
        }
        if (dots.size > 0) {
            dots[position]!!.setTextColor(resources.getColor(R.color.black))
        }
    }

    private val listener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            addDots(position)
            if (position == 0) {
                btnLeft!!.visibility = View.VISIBLE
                btnLeft!!.isEnabled = true
                btnLeft!!.text = "Saltar"
                btnRight!!.text = "Siguiente"
            } else if (position == 1) {
                btnLeft!!.visibility = View.VISIBLE
                btnLeft!!.isEnabled = true
                btnLeft!!.text = "Atras"
                btnRight!!.text = "Siguiente"
            } else {
                btnLeft!!.visibility = View.GONE
                btnLeft!!.isEnabled = true
                btnRight!!.text = "Finalizar"

                btnLeft!!.visibility = View.VISIBLE
                btnLeft!!.isEnabled = true
                btnLeft!!.text = "Atras"
            }
        }

        override fun onPageScrollStateChanged(state: Int) {}
    }
}