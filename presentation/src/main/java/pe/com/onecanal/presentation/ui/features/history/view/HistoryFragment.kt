package pe.com.onecanal.presentation.ui.features.history.view

import android.annotation.SuppressLint
import android.view.View
import android.widget.AbsListView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pe.com.onecanal.BR
import pe.com.onecanal.R
import pe.com.onecanal.databinding.FragmentHistoryBinding
import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.entity.HistoryItem
import pe.com.onecanal.domain.entity.HistoryPagination
import pe.com.onecanal.presentation.ui.base.BaseFragmentWithViewModel
import pe.com.onecanal.presentation.ui.dialogs.MessageDialogType
import pe.com.onecanal.presentation.ui.features.history.adapter.HistoryAdapter
import pe.com.onecanal.presentation.ui.features.history.dialogs.HistoryDetailDialog
import pe.com.onecanal.presentation.ui.features.history.intent.HistoryIntent
import pe.com.onecanal.presentation.ui.features.history.viewModel.HistoryViewModel
import pe.com.onecanal.presentation.ui.features.mainDrawer.view.MainDrawerActivity
import pe.com.onecanal.presentation.ui.features.history.intent.HistoryIntentConfig

@AndroidEntryPoint
class HistoryFragment : BaseFragmentWithViewModel<FragmentHistoryBinding, HistoryViewModel>(),
    HistoryIntentConfig.IntentCallback {
    private var historyItems = mutableListOf<HistoryItem>()
    private lateinit var lastPagination: HistoryPagination
    private lateinit var adapter: HistoryAdapter
    private var isScrolling = false
    private val isLastItemReached = false
    override fun onCreateView(view: View) {
        binding.apply {
            rvHistory.layoutManager = LinearLayoutManager(requireContext())
            adapter = HistoryAdapter(historyItems, requireContext(), onItemClicked = {
               HistoryDetailDialog(it).show(parentFragmentManager,"HistoryDetailDialog")
            })
            rvHistory.adapter = adapter
            myTopBar.tvTitle.text = resources.getString(R.string.history)
            myTopBar.btnBack.setOnClickListener {
                findNavController().popBackStack()
            }

            rvHistory.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                        isScrolling = true
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    //                            if (!userFiltered) {
                    val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                    val firstVisibleItemPosition =
                        linearLayoutManager!!.findFirstVisibleItemPosition()
                    val visibleItemCount = linearLayoutManager.childCount
                    val totalItemCount = linearLayoutManager.itemCount
                    if (isScrolling && firstVisibleItemPosition + visibleItemCount == totalItemCount && !isLastItemReached) {
                        isScrolling = false
                        if (lastPagination.meta.currentPage != lastPagination.meta.lastPage)
                            getHistoryData(lastPagination.meta.lastPage)

                    }
                }
            })
        }
        observeUserIntentStates()

        getUserSessionData()
        getHistoryData(1)
    }

    private fun getHistoryData(page: Int) {
        lifecycleScope.launch {
            viewModel.userIntent.send(HistoryIntent.GetHistory(page))
        }
    }

    private fun observeUserIntentStates() {
        lifecycleScope.launch {
            viewModel.intentState.collect {
                HistoryIntentConfig.instance(this@HistoryFragment).collect(it)
            }
        }
    }

    private fun getUserSessionData() {
        lifecycleScope.launch {
            viewModel.userIntent.send(HistoryIntent.GetUserSession)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onHistoryFetched(historyPagination: HistoryPagination) {
        closeLoadingDialog()
        this.historyItems.addAll(historyPagination.items)
        adapter.let { it.notifyDataSetChanged() }
        this.lastPagination = historyPagination
    }

    override fun onLoading() {
        showLoadingDialog()
    }

    override fun onError(error: Failure) {
        closeLoadingDialog()
        handleUseCaseFailureFromBase(error).message?.let {
            showMessageDialog(
                MessageDialogType.Error,
                it
            )
        }
    }

    override val configureViewModelBindingVariable: Int = BR.historyViewModel

    override fun configureDataBinding() = FragmentHistoryBinding.inflate(layoutInflater)

    override fun configureViewModel(): HistoryViewModel {
        val viewModel: HistoryViewModel by viewModels()
        return viewModel
    }

    override fun onResume() {
        super.onResume()
        //HIDE THE TOOLBAR ACCORDING TO UI REQUIREMENTS
        (activity as MainDrawerActivity).showTopBar(false)
    }
}
