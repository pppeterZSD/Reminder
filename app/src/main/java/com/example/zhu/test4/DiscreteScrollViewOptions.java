package com.example.zhu.test4;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;

import java.util.HashMap;
import java.util.List;

public class DiscreteScrollViewOptions {
    /**
     * Configurations of the popup menu(the navigation part)
     */

    private static DiscreteScrollViewOptions instance;

    private final String KEY_TRANSITION_TIME;
    private static HashMap<String,Integer> hashMap = new HashMap<>();


    public static void init(Context context) {
        instance = new DiscreteScrollViewOptions(context);
    }
    //
    private DiscreteScrollViewOptions(Context context) {
        KEY_TRANSITION_TIME = context.getString(R.string.pref_key_transition_time);
    }


    public static void smoothScrollToUserSelectedPosition(final DiscreteScrollView scrollView, View anchor,List<Item> data) {
        PopupMenu popupMenu = new PopupMenu(scrollView.getContext(), anchor);

        Menu menu = popupMenu.getMenu();
        final RecyclerView.Adapter adapter = scrollView.getAdapter();
        int itemCount = (adapter instanceof InfiniteScrollAdapter) ?
                ((InfiniteScrollAdapter) adapter).getRealItemCount() :
                adapter.getItemCount();
        Log.d("itemcount=",String.valueOf(itemCount));
        Log.d("datasize=",String.valueOf(data.size()));
        for (int i = 0; i < itemCount; i++) {
            menu.add(data.get(i).getName());
            hashMap.put(data.get(i).getName(),i);
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int destination = hashMap.get(item.getTitle());
                if (adapter instanceof InfiniteScrollAdapter) {
                    destination = ((InfiniteScrollAdapter) adapter).getClosestPosition(destination);
                }
                scrollView.smoothScrollToPosition(destination);
                return true;
            }
        });
        popupMenu.show();
    }


    private static SharedPreferences defaultPrefs() {
        return PreferenceManager.getDefaultSharedPreferences(App.getInstance());
    }


}
