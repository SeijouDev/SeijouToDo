package dev.seijou.com.seijoutodo.Helpers

import android.graphics.Color
import android.graphics.Typeface
import android.media.Image
import android.widget.TextView
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import dev.seijou.com.seijoutodo.Helpers.Constants.Companion.ObjectTypes.*
import dev.seijou.com.seijoutodo.Objects.BasicListItem
import dev.seijou.com.seijoutodo.R


/**
 * Created by frontend on 13/12/17.
 */
class BasicListAdpater : RecyclerView.Adapter<BasicListAdpater.BasicViewHolder> {

    class BasicViewHolder : RecyclerView.ViewHolder , View.OnLongClickListener {

        internal var image: ImageView
        internal var title: TextView
        internal var desc: TextView

        private var handler : OnClickHandler

        constructor(itemView: View, handler: OnClickHandler) : super(itemView) {
            image = itemView.findViewById(R.id.imgItem)
            title = itemView.findViewById(R.id.nameItem)
            desc = itemView.findViewById(R.id.valueItem)
            this.handler = handler
            itemView.setOnLongClickListener(this)
        }

        override fun onLongClick(v: View): Boolean {
            handler.onItemLongClick(v , adapterPosition)
            return true
        }
    }

    var handler: OnClickHandler
    var list = ArrayList<BasicListItem>()

    constructor(list: ArrayList<BasicListItem>, handler: OnClickHandler) {
        this.list = list
        this.handler = handler
    }

    override fun onBindViewHolder(holder: BasicViewHolder, position: Int) {
        var item = list[position]
        //holder.image.setImageResource(R.drawable.ic_money)
        holder.title.setText(item.title)

        if(item.type == Constants.Companion.ObjectTypes.expenses)
            holder.desc.setText(String.format("$ %,.0f",item.desc.toFloat()))
        else
            holder.desc.setText(item.desc)

        if(!item.active) {
            holder.desc.setTypeface(holder.desc.getTypeface(), Typeface.ITALIC)
            holder.title.setTypeface(holder.desc.getTypeface(), Typeface.ITALIC)
            holder.desc.setTextColor(Color.parseColor("#BDBDBD"))
            holder.title.setTextColor(Color.parseColor("#BDBDBD"))
        }
    }

    override fun getItemCount(): Int = this.list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasicViewHolder {
        var v : View = LayoutInflater.from(parent.context).inflate(R.layout.basic_list_item, parent,false)

        when(viewType){
            0 -> v = LayoutInflater.from(parent.context).inflate(R.layout.basic_list_item, parent,false)
            1 -> v = LayoutInflater.from(parent.context).inflate(R.layout.purchase_list_item, parent,false)
            2 -> v = LayoutInflater.from(parent.context).inflate(R.layout.music_list_item, parent,false)
        }

        var viewHolder = BasicViewHolder(v, this.handler)
        return viewHolder
    }

    override fun getItemViewType(position: Int): Int {
        var v : Int
        when(this.list[position].type){
            expenses -> v = 0
            purchase -> v = 1
            music -> v = 2
        }
        return v
    }


}