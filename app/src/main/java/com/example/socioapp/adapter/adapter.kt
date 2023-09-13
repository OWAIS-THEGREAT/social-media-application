package com.example.socioapp

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.socioapp.responses.feed_responseItem
import com.example.socioapp.responses.followresposnse
import com.example.socioapp.responses.likeresponse
import com.example.socioapp.retrofitinstances_interface.Retrofit_instance_header
import com.squareup.picasso.Picasso
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyAdapter(private val context: Context, private val itemList: ArrayList<feed_responseItem>, private val username: String,private val token: String) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val caption: TextView = itemView.findViewById(R.id.caption)
        val post_img_url :ImageView = itemView.findViewById(R.id.post_image)
        val user_name : TextView = itemView.findViewById(R.id.user_info)
        val likeButton: ImageView = itemView.findViewById(R.id.like_button)
        val likes:TextView = itemView.findViewById(R.id.likes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.itemview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        // Bind data to TextViews
        holder.caption.text = item.caption
        holder.user_name.text = item.user
        holder.likes.text = item.likes.toString()
        Picasso.get()
            .load(item.post_image_url)
            .into(holder.post_img_url)

        val clickedItem = itemList[position]
        holder.likeButton.setOnClickListener {


            liking(clickedItem.id,holder.likes,holder.likeButton)

        }

        checkliked(clickedItem.id,holder.likeButton)


        holder.user_name.setOnClickListener {


            val intent = Intent(context, userprofile::class.java)
            intent.putExtra("username",username)
            intent.putExtra("following",holder.user_name.text.toString())
            intent.putExtra("key",token)
            context.startActivity(intent)
        }
    }

    private fun checkliked(id: String, likeButton: ImageView) {
        val api_interface = Retrofit_instance_header.createApiService(token)

        val requestBodyBuilder = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("id", id)

        val requestBody = requestBodyBuilder.build()

        api_interface.checklike(requestBody).enqueue(object : Callback<followresposnse?> {
            override fun onResponse(
                call: Call<followresposnse?>,
                response: Response<followresposnse?>
            ) {
                val resp = response.body()?.message.toString()
                checkliking(resp,likeButton)
            }

            override fun onFailure(call: Call<followresposnse?>, t: Throwable) {
                Log.d("err","Invalid Credentials")
            }
        })
    }

    private fun checkliking(resp: String, likeButton: ImageView) {

        if(resp=="no"){
            likeButton.setImageResource(R.drawable.likeed)
        }
        else{
            likeButton.setImageResource(R.drawable.baseline_thumb_up_alt_24)
        }
    }

    private fun liking(id: String, likeness: TextView, likeButton: ImageView) {

        val api_interface = Retrofit_instance_header.createApiService(token)

        val requestBodyBuilder = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("id", id)

        val requestBody = requestBodyBuilder.build()

        api_interface.like(requestBody).enqueue(object : Callback<likeresponse?> {
            override fun onResponse(call: Call<likeresponse?>, response: Response<likeresponse?>) {
                val point = response.body()?.no_of_likes
                likeness.text = point.toString()
                checkliked(id,likeButton)
            }

            override fun onFailure(call: Call<likeresponse?>, t: Throwable) {
                Log.d("err","Invalid Credentials")
            }
        })
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}
