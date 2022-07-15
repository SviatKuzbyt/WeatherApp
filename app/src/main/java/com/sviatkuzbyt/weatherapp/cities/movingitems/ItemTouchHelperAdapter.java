package com.sviatkuzbyt.weatherapp.cities.movingitems;

public interface ItemTouchHelperAdapter {

    void onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position2);
}