package com.karthik.goweather.weather.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.karthik.goweather.R
import com.karthik.goweather.databinding.ListItemForecastBinding
import com.karthik.goweather.weather.data.ForecastDay


/**
 * created by Karthik A on 2019-07-09
 */
class ForecastListAdapter: RecyclerView.Adapter<ForecastListAdapter.ForecastViewHolder>() {

    private var forecastList: MutableList<ForecastDay> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {

        var inflater = LayoutInflater.from(parent.context)
        val binding: ListItemForecastBinding =
            DataBindingUtil.inflate(inflater, R.layout.list_item_forecast, parent, false)

        return ForecastViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return forecastList.size
    }


    fun setItems(items: List<ForecastDay>?){

        items?.let {
            forecastList = items.toMutableList()
            notifyDataSetChanged()
        }
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {

        forecastList[position].let {
            holder.binding.forecast = it

        }
    }

    inner class ForecastViewHolder(var binding: ListItemForecastBinding):RecyclerView.ViewHolder(binding.root)
}