package com.example.spotifysearch.ui.groupie

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.xwray.groupie.viewbinding.BindableItem

abstract class DataBindingListItem<B : ViewDataBinding>(@LayoutRes val layoutResourceId: Int) :
    BindableItem<B>() {

    private var _binding: B? = null
    val context: Context get() = _binding!!.root.context

    override fun bind(viewBinding: B, position: Int) {
        _binding = viewBinding
        viewBinding.onBind(position)
        viewBinding.executePendingBindings()
    }

    abstract fun B.onBind(position: Int)

    final override fun getLayout(): Int = layoutResourceId

    override fun initializeViewBinding(view: View): B {
        return DataBindingUtil.bind<B>(view)!!
    }
}