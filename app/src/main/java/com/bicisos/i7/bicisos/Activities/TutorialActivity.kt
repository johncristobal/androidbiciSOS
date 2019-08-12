package com.bicisos.i7.bicisos.Activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.VideoView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bicisos.i7.bicisos.R
import kotlinx.android.synthetic.main.activity_tutorial.*

class TutorialActivity : AppCompatActivity() {

    //private var viewPager: ViewPager? = null
    private var myViewPagerAdapter: MyViewPagerAdapter? = null
    private var layouts: IntArray? = null
    //private var btnNext: Button? = null
    var video: VideoView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)

        layouts = intArrayOf(
            R.layout.welcome_slide1,
            R.layout.welcome_slide2,
            R.layout.welcome_slide3,
            R.layout.welcome_slide4,
            R.layout.welcome_slide5,
            R.layout.welcome_slide6
        )

        myViewPagerAdapter = MyViewPagerAdapter(this)
        view_pager.setAdapter(myViewPagerAdapter)
        view_pager.addOnPageChangeListener( object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                addBottomDots(position)

                val uri = "android.resource://$packageName/"
                // changing the next button text 'NEXT' / 'GOT IT'
                if (position == layouts!!.size - 1) {
                    btnNext.setVisibility(View.VISIBLE)
                } else {
                    btnNext.setVisibility(View.INVISIBLE)
                }
            }
        })

        btnNext.setOnClickListener {
            val prefs = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
            val sesion = prefs.getString("tutorial","null")
            if (sesion!!.equals("1")){
                finish()
            }else{
                prefs.edit().putString("tutorial","1").apply()
                val intent = Intent(applicationContext,PrincipalActivity::class.java)
                startActivity(intent)
            }
        }

        btn_next.setOnClickListener {
            val prefs = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
            val sesion = prefs.getString("tutorial","null")
            if (sesion!!.equals("1")){
                finish()
            }else{
                prefs.edit().putString("tutorial","1").apply()
                val intent = Intent(applicationContext,PrincipalActivity::class.java)
                startActivity(intent)
            }
        }

        addBottomDots(0)
        view_pager.setCurrentItem(1)
        view_pager.setCurrentItem(0)
    }

    private fun addBottomDots(currentPage: Int) {
        val dots = ArrayList<TextView>()

        val colorsActive = resources.getIntArray(R.array.array_dot_active)
        val colorsInactive = resources.getIntArray(R.array.array_dot_inactive)

        layoutDots!!.removeAllViews()

        for (i in 0..5) {

            var textTemp = TextView(this)
            textTemp.setText(Html.fromHtml("&#8226;"))
            textTemp.setTextSize(35f)
            textTemp.setTextColor(colorsInactive[currentPage])


            dots.add(textTemp)
            layoutDots.addView(textTemp)
        }

        if (dots.size > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage])
    }

    inner class MyViewPagerAdapter(internal var c: Context) : PagerAdapter() {
        private var layoutInflater: LayoutInflater? = null
        internal var resource: String? = null
        internal var flag: String? = null

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            //            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layoutInflater = LayoutInflater.from(c)
            //            View view = layoutInflater.inflate(layouts[position], container, false);
            val view = layoutInflater!!.inflate(layouts!![position], container, false) as ViewGroup

            try {
                container.addView(view)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return view
        }

        override fun getCount(): Int {
            return layouts!!.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view === obj
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
        }
    }
}
