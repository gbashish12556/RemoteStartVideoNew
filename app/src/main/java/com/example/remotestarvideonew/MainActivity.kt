package com.example.remotestarvideonew

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.annotation.VisibleForTesting
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.test.espresso.IdlingResource
import butterknife.BindView
import com.facebook.shimmer.ShimmerFrameLayout
import java.util.ArrayList

class MainActivity : AppCompatActivity() , SwipeRefreshLayout.OnRefreshListener{

    private var mIdlingResource: RetrofitIdlingResource? = null
    private var viewModel: ViewModel? = null

    private var allVideos: ArrayList<Video>? = ArrayList()

    @BindView(R.id.recyclerView) lateinit var recyclerView: RecyclerView
    @BindView(R.id.shimmer_view_container) lateinit var shimmerFrameLayout: ShimmerFrameLayout
    @BindView(R.id.swipe_layout) lateinit var swipeRefreshLayout: SwipeRefreshLayout

    @BindView(R.id.errorOuterLayout) lateinit var errorOuterLayout: ConstraintLayout
    @BindView(R.id.retryButton) lateinit var retryButton: Button

    private var noteAdapter:ContactsRecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getIdlingResource()
        if(mIdlingResource != null) {
            mIdlingResource!!.setIdleState(false)
        }

        swipeRefreshLayout = findViewById(R.id.swipe_layout)
        swipeRefreshLayout!!.setOnRefreshListener(this);
        swipeRefreshLayout!!.visibility = View.GONE

        errorOuterLayout = findViewById(R.id.errorOuterLayout)
        retryButton = findViewById(R.id.retryButton)
        retryButton!!.setOnClickListener{

            fetchAgain(null)
        }

        shimmerFrameLayout = findViewById(R.id.shimmer_view_container)
        shimmerFrameLayout!!.startShimmerAnimation()

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        noteAdapter = ContactsRecyclerViewAdapter(allVideos!!,this )
        recyclerView!!.adapter = noteAdapter
        intialiseViewModel()


    }

    fun intialiseViewModel(){

        viewModel = ViewModelProviders.of(this).get(ViewModel::class.java)

        viewModel!!.apiStatus.observe(this, Observer { aBoolean ->
            if (aBoolean == false) {
                apiFailed()
            }
        })

        viewModel!!.videos.observe(this, Observer { videos ->
            Handler().postDelayed({
                dataRecived(videos.results!!)
            },1000)
        })

    }

    override fun onRefresh() {
        fetchAgain("no-cache")
    }

    fun apiFailed(){
        if(mIdlingResource == null) { shimmerFrameLayout!!.stopShimmerAnimation()
           swipeRefreshLayout!!.setRefreshing(false);
        }
        shimmerFrameLayout!!.visibility = View.GONE
        swipeRefreshLayout!!.visibility = View.GONE
        errorOuterLayout!!.visibility = View.VISIBLE
        mIdlingResource!!.setIdleState(true)
    }

    fun dataRecived(result:ArrayList<Video>){

        shimmerFrameLayout!!.stopShimmerAnimation()
        shimmerFrameLayout!!.visibility = View.GONE
        swipeRefreshLayout!!.setRefreshing(false);
        swipeRefreshLayout!!.visibility = View.VISIBLE
        mIdlingResource!!.setIdleState(true)
        noteAdapter!!.setData(result)

    }

    fun fetchAgain(cahceControl:String?){
        mIdlingResource!!.setIdleState(false)
        swipeRefreshLayout!!.visibility = View.GONE
        shimmerFrameLayout!!.visibility = View.VISIBLE
        shimmerFrameLayout!!.startShimmerAnimation()
        errorOuterLayout!!.visibility = View.GONE
        viewModel!!.fetchVideos(cahceControl)
    }

    @VisibleForTesting
    @NonNull
    fun getIdlingResource(): IdlingResource {
        if (mIdlingResource == null) {
            mIdlingResource = RetrofitIdlingResource()
        }
        return mIdlingResource!!
    }

}
