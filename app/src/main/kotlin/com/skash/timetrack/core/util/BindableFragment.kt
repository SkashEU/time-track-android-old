package com.skash.timetrack.core.util

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.skash.timetrack.core.helper.dialog.FullscreenDialogFragment


abstract class BindableFragment<T : ViewBinding>(
    @LayoutRes contentLayoutId: Int
) : Fragment(contentLayoutId) {

    private var _binding: T? = null
    protected val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = createBindingInstance(view)
        bindActions()
    }

    abstract fun createBindingInstance(view: View): T

    abstract fun bindActions()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

abstract class BindableFullscreenDialog<T : ViewBinding>(
    @LayoutRes contentLayoutId: Int
) : FullscreenDialogFragment(contentLayoutId) {

    private var _binding: T? = null
    protected val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = createBindingInstance(view)
        bindActions()
    }

    abstract fun createBindingInstance(view: View): T

    abstract fun bindActions()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

abstract class BindableBottomSheet<T : ViewBinding>(
    @LayoutRes contentLayoutId: Int
) : BottomSheetDialogFragment(contentLayoutId) {

    private var _binding: T? = null
    protected val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = createBindingInstance(view)
        bindActions()
    }

    abstract fun createBindingInstance(view: View): T

    abstract fun bindActions()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}