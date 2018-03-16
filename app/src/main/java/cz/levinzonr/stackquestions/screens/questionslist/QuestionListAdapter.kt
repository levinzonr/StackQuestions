package cz.levinzonr.stackquestions.screens.questionslist

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.squareup.picasso.Picasso
import cz.levinzonr.stackquestions.R
import cz.levinzonr.stackquestions.model.Question
import kotlinx.android.synthetic.main.item_loading_indicator.view.*
import kotlinx.android.synthetic.main.item_question.view.*

/**
 * Created by nomers on 3/15/18.
 */
class QuestionListAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    private val items: ArrayList<Question> = ArrayList()
    var isLoading: Boolean = false
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    companion object {
        const val VIEW_TYPE_LOADER = 1
        const val VIEW_TYPE_ITEM = 2
    }

    inner class LoaderHolder(view: View) : RecyclerView.ViewHolder(view) {
        val progressBar: ProgressBar = view.progressbar
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bindView(question: Question) {
            view.question_title.text = question.title
            view.question_times_viewed.text = question.viewCount.toString()
            if (question.owner != null) {
                view.question_author_name.text = question.owner.displayName
                Picasso.get().load(question.owner.profileImage).into(view.question_author_image)
            }
            if (question.isAnswered) {
                view.question_status_icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_done_black_24dp))
                view.question_status_icon.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent))
                view.question_status_label.text = context.getString(R.string.question_answered)
                view.question_status_label.setTextColor(ContextCompat.getColor(context, R.color.colorAccent))

            } else {
                view.question_status_icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_close_black_24dp))
                view.question_status_icon.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimaryDark))
                view.question_status_label.text = context.getString(R.string.question_not_ansewered)
                view.question_status_label.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
            }
            view.question_times_answered.text = question.answerCount.toString()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            VIEW_TYPE_ITEM ->  ViewHolder(layoutInflater.inflate(R.layout.item_question, parent, false))
            VIEW_TYPE_LOADER -> LoaderHolder(layoutInflater.inflate(R.layout.item_loading_indicator, parent, false))
            else -> throw IllegalArgumentException()

        }
    }

    fun setItems(list: List<Question>) {
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        (holder as? ViewHolder)?.bindView(items[position])
        if (holder is LoaderHolder) {
            if (isLoading)
                holder.progressBar.visibility = View.VISIBLE
            else
                holder.progressBar.visibility = View.INVISIBLE
        }


    }

    override fun getItemViewType(position: Int): Int {
        if (position >= items.size) {
            return VIEW_TYPE_LOADER
        }
        return VIEW_TYPE_ITEM
    }
}