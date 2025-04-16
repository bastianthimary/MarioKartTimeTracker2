package com.buffe.mariokarttimetracker.ui.summary

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.buffe.mariokarttimetracker.R

class SummaryExpandableListAdapter(
    private val context: Context,
    // Die Liste der Kinder: Ein Paar (Trackname, Rennzeit als String)
    private val childData: List<Pair<String, String>>
) : BaseExpandableListAdapter() {

    // Es gibt nur eine Gruppe: "Rennzeiten Übersicht"
    private val groupTitle = "Rennzeiten Übersicht"

    override fun getGroupCount(): Int = 1

    override fun getChildrenCount(groupPosition: Int): Int = childData.size

    override fun getGroup(groupPosition: Int): Any = groupTitle

    override fun getChild(groupPosition: Int, childPosition: Int): Any = childData[childPosition]

    override fun getGroupId(groupPosition: Int): Long = groupPosition.toLong()

    override fun getChildId(groupPosition: Int, childPosition: Int): Long = childPosition.toLong()

    override fun hasStableIds(): Boolean = false

    override fun getGroupView(
        groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?
    ): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_expandable_list_item_1, parent, false)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = groupTitle
        textView.setTextColor(ContextCompat.getColor(context, R.color.titleText))
        return view
    }

    override fun getChildView(
        groupPosition: Int, childPosition: Int, isLastChild: Boolean,
        convertView: View?, parent: ViewGroup?
    ): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        val (trackName, raceTime) = childData[childPosition]
        textView.text = "$trackName: $raceTime"
        textView.setTextColor(ContextCompat.getColor(context, R.color.raceTimeLead))
        return view
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean = true
}
