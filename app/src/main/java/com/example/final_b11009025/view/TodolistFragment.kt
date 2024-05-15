package com.example.final_b11009025.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.final_b11009025.R
import com.example.final_b11009025.adapter.TodolistAdapter
import com.example.final_b11009025.database.RoomDB
import com.example.final_b11009025.database.TodolistEntity
import com.example.final_b11009025.databinding.FragmentTodolistBinding
import com.example.final_b11009025.viewmodel.TodoListViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class TodolistFragment: Fragment() {

    //viewBinding
    private var _binding: FragmentTodolistBinding? = null
    private val binding get() = _binding!!

    //viewModel
    private val model:TodoListViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        _binding = FragmentTodolistBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //建立RoomDB
        val db = RoomDB(view.context)

        //初始化viewModel
        model.initDB(view.context)
        model.updateLiveData()

        //recyclerView連接adapter
        val adapter = TodolistAdapter(listOf())
        binding.todolist.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        binding.todolist.adapter = adapter

        //觀察livedata資料有無變動
        model.get().observe(this, Observer{
            adapter.DataSetUpdate(it)
        })

        //新增代辦事項按鈕監聽事件
        binding.addTodolist.setOnClickListener {

            //建立insert todolist dialog
            val dialog = Dialog(view.context)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.dialog_insert_todolist)
            val insertText: EditText = dialog.findViewById(R.id.TDLIText)
            val confirm: TextView = dialog.findViewById(R.id.TDLIConfirm)
            val cancel: TextView = dialog.findViewById(R.id.TDLICancel)

            //確認按鈕監聽事件
            confirm.setOnClickListener {

                val insertItem = TodolistEntity(insertText.text.toString())
                GlobalScope.launch {

                    //資料庫新增資料
                    db.getTodolistDao().insert(insertItem)

                    //livedata 更新資料
                    model.updateLiveData()
                }

                //dialog關閉
                dialog.dismiss()

            }

            //取消按鈕監聽事件
            cancel.setOnClickListener {
                //dialog關閉
                dialog.dismiss()
            }

            //dialog顯示
            dialog.show()

        }

        //建立item點擊監聽事件
        adapter.setOnItemClickListener(object : TodolistAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int, item: TodolistEntity) {

                //建立delete todolist dialog
                val dialog = Dialog(view.context)
                dialog.setCancelable(false)
                dialog.setContentView(R.layout.dialog_delete_item)
                val deleteText: TextView = dialog.findViewById(R.id.deleteText)
                val yes: TextView = dialog.findViewById(R.id.yes)
                val no: TextView = dialog.findViewById(R.id.no)

                //設置deleteText文字
                deleteText.text = item.text

                //yes按鈕監聽事件
                yes.setOnClickListener {


                    GlobalScope.launch {

                        //資料庫刪除資料
                        db.getTodolistDao().delete(item)

                        //livedata更新資料
                        model.updateLiveData()

                    }

                    //livedata刪除資料
                    model.entityList.value = model.entityList.value!! - item

                    //dialog關閉
                    dialog.dismiss()
                }

                //no按鈕監聽事件
                no.setOnClickListener {
                    //dialog關閉
                    dialog.dismiss()
                }

                //dialog顯示
                dialog.show()

            }
        })
    }
}