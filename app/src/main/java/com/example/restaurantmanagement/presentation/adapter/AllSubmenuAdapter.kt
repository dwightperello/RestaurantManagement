package com.example.restaurantmanagement.presentation.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.restaurantmanagement.databinding.AllsubmenuAdapterBinding
import com.example.restaurantmanagement.databinding.MenuDishLayoutBinding
import com.example.restaurantmanagement.domain.model.response.AllMenuModelItem
import com.example.restaurantmanagement.domain.model.response.Submenu
import com.example.restaurantmanagement.presentation.activity.Menu.EditMenuActivity
import com.example.restaurantmanagement.presentation.activity.Subitems.EditSubMenuitemActivity

class AllSubmenuAdapter (private val activity: Activity): RecyclerView.Adapter<AllSubmenuAdapter.ViewHolder>(){
    private var allmenu= ArrayList<Submenu>()

    class ViewHolder(view: AllsubmenuAdapterBinding) : RecyclerView.ViewHolder(view.root) {
        val ivAllImage = view.ivDishImage
        val tvAllTitle = view.tvDishTitle
        val deletebtn=view.btndelete
        val updatebtn=view.btnupdate
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding:AllsubmenuAdapterBinding=
            AllsubmenuAdapterBinding.inflate(LayoutInflater.from(activity),parent,false)
        return AllSubmenuAdapter.ViewHolder(binding)
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


        holder.deletebtn.setOnClickListener {
            if(activity is EditSubMenuitemActivity){
               activity.confirmdelete(menus)
            }
        }

        holder.updatebtn.setOnClickListener {
            if(activity is EditSubMenuitemActivity){
                activity.ConfirmUpdate(menus)
            }
        }
    }

    fun populateSubMenu(list: ArrayList<Submenu>) {
        allmenu = list
        notifyDataSetChanged()
    }
}