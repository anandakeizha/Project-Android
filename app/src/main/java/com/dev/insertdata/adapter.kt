package com.dev.insertdata

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.Image
import android.view.LayoutInflater
import android.widget.TextView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate

class adapter (private var data: List<inputData>, private val context: Context): RecyclerView.Adapter<adapter.dataViewHolder>() {

    private val db: DBmain = DBmain(context)

    class dataViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val nameView: TextView = itemView.findViewById(R.id.isiNama)
        val nickView: TextView = itemView.findViewById(R.id.isiNick)
        val emailView: TextView = itemView.findViewById(R.id.isiEmail)
        val addresView: TextView = itemView.findViewById(R.id.isiAddres)
        val bornView: TextView = itemView.findViewById(R.id.isiBorn)
        val phoneView: TextView = itemView.findViewById(R.id.isiPhone)
        val imageView: ImageView = itemView.findViewById(R.id.fotoUser)
        val updateButton: ImageView = itemView.findViewById(R.id.edit)
        val deleteButton: ImageView = itemView.findViewById(R.id.delete)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): dataViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.updateuser, parent, false)
        return dataViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: dataViewHolder, position: Int) {
        val data = data[position]
        val img = data.image
        val bitmap = BitmapFactory.decodeByteArray(img, 0, img.size)
        holder.imageView.setImageBitmap(bitmap)
        holder.nameView.text = data.name
        holder.nickView.text = data.nickname
        holder.emailView.text = data.email
        holder.addresView.text = data.addres
        holder.bornView.text = data.born
        holder.phoneView.text = data.phone
        holder.updateButton.setOnClickListener(){
            val intent = Intent(holder.itemView.context , updateData::class.java).apply {
                putExtra("data_id", data.id)
            }
            holder.itemView.context.startActivity(intent)
        }

        holder.deleteButton.setOnClickListener(){

            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setTitle("Confirmation")
            builder.setMessage("Are you sure?")

            builder.setPositiveButton("Yes") { dialog, _ ->
                db.deleteNote(data.id)
                refreshData(db.getAllData())
                Toast.makeText(holder.itemView.context , "Data Deleted", Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton("No") { dialog, _ ->
                // Tetap di laman tambahdata, hanya tutup dialog
                dialog.dismiss()
            }
            builder.show()

        }
    }

    fun refreshData(newData:  List<inputData>){
        data = newData
        notifyDataSetChanged()
    }

    fun updateData(newData: List<inputData>){
        data = newData
        notifyDataSetChanged()
    }


}