package com.kadmiv.newsapi.activity_main


import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.kadmiv.lesson_6.abstractions.fragments.UniversalDialog
import com.kadmiv.newsapi.R
import com.kadmiv.newsapi.activity_main.adapter.NewsAdapter
import com.kadmiv.newsapi.activity_show_news.ActivityNews
import com.kadmiv.newsapi.activity_show_news.NEWS_URL
import com.kadmiv.newsapi.databinding.DialogErrorBinding
import com.kadmiv.newsapi.databinding.DialogNewsDescriptionBinding
import com.kadmiv.newsapi.repo.model.Article
import com.kadmiv.newsapi.utils.NewsDiffUtilCallback
import com.miguelcatalan.materialsearchview.MaterialSearchView
import kotlinx.android.synthetic.main.activity_main.*


interface IView {
    fun showTopNews(news: List<Article>)
    fun openSeparateNews(item: Article)
    fun showMoreDetails(item: Article)
    fun createInfoDialog()
    fun closeDialog()
    fun hasConnection(): Boolean
    fun showLoadingProcess(isNeedShow: Boolean)
}

const val OLD_NEWS_LIST = "OLD_NEWS_LIST"
const val OLD_NEWS_MANAGER = "OLD_NEWS_MANAGER"

class MainActivity : AppCompatActivity(), IView {


    var mPresenter: Presenter? = null
    private var adapter: NewsAdapter? = null
    var managerState: Parcelable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        mPresenter = Presenter(this)

        if (savedInstanceState != null) {
            mPresenter?.oldList = savedInstanceState.getParcelableArrayList<Article>(OLD_NEWS_LIST)
            managerState = savedInstanceState.getParcelable(OLD_NEWS_MANAGER)
        }
    }

    override fun onStart() {
        super.onStart()
        mPresenter?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mPresenter?.onStop()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_main_menu, menu)

        val item = menu.findItem(R.id.action_search)
        searchView.setMenuItem(item)

        searchView.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(queryText: String): Boolean {
                Log.d("12", queryText)
                mPresenter?.getQueryNews(queryText)
                searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })

        return true
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        var forSave = arrayListOf<Article>()
        forSave.addAll(adapter!!.mValues)
        outState?.putParcelableArrayList(OLD_NEWS_LIST, forSave)

        var manager = newsRecycler.layoutManager
        outState?.putParcelable(OLD_NEWS_MANAGER, manager?.onSaveInstanceState())
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.onDestroy()
        mPresenter = null
    }

    //Check internet connection
    override fun hasConnection(): Boolean {
        val cm = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activeNetworkInfo: NetworkInfo? = null
        activeNetworkInfo = cm.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
    }

    override fun createInfoDialog() {
        val inflater = LayoutInflater.from(this)

        val binding = DataBindingUtil.inflate<DialogErrorBinding>(
            inflater, R.layout.dialog_error,
            null, false
        )

        binding.listener = mPresenter

        showDialog(binding, false) { }
    }

    override fun showMoreDetails(item: Article) {
        Log.d("12", "showMoreDetails")
        val inflater = LayoutInflater.from(this)

        val binding = DataBindingUtil.inflate<DialogNewsDescriptionBinding>(
            inflater, R.layout.dialog_news_description,
            null, false
        )

        binding.model = item
        binding.listener = mPresenter

        showDialog(binding, true) { }
    }

    private lateinit var dialog: UniversalDialog

    private fun showDialog(binding: ViewDataBinding, isCancelable: Boolean, doOnCancel: () -> Unit) {
        UniversalDialog.binding = binding
        dialog = UniversalDialog()

        val fManager = supportFragmentManager.beginTransaction()
        val prev = supportFragmentManager.findFragmentByTag("dialog")
        if (prev != null) {
            fManager.remove(prev)
        }
        fManager.addToBackStack(null)

        dialog.isCancelable = isCancelable
        dialog.doOnCancel = doOnCancel
        dialog.show(fManager, "new_purchase_dialog")
    }

    override fun closeDialog() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss()
    }

    override fun showTopNews(data: List<Article>) {

        if (data.isEmpty()) {
            Toast.makeText(applicationContext, getString(R.string.nothing_found_by_request), Toast.LENGTH_LONG).show()
            return
        }

        // Check adapter
        if (adapter == null) {
            adapter = NewsAdapter(mPresenter!!)
            adapter?.mValues = data
            newsRecycler.adapter = adapter
        }

        // Find difference between data
        val diffUtilCallback = NewsDiffUtilCallback(adapter!!.mValues, data)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)
        adapter?.mValues = data
        diffResult.dispatchUpdatesTo(adapter!!)

        // Create layout and restore state(if need)
        if (newsRecycler.layoutManager == null) {
            newsRecycler.layoutManager = LinearLayoutManager(this)
            if (managerState != null)
                newsRecycler.layoutManager!!.onRestoreInstanceState(managerState)
        }
    }

    override fun openSeparateNews(item: Article) {
        Log.d("12", "openSeparateNews")
        var intent = Intent(applicationContext, ActivityNews::class.java)
        intent.putExtra(NEWS_URL, item.url)
        startActivity(intent)
    }


    override fun showLoadingProcess(needShow: Boolean) {
        if (needShow) {
            loadingProcess.visibility = View.VISIBLE

            val options = RequestOptions()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.DATA)

            Glide.with(loadingProcess)
                .load(R.raw.waiting_icon)
                .apply(options)
                .into(loadingProcess);
        } else {
            loadingProcess.visibility = View.GONE
        }
    }
}
