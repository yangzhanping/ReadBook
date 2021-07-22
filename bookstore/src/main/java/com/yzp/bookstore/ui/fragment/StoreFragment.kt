package com.yzp.bookstore.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.yzp.bookstore.databinding.StoreFragmentBinding
import com.yzp.common.base.BaseFragment

@Route(path = "/store/StoreFragment")
class StoreFragment : BaseFragment() {

    var _binding: StoreFragmentBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = StoreFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initView() {
    }

    override fun initData() {
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}