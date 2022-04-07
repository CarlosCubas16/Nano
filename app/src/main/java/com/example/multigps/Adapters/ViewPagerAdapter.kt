package com.example.multigps.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.example.multigps.R

class ViewPagerAdapter(private  val context: Context) : PagerAdapter() {
    private var inflater: LayoutInflater? = null
    private val images = intArrayOf(
        R.drawable.p1,
        R.drawable.p2,
        R.drawable.p3,
    )

    private val titles = arrayOf(
        "GPS",
        "GPS",
        "GPS"
    )
    private val descs = arrayOf(
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
    )

    override fun getCount(): Int {
        return titles.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = inflater!!.inflate(R.layout.view_pager, container, false)
        val imageView = v.findViewById<ImageView>(R.id.imgViewPager)
        val textTitle = v.findViewById<TextView>(R.id.txtTitleViewPager)
        val textDesc = v.findViewById<TextView>(R.id.txtDescViewPager)
        imageView.setImageResource(images[position])
        textTitle.text = titles[position]
        textDesc.text = descs[position]
        container.addView(v)
        return v
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}