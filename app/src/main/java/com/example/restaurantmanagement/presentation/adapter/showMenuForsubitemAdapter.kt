package com.example.restaurantmanagement.presentation.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.restaurantmanagement.databinding.MenuDishForsubeitemBinding
import com.example.restaurantmanagement.databinding.MenuDishLayoutBinding
import com.example.restaurantmanagement.domain.model.response.AllMenuModelItem
import com.example.restaurantmanagement.presentation.activity.Menu.EditMenuActivity
import com.example.restaurantmanagement.presentation.activity.Subitems.AddSubItemActivity

class showMenuForsubitemAdapter  (private val activity: Activity): RecyclerView.Adapter<showMenuForsubitemAdapter.ViewHolder>() {
    private var allmenu= ArrayList<AllMenuModelItem>()

    class ViewHolder(view: MenuDishForsubeitemBinding) : RecyclerView.ViewHolder(view.root) {
        val ivAllImage = view.ivDishImage
        val tvAllTitle = view.tvDishTitle
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding:MenuDishForsubeitemBinding=
            MenuDishForsubeitemBinding.inflate(LayoutInflater.from(activity),parent,false)
        return showMenuForsubitemAdapter.ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return allmenu.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val menus= allmenu[position]
        Glide.with(activity)
            .load(menus.imageURL)
            .apply(RequestOptions.centerCropTransform())
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.ivAllImage)

        holder.tvAllTitle.text = menus.name
        // holder.tvAllPrice.text = menus.price.toString()
        holder.itemView.setOnClickListener {
            if(activity is AddSubItemActivity){
                activity.showmenu(menus)
            }
        }
    }

    fun populateMenu(list: ArrayList<AllMenuModelItem>) {
        allmenu = list
        notifyDataSetChanged()
    }
}