package com.pniew.mentalahasz.module_test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.pniew.mentalahasz.R;

import java.util.ArrayList;
import java.util.List;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<TestArtPeriod> artPeriodList;
    private ExpandableListView expandableListView;

    public MyExpandableListAdapter(Context context, ExpandableListView expandableListView, List<TestArtPeriod> artPeriodList) {
        this.context = context;
        this.expandableListView = expandableListView;
        this.artPeriodList = artPeriodList;
    }

    class ChildHolder{
        CheckBox checkBox;
        TextView movementName;
    }

    class GroupHolder{
        CheckBox checkBox;
        TextView artPeriodName;
    }

    @Override
    public int getGroupCount() {
        return artPeriodList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return artPeriodList.get(groupPosition).subList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return artPeriodList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return artPeriodList.get(groupPosition).subList.get(childPosition).name;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder groupHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.test_list_parent, null);
            groupHolder = new GroupHolder();
            groupHolder.artPeriodName = (TextView) convertView.findViewById(R.id.test_choose_list_art_period_name);
            groupHolder.checkBox = (CheckBox) convertView.findViewById(R.id.test_choose_art_period_checkbox);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }
        groupHolder.artPeriodName.setText(artPeriodList.get(groupPosition).artPeriodName);
        groupHolder.checkBox.setChecked(artPeriodList.get(groupPosition).selected);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder childHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.test_list_child, null);
            childHolder = new ChildHolder();
            childHolder.movementName = (TextView) convertView.findViewById(R.id.test_choose_movement_name);
            childHolder.checkBox = (CheckBox) convertView.findViewById(R.id.test_choose_movement_checkbox);
            convertView.setTag(childHolder);
        } else {
            childHolder = (ChildHolder) convertView.getTag();
        }
        childHolder.movementName.setText(artPeriodList.get(groupPosition).subList.get(childPosition).name);
        childHolder.checkBox.setChecked(artPeriodList.get(groupPosition).subList.get(childPosition).selected);
        return convertView;
    }

    public void refreshList(List<TestArtPeriod> list){
        artPeriodList = list;
        notifyDataSetChanged();
        for (int g = 0; g < artPeriodList.size(); g++){
            if(artPeriodList.get(g).selected){
                expandableListView.expandGroup(g);
            } else {
                expandableListView.collapseGroup(g);
            }
        }
    }

    public ArrayList<TestArtPeriod> getArtPeriodsArrayList(){
        return (ArrayList<TestArtPeriod>) artPeriodList;
    }



}
